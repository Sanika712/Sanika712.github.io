const mongoose = require('mongoose');

// Define the schema
const ownerSchema = new mongoose.Schema({
    Owner_id: { type: String, required: true },
    ssn: { type: Number, required: true },
    name: { type: String, required: true },
    age: { type: Number, required: true },
    profession: { type: String, required: true },
    income: { type: Number, required: true },
    dependents: { type: Number, required: true }
});

// Create a model from the schema
const Owner = mongoose.model('Owner', ownerSchema);

module.exports = Owner;
