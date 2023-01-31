import * as React from 'react';
import {Box, CircularProgress, Grid, Paper} from '@mui/material';
import {Suspense, useEffect} from 'react';
import {useParams} from 'react-router';
import {useNavigate} from 'react-router-dom';
import {changeVersion} from '../store/reducers/VersionSlice';
import {useAppDispatch} from '../store/hooks';
import {useGetVersionsQuery} from '../store/api/versionApi';

const KnowledgeTreeSection = React.lazy(() =>
  import('../sections/knowledge/KnowledgeTreeSection'),
);
const WorkflowSection = React.lazy(() =>
  import('../sections/knowledge/WorkflowSection'),
);
const ConstraintsSection = React.lazy(() =>
  import('../sections/knowledge/ConstraintsSection'),
);

export default function KnowledgePage() {
  const {data: versions, isError, error, isFetching} = useGetVersionsQuery();

  const dispatch = useAppDispatch();

  const {version: urlVersion} = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    if (isError && error) {
      console.error(
        `Failed to retrieve versions. Error is ${error.status}: ${error.error}`,
      );
    }
    if (isError) {
      // TODO: snackbar
    }
  }, [isError, error]);

  useEffect(() => {
    if (isFetching) {
      return;
    }
    if (!urlVersion) {
      if (versions === undefined || versions.versions.length === 0) {
        navigate('/workflow/new');
      } else {
        navigate(
          `/knowledge/${
            versions?.versions.reduce((vA, vB) =>
              vA.major * 100 + vA.minor * 10 + vA.patch >
              vB.major * 100 + vB.minor * 10 + vB.patch
                ? vA
                : vB,
            ).name
          }`,
        );
      }
    } else if (!versions?.versions.find((v) => v.name === urlVersion)) {
      navigate('/404');
    } else {
      dispatch(changeVersion(urlVersion));
    }
  }, [isFetching, urlVersion]);

  if (isFetching) {
    return <CircularProgress />;
  }

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
