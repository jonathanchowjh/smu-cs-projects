import { CustomerDB, OrderDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const insert = async (req: any, res: any) => {
  const { _id, password } = req.user;

  const { uen, orderList } = req.body;

  let customer: DynamicMap;
  try {
    customer = <DynamicMap> await CustomerDB.findOne({ _id, password });
  } catch (err) {
    throw new BadRequestError('customer', err);
  }
  if (!customer) {
    throw new BadRequestError('customer', 'not authenticated');
  }

  const orders = await OrderDB.find();

  orderList.forEach(async (order: any) => {
    const OrderModel = new OrderDB({
      orderId: orders.length + 1,
      customerId: customer.id,
      uen,
      menuId: order.menuid,
      status: 'Order made',
      quantity: order.quantity,
    });

    try {
      await OrderModel.save();
    } catch (err) {
      throw new BadRequestError('customer', err);
    }
  });

  return res.status(200).json({
    message: 'Successfully added menu',
  });
};

export default insert;
