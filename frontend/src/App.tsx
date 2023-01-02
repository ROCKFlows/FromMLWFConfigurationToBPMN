import * as React from 'react';
import {Stack, Typography} from "@mui/material";
import {TreeItem, TreeView} from "@mui/lab";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';

function App() {

  return (
      <Stack>
          <Typography>ml2wf</Typography>
          <TreeView
              aria-label="knowledge-tree"
              defaultCollapseIcon={<ExpandMoreIcon />}
              defaultExpandIcon={<ChevronRightIcon />}
          >
              <TreeItem nodeId="1" label="nodeA">
                  <TreeItem nodeId="2" label="nodeA-1" />
              </TreeItem>
              <TreeItem nodeId="5" label="nodeB">
                  <TreeItem nodeId="10" label="nodeB-1" />
              </TreeItem>
          </TreeView>
      </Stack>
  )
}

export default App
