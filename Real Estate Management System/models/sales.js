const mongoose = require('mongoose');

// Define the schema
const saleSchema = new mongoose.Schema({
    Sale_id: { type: String, required: true },
    home_id: { type: String, required: true },
    agentId: { type: String, required: true },
    sellerId: { type: String, required: true },
    buyerId: { type: String, required: true },
    salePrice: { type: Number, required: true }
});

// Create a model from the schema
const Sale = mongoose.model('Sale', saleSchema);

module.exports = Sale;
