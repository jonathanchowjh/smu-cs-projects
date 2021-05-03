import { StallDB } from '@mongo/models';
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

  const menuEntry: DynamicMap = {
    date: Date.now(),
    name: req.body.name,
    description: req.body.description,
    image: req.body.image,
    price: req.body.price,
  };

  const stall = <DynamicMap> await <unknown> StallDB.findOneAndUpdate(
    { _id, password },
    { $push: { menu: menuEntry } },
    { new: true },
  );

  return res.status(200).json({
    ...stall.menu.slice(-1)[0].toJSON(),
    message: 'Successfully added menu',
  });
};

export default insert;
