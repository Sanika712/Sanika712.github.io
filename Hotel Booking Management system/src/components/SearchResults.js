import React, { useState, useEffect } from 'react';
import './SearchResults.css';
import { Link } from 'react-router-dom'; // Make sure to import Link

import History from './History';



const SearchResults = () => {
  const [hotels, setHotels] = useState([]);
  const [selectedImage, setSelectedImage] = useState(null);
  const [selectedHotel, setSelectedHotel] = useState(null);

  useEffect(() => {
    // Fetch hotels from Flask API
    fetch('http://127.0.0.1:5000/hotels')
      .then(response => response.json())
      .then(data => {
        console.log("Fetched data:", data);
        if (Array.isArray(data)) {
          setHotels(data);
        } else {
          console.error('Expected an array of hotels, but received:', data);
        }
      })
      .catch(error => console.error('Error fetching data:', error));
  }, []);

  const bookRoom = (hotelId) => {
    fetch(`http://127.0.0.1:5000/book_room/${hotelId}`, {
      method: 'POST',
    })
      .then(response => response.json())
      .then(data => {
        if (data.message) {
          alert(data.message);
          setHotels(prevHotels =>
            prevHotels.map(hotel =>
              hotel.id === hotelId ? { ...hotel, available_rooms: data.available_rooms } : hotel
            )
          );
        } else {
          alert(data.error || "An error occurred!");
        }
      })
      .catch(error => {
        console.error('Error booking room:', error);
        alert('Error booking room. Please try again.');
      });
  };

  const cancelBooking = (hotelId) => {
    fetch(`http://127.0.0.1:5000/cancel_booking/${hotelId}`, {
      method: 'POST',
    })
      .then(response => response.json())
      .then(data => {
        if (data.message) {
          alert(data.message);
          setHotels(prevHotels =>
            prevHotels.map(hotel =>
              hotel.id === hotelId ? { ...hotel, available_rooms: data.available_rooms } : hotel
            )
          );
        } else {
          alert(data.error || "An error occurred!");
        }
      })
      .catch(error => {
        console.error('Error canceling booking:', error);
        alert('Error canceling booking. Please try again.');
      });
  };

  const openImageModal = (imageUrl, hotel) => {
    console.log('Opening image modal with image URL:', imageUrl);
    const fullImageUrl = `http://127.0.0.1:5000/images/${imageUrl}`;
    setSelectedImage(fullImageUrl);
    setSelectedHotel(hotel);
  };

  const closeImageModal = () => {
    setSelectedImage(null);
    setSelectedHotel(null);
  };

  return (
    <div className="search-results">
      <h2>Search Results</h2>
      
         <div className="hotel-list">
        {Array.isArray(hotels) && hotels.length > 0 ? (
          hotels.map((hotel) => (
            <div key={hotel.id} className="hotel-item">
              <div className="hotel-info">
                <h3>{hotel.name}</h3>
                <button className="image-btn" onClick={() => openImageModal(hotel.photos, hotel)}>
                  <img src="https://img.icons8.com/ios/452/image.png" alt="Image Icon" />
                </button>
              </div>
              <p>Location: {hotel.location}</p>
              <p>Price per night: ${hotel.price}</p>
              <p><strong>Available rooms: {hotel.available_rooms}</strong></p>

              <button
                onClick={() => bookRoom(hotel.id)}
                disabled={hotel.available_rooms <= 0}
                style={{
                  backgroundColor: hotel.available_rooms > 0 ? '#4CAF50' : '#ccc',
                  cursor: hotel.available_rooms > 0 ? 'pointer' : 'not-allowed',
                  color: 'white',
                  padding: '10px 15px',
                  fontSize: '16px',
                  border: 'none',
                  borderRadius: '5px',
                  marginRight: '10px',
                }}
              >
                {hotel.available_rooms > 0 ? 'Book Now!' : 'No Rooms Available'}
              </button>

              <button
                onClick={() => cancelBooking(hotel.id)}
                disabled={hotel.available_rooms >= 5}
                style={{
                  backgroundColor: hotel.available_rooms < 5 ? 'red' : 'grey',
                  cursor: hotel.available_rooms < 5 ? 'pointer' : 'not-allowed',
                  color: 'white',
                  padding: '10px 15px',
                  fontSize: '16px',
                  border: 'none',
                  borderRadius: '5px',
                }}
              >
                {hotel.available_rooms < 5 ? 'Cancel Booking' : 'Cancel booking'}
              </button>
            </div>
          ))
        ) : (
          <p>No hotels available.</p>
        )}
      </div>

      {selectedImage && selectedHotel && (
        <div className="image-modal" onClick={closeImageModal}>
          <span className="close-btn">&times;</span>
          <img className="modal-image" src={selectedImage} alt={`Hotel image of ${selectedHotel.name}`} />
        </div>
      )}
    </div>
  );
};

export default SearchResults;
