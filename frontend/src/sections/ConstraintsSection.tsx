import * as React from 'react';
import {Box, CircularProgress, Stack, Typography} from '@mui/material';
import {useEffect} from 'react';
import {useAppDispatch, useAppSelector} from '../store/hooks';
import {useGetKnowledgeTreeQuery} from '../store/api/knowledgeApi';
import {showSnackbar} from '../store/reducers/SnackbarSlice';

const getOperatorRepresentation = (o) => {
  if (o.imp) return <span style={{color: 'red'}}>implies</span>;
  if (o.conj) return <span style={{color: 'red'}}>and</span>;
  if (o.disj) return <span style={{color: 'red'}}>or</span>;
  if (o.not) return <span style={{color: 'red'}}>not</span>;
  if (o.var) return <span>{o.var[0]['#text']}</span>;
  return <span style={{color: 'grey'}}>error</span>;
};

const getConstraintOperator = (c) => {
  if (c.imp) return c.imp;
  if (c.conj) return c.conj;
  if (c.disj) return c.disj;
  if (c.not) return c.not;
};

export default function ConstraintsSection() {
  const {currentVersion} = useAppSelector((state) => state.version);
  const {
    data: knowledgeTree,
    isSuccess,
    isError,
    error,
    isFetching,
  } = useGetKnowledgeTreeQuery(currentVersion, {skip: currentVersion === ''});
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

  const getConstraint = (c, isRoot: boolean) => {
    const operatorRepresentation = getOperatorRepresentation(c);
    const operator = getConstraintOperator(c);
    if (operator?.length === 1) {
      return (
        <span>
          {!isRoot && operator && ' ('}
          {operatorRepresentation}
          {operator && operator[0] && getConstraint(operator[0], false)}
          {!isRoot && operator && ' )'}
        </span>
      );
    }
    return (
      <span>
        {!isRoot && operator && '('}
        {operator && operator[0] && getConstraint(operator[0], false)}{' '}
        {operatorRepresentation}
        {operator && operator.slice(1).map(getConstraint)}
        {!isRoot && operator && ' )'}
      </span>
    );
  };

  return (
    <Stack spacing={1} height="100%">
      <Typography>Constraints</Typography>
      {isFetching && <CircularProgress />}
      {isSuccess &&
        knowledgeTree?.extendedFeatureModel[1].constraints?.map((r) => (
          <Box>
            <Typography>{getConstraint(r.rule[0], true)}</Typography>
          </Box>
        ))}
    </Stack>
  );
}
