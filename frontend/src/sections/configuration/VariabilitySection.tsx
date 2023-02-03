import * as React from 'react';
import {useEffect} from 'react';
import {CircularProgress, Stack} from '@mui/material';
import {DataGrid, GridColDef} from '@mui/x-data-grid';
import {useGetAllConfigurationsQuery} from '../../store/api/graphql/configurationGraphqlApi';
import {showSnackbar} from '../../store/reducers/SnackbarSlice';
import {useAppDispatch} from '../../store/hooks';
import {FeatureSelectionStatus} from '../../store/types';

export default function VariabilitySection() {
  const {
    data: configurations,
    isSuccess,
    isError,
    error,
    isFetching,
  } = useGetAllConfigurationsQuery();

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

  const allFeatures = configurations?.configurations.length
    ? configurations?.configurations[0].features
    : [];
  const nbFeatures = allFeatures.length;

  const columns: GridColDef[] = [
    {field: 'id', headerName: 'Feature', width: 200},
    {
      field: 'selected',
      headerName: 'Selected',
      valueFormatter: (p) => `${p.value}/${nbFeatures}`,
    },
    {
      field: 'unselected',
      headerName: 'Unselected',
      valueFormatter: (p) => `${p.value}/${nbFeatures}`,
    },
    {
      field: 'undefined',
      headerName: 'Undefined',
      valueFormatter: (p) => `${p.value}/${nbFeatures}`,
    },
  ];

  const variability = allFeatures.map((f) =>
    configurations?.configurations.reduce(
      (r, c) => {
        const result = c.features.find((cf) => cf.name === f.name);
        if (
          result?.manual === FeatureSelectionStatus.SELECTED ||
          result?.automatic === FeatureSelectionStatus.SELECTED
        ) {
          r.selected += 1;
        }
        if (
          result?.manual === FeatureSelectionStatus.UNSELECTED ||
          result?.automatic === FeatureSelectionStatus.UNSELECTED
        ) {
          r.unselected += 1;
        }
        if (
          result?.manual === FeatureSelectionStatus.UNDEFINED &&
          result?.automatic === FeatureSelectionStatus.UNDEFINED
        ) {
          r.undefined += 1;
        }
        return r;
      },
      {id: f.name, selected: 0, unselected: 0, undefined: 0},
    ),
  );

  return (
    <Stack spacing={1} height="100%">
      <DataGrid
        density="compact"
        rows={variability}
        columns={columns}
        pageSize={5}
        rowsPerPageOptions={[5]}
        disableSelectionOnClick
      />
    </Stack>
  );
}
