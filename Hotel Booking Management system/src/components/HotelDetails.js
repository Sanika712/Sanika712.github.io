import React from 'react';
import './HotelDetails.css';

const HotelDetails = ({ hotel }) => {
  return (
    <div className="hotel-details">
      <h2>{hotel.name}</h2>
      <p>Location: {hotel.location}</p>
      <p>Price per night: ${hotel.price}</p>
      <button>Book Now</button>
    </div>
  );
};

export default HotelDetails;
