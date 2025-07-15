// src/index.tsx
import React from 'react';
import ReactDOM from 'react-dom/client'; // Import from 'react-dom/client'
import './index.css';
import App from './components/App';

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement); // Use createRoot here
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
