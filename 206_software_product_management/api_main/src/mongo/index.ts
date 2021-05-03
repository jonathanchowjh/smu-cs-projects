import mongoose, { Mongoose } from 'mongoose';

class MongoDB {
  private uri: string;

  private database: string;

  private config: any;

  private mongoserver: any;

  constructor(uri?: string) {
    this.uri = uri || <string> process.env.MONGO_URI;
    this.database = uri || <string> process.env.MONGO_DB;

    this.config = {
      appName: `jiak-${process.env.NODE_ENV}`,
      useCreateIndex: true,
      useNewUrlParser: true,
      useUnifiedTopology: true,
      useFindAndModify: false,
    };

    this.mongoserver = mongoose;
  }

  async start(): Promise<Mongoose> {
    try {
      const mongoUri = `${this.uri}/${this.database}`;
      this.mongoserver = await this.mongoserver.connect(mongoUri, this.config);
    } catch (err) {
      console.error(err);
    }

    return this.mongoserver;
  }

  stop(): void {
    this.mongoserver.disconnect();
  }
}

export default MongoDB;
