import * as React from 'react';
import {useEffect} from 'react';
import {CircularProgress, Stack} from '@mui/material';
import {useAppDispatch, useAppSelector} from '../../store/hooks';
import {useGetFeatureModelQuery} from '../../store/api/rest/knowledgeRestApi';
import {showSnackbar} from '../../store/reducers/SnackbarSlice';
import KnowledgeTreeComponent from './components/KnowledgeTreeComponent';

const VersionsSelect = React.lazy(() => import('./VersionsSelect'));

export default function KnowledgeTreeSection() {
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
    if (!(isSuccess || isError)) {
      return;
    }
    dispatch(
      showSnackbar({
        severity: isError ? 'error' : 'success',
        message: isError
          ? `Failed to retrieve knowledge tree for version ${currentVersion}.`
          : `Knowledge tree successfully retrieved for version ${currentVersion}.`,
      }),
    );
  }, [isSuccess, isError]);

  useEffect(() => {
    if (isError && error) {
      console.error(
        `Failed to retrieve knowledge tree for version ${currentVersion}. Error is ${error.status}: ${error.error}`,
      );
    }
  }, [isError, error]);

  return (
    <Stack spacing={1} height="100%">
      <Stack direction="row" padding={1}>
        <VersionsSelect />
      </Stack>
      {isFetching && <CircularProgress />}
      {isSuccess && knowledgeTree.structure && (
        <KnowledgeTreeComponent knowledgeTree={knowledgeTree} />
      )}
    </Stack>
  );
}
