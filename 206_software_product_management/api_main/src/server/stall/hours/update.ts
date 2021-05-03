import { StallDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const insert = async (req: any, res: any) => {
  const { _id, password } = req.user;

  const { operatinghours } = req.body;

  let oldStall;
  try {
    oldStall = <DynamicMap> await StallDB.findOne({ _id, password });
  } catch (err) {
    throw new BadRequestError('stall', err);
  }
  if (!oldStall) {
    throw new BadRequestError('stall', 'not authenticated');
  }

  const newStall = <DynamicMap> await <unknown> StallDB.findOneAndUpdate(
    { _id, password },
    { $set: { operatinghours } },
    { new: true },
  );

  return res.status(200).json({
    ...newStall.operatinghours,
    message: 'Successfully updated hours',
  });
};

export default insert;
