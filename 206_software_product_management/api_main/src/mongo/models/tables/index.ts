import mongoose from 'mongoose';

const TableSchema = new mongoose.Schema({
  mapId: {
    type: String,
  },
  number: {
    type: Number,
  },
  coordX: {
    type: Number,
  },
  coordY: {
    type: Number,
  },
},
{
  strict: true,
});

export default TableSchema;
