import { StallDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const insert = async (req: any, res: any) => {
  const { _id, password } = req.user;

  const { _id: searchId } = req.body;

  let oldStall;
  try {
    oldStall = <DynamicMap> await StallDB.findOne({ _id, password });
  } catch (err) {
    throw new BadRequestError('stall', err);
  }
  if (!oldStall) {
    throw new BadRequestError('stall', 'not authenticated');
  }

  const updateIndex = oldStall.menu.findIndex((menu: any) => menu.id === searchId);
  if (updateIndex < 0) {
    throw new BadRequestError('menu', 'not found');
  }

  const menuUpdated: DynamicMap = {
    date: Date.now(),
    name: req.body.name || oldStall.name,
    description: req.body.description || oldStall.description,
    image: req.body.image || oldStall.image,
    price: req.body.price || oldStall.price,
    availability: req.body.availability || oldStall.availability,
  };

  const newStall = <DynamicMap> await <unknown> StallDB.findOneAndUpdate(
    { _id, password },
    { $set: { 'experiences.$[index]': menuUpdated } },
    { arrayFilters: [{ 'index._id': searchId }], new: true },
  );

  const updated = newStall.menu[updateIndex];

  return res.status(200).json({
    ...updated.toJSON(),
    message: 'Successfully updated menu',
  });
};

export default insert;
