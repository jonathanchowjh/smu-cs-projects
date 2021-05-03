import mongoose from 'mongoose';

import CoordinateSchema from './coordinates';
import CustomerSchema from './customers';
import MapSchema from './maps';
import OrderSchema from './orders';
import StallSchema from './stalls';
import TableSchema from './tables';

export const CoordinateDB = mongoose.model('coordinates', CoordinateSchema);
export const CustomerDB = mongoose.model('customers', CustomerSchema);
export const MapDB = mongoose.model('maps', MapSchema);
export const OrderDB = mongoose.model('orders', OrderSchema);
export const StallDB = mongoose.model('stalls', StallSchema);
export const TableDB = mongoose.model('tables', TableSchema);
