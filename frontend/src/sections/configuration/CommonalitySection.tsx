import * as React from 'react';
import {useEffect} from 'react';
import {
  CircularProgress,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from '@mui/material';
import {useTheme} from '@mui/material/styles';
import {
  FeatureSelectionStatus,
  useGetAllConfigurationsQuery,
} from '../../store/api/configurationApi';
import {useAppDispatch} from '../../store/hooks';
import {showSnackbar} from '../../store/reducers/SnackbarSlice';

export default function CommonalitySection() {
  const {
    data: configurations,
    isSuccess,
    isError,
    error,
    isFetching,
  } = useGetAllConfigurationsQuery();

  const dispatch = useAppDispatch();

  const theme = useTheme();

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

  const commonality = configurations?.configurations[0].features.filter(
    (f) =>
      f.manual !== FeatureSelectionStatus.UNDEFINED &&
      !configurations.configurations.find((c) =>
        c.features.find(
          (cf) =>
            f.name === cf.name &&
            (f.manual !== cf.manual || f.automatic !== cf.automatic),
        ),
      ),
  );

  return (
    <Stack spacing={1} height="100%" style={{overflowY: 'auto'}}>
      <Typography textAlign="center">
        <b>Commonality</b>
      </Typography>
      <TableContainer>
        <Table aria-label="commonality table">
          <TableHead
            style={{
              position: 'sticky',
              top: 0,
              backgroundColor: theme.palette.background.paper,
            }}
          >
            <TableRow>
              <TableCell align="center">Feature Name</TableCell>
              <TableCell align="center">Status</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {commonality?.map((f) => (
              <TableRow key={`row-feature-${f.name}`}>
                <TableCell key={`cell-feature-name-${f.name}`} align="center">
                  {f.name}
                </TableCell>
                <TableCell
                  key={`cell-feature-manual-${f.manual}`}
                  align="center"
                >
                  {f.manual}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Stack>
  );
}
