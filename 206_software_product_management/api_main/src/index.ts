import 'module-alias/register';
import 'source-map-support/register';

import Server from '@server/index';
import MongoDB from '@mongo/index';
import { Mongoose } from 'mongoose';

const server = new Server();
const mongoDb = new MongoDB();

console.info('[STARED] vita-verify-api');
console.info(`[INFO] NODE_ENV=${process.env.NODE_ENV}`);

const start = async () => {
  const database: Mongoose = await mongoDb.start();
  console.info('[INFO] ✅ Server successfully connected to MongoDB');
  console.info(`[INFO]     - Host    : ${database.connection.host}`);
  console.info(`[INFO]     - Database: ${database.connection.name}`);

  const applicationPort: string = <string> await server.start();
  console.info(`[INFO] ✅ Server initated on localhost port ${applicationPort}`);
};

start();
