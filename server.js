const express = require('express');
const bcrypt = require('bcryptjs');
const cors = require('cors'); // Import cors
const nodemailer = require('nodemailer');
const { Sequelize, DataTypes } = require('sequelize');

// Create a new instance of Sequelize with your database configuration
const sequelize = new Sequelize('testdevaccount1', 'testdevaccount1', '132testdevaccount1', {
  host: 'db4free.net',
  dialect: 'mysql',
  logging: false
});

// Define the User model
const User = sequelize.define('User', {
  email: {
    type: DataTypes.STRING,
    allowNull: false
  },
  password: {
    type: DataTypes.STRING,
    allowNull: false
  }
});

// Synchronize the model with the database
sequelize.sync()
  .then(() => {
    console.log('Database synced');
  })
  .catch((error) => {
    console.error('Error syncing database:', error);
  });

const app = express();
app.use(cors()); // Enable CORS middleware
app.use(express.json());
app.use(express.json());

app.post('/api/registration', async (req, res) => {
    console.log("Received POST /api/registration request with body:", req.body); // Add this line
  try {
    const { email, password } = req.body;

    // Validate email and password inputs
    if (!email || !password) {
      return res.status(400).json({ message: 'Email and password are required.' });
    }

    // Validate email format
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      return res.status(400).json({ message: 'Invalid email format.' });
    }

    // Validate password requirements
    if (password.length < 8) {
      return res.status(400).json({ message: 'Password must be at least 8 characters long.' });
    }

    // Check if the email is already registered
    const existingUser = await User.findOne({ where: { email } });
    if (existingUser) {
      return res.status(400).json({ message: 'Email is already registered.' });
    }

    // Hash the password
    const hashedPassword = await bcrypt.hash(password, 10);

    // Create a new user record in the database
    await User.create({ email, password: hashedPassword });

    res.status(201).json({ message: 'User registered successfully. Please check your email for verification.' });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'An error occurred while registering the user.', error: error.message });
  }
});

const port = process.env.PORT || 8080;

app.listen(port, () => {
  console.log(`Server started on port ${port}`);
});
