import { CoordinateDB, MapDB, StallDB } from '@mongo/models';
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
          });
        } catch (err) {
          throw new BadRequestError('stall', err);
        }
      }
    } catch (err) {
      throw new BadRequestError('stall', err);
    }
    mapWithStalls.push({
      ...mp.toJSON(),
      stalls,
    });
  }

  // map = map.map((stall: any) => ({
  //   ...stall.toJSON(),
  // }));

  return res.status(200).json(mapWithStalls);
};

export default retrieve;
