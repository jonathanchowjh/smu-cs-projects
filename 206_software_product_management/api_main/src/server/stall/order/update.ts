import { StallDB, OrderDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const update = async (req: any, res: any) => {
  const { _id, password } = req.user;

  const { orderId, status } = req.body;

  let stall;
  try {
    stall = <DynamicMap> await StallDB.findOne({ _id, password });
  } catch (err) {
    throw new BadRequestError('stall', err);
  }
  if (!stall) {
    throw new BadRequestError('stall', 'not authenticated');
  }

  let orders: DynamicMap;
  try {
    orders = <DynamicMap> await OrderDB.find({ orderId });
  } catch (err) {
    throw new BadRequestError('customer', err);
  }
  if (!orders) {
    throw new BadRequestError('customer', 'not authenticated');
  }

  orders.forEach(async (order: any) => {
    try {
      await OrderDB.findOneAndUpdate(
        { _id: order._id },
        { $set: { status } },
        { new: true },
      );
    } catch (err) {
      throw new BadRequestError('customer', err);
    }
    if (!orders) {
      throw new BadRequestError('customer', 'not authenticated');
    }
  });

  return res.status(200).json({
    message: 'Successfully updated menu',
  });
};

export default update;
