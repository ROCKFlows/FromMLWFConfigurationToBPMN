import * as React from 'react';
import {Suspense, useEffect, useState} from 'react';
import {
  Alert,
  AlertColor,
  Box,
  CircularProgress,
  Grid,
  Paper,
  Snackbar,
  Stack,
} from '@mui/material';
import axios from 'axios';
import {XMLParser} from 'fast-xml-parser';
import TopToolbar from './TopToolbar';

const KnowledgeTreeSection = React.lazy(() =>
  import('./sections/KnowledgeTreeSection'),
);
const WorkflowSection = React.lazy(() => import('./sections/WorkflowSection'));
const ConstraintsSection = React.lazy(() =>
  import('./sections/ConstraintsSection'),
);

function App() {
  const [version, setVersion] = useState<string>('');
  const [knowledgeTree, setKnowledgeTree] = useState<
    {extendedFeatureModel: {constraints: any; struct: any}} | undefined
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
        setKnowledgeTree(
          new XMLParser({
            ignoreAttributes: false,
            attributeNamePrefix: '@_',
            allowBooleanAttributes: true,
          }).parse(r.data),
        );
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
            <Suspense
              fallback={
                <Box sx={{display: 'flex'}}>
                  <CircularProgress />
                </Box>
              }
            >
              <KnowledgeTreeSection
                knowledgeTree={knowledgeTree?.extendedFeatureModel?.struct}
                onSelectedVersion={(v: string) => setVersion(v)}
              />
            </Suspense>
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
                <Suspense
                  fallback={
                    <Box sx={{display: 'flex'}}>
                      <CircularProgress />
                    </Box>
                  }
                >
                  <ConstraintsSection
                    constraints={
                      knowledgeTree?.extendedFeatureModel?.constraints
                    }
                  />
                </Suspense>
              </Paper>
            </Grid>
            <Grid item xs={6}>
              <Paper sx={{height: '44vh', padding: '1em', overflow: 'auto'}}>
                <Suspense
                  fallback={
                    <Box sx={{display: 'flex'}}>
                      <CircularProgress />
                    </Box>
                  }
                >
                  <WorkflowSection version={version} onImported={getTree} />
                </Suspense>
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
