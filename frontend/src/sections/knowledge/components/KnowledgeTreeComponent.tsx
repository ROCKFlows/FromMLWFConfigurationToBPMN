import * as React from 'react';
import {ReactElement} from 'react';
import {Stack, Tooltip, Typography} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import {TreeItem, TreeView} from '@mui/lab';
import InfoIcon from '@mui/icons-material/Info';
import {KnowledgeTree} from '../../../store/api/knowledgeApi';

type KnowledgeTreeComponentProps = {
  knowledgeTree: KnowledgeTree;
};

export default function KnowledgeTreeComponent(
  props: KnowledgeTreeComponentProps,
) {
  const {knowledgeTree} = props;

  const structure = knowledgeTree.extendedFeatureModel[0].struct
    ? knowledgeTree.extendedFeatureModel[0].struct[0]
    : knowledgeTree.extendedFeatureModel[1].struct[0]; // skipping properties

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
      <TreeView
        aria-label="knowledge-tree"
        defaultCollapseIcon={<ExpandMoreIcon />}
        defaultExpandIcon={<ChevronRightIcon />}
        defaultExpanded={getNodesIds(structure)}
      >
        {getNodes(structure)}
      </TreeView>
    </Stack>
  );
}
