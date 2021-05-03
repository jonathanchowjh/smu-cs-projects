import { StallDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const retrieve = async (req: any, res: any) => {
  const { _id, password } = req.user;

  let stall;
  try {
    stall = <DynamicMap> await StallDB.findOne({ _id, password });
  } catch (err) {
    throw new BadRequestError('stall', err);
  }
  if (!stall) {
    throw new BadRequestError('stall', 'not authenticated');
  }

  return res.status(200).json({
    uen: stall.uen,
    name: stall.name,
  });
};

export default retrieve;
