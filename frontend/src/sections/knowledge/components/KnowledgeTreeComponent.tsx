import * as React from 'react';
import {ReactElement} from 'react';
import {Stack, Tooltip, Typography} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import {TreeItem, TreeView} from '@mui/lab';
import InfoIcon from '@mui/icons-material/Info';
import {FeatureModel, FeatureModelTask} from '../../../store/types';

type KnowledgeTreeComponentProps = {
  knowledgeTree: FeatureModel;
};

export default function KnowledgeTreeComponent(
  props: KnowledgeTreeComponentProps,
) {
  const {knowledgeTree} = props;

  const structure = knowledgeTree.structure?.children;

  const getNodesIds = (task: FeatureModelTask): string[] => [
    task.name,
    ...(task.children?.length ? task.children : []).flatMap((c) =>
      getNodesIds(c),
    ),
  ];

  const getNodes = (task: FeatureModelTask): ReactElement => {
    const description = task.descriptions?.length
      ? task.descriptions[0]?.content
      : undefined;
    return (
      <TreeItem
        key={`tree-item-knowledge-${task.name}`}
        nodeId={task.name}
        label={
          description ? (
            <Stack spacing={1} direction="row">
              <Typography>{task.name}</Typography>
              <Tooltip title={description}>
                <InfoIcon />
              </Tooltip>
            </Stack>
          ) : (
            <Typography>{task.name}</Typography>
          )
        }
      >
        {task.children &&
          task.children.length > 0 &&
          task.children.map(getNodes)}
      </TreeItem>
    );
  };

  return (
    <Stack spacing={1} height="100%">
      <TreeView
        aria-label="knowledge-tree"
        defaultCollapseIcon={<ExpandMoreIcon />}
        defaultExpandIcon={<ChevronRightIcon />}
        defaultExpanded={structure && getNodesIds(structure[0])}
      >
        {structure && getNodes(structure[0])}
      </TreeView>
    </Stack>
  );
}
