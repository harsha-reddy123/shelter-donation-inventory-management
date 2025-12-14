import React, { useState, useEffect } from "react";
import {
  Paper,
  Typography,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Button,
  Grid,
  CircularProgress,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Box,
  Divider
} from "@mui/material";
import axios from "axios";

const donationTypes = ["FOOD", "CLOTHING", "MONEY", "TOYS"];

function InventoryLookup() {
  const [selectedType, setSelectedType] = useState("FOOD");
  const [inventory, setInventory] = useState(null);
  const [summaryReport, setSummaryReport] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const handleCheck = async () => {
    setLoading(true);
    setError(null);
    setInventory(null);
    try {
      const res = await axios.get(`http://localhost:8080/api/reports/inventory/${selectedType}`);
      setInventory(res.data);
    } catch (err) {
      setError("Unable to fetch inventory for selected type.");
    } finally {
      setLoading(false);
    }
  };

  const fetchSummary = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/reports/inventory");
      setSummaryReport(res.data);
    } catch (err) {
      setError("Unable to fetch inventory summary.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSummary();
  }, []);

  return (
    <Paper sx={{ p: 4, mt: 4 }}>
      <Typography variant="h6" gutterBottom>
        Inventory Overview Dashboard
      </Typography>

      <Grid container spacing={2} alignItems="center" sx={{ mb: 2 }}>
        <Grid item xs={12} sm={6}>
          <FormControl fullWidth>
            <InputLabel>Donation Type</InputLabel>
            <Select
              value={selectedType}
              label="Donation Type"
              onChange={(e) => setSelectedType(e.target.value)}
            >
              {donationTypes.map((type) => (
                <MenuItem key={type} value={type}>
                  {type}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>

        <Grid item xs={12} sm={3}>
          <Button variant="contained" onClick={handleCheck} fullWidth>
            Check Inventory
          </Button>
        </Grid>
      </Grid>

      {loading && <CircularProgress sx={{ mt: 3 }} />}

      {error && (
        <Alert severity="error" sx={{ mt: 3 }}>
          {error}
        </Alert>
      )}

      {inventory && (
        <Box sx={{ mt: 3 }}>
          <Typography variant="subtitle1" gutterBottom>
            Selected Type Summary
          </Typography>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Donation Type</TableCell>
                <TableCell>Total Donated</TableCell>
                <TableCell>Total Distributed</TableCell>
                <TableCell>Current Stock</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              <TableRow>
                <TableCell>{inventory.donationType}</TableCell>
                <TableCell>{inventory.totalDonated}</TableCell>
                <TableCell>{inventory.totalDistributed}</TableCell>
                <TableCell
                  sx={{ color: inventory.currentStock < 0 ? "error.main" : "inherit" }}
                >
                  {inventory.currentStock}
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </Box>
      )}

      <Divider sx={{ my: 4 }} />

      {summaryReport && (
        <Box>
          <Typography variant="subtitle1" gutterBottom>
            Full Inventory Summary
          </Typography>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Donation Type</TableCell>
                <TableCell>Total Donated</TableCell>
                <TableCell>Total Distributed</TableCell>
                <TableCell>Current Stock</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {summaryReport.items.map((item) => (
                <TableRow key={item.donationType}>
                  <TableCell>{item.donationType}</TableCell>
                  <TableCell>{item.totalDonated}</TableCell>
                  <TableCell>{item.totalDistributed}</TableCell>
                  <TableCell
                    sx={{ color: item.currentStock < 0 ? "error.main" : "inherit" }}
                  >
                    {item.currentStock}
                  </TableCell>
                </TableRow>
              ))}
              <TableRow>
                <TableCell colSpan={3} align="right">
                  <strong>Total Value:</strong>
                </TableCell>
                <TableCell><strong>{summaryReport.totalValue}</strong></TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </Box>
      )}
    </Paper>
  );
}

export default InventoryLookup;