import express from 'express';
import expressJwt from 'express-jwt';

const customerRouter = express.Router();

const requireCookie = expressJwt({
  algorithms: ['HS256'],
  secret: process.env.CUSTOMER_JWT_SECRET || '',
  getToken: (req) => req.cookies['customer-token'],
});

customerRouter.get('/', (req, res) => {
  res.status(200).json({ data: 'customer-api' });
});

customerRouter.use('/auth/gateway', requireCookie);
customerRouter.get('/auth/gateway', require('./authentication/gateway').default);

customerRouter.post('/auth/register', require('./authentication/register').default);
customerRouter.post('/auth/login', require('./authentication/login').default);
customerRouter.post('/auth/logout', require('./authentication/logout').default);

customerRouter.use('/profile', requireCookie);
customerRouter.get('/profile', require('./profile/retrieve').default);

customerRouter.get('/qr', require('./qr/get').default);

customerRouter.get('/stall', require('./stall/get').default);
customerRouter.get('/dish', require('./stall/getMenu').default);

customerRouter.get('/map', require('./map/retrieve').default);
customerRouter.get('/hawkercenter/retrieve', require('./map/retrieve').default);
customerRouter.get('/hawkercenter/get', require('./map/get').default);

customerRouter.use('/order', requireCookie);
customerRouter.get('/order', require('./order/retrieve').default);
customerRouter.post('/order', require('./order/insert').default);

customerRouter.use('/favourite', requireCookie);
customerRouter.get('/favourite', require('./favourite/retrieve').default);
customerRouter.post('/favourite', require('./favourite/insert').default);
customerRouter.delete('/favourite', require('./favourite/remove').default);

customerRouter.use('/recents', requireCookie);
customerRouter.get('/recents', require('./recents/retrieve').default);

export default customerRouter;
export { default as customerPassport } from '@server/customer/passport';
