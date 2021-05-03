import mongoose from 'mongoose';
import validator from 'validator';

const CustomerSchema = new mongoose.Schema({
  date: {
    type: Date,
    default: Date.now,
  },
  phone: {
    unique: true,
    type: String,
    required: true,
  },
  email: {
    type: String,
    validate: {
      validator: validator.isEmail,
      message: 'Mongoose: Please enter a valid email',
    },
  },
  favourites: {
    type: Array,
    default: [],
  },
  password: {
    type: String,
    required: true,
  },
},
{
  strict: true,
});

export default CustomerSchema;
