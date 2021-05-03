import {
  SimpleError,
  SimpleResponseError,
} from '@interfaces/index';

export class BadRequestError extends Error implements SimpleError {
  name: string;

  message: string;

  code: Number;

  error: any;

  constructor(field: string, error: any) {
    super(field);
    Error.captureStackTrace(this, this.constructor);

    this.code = 400;
    this.name = 'BadRequestError';
    this.message = `Error: ${field}`;
    this.error = error;
  }

  response() {
    const responseMessage: SimpleResponseError = {
      message: this.message,
      error: this.error,
    };
    return responseMessage;
  }
}
