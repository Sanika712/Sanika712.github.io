const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const path = require('path');

// Import models for MongoDB interactions
const Home = require('./models/home.js');
const Agent = require('./models/agents.js');
const Location = require('./models/locations.js');
const Appliance = require('./models/appliances.js');
const Owner = require('./models/owners.js');
const Sale = require('./models/sales.js');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(express.urlencoded({ extended: true }));
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

// Connect to MongoDB
mongoose.connect('mongodb://localhost:27017/trial', {
  useNewUrlParser: true,
  useUnifiedTopology: true
}).then(() => {
  console.log('Connected to MongoDB');
}).catch(err => {
  console.error('Error connecting to MongoDB:', err);
  process.exit(1); // Exit the process if MongoDB connection fails
});

// Define schemas and models
const AgentSchema = new mongoose.Schema({
  Agent_id: String,
  name: String,
  commissionRate: Number
});


// Routes
app.post('/add-agent', async (req, res) => {

  try {
    const newAgent = new Agent({
      Agent_id: req.body.Agent_id,
      name: req.body.name,
      commissionRate: req.body.commissionRate
    });
    await newAgent.save();
    res.status(201).send('Agent added successfully!');
  } catch (err) {
    console.error(err);
    res.status(500).send('Internal Server Error');
  }
});

const homeSchema = new mongoose.Schema({
  home_id: { type: String, required: true },
  type: { type: String, required: true },
  Location_id: { type: String, required: true },
  Owner_id: { type: String, required: true },
  bathRooms: { type: Number, required: true },
  bedRooms: { type: Number, required: true },
  Floors: { type: Number, required: true },
  landSize: { type: String, required: true },
  yearConstructed: { type: Number, required: true },
  floorSpace: { type: String, required: true },
  Openforsale: { type: String, required: true },
  soldnumber: { type: Number, required: true }, // Add soldnumber field
  salePrice: { type: Number, required: true }, // Add salePrice field
});



app.post('/add-home', async (req, res) => {
  try {
    const newHome = new Home({
      home_id: req.body.home_id,
      type: req.body.type,
      Location_id: req.body.Location_id, 
      Owner_id: req.body.Owner_id, 
      bathRooms: req.body.bathRooms,
      bedRooms: req.body.bedRooms,
      Floors: req.body.Floors, 
      landSize: req.body.landSize,
      yearConstructed: req.body.yearConstructed,
      floorSpace: req.body.floorSpace,
      Openforsale: req.body.Openforsale, 
      soldnumber: req.body.soldnumber,
      salePrice: req.body.salePrice
    });


    await newHome.save(); 

    res.send('Home added successfully');
  } catch (error) {
    console.log(error);
    res.status(500).send('Error adding the home');
  }
});

app.get('/agent-form', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'agent.html'));
});

app.get('/home-form', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'homeform.html'));
});

app.get('/home', async (req, res) => {
  try {
    const homes = await Home.find({});
    res.json(homes);
  } catch (error) {
    console.error('Error fetching home data:', error);
    res.status(500).json({ error: 'Error fetching home data' });
  }
});

app.get('/agents', async (req, res) => {
  try {
    const agents = await Agent.find({});
    res.json(agents);
  } catch (error) {
    console.error('Error fetching agent data:', error);
    res.status(500).json({ error: 'Error fetching agent data' });
  }
});

app.get('/locations', async (req, res) => {
  try {
    const locations = await Location.find({});
    res.json(locations);
  } catch (error) {
    console.error('Error fetching location data:', error);
    res.status(500).json({ error: 'Error fetching location data' });
  }
});


// Route for handling combined form submissions
app.post('/combinedSearch', async (req, res) => {
    const { Location_id,Owner_id,name,make,Agent_id,salePrice,Floors,bedRooms,bathRooms,type,maxPrice, city,soldnumber} = req.body;

    console.log('Received form data:', { Location_id,Owner_id,name,make,Agent_id,salePrice,Floors,bedRooms,bathRooms,type,maxPrice, city,soldnumber});

let homeResults, applianceResults, locationResults, ownerResults,agentResults, saleResults;

    try {
        
	const homeQuery = {};
        const applianceQuery = {};
        const locationQuery = {};
        const ownerQuery = {};
	const agentQuery = {};
	const saleQuery = {};

        // Populate appliance query object
        if (name) applianceQuery.name = name;
        if (make) applianceQuery.make = make;


        // Populate home query object
        if (Floors) homeQuery.Floors = parseInt(Floors);
        if (bedRooms) homeQuery.bedRooms = parseInt(bedRooms);
        if (bathRooms) homeQuery.bathRooms = parseInt(bathRooms);
        if (type) homeQuery.type = type;
	if (salePrice) homeQuery.salePrice = parseInt(salePrice);
	
	
        // Populate location query object
        if (Location_id) {
		locationQuery.Location_id = Location_id;
		homeQuery.Location_id = Location_id;
	}

         // Populate owner query object
        if (Owner_id) {
		 ownerQuery.Owner_id = Owner_id;
		 homeQuery.Owner_id = Owner_id;
	}

	//populate agent query object
        if (Agent_id) {
		 homeQuery.Agent_id = Agent_id;
		 }

	if (maxPrice) {
    		homeQuery.salePrice = { $lt: parseInt(maxPrice) }; 
	}

	if (soldnumber) {
    const selectedValue = parseInt(soldnumber);
    if (selectedValue === 1) {
        homeQuery.soldnumber = { $gt: 1 };
    } else if (selectedValue === 2) {
        homeQuery.soldnumber = { $gt: 2 };
    }
}


 // Find people who own apartments as well as mansions
        if (type && type.length === 2) {
            homeQuery.type = { $in: type };
        }

        
        const homeResults = await Home.find(homeQuery)
            .where('appliances')
            .elemMatch(applianceQuery);

        const response = {
            homeResults,
            applianceResults,
            locationResults,
            ownerResults,
	    agentResults,
	    saleResults
        };

	res.json(response);

    } catch (error) {
        console.error('Error fetching data:', error);
        res.status(500).json({ error: 'Error fetching data' });
    }
});


// Serve the HTML file
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'finalui.html'));
});

app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});
