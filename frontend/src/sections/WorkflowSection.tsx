import * as React from 'react';
import {Stack} from '@mui/material';
import {useCallback, useEffect, useState} from 'react';
import axios from 'axios';
import {XMLParser} from 'fast-xml-parser';
import ReactFlow, {
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
} from 'reactflow';

import 'reactflow/dist/style.css';

const initialNodes = [
  {
    id: '1',
    position: {x: 0, y: 0},
    data: {label: '1'},
    sourcePosition: 'right',
    targetPosition: 'left',
  },
  {
    id: '2',
    position: {x: 0, y: 100},
    data: {label: '2'},
    sourcePosition: 'right',
    targetPosition: 'left',
  },
];

const initialEdges = [{id: 'e1-2', source: '1', target: '2'}];

type WorkflowSectionProps = {
  version: string;
  onImported: () => void;
};

export default function WorkflowSection(props: WorkflowSectionProps) {
  const {version, onImported} = props;

  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);

  const onConnect = useCallback(
    (params) => setEdges((eds) => addEdge(params, eds)),
    [setEdges],
  );

  useEffect(() => {
    setEdges(
      nodes.slice(0, -1).map((n, i) => ({
        id: `e${i}`,
        source: nodes[i].id,
        target: nodes[i + 1].id,
      })),
    );
  }, [nodes]);

  useEffect(() => {
    if (!version) {
      return;
    }
    axios
      .get(`http://localhost:8080/ml2wf/api/v1/bpmn?versionName=${version}`)
      .then((r) => {
        setNodes(
          new XMLParser({
            ignoreAttributes: false,
            attributeNamePrefix: '@_',
            allowBooleanAttributes: true,
            preserveOrder: true,
          })
            .parse(r.data)[0]
            ['bpmn2:definitions'][0]['bpmn2:process'].filter(
              (t) => t['bpmn2:task'],
            )
            .map((t, i) => ({
              id: t[':@']['@_id'],
              position: {x: i * 200, y: 0},
              data: {label: t[':@']['@_name']},
              sourcePosition: 'right',
              targetPosition: 'left',
            })),
        );
      });
  }, [version]);

  return (
    <Stack spacing={2} height="100%">
      <ReactFlow
        nodes={nodes}
        edges={edges}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        onConnect={onConnect}
        attributionPosition="bottom-right"
      >
        <Controls />
        <Background />
      </ReactFlow>
    </Stack>
  );
}
