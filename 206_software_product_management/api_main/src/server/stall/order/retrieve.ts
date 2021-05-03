import { StallDB, OrderDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const insert = async (req: any, res: any) => {
  const { _id, password } = req.user;

  let oldStall;
  try {
    oldStall = <DynamicMap> await StallDB.findOne({ _id, password });
  } catch (err) {
    throw new BadRequestError('stall', err);
  }
  if (!oldStall) {
    throw new BadRequestError('stall', 'not authenticated');
  }

  let orders: DynamicMap;
  try {
    orders = <DynamicMap> await OrderDB.find({ uen: oldStall.uen });
  } catch (err) {
    throw new BadRequestError('customer', err);
  }
  if (!orders) {
    throw new BadRequestError('customer', 'not authenticated');
  }

  let bundledOrders: any[] = [];

  orders.forEach((order: any) => {
    if (!bundledOrders[order.orderId]) bundledOrders[order.orderId] = [];
    bundledOrders[order.orderId].push({
      uen: order.uen,
      menuId: order.menuId,
      customerId: order.customerId,
      quantity: order.quantity,
      status: order.status,
      orderId: order.orderId,
    });
    bundledOrders[order.orderId].orderId = order.orderId;
    bundledOrders[order.orderId].customerId = order.customerId;
    bundledOrders[order.orderId].quantity = order.quantity;
    bundledOrders[order.orderId].status = order.status;
  });

  bundledOrders = bundledOrders.filter((order) => order !== null);

  return res.status(200).json({
    bundledOrders,
  });
};

export default insert;
