const mongoose = require('mongoose');

// Define the schema
const applianceSchema = new mongoose.Schema({
    Appliance_id: { type: String, required: true },
    name: { type: String, required: true },
    make: { type: String, required: true },
    year: { type: Number, required: true },
    price: { type: Number, required: true }
});

// Create a model from the schema
const Appliance = mongoose.model('Appliance', applianceSchema);

module.exports = Appliance;
