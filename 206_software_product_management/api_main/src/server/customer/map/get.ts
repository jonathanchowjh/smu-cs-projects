import { CoordinateDB, MapDB, StallDB, TableDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const retrieve = async (req: any, res: any) => {
  let map;
  try {
    map = await MapDB.find();
  } catch (err) {
    throw new BadRequestError('stall', err);
  }
  if (!map) {
    throw new BadRequestError('stall', 'not found');
  }

  const mapWithStalls: any[] = [];
  for await (const mp of map) {
    let coordinates;
    const stalls: any[] = [];

    try {
      coordinates = await CoordinateDB.find({ mapId: mp._id });
      for await (const coordinate of coordinates) {
        const lazyToMakeInterface = <DynamicMap> coordinate;
        let stall;
        try {
          stall = <DynamicMap> await StallDB.findOne({ uen: lazyToMakeInterface.uen });
          stalls.push({
            ...stall.toJSON(),
            password: undefined,
            startX: lazyToMakeInterface.startX,
            startY: lazyToMakeInterface.startY,
            endX: lazyToMakeInterface.endX,
            endY: lazyToMakeInterface.endY,
          });
        } catch (err) {
          throw new BadRequestError('stall', err);
        }
      }
    } catch (err) {
      throw new BadRequestError('stall', err);
    }

    let tables;

    try {
      tables = await TableDB.find({ mapId: mp._id });
    } catch (err) {
      throw new BadRequestError('stall', err);
    }

    mapWithStalls.push({
      ...mp.toJSON(),
      stalls,
      tables,
    });
  }

  // eslint-disable-next-line eqeqeq
  const findMap = mapWithStalls.find((maps) => maps._id == req.query.id);

  return res.status(200).json(findMap);
};

export default retrieve;
