import { CustomerDB } from '@mongo/models';
import { BadRequestError } from '@interfaces/index';

const gateway = async (req: any, res: any) => {
  if (!req.user) {
    throw new BadRequestError('customer', 'not authenticated');
  }

  const { _id, password } = req.user;

  let customer;

  try {
    customer = await CustomerDB.findOne({ _id, password });
  } catch (err) {
    throw new BadRequestError('customer', err);
  }

  if (!customer) {
    throw new BadRequestError('customer', 'not authenticated');
  }

  return res.status(200).json({ data: true });
};

export default gateway;
