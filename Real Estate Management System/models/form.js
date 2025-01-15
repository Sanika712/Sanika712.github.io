const mongoose = require('mongoose');

// Define schema for form model
const formSchema = new mongoose.Schema({
    floors: { type: Number, required: true },
    bedrooms: { type: Number, required: true },
    bathrooms: { type: Number, required: true }
});

// Create model from schema
const Form = mongoose.model('Form', formSchema);

module.exports = Form;
