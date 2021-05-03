import Server from '@server/index';
import MongoDB from '@mongo/index';

const server = new Server();
const mongoDb = new MongoDB();

describe('Start test', () => {
  test('connect to database', (done) => {
    mongoDb.start()
      .then((database) => {
        expect(database.connection.name).toEqual('jiak-dev');
        done();
      });
  });

  test('start server', (done) => {
    server.start()
      .then((port) => {
        expect(port).toEqual(4000);
        done();
      });
  });

  test('stop server', () => {
    server.stop();
  });

  test('stop database', () => {
    mongoDb.stop();
  });
});
