import * as React from 'react';
import {Suspense} from 'react';
import {
  Alert,
  Box,
  CircularProgress,
  Grid,
  Paper,
  Snackbar,
  Stack,
} from '@mui/material';
import TopToolbar from './TopToolbar';
import {useAppDispatch, useAppSelector} from './store/hooks';
import {hideSnackbar} from './store/reducers/SnackbarSlice';

const KnowledgeTreeSection = React.lazy(() =>
  import('./sections/KnowledgeTreeSection'),
);
const WorkflowSection = React.lazy(() => import('./sections/WorkflowSection'));
const ConstraintsSection = React.lazy(() =>
  import('./sections/ConstraintsSection'),
);

function App() {
  const snackbar = useAppSelector((state) => state.snackbar);

  const dispatch = useAppDispatch();

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
              <KnowledgeTreeSection />
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
                  <ConstraintsSection />
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
                  <WorkflowSection />
                </Suspense>
              </Paper>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
      <Snackbar
        open={snackbar.show}
        autoHideDuration={6000}
        onClose={() => dispatch(hideSnackbar())}
      >
        <Alert
          onClose={() => dispatch(hideSnackbar())}
          severity={snackbar.severity}
          sx={{width: '100%'}}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Stack>
  );
}

export default App;
