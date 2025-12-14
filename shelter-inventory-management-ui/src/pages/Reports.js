import React, { useEffect, useState } from "react";
import {
  Paper,
  Typography,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  CircularProgress,
  Grid,
  Alert
} from "@mui/material";  
import axios from "axios";

function Reports() {
  const [inventoryReport, setInventoryReport] = useState(null);
  const [donorReport, setDonorReport] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchReports = async () => {
      try {
        const [inventoryRes, donorRes] = await Promise.all([
          axios.get("http://localhost:8080/api/reports/inventory"),
          axios.get("http://localhost:8080/api/reports/donors")
        ]);
        setInventoryReport(inventoryRes.data);
        setDonorReport(donorRes.data);
      } catch (err) {
        setError("Failed to load reports. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchReports();
  }, []);

  if (loading) return <CircularProgress sx={{ mt: 5 }} />;
  if (error) return <Alert severity="error">{error}</Alert>;

  return (
    <Grid container spacing={4} sx={{ p: 4 }}>
      <Grid item xs={12} md={6}>
        <Paper sx={{ p: 3 }}>
          <Typography variant="h6" gutterBottom>
            Inventory Report
          </Typography>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Type</TableCell>
                <TableCell>Total Donated</TableCell>
                <TableCell>Total Distributed</TableCell>
                <TableCell>Current Stock</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {inventoryReport.items.map((item) => (
                <TableRow key={item.donationType}>
                  <TableCell>{item.donationType}</TableCell>
                  <TableCell>{item.totalDonated}</TableCell>
                  <TableCell>{item.totalDistributed}</TableCell>
                  <TableCell>{item.currentStock}</TableCell>
                </TableRow>
              ))}
              <TableRow>
                <TableCell colSpan={3} align="right">
                  <strong>Total Value:</strong>
                </TableCell>
                <TableCell><strong>{inventoryReport.totalValue}</strong></TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </Paper>
      </Grid>

      <Grid item xs={12} md={6}>
        <Paper sx={{ p: 3 }}>
          <Typography variant="h6" gutterBottom>
            Donor Report
          </Typography>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Donor</TableCell>
                <TableCell>Type</TableCell>
                <TableCell>Quantity</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {donorReport.contributions.map((donor) => (
                <React.Fragment key={donor.donorName}>
                  {donor.donations.map((d, idx) => (
                    <TableRow key={donor.donorName + idx}>
                      <TableCell>{idx === 0 ? donor.donorName : ""}</TableCell>
                      <TableCell>{d.donationType}</TableCell>
                      <TableCell>{d.quantity}</TableCell>
                    </TableRow>
                  ))}
                  <TableRow>
                    <TableCell colSpan={2} align="right">
                      <strong>Total Value:</strong>
                    </TableCell>
                    <TableCell><strong>{donor.totalValue}</strong></TableCell>
                  </TableRow>
                </React.Fragment>
              ))}
            </TableBody>
          </Table>
        </Paper>
      </Grid>
    </Grid>
  );
}

export default Reports;
