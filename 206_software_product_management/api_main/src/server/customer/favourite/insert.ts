import { CustomerDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const insert = async (req: any, res: any) => {
  const { _id, password } = req.user;

  let oldCustomer;
  try {
    oldCustomer = <DynamicMap> await CustomerDB.findOne({ _id, password });
  } catch (err) {
    throw new BadRequestError('customer', err);
  }
  if (!oldCustomer) {
    throw new BadRequestError('customer', 'not authenticated');
  }

  const newHawker = req.body.mapId;

  const newCustomer = <DynamicMap> await <unknown> CustomerDB.findOneAndUpdate(
    { _id, password },
    { $push: { favourites: newHawker } },
    { new: true },
  );

  return res.status(200).json({
    newFav: newCustomer.favourites.slice(-1)[0],
    message: 'Successfully added menu',
  });
};

export default insert;
