import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import moment from 'moment-timezone';

import { CustomerDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const register = async (req: any, res: any) => {
  const { phone, password } = req.body;

  let customer;

  try {
    customer = await CustomerDB.findOne({ phone });
  } catch (err) {
    throw new BadRequestError('customer', err);
  }

  if (customer) {
    throw new BadRequestError('customer', 'email already registered');
  }

  const salt = await bcrypt.genSalt(10);
  const hash = await bcrypt.hash(password, salt);

  const CustomerModel = new CustomerDB({
    date: moment(Date.now()).toDate(),
    phone,
    password: hash,
  });

  let newCustomer;
  try {
    newCustomer = <DynamicMap> await <unknown> CustomerModel.save();
  } catch (err) {
    throw new BadRequestError('customer', err);
  }

  const { _id, password: hashedPassword } = newCustomer;
  const token = { _id, password: hashedPassword };

  const jwtToken = jwt.sign(
    token,
    process.env.CUSTOMER_JWT_SECRET || '',
  );

  const isHTTPS = process.env.NODE_ENV !== 'local';
  res.cookie('customer-token', jwtToken, {
    domain: isHTTPS ? '.vitaverify.me' : 'localhost',
    path: '/',
    httpOnly: true,
    maxAge: 1000 * 3600 * 24 * 30,
    sameSite: isHTTPS ? 'none' : true,
    secure: isHTTPS,
  });

  return res.status(200).json({ data: true });
};

export default register;
