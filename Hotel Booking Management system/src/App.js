import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar';
import HomePage from './components/HomePage';
import SearchResults from './components/SearchResults';
import HotelDetails from './components/HotelDetails';
import Footer from './components/Footer';
import History from './components/History';
  // Import History component
import './App.css';

const App = () => {
  return (
    <Router>
      <Navbar />
      <div className="content">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/search" element={<SearchResults />} />
          <Route path="/hotel/:id" element={<HotelDetails hotel={{ name: 'Hotel 1', location: 'Paris', price: 120 }} />} />
          <Route path="/history" element={<History />} /> {/* Corrected route */}
        </Routes>
      </div>
      <Footer />
    </Router>
  );
};

export default App;
