import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';

import { CustomerDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const login = async (req: any, res: any) => {
  const { phone, password } = req.body;

  let customer;

  try {
    customer = <DynamicMap> await CustomerDB.findOne({ phone });
  } catch (err) {
    throw new BadRequestError('customer', err);
  }

  if (!customer) {
    throw new BadRequestError('customer', 'not authenticated');
  }

  const isMatch = await bcrypt.compare(password, customer.password);

  if (!isMatch) {
    throw new BadRequestError('password', 'invalid password');
  }

  const { _id, password: hashedPassword } = customer;
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

export default login;
