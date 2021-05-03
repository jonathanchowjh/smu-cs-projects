import mongoose from 'mongoose';

const OrderSchema = new mongoose.Schema({
  orderId: {
    type: Number,
  },
  customerId: {
    type: String,
    required: true,
  },
  uen: {
    type: String,
    required: true,
  },
  menuId: {
    type: String,
    required: true,
  },
  status: {
    type: String,
    required: true,
  },
  quantity: {
    type: Number,
  },
},
{
  strict: true,
});

export default OrderSchema;
