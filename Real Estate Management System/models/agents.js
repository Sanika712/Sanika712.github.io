const mongoose = require('mongoose');

// Define the agent schema
const agentSchema = new mongoose.Schema({
  Agent_id: { type: String, required: true, unique: true }, // Change agentId to id to match the provided document structure
  name: { type: String, required: true },
  commissionRate: { type: Number, required: true },
});

// Create the Agent model using the agent schema
const Agent = mongoose.model('Agent', agentSchema);

// Export the Agent model
module.exports = Agent;
