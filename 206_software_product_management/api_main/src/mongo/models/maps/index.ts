import mongoose from 'mongoose';

const MapSchema = new mongoose.Schema({
  name: {
    type: String,
    required: true,
  },
  image: {
    type: String,
    required: true,
  },
},
{
  strict: true,
});

export default MapSchema;
