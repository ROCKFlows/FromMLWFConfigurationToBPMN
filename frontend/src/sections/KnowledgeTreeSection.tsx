import * as React from 'react';
import {ReactElement, useState} from 'react';
import {Button, Stack, TextField} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import {TreeItem, TreeView} from '@mui/lab';

type KnowledgeTreeSectionProps = {
  knowledgeTree: any | undefined;
  onVersionSelected: (version: string) => void;
};

export default function KnowledgeTreeSection(props: KnowledgeTreeSectionProps) {
  const {knowledgeTree, onVersionSelected} = props;

  const [tmpVersion, setTmpVersion] = useState<string>('');

  const getNodesIds = (node: any): string[] => {
    let result = [node['@_name']];
    if (node.and) {
      result = [
        ...result,
        ...(Array.isArray(node.and)
          ? node.and.flatMap((n) => getNodesIds(n))
          : getNodesIds(node.and)),
      ];
    }
    if (node.feature) {
      result = [
        ...result,
        ...(Array.isArray(node.feature)
          ? node.feature.flatMap((n) => getNodesIds(n))
          : getNodesIds(node.feature)),
      ];
    }
    return result;
  };

  const getNodes = (node: any): ReactElement => {
    if (!node.and && !node.feature) {
      return <TreeItem nodeId={node['@_name']} label={node['@_name']} />;
    }
    return (
      <TreeItem nodeId={node['@_name']} label={node['@_name']}>
        {node.and &&
          (Array.isArray(node.and)
            ? node.and.map((n) => getNodes(n))
            : getNodes(node.and))}
        {node.feature &&
          (Array.isArray(node.feature)
            ? node.feature.map((n) => getNodes(n))
            : getNodes(node.feature))}
      </TreeItem>
    );
  };

  return (
    <Stack spacing={1} height="100%">
      <Stack direction="row" padding={1}>
        <TextField
          label="Version"
          variant="outlined"
          value={tmpVersion}
          onChange={(e) => setTmpVersion(e.target.value)}
        />
        <Button
          variant="outlined"
          onClick={() => onVersionSelected(tmpVersion)}
        >
          Confirm
        </Button>
      </Stack>
      {knowledgeTree && (
        <TreeView
          aria-label="knowledge-tree"
          defaultCollapseIcon={<ExpandMoreIcon />}
          defaultExpandIcon={<ChevronRightIcon />}
          defaultExpanded={getNodesIds(knowledgeTree.and)}
        >
          {getNodes(knowledgeTree.and)}
        </TreeView>
      )}
    </Stack>
  );
}
