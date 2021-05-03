import { CustomerDB, OrderDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

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

  const bundledOrders: any[] = [];

  orders.forEach((order: any) => {
    if (!bundledOrders[order.orderId]) bundledOrders[order.orderId] = []
    bundledOrders[order.orderId].push({
      uen: order.uen,
      menuId: order.menuId,
      customerId: order.customerId,
      quantity: order.quantity,
      status: order.status,
    });
    bundledOrders[order.orderId].customerId = order.customerId;
    bundledOrders[order.orderId].quantity = order.quantity;
    bundledOrders[order.orderId].status = order.status;
  });

  return res.status(200).json(
    bundledOrders,
  );
};

export default retrieve;
