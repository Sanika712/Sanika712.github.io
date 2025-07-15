import React, { useState, useEffect } from 'react';
import './History.css';

const History = () => {
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);  // For handling loading state
  const [error, setError] = useState(null);  // For handling errors

  // Using async/await for better readability
  const fetchHistory = async () => {
    try {
      const response = await fetch('http://127.0.0.1:5000/booking_history');

      // Check if the response is not ok (status not in range 200-299)
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();

      // Ensure that the data returned is an array
      if (Array.isArray(data)) {
        // Sort the history array by timestamp in descending order
        const sortedHistory = data.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
        setHistory(sortedHistory);
      } else {
        throw new Error("Received data is not an array.");
      }
      
      setLoading(false);
    } catch (error) {
      console.error("Error fetching booking history:", error);
      setError(error);
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchHistory();
  }, []);  // Empty array ensures this runs only once when the component mounts

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error loading booking history: {error.message}</div>;
  }

  return (
    <div className="history">
      <h2>Booking History</h2>
      {history.length === 0 ? (
        <p>No booking history found.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Hotel Name</th>
              <th>Action</th>
              <th>Timestamp</th>
            </tr>
          </thead>
          <tbody>
            {history.map(item => (
              <tr key={item.id}>
                <td>{item.hotel_name}</td>
                <td>{item.action}</td>
                <td>{new Date(item.timestamp).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default History;
