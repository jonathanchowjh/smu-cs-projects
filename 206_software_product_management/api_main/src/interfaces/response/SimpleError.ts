export interface SimpleError extends Error {
  name: string;
  message: string;
  code: Number;
  error: any;
}
