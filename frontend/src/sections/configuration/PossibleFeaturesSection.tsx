import * as React from 'react';
import {
  Grid,
  List,
  ListItem,
  ListItemText,
  Stack,
} from '@mui/material';

export default function PossibleFeaturesSection() {
  return (
    <Stack spacing={1} height="100%">
      <Grid container>
        <Grid item xs={6}>
          <List>
            <ListItem key="possible">
              <ListItemText id="Possible" primary="Possible" />
            </ListItem>
            <ListItem key="elementA">
              <ListItemText id="ElementA" primary="Element A" />
            </ListItem>
            <ListItem key="elementB">
              <ListItemText id="ElementB" primary="Element B" />
            </ListItem>
            <ListItem key="elementC">
              <ListItemText id="ElementC" primary="Element C" />
            </ListItem>
            <ListItem key="elementD">
              <ListItemText id="ElementD" primary="Element D" />
            </ListItem>
          </List>
        </Grid>
        <Grid item xs={6}>
          <List>
            <ListItem key="impossible">
              <ListItemText id="Impossible" primary="Impossible" />
            </ListItem>
            <ListItem key="elementA">
              <ListItemText id="ElementA" primary="Element A" />
            </ListItem>
            <ListItem key="elementB">
              <ListItemText id="ElementB" primary="Element B" />
            </ListItem>
            <ListItem key="elementC">
              <ListItemText id="ElementC" primary="Element C" />
            </ListItem>
            <ListItem key="elementD">
              <ListItemText id="ElementD" primary="Element D" />
            </ListItem>
          </List>
        </Grid>
      </Grid>
    </Stack>
  );
}
