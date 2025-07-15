import React from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';

const Navbar = () => {
  return (
    <nav className="navbar">
      <div className="navbar-logo">
        <h2>Hotel Booking</h2>
      </div>
      <div className="navbar-links">
        <Link to="/"id="home-link">Home</Link>
        <Link to="/search"id="search-link">Search Hotels</Link>
        <Link to="/History"id="booking-link">Booking History</Link>
      </div>
    </nav>
  );
};

export default Navbar;
