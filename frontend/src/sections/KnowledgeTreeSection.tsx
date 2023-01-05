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

  const getNodes = (node: any): ReactElement => {
    if (!node.children) {
      return <TreeItem nodeId={node.name} label={node.name} />;
    }
    if (!node.name) {
      return Array.isArray(node.children)
        ? node.children.map(getNodes)
        : getNodes(node.children);
    }
    return (
      <TreeItem nodeId={node.name} label={node.name}>
        {Array.isArray(node.children)
          ? node.children.map(getNodes)
          : getNodes(node.children)}
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
      <TreeView
        aria-label="knowledge-tree"
        defaultCollapseIcon={<ExpandMoreIcon />}
        defaultExpandIcon={<ChevronRightIcon />}
      >
        {knowledgeTree && getNodes(knowledgeTree.children)}
      </TreeView>
    </Stack>
  );
}
