import { StallDB } from '@mongo/models';
import { BadRequestError } from '@interfaces/index';

const gateway = async (req: any, res: any) => {
  if (!req.user) {
    throw new BadRequestError('stall', 'not authenticated');
  }

  const { _id, password } = req.user;

  let stall;

  try {
    stall = await StallDB.findOne({ _id, password });
  } catch (err) {
    throw new BadRequestError('stall', err);
  }

  if (!stall) {
    throw new BadRequestError('stall', 'not authenticated');
  }

  return res.status(200).json({ data: true });
};

export default gateway;
