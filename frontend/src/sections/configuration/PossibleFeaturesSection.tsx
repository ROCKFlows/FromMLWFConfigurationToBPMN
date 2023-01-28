import * as React from 'react';
import {
  CircularProgress,
  Grid,
  List,
  ListItem,
  ListItemText,
  Stack,
  Typography,
} from '@mui/material';
import {useAppDispatch, useAppSelector} from '../../store/hooks';
import {
  FeatureSelectionStatus,
  useGetAllConfigurationsQuery,
} from '../../store/api/configurationApi';
import {useEffect} from 'react';
import {showSnackbar} from '../../store/reducers/SnackbarSlice';

export default function PossibleFeaturesSection() {
  const {
    data: configurations,
    isSuccess,
    isError,
    error,
    isFetching,
  } = useGetAllConfigurationsQuery();
  const {currentConfiguration} = useAppSelector((state) => state.configuration);

  const dispatch = useAppDispatch();

  useEffect(() => {
    if (!(isSuccess || isError)) {
      return;
    }
    dispatch(
      showSnackbar({
        severity: isError ? 'error' : 'success',
        message: isError
          ? `Failed to retrieve configurations.`
          : `Configurations list successfully retrieved.`,
      }),
    );
  }, [isSuccess, isError]);

  useEffect(() => {
    if (isError && error) {
      console.error(
        `Failed to retrieve configurations. Error is ${error.status}: ${error.error}`,
      );
    }
  }, [isError, error]);

  if (isFetching) {
    return <CircularProgress />;
  }

  const unselectedFeatures = currentConfiguration.features
    .filter(
      (f) =>
        f.manual === FeatureSelectionStatus.UNSELECTED ||
        f.automatic === FeatureSelectionStatus.UNSELECTED ||
        f.manual === FeatureSelectionStatus.UNDEFINED ||
        f.automatic === FeatureSelectionStatus.UNDEFINED,
    )
    .map((f) => f.name);

  return (
    <Stack spacing={1} height="100%">
      <Grid container>
        <Grid item xs={6} textAlign="center">
          <Typography>
            <b>Possible</b>
          </Typography>
          <List>
            {configurations?.configurations
              .filter(
                (c) =>
                  !c.features.find(
                    (f) =>
                      (f.manual === FeatureSelectionStatus.SELECTED ||
                        f.automatic === FeatureSelectionStatus.SELECTED) &&
                      unselectedFeatures.includes(f.name),
                  ),
              )
              .map((c) => (
                <ListItem key={`list-item-possible-${c.name}`}>
                  <ListItemText
                    key={`list-item-possible-${c.name}`}
                    id={`list-item-possible-${c.name}`}
                    primary={c.name}
                  />
                </ListItem>
              ))}
          </List>
        </Grid>
        <Grid item xs={6} textAlign="center">
          <Typography>
            <b>Impossible</b>
          </Typography>
          <List>
            {configurations?.configurations
              .filter((c) =>
                c.features.find(
                  (f) =>
                    (f.manual === FeatureSelectionStatus.SELECTED ||
                      f.automatic === FeatureSelectionStatus.SELECTED) &&
                    unselectedFeatures.includes(f.name),
                ),
              )
              .map((c) => (
                <ListItem key={`list-item-possible-${c.name}`}>
                  <ListItemText
                    key={`list-item-possible-${c.name}`}
                    id={`list-item-possible-${c.name}`}
                    primary={c.name}
                  />
                </ListItem>
              ))}
          </List>
        </Grid>
      </Grid>
    </Stack>
  );
}
