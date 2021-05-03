import mongoose from 'mongoose';

const CoordinateSchema = new mongoose.Schema({
  mapId: {
    type: String,
  },
  uen: {
    type: String,
  },
  startX: {
    type: Number,
  },
  startY: {
    type: Number,
  },
  endX: {
    type: Number,
  },
  endY: {
    type: Number,
  },
},
{
  strict: true,
});

export default CoordinateSchema;
