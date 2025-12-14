import { AppBar, Toolbar, Typography, Button, Stack } from "@mui/material";
import { Link as RouterLink } from "react-router-dom";

function Navbar() {
  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          Donation Manager
        </Typography>

        <Stack direction="row" spacing={2}>
          <Button color="inherit" component={RouterLink} to="/">
            Home
          </Button>
          <Button color="inherit" component={RouterLink} to="/donations">
            Donate
          </Button>
          <Button color="inherit" component={RouterLink} to="/distributions">
            Distribute
          </Button>
          <Button color="inherit" component={RouterLink} to="/reports">
            Reports
          </Button>
        </Stack>
      </Toolbar>
    </AppBar>
  );
}

export default Navbar;