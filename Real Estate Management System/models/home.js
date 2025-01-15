const mongoose = require('mongoose');

// Define the schema
const homeSchema = new mongoose.Schema({
    home_id: { type: String, required: true },
    type: { type: String, required: true },
    Location_id: { type: String, required: true },
    appliances: [
        {
            Appliance_id: { type: String, required: true },
            name: { type: String, required: true },
            make: { type: String, required: true }
        }
    ],
    Owner_id: { type: String, required: true },
    bathRooms: { type: Number, required: true },
    bedRooms: { type: Number, required: true },
    Floors: { type: Number, required: true },
    landSize: { type: String, required: true },
    yearConstructed: { type: Number, required: true },
    floorSpace: { type: String, required: true },
    Openforsale: { type: String, required: true },
    salePrice: { type: Number, required: true },
    soldnumber: { type: Number, required: true }
});

// Create a model from the schema
const Home = mongoose.model('Home', homeSchema);

module.exports = Home;
