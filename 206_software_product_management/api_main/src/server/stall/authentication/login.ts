import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';

import { StallDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const login = async (req: any, res: any) => {
  const { uen, password } = req.body;

  let stall;

  try {
    stall = <DynamicMap> await StallDB.findOne({ uen });
  } catch (err) {
    throw new BadRequestError('stall', err);
  }

  if (!stall) {
    throw new BadRequestError('stall', 'not authenticated');
  }

  const isMatch = await bcrypt.compare(password, stall.password);

  if (!isMatch) {
    throw new BadRequestError('password', 'invalid password');
  }

  const { _id, password: hashedPassword } = stall;
  const token = { _id, password: hashedPassword };

  const jwtToken = jwt.sign(
    token,
    process.env.CUSTOMER_JWT_SECRET || '',
  );

  const isHTTPS = process.env.NODE_ENV !== 'local';
  res.cookie('stall-token', jwtToken, {
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
