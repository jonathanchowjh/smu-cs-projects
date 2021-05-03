import 'express-async-errors';

import cors from 'cors';
import express from 'express';
import bodyParser from 'body-parser';
import cookieParser from 'cookie-parser';
import mongoSanitize from 'express-mongo-sanitize';

import customerRouter, { customerPassport } from '@server/customer';
import stallRouter, { stallPassport } from '@server/stall';

import {
  requestErrors,
} from '@interfaces/index';

class Routes {
  private baseRoute: any;

  private router: any;

  private customerCors: any;

  private stallCors: any;

  constructor() {
    this.baseRoute = '/api/v1';
    this.router = express.Router();

    this.initializeParser();
    this.initializeRootRoute();

    this.initializeCors();
    this.initializeApplicationRoute();

    this.initializeErrorHandling();
  }

  getRouter(): any {
    return this.router;
  }

  static configCors(whitelist: string[]) {
    let allowAll = false;
    if (whitelist.length === 0) allowAll = true;
    return {
      methods: 'POST,PUT,PATCH,GET,UPDATE,DELETE',
      allowedHeaders: [
        'Origin',
        'X-Requested-With',
        'Content-Type',
        'Authorization',
      ],
      credentials: true,
      origin: (origin: string, callback: any) => {
        if (allowAll || whitelist.indexOf(origin) !== -1) {
          callback(null, true);
        } else {
          callback({
            name: 'UnauthorizedOriginError',
            message: `Unauthorized 401: CORS from Origin ${origin} not Allowed`,
          });
        }
      },
    };
  }

  private initializeCors() {
    const {
      CUSTOMER_CORS_WHITELIST,
      STALL_CORS_WHITELIST,
    } = process.env;

    this.customerCors = Routes.configCors(CUSTOMER_CORS_WHITELIST ? CUSTOMER_CORS_WHITELIST.split(',') : []);
    this.stallCors = Routes.configCors(STALL_CORS_WHITELIST ? STALL_CORS_WHITELIST.split(',') : []);
  }

  private initializeParser() {
    this.router.use(cookieParser());
    this.router.use(bodyParser.json());
    this.router.use(bodyParser.urlencoded({ extended: false }));
    this.router.use(mongoSanitize({ replaceWith: '_' }));
  }

  private initializeRootRoute() {
    this.router.get('/', (req: any, res: any) => {
      res.status(200).json({ message: 'jiak' });
    });

    this.router.get(`${this.baseRoute}/`, (req: any, res: any) => {
      res.status(200).json({ message: 'jiak-api' });
    });
  }

  private initializeApplicationRoute() {
    this.router.use(
      `${this.baseRoute}/customer`,
      cors(this.customerCors),
      customerPassport.initialize(),
      customerPassport.session(),
      customerRouter,
    );

    this.router.use(
      `${this.baseRoute}/stall`,
      cors(this.stallCors),
      stallPassport.initialize(),
      stallPassport.session(),
      stallRouter,
    );
  }

  private initializeErrorHandling() {
    this.router.use((err: any, req: any, res: any, next: any) => {
      if (err.name === 'UnauthorizedError') {
        res.status(401).json({ message: 'UnauthorizedError: not loggedin or invalid cookie (make sure to enable cookie)', error: err });
      } else if (err.name === 'UnauthorizedOriginError') {
        res.status(401).json({ message: 'Unauthorized Origin', error: err.message });
      } else if (requestErrors.includes(err.name)) {
        res.status(err.code).json(err.response());
      } else {
        next(err);
      }
    });
  }
}

export default Routes;
