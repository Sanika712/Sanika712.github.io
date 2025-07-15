import React from 'react';
import { Link } from 'react-router-dom'; // Importing Link from react-router-dom
import './HomePage.css';

const HomePage = () => {
  return (
    <div className="home-page">
      <h1>Welcome to Our Hotel Booking Platform</h1>
      <p>Find the best deals on hotels and make your booking experience smooth and secure.</p>
      
      {/* Using the Link component for navigation */}
      <Link to="/search" className="cta-btn"id="booknow-button">Book Now</Link>

      {/* Featured Section */}
      <section className="featured-section">
        <h2>Our Featured Services</h2>
        <div className="feature-card">
          <h3>Best Prices</h3>
          <p>Get the best hotel deals, discounts, and offers to make your stay affordable.</p>
        </div>
        <div className="feature-card">
          <h3>Secure Booking</h3>
          <p>Book with confidence knowing your payment is processed securely.</p>
        </div>
        <div className="feature-card">
          <h3>24/7 Customer Support</h3>
          <p>Our support team is always here to assist you with your booking and stay.</p>
        </div>
      </section>
    </div>
  );
};

export default HomePage;
