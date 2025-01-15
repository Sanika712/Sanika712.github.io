const mongoose = require('mongoose');

// Define the schema
const locationSchema = new mongoose.Schema({
    Location_id: { type: String, required: true },
    address: { type: String, required: true },
    city: { type: String, required: true },
    zipCode: { type: Number, required: true },
    county: { type: String, required: true }
});

// Create a model from the schema
const Location = mongoose.model('Location', locationSchema);

module.exports = Location;
