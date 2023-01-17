import * as React from 'react';
import {CircularProgress, Stack} from '@mui/material';
import {useCallback, useEffect} from 'react';
import ReactFlow, {
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
} from 'reactflow';

import 'reactflow/dist/style.css';
import {useAppDispatch, useAppSelector} from '../store/hooks';
import {useGetWorkflowQuery} from '../store/api/knowledgeApi';
import {showSnackbar} from '../store/reducers/SnackbarSlice';

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

export default function WorkflowSection() {
  const {currentVersion} = useAppSelector((state) => state.version);
  const {
    data: workflow,
    isSuccess,
    isError,
    error,
    isFetching,
  } = useGetWorkflowQuery(currentVersion, {skip: currentVersion === ''});

  const dispatch = useAppDispatch();

  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);

  const onConnect = useCallback(
    (params) => setEdges((eds) => addEdge(params, eds)),
    [setEdges],
  );

  useEffect(() => {
    const newWorkflow = workflow ?? {nodes: initialNodes, edges: initialEdges};
    setNodes(newWorkflow.nodes);
    setEdges(newWorkflow.edges);
  }, [workflow]);

  useEffect(() => {
    if (!isError) {
      return;
    }
    dispatch(
      showSnackbar({
        severity: 'error',
        message: `Failed to retrieve workflow for version ${currentVersion}.`,
      }),
    );
  }, [isError]);

  useEffect(() => {
    if (isError && error) {
      console.error(
        `Failed to retrieve workflow for version ${currentVersion}. Error is ${error.status}: ${error.error}`,
      );
    }
  }, [isError, error]);

  return (
    <Stack spacing={2} height="100%">
      {isFetching && <CircularProgress />}
      {isSuccess && (
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
      )}
    </Stack>
  );
}
