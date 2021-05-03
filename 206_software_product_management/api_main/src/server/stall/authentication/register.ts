import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import moment from 'moment-timezone';

import { StallDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const register = async (req: any, res: any) => {
  const { uen, name, password } = req.body;

  let stall;

  try {
    stall = await StallDB.findOne({ uen });
  } catch (err) {
    throw new BadRequestError('stall', err);
  }

  if (stall) {
    throw new BadRequestError('stall', 'email already registered');
  }

  const salt = await bcrypt.genSalt(10);
  const hash = await bcrypt.hash(password, salt);

  const StallModel = new StallDB({
    date: moment(Date.now()).toDate(),
    uen,
    name,
    password: hash,
  });

  let newStall;
  try {
    newStall = <DynamicMap> await <unknown> StallModel.save();
  } catch (err) {
    throw new BadRequestError('customer', err);
  }

  const { _id, password: hashedPassword } = newStall;
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

export default register;
