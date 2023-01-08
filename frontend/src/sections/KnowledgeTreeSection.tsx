import * as React from 'react';
import {ReactElement} from 'react';
import {Stack} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import {TreeItem, TreeView} from '@mui/lab';

const VersionsSelect = React.lazy(() => import('./VersionsSelect'));

type KnowledgeTreeSectionProps = {
  knowledgeTree: any | undefined;
  onSelectedVersion: (version: string) => void;
};

export default function KnowledgeTreeSection(props: KnowledgeTreeSectionProps) {
  const {knowledgeTree, onSelectedVersion} = props;

  const getNodesIds = (node: any): string[] => {
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
    if (!node.and && !node.feature) {
      return (
        <TreeItem nodeId={node[':@']['@_name']} label={node[':@']['@_name']} />
      );
    }
    return (
      <TreeItem nodeId={node[':@']['@_name']} label={node[':@']['@_name']}>
        {node.and &&
          (Array.isArray(node.and)
            ? node.and.map((n) => getNodes(n))
            : getNodes(node.and))}
      </TreeItem>
    );
  };

  return (
    <Stack spacing={1} height="100%">
      <Stack direction="row" padding={1}>
        <VersionsSelect onSelectedVersion={onSelectedVersion} />
      </Stack>
      {knowledgeTree && (
        <TreeView
          aria-label="knowledge-tree"
          defaultCollapseIcon={<ExpandMoreIcon />}
          defaultExpandIcon={<ChevronRightIcon />}
          defaultExpanded={getNodesIds(knowledgeTree)}
        >
          {getNodes(knowledgeTree)}
        </TreeView>
      )}
    </Stack>
  );
}
