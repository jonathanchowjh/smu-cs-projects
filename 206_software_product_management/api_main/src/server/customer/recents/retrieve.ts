import { CustomerDB, OrderDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

function onlyUnique(value: any, index: any, self: any) {
  return self.indexOf(value) === index;
}

const retrieve = async (req: any, res: any) => {
  const { _id, password } = req.user;

  let customer: DynamicMap;
  try {
    customer = <DynamicMap> await CustomerDB.findOne({ _id, password });
  } catch (err) {
    throw new BadRequestError('customer', err);
  }
  if (!customer) {
    throw new BadRequestError('customer', 'not authenticated');
  }

  let orders: DynamicMap;
  try {
    orders = <DynamicMap> await OrderDB.find({ customerId: customer._id });
  } catch (err) {
    throw new BadRequestError('customer', err);
  }
  if (!orders) {
    throw new BadRequestError('customer', 'not authenticated');
  }

  const recentHawkers: any[] = [];

  orders.forEach((order: any) => {
    recentHawkers.push(order.uen);
  });

  const uniqueRecentHawkers = recentHawkers.filter(onlyUnique);

  return res.status(200).json(
    uniqueRecentHawkers,
  );
};

export default retrieve;
