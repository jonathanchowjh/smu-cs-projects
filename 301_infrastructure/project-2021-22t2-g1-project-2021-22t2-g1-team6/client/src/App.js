import './App.css';

import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Link
} from "react-router-dom";
import Navbar from './components/Navbar';
import Index from './pages/index';
import Hotels from './pages/hotels';
import HotelRooms from './pages/hotel-rooms';
import Booking from './pages/booking';
import ViewBooking from './pages/view-booking'

function App() {
  return (
    <Router>
      <Navbar />
      <div className="container">
        <Routes>
          <Route exact path="/" element={<Index />}></Route>
          <Route path="/hotel" element={<Hotels />}></Route>
          <Route path="/hotel/:id" element={<HotelRooms />}></Route>
          <Route path="/hotel/:id/:room" element={<Booking />}></Route>
          <Route path="/booking" element={<ViewBooking />}></Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
