import * as React from 'react';
import {Box, CircularProgress, Grid, Paper} from '@mui/material';
import {Suspense} from 'react';

const KnowledgeTreeSection = React.lazy(() =>
  import('../sections/KnowledgeTreeSection'),
);
const WorkflowSection = React.lazy(() => import('../sections/WorkflowSection'));
const ConstraintsSection = React.lazy(() =>
  import('../sections/ConstraintsSection'),
);

export default function HomePage() {
  return (
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
        <Grid container direction="column" rowSpacing={1} sx={{height: '90vh'}}>
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
  );
}
