import * as React from 'react';
import {useEffect} from 'react';
import {CircularProgress, Stack} from '@mui/material';
import {useAppDispatch, useAppSelector} from '../../store/hooks';
import {useGetKnowledgeTreeQuery} from '../../store/api/knowledgeApi';
import {showSnackbar} from '../../store/reducers/SnackbarSlice';
import {
  setConfigurationFeatures,
  updateConfigurationFeature,
} from '../../store/reducers/ConfigurationSlice';
import {
  ConfigurationFeature,
  FeatureSelectionStatus,
} from '../../store/api/configurationApi';
import ConfigurationComponent from './components/ConfigurationComponent';

export default function ConfigurationElementsSection() {
  const {currentVersion} = useAppSelector((state) => state.version);
  const {
    data: knowledgeTree,
    isSuccess,
    isError,
    error,
    isFetching,
  } = useGetKnowledgeTreeQuery(currentVersion, {skip: currentVersion === ''});
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

  const getNodesIds = (node: any): string[] => {
    if (!node || node.description) {
      return [];
    }
    let result = [node[':@']['@_name']];
    if (node.and) {
      result = [
        ...result,
        ...(Array.isArray(node.and)
          ? node.and.flatMap((n) => getNodesIds(n))
          : getNodesIds(node.and)),
      ];
    }
    return result;
  };

  useEffect(() => {
    const features = getNodesIds(
      knowledgeTree?.extendedFeatureModel[0].struct[0],
    ).map(
      (f) =>
        ({
          name: f,
          automatic: FeatureSelectionStatus.UNDEFINED,
          manual: FeatureSelectionStatus.UNSELECTED,
        } as ConfigurationFeature),
    );
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
