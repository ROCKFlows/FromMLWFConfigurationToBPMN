import * as React from 'react';
import {useEffect, useState} from 'react';
import {Alert, AlertColor, Grid, Paper, Snackbar, Stack} from '@mui/material';
import axios from 'axios';
import {XMLParser} from 'fast-xml-parser';
import TopToolbar from './TopToolbar';
import KnowledgeTreeSection from './sections/KnowledgeTreeSection';
import WorkflowSection from './sections/WorkflowSection';
import ConstraintsSection from './sections/ConstraintsSection';

function App() {
  const [version, setVersion] = useState<string>('');
  const [knowledgeTree, setKnowledgeTree] = useState<
    {FeatureModel: {constraints: any; structure: any}} | undefined
  >(undefined);
  const [isSnackbarOpen, setSnackbarOpen] = useState<boolean>(false);
  const [snackbarSeverity, setSnackbarSeverity] = useState<
    AlertColor | undefined
  >(undefined);
  const [snackbarMessage, setSnackbarMessage] = useState<string | undefined>(
    undefined,
  );

  const getTree = () => {
    axios
      .get(`http://localhost:8080/ml2wf/api/v1/fm?versionName=${version}`)
      .then((r) => {
        setKnowledgeTree(new XMLParser().parse(r.data));
        setSnackbarSeverity('success');
        setSnackbarMessage('Knowledge tree successfully retrieved.');
      })
      .catch(() => {
        setSnackbarSeverity('error');
        setSnackbarMessage('Failed to retrieve knowledge tree.');
      })
      .finally(() => {
        setSnackbarOpen(true);
      });
  };

  useEffect(() => {
    if (!version) {
      return;
    }
    getTree();
  }, [version]);

  return (
    <Stack>
      <TopToolbar pages={['Home']} />
      <Grid
        container
        direction="column"
        rowSpacing={1}
        columnSpacing={{xs: 1, sm: 2, md: 3}}
        sx={{height: '90vh', padding: '1em'}}
      >
        <Grid item xs={12}>
          <Paper sx={{height: '89vh', padding: '1em', overflow: 'auto'}}>
            <KnowledgeTreeSection
              knowledgeTree={knowledgeTree?.FeatureModel?.structure}
              onVersionSelected={(v: string) => setVersion(v)}
            />
          </Paper>
        </Grid>
        <Grid item>
          <Grid
            container
            direction="column"
            rowSpacing={1}
            sx={{height: '90vh'}}
          >
            <Grid item xs={6}>
              <Paper sx={{height: '44vh', padding: '1em', overflow: 'auto'}}>
                <ConstraintsSection
                  constraints={knowledgeTree?.FeatureModel?.constraints}
                />
              </Paper>
            </Grid>
            <Grid item xs={6}>
              <Paper sx={{height: '44vh', padding: '1em', overflow: 'auto'}}>
                <WorkflowSection version={version} onImported={getTree} />
              </Paper>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
      <Snackbar
        open={isSnackbarOpen}
        autoHideDuration={6000}
        onClose={() => setSnackbarOpen(false)}
      >
        <Alert
          onClose={() => setSnackbarOpen(false)}
          severity={snackbarSeverity}
          sx={{width: '100%'}}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </Stack>
  );
}

export default App;
