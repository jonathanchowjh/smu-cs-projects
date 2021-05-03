import http from 'http';
import express from 'express';

import Routes from '@server/router';

class Server {
  private port: number | string | boolean;

  private config: any;

  private server: any;

  private routes: any;

  constructor() {
    this.config = express();
    this.routes = new Routes();
    this.config.use('/', this.routes.getRouter());
    this.port = Server.normalizePort(process.env.PORT || '4000');
  }

  static normalizePort(port: string) : number | string | boolean {
    const parsedPort = parseInt(port, 10);
    if (Number.isNaN(parsedPort)) return 0;
    if (parsedPort >= 0) return parsedPort;
    return false;
  }

  start() : Promise<number | string | boolean> {
    return new Promise((resolve) => {
      this.server = http.createServer(this.config);
      this.server.listen(this.port, () => {
        resolve(this.port);
      });
    });
  }

  stop(): void {
    this.server.close();
  }
}

export default Server;
