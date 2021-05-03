import { MapDB, TableDB, CoordinateDB } from '@mongo/models';
import { DynamicMap, BadRequestError } from '@interfaces/index';

const get = async (req: any, res: any) => {
  const { mapId, number } = req.query;

  let map;
  try {
    map = <DynamicMap> await MapDB.findOne({ _id: mapId });
  } catch (err) {
    throw new BadRequestError('map', err);
  }
  if (!map) {
    throw new BadRequestError('map', 'not found');
  }

  let table;
  try {
    table = <DynamicMap> await TableDB.findOne({ mapId, number });
  } catch (err) {
    throw new BadRequestError('coordinate', err);
  }
  if (!table) {
    throw new BadRequestError('coordinate', 'not found');
  }

  let coordinates;
  try {
    coordinates = <DynamicMap> await CoordinateDB.find({ mapId });
  } catch (err) {
    throw new BadRequestError('coordinate', err);
  }
  if (!coordinates) {
    throw new BadRequestError('coordinate', 'not found');
  }

  return res.status(200).json({
    hawker: map.name,
    map: map.image,
    tableX: table.coordX,
    tableY: table.coordY,
    number: table.number,
    stalls: coordinates,
  });
};

export default get;
