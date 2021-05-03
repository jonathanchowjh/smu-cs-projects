import { CustomerDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const retrieve = async (req: any, res: any) => {
  const { _id, password } = req.user;

  let stall;
  try {
    stall = <DynamicMap> await CustomerDB.findOne({ _id, password });
  } catch (err) {
    throw new BadRequestError('stall', err);
  }
  if (!stall) {
    throw new BadRequestError('stall', 'not authenticated');
  }

  const removeHawker = req.body.mapId;

  const newCustomer = <DynamicMap> await <unknown> CustomerDB.findOneAndUpdate(
    { _id, password },
    { $pull: { favourites: removeHawker } },
    { new: true },
  );

  return res.status(200).json(
    newCustomer.favourites,
  );
};

export default retrieve;
