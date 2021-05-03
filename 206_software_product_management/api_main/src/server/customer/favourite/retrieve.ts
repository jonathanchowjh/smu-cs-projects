import { CustomerDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const retrieve = async (req: any, res: any) => {
  const { _id, password } = req.user;

  let customer;
  try {
    customer = <DynamicMap> await CustomerDB.findOne({ _id, password });
  } catch (err) {
    throw new BadRequestError('customer', err);
  }
  if (!customer) {
    throw new BadRequestError('customer', 'not authenticated');
  }

  return res.status(200).json(
    customer.favourites,
  );
};

export default retrieve;
