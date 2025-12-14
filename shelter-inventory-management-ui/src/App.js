import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { Container, CssBaseline } from "@mui/material";
import Navbar from "./components/Navbar";
import Dashboard from "./pages/Dashboard";
import Donations from "./pages/Donations";
import Distributions from "./pages/Distributions";
import Reports from "./pages/Reports";

function App() {
  return (
    <Router>
      <CssBaseline />
      <Navbar />

      <Container sx={{ mt: 4 }}>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/donations" element={<Donations />} />
          <Route path="/distributions" element={<Distributions />} />
          <Route path="/reports" element={<Reports />} />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </Container>
    </Router>
  );
}

export default App;
