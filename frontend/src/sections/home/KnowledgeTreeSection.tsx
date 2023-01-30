import * as React from 'react';
import {ReactElement, useEffect} from 'react';
import {CircularProgress, Stack, Tooltip, Typography} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import {TreeItem, TreeView} from '@mui/lab';
import InfoIcon from '@mui/icons-material/Info';
import {useAppDispatch, useAppSelector} from '../../store/hooks';
import {useGetKnowledgeTreeQuery} from '../../store/api/knowledgeApi';
import {showSnackbar} from '../../store/reducers/SnackbarSlice';

const VersionsSelect = React.lazy(() => import('./VersionsSelect'));

export default function KnowledgeTreeSection() {
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

  const getNodesIds = (node: any): string[] => {
    if (node.description) {
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

  const getNodes = (node: any): ReactElement => {
    const description = Array.isArray(node.and)
      ? node.and
          .filter((n) => n.description && n.description[0]['#text'])
          .map((n) => n.description[0]['#text'])[0]
      : undefined;
    if (!node.and && !node.feature) {
      return (
        <TreeItem
          key={`tree-item-knowledge-${node[':@']['@_name']}`}
          nodeId={node[':@']['@_name']}
          label={node[':@']['@_name']}
        />
      );
    }
    return (
      <TreeItem
        key={`tree-item-knowledge-${node[':@']['@_name']}`}
        nodeId={node[':@']['@_name']}
        label={
          description ? (
            <Stack spacing={1} direction="row">
              <Typography>{node[':@']['@_name']}</Typography>
              <Tooltip title={description}>
                <InfoIcon />
              </Tooltip>
            </Stack>
          ) : (
            <Typography>{node[':@']['@_name']}</Typography>
          )
        }
      >
        {node.and &&
          (Array.isArray(node.and)
            ? node.and.filter((n) => !n.description).map((n) => getNodes(n))
            : getNodes(node.and))}
      </TreeItem>
    );
  };

  return (
    <Stack spacing={1} height="100%">
      <Stack direction="row" padding={1}>
        <VersionsSelect />
      </Stack>
      {isFetching && <CircularProgress />}
      {isSuccess && knowledgeTree?.extendedFeatureModel[0].struct[0] && (
        <TreeView
          aria-label="knowledge-tree"
          defaultCollapseIcon={<ExpandMoreIcon />}
          defaultExpandIcon={<ChevronRightIcon />}
          defaultExpanded={getNodesIds(
            knowledgeTree.extendedFeatureModel[0].struct[0],
          )}
        >
          {getNodes(knowledgeTree.extendedFeatureModel[0].struct[0])}
        </TreeView>
      )}
    </Stack>
  );
}