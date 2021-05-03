import { StallDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const get = async (req: any, res: any) => {
  const { uen, menuId } = req.query;

  let stall;
  try {
    stall = <DynamicMap> await StallDB.findOne({ uen });
  } catch (err) {
    throw new BadRequestError('stall', err);
  }
  if (!stall) {
    throw new BadRequestError('stall', 'not found');
  }

  // eslint-disable-next-line eqeqeq
  const dish = stall.menu.find((menu: any) => menu._id == menuId);

  return res.status(200).json({
    ...dish.toJSON(),
    uen,
  });
};

export default get;
