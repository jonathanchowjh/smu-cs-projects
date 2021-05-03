import mongoose from 'mongoose';

import MenuSchema from './MenuSchema';

const StallSchema = new mongoose.Schema({
  date: {
    type: Date,
    default: Date.now,
  },
  name: {
    type: String,
    required: true,
  },
  uen: {
    unique: true,
    type: String,
    required: true,
  },
  password: {
    type: String,
    required: true,
  },
  menu: [
    MenuSchema,
  ],
  operatinghours: {
    type: Object,
    default: {
      monday: {
        open: 7,
        close: 9,
      },
      tuesday: {
        open: 7,
        close: 9,
      },
      wednesday: {
        open: 7,
        close: 9,
      },
      thursday: {
        open: 7,
        close: 9,
      },
      friday: {
        open: 7,
        close: 9,
      },
      saturday: {
        open: 7,
        close: 9,
      },
      sunday: {
        open: 7,
        close: 9,
      },
    },
  },
},
{
  strict: true,
});

export default StallSchema;
