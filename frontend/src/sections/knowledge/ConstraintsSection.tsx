import * as React from 'react';
import {Box, CircularProgress, Stack, Typography} from '@mui/material';
import {useEffect} from 'react';
import {useAppDispatch, useAppSelector} from '../../store/hooks';
import {useGetFeatureModelQuery} from '../../store/api/rest/knowledgeRestApi';
import {showSnackbar} from '../../store/reducers/SnackbarSlice';
import {AbstractOperand} from '../../store/types';

const getOperatorRepresentation = (o: AbstractOperand) => {
  if (o.operandName === 'imp')
    return <span style={{color: 'red'}}>implies</span>;
  if (o.operandName === 'conj') return <span style={{color: 'red'}}>and</span>;
  if (o.operandName === 'disj') return <span style={{color: 'red'}}>or</span>;
  if (o.operandName === 'not') return <span style={{color: 'red'}}>not</span>;
  if (o.value) return <span>{o.value}</span>;
  return <span style={{color: 'grey'}}>error</span>;
};

export default function ConstraintsSection() {
  const {currentVersion} = useAppSelector((state) => state.version);
  const {
    data: knowledgeTree,
    isSuccess,
    isError,
    error,
    isFetching,
  } = useGetFeatureModelQuery(
    {versionName: currentVersion},
    {skip: currentVersion === ''},
  );
  const dispatch = useAppDispatch();

  useEffect(() => {
    if (!isError) {
      return;
    }
    dispatch(
      showSnackbar({
        severity: 'error',
        message: `Failed to retrieve constraints for version ${currentVersion}.`,
      }),
    );
  }, [isError]);

  useEffect(() => {
    if (isError && error) {
      console.error(
        `Failed to retrieve knowledge tree for version ${currentVersion}. Error is ${error.status}: ${error.error}`,
      );
    }
  }, [isError, error]);

  const getConstraint = (c: AbstractOperand, isRoot: boolean) => {
    const operatorRepresentation = getOperatorRepresentation(c);
    if (c.operands?.length === 1) {
      return (
        <span>
          {!isRoot && ' ('}
          {operatorRepresentation}
          {c.operands[0] && getConstraint(c.operands[0], false)}
          {!isRoot && ' )'}
        </span>
      );
    }
    return (
      <span>
        {!isRoot && c?.operands && c?.operands[0] && '('}
        {c?.operands &&
          c?.operands[0] &&
          getConstraint(c.operands[0], false)}{' '}
        {operatorRepresentation}
        {c?.operands &&
          c?.operands.slice(1).map((o) => getConstraint(o, false))}
        {!isRoot && c?.operands && c?.operands[0] && ')'}
      </span>
    );
  };

  return (
    <Stack spacing={1} height="100%">
      <Typography>Constraints</Typography>
      {isFetching && <CircularProgress />}
      {isSuccess &&
        knowledgeTree?.constraints?.map(
          (c) =>
            c.constraint?.operator && (
              <Box>
                <Typography>
                  {getConstraint(c.constraint.operator, true)}
                </Typography>
              </Box>
            ),
        )}
    </Stack>
  );
}
