import { useState } from "react";
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

function DistributionForm() {
  const [formData, setFormData] = useState({
    donationType: "FOOD",
    quantity: "",
    distributionDate: new Date().toISOString().slice(0, 10),
    recipient: "",
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
      await axios.post("http://localhost:8080/api/distributions", formData);
      setSuccess(true);
      setError(null);
      setFormData({
        donationType: "FOOD",
        quantity: "",
        distributionDate: new Date().toISOString().slice(0, 10),
        recipient: "",
      });
    } catch (err) {
      setSuccess(false);
      const errorMsg = err.response?.data?.message || "Failed to submit distribution. Please try again.";
      setError(errorMsg);
    }
  };

  const quantityLabel = formData.donationType === "MONEY" ? "Amount" : "Quantity";

  return (
    <Paper elevation={3} sx={{ p: 4, maxWidth: 600, mx: "auto", mt: 5 }}>
      <Typography variant="h5" gutterBottom>
        Record a Distribution
      </Typography>
      <form onSubmit={handleSubmit}>
        <Grid container spacing={2}>
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
              name="recipient"
              label="Recipient Name"
              value={formData.recipient}
              onChange={handleChange}
              required
            />
          </Grid>

          <Grid item xs={12}>
            <TextField
              fullWidth
              name="distributionDate"
              label="Distribution Date"
              type="date"
              value={formData.distributionDate}
              onChange={handleChange}
              InputLabelProps={{ shrink: true }}
              required
            />
          </Grid>

          <Grid item xs={12}>
            <Button variant="contained" color="primary" type="submit" fullWidth>
              Submit Distribution
            </Button>
          </Grid>

          {success && (
            <Grid item xs={12}>
              <Alert severity="success">Distribution recorded successfully!</Alert>
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

export default DistributionForm;