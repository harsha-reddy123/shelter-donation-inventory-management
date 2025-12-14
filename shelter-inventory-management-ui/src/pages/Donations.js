import React, { useState } from "react";
import {
  TextField,
  Button,
  MenuItem,
  Grid,
  Paper,
  Typography,
  Alert,
} from "@mui/material";
import axios from "axios";

const donationTypes = ["FOOD", "CLOTHING", "MONEY", "TOYS","MEDICINE",
    "BLANKETS","HYGIENE PRODUCTS","FURNITURE","BOOKS","OTHER"];

function DonationForm() {
  const [formData, setFormData] = useState({
    donorName: "",
    donationType: "FOOD",
    quantity: "",
    donationDate: new Date().toISOString().slice(0, 10),
  });

  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post("http://localhost:8080/api/donations", formData);
      setSuccess(true);
      setError(null);
      setFormData({
        donorName: "",
        donationType: "FOOD",
        quantity: "",
        donationDate: new Date().toISOString().slice(0, 10),
      });
    } catch (err) {
      setSuccess(false);
      setError("Failed to submit donation. Please try again.");
    }
  };

  const quantityLabel = formData.donationType === "MONEY" ? "Amount" : "Quantity";

  return (
    <Paper elevation={3} sx={{ p: 4, maxWidth: 600, mx: "auto", mt: 5 }}>
      <Typography variant="h5" gutterBottom>
        Record a Donation
      </Typography>
      <form onSubmit={handleSubmit}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <TextField
              fullWidth
              name="donorName"
              label="Donor Name"
              value={formData.donorName}
              onChange={handleChange}
              required
            />
          </Grid>

          <Grid item xs={12}>
            <TextField
              select
              fullWidth
              name="donationType"
              label="Donation Type"
              value={formData.donationType}
              onChange={handleChange}
              required
            >
              {donationTypes.map((type) => (
                <MenuItem key={type} value={type}>
                  {type}
                </MenuItem>
              ))}
            </TextField>
          </Grid>

          <Grid item xs={12}>
            <TextField
              fullWidth
              name="quantity"
              label={quantityLabel}
              type="number"
              value={formData.quantity}
              onChange={handleChange}
              required
            />
          </Grid>

          <Grid item xs={12}>
            <TextField
              fullWidth
              name="donationDate"
              label="Donation Date"
              type="date"
              value={formData.donationDate}
              onChange={handleChange}
              InputLabelProps={{ shrink: true }}
              required
            />
          </Grid>

          <Grid item xs={12}>
            <Button variant="contained" color="primary" type="submit" fullWidth>
              Submit Donation
            </Button>
          </Grid>

          {success && (
            <Grid item xs={12}>
              <Alert severity="success">Donation recorded successfully!</Alert>
            </Grid>
          )}
          {error && (
            <Grid item xs={12}>
              <Alert severity="error">{error}</Alert>
            </Grid>
          )}
        </Grid>
      </form>
    </Paper>
  );
}

export default DonationForm;
