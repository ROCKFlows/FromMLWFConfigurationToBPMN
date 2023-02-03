import * as React from 'react';
import {useEffect} from 'react';
import {CircularProgress, Stack} from '@mui/material';
import {useAppDispatch, useAppSelector} from '../../store/hooks';
import {useGetFeatureModelQuery} from '../../store/api/rest/knowledgeRestApi';
import {showSnackbar} from '../../store/reducers/SnackbarSlice';
import {
  setConfigurationFeatures,
  updateConfigurationFeature,
} from '../../store/reducers/ConfigurationSlice';
import type {ConfigurationFeature} from '../../store/types';
import ConfigurationComponent from './components/ConfigurationComponent';
import {FeatureModelTask, FeatureSelectionStatus} from '../../store/types';

export default function ConfigurationElementsSection() {
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
          ? `Failed to retrieve features list for version ${currentVersion}.`
          : `Features list successfully retrieved for version ${currentVersion}.`,
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

  const getNodesIds = (task: FeatureModelTask): string[] => [
    task.name,
    ...(task.children?.length ? task.children : []).flatMap((c) =>
      getNodesIds(c),
    ),
  ];

  useEffect(() => {
    const features =
      knowledgeTree?.structure?.children && knowledgeTree.structure.children[0]
        ? getNodesIds(knowledgeTree.structure.children[0]).map(
            (f) =>
              ({
                name: f,
                automatic: FeatureSelectionStatus.UNDEFINED,
                manual: FeatureSelectionStatus.UNSELECTED,
              } as ConfigurationFeature),
          )
        : [];
    dispatch(setConfigurationFeatures({features}));
  }, [currentVersion]);

  if (isFetching) {
    return <CircularProgress />;
  }

  return (
    <Stack spacing={1} height="100%">
      <ConfigurationComponent
        configuration={currentConfiguration}
        onSelected={(f) => dispatch(updateConfigurationFeature(f))}
      />
    </Stack>
  );
}
