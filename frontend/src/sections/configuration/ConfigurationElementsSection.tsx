import * as React from 'react';
import {
  Checkbox,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Stack,
} from '@mui/material';

export default function ConfigurationElementsSection() {
  return (
    <Stack spacing={1} height="100%">
      <List dense>
        <ListItem key="elementA" secondaryAction={<Checkbox edge="end" />}>
          <ListItemButton>
            <ListItemText id="ElementA" primary="Element A" />
          </ListItemButton>
        </ListItem>
        <ListItem key="elementB" secondaryAction={<Checkbox edge="end" />}>
          <ListItemButton>
            <ListItemText id="ElementB" primary="Element B" />
          </ListItemButton>
        </ListItem>
        <ListItem key="elementC" secondaryAction={<Checkbox edge="end" />}>
          <ListItemButton>
            <ListItemText id="ElementC" primary="Element C" />
          </ListItemButton>
        </ListItem>
        <ListItem key="elementD" secondaryAction={<Checkbox edge="end" />}>
          <ListItemButton>
            <ListItemText id="ElementD" primary="Element D" />
          </ListItemButton>
        </ListItem>
      </List>
    </Stack>
  );
}
