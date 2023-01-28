import * as React from 'react';
import {Box, CircularProgress, Grid, Paper} from '@mui/material';
import {Suspense} from 'react';

const ConfigurationElementsSection = React.lazy(() =>
  import('../sections/configuration/ConfigurationElementsSection'),
);
const VariabilitySection = React.lazy(() =>
  import('../sections/configuration/VariabilitySection'),
);
const CommonalitySection = React.lazy(() =>
  import('../sections/configuration/CommonalitySection'),
);
const PossibleFeaturesSection = React.lazy(() =>
  import('../sections/configuration/PossibleFeaturesSection'),
);

export default function ConfigurationPage() {
  return (
    <Grid
      container
      direction="column"
      rowSpacing={1}
      columnSpacing={{xs: 1, sm: 2, md: 3}}
      sx={{height: '90vh', padding: '1em'}}
    >
      <Grid item xs={5}>
        <Grid container direction="column" rowSpacing={1} sx={{height: '90vh'}}>
          <Grid item xs={7} overflow="auto">
            <Paper sx={{height: '100%', padding: '1em'}}>
              <Suspense
                fallback={
                  <Box sx={{display: 'flex'}}>
                    <CircularProgress />
                  </Box>
                }
              >
                <ConfigurationElementsSection />
              </Suspense>
            </Paper>
          </Grid>
          <Grid item xs={5} overflow="auto">
            <Paper sx={{height: '100%', padding: '1em', overflow: 'auto'}}>
              <Suspense
                fallback={
                  <Box sx={{display: 'flex'}}>
                    <CircularProgress />
                  </Box>
                }
              >
                <VariabilitySection />
              </Suspense>
            </Paper>
          </Grid>
        </Grid>
      </Grid>
      <Grid item xs={7}>
        <Grid container direction="column" rowSpacing={1} sx={{height: '90vh'}}>
          <Grid item xs={6} overflow="auto">
            <Paper sx={{height: '100%', padding: '1em', overflow: 'auto'}}>
              <Suspense
                fallback={
                  <Box sx={{display: 'flex'}}>
                    <CircularProgress />
                  </Box>
                }
              >
                <PossibleFeaturesSection />
              </Suspense>
            </Paper>
          </Grid>
          <Grid item xs={6} overflow="auto">
            <Paper sx={{height: '100%', padding: '1em', overflow: 'auto'}}>
              <Suspense
                fallback={
                  <Box sx={{display: 'flex'}}>
                    <CircularProgress />
                  </Box>
                }
              >
                <CommonalitySection />
              </Suspense>
            </Paper>
          </Grid>
        </Grid>
      </Grid>
    </Grid>
  );
}
