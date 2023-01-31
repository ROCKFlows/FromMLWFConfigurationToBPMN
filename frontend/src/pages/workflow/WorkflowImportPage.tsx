import * as React from 'react';
import {
  Box,
  CircularProgress,
  Fab,
  Paper,
  Stack,
  TextField,
  Typography,
} from '@mui/material';
import FileUploadIcon from '@mui/icons-material/FileUpload';
import {useTheme} from '@mui/material/styles';
import {useDropzone} from 'react-dropzone';
import {useCallback, useEffect, useMemo, useState} from 'react';
import ReactFlow, {
  addEdge,
  Background,
  Controls,
  useEdgesState,
  useNodesState,
} from 'reactflow';

import 'reactflow/dist/style.css';

import {useNavigate} from 'react-router-dom';
import {useAppDispatch} from '../../store/hooks';
import {
  usePostNewWorkflowMutation,
  Workflow,
} from '../../store/api/knowledgeApi';
import {showSnackbar} from '../../store/reducers/SnackbarSlice';
import {parseWorkflowXMLToObject} from '../../xml/parser';

const focusedStyle = {
  borderColor: '#2196f3',
};

const acceptStyle = {
  borderColor: '#00e676',
};

const rejectStyle = {
  borderColor: '#ff1744',
};

export default function WorkflowImportPage() {
  const [newVersionName, setNewVersionName] = useState<string>('');
  const [newWorkflowFile, setNewWorkflowFile] = useState<File | undefined>(
    undefined,
  );
  const [newWorkflow, setNewWorkflow] = useState<Workflow | undefined>(
    undefined,
  );

  const navigate = useNavigate();

  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);

  const onConnect = useCallback(
    (params) => setEdges((eds) => addEdge(params, eds)),
    [setEdges],
  );

  const theme = useTheme();

  const baseStyle = {
    flex: 1,
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    padding: '20px',
    borderWidth: 2,
    borderRadius: 2,
    borderColor: theme.palette.primary.main,
    borderStyle: 'dashed',
    color: theme.palette.primary.main,
    outline: 'none',
    transition: 'border .24s ease-in-out',
  };

  const {
    getRootProps,
    getInputProps,
    acceptedFiles,
    isFocused,
    isDragAccept,
    isDragReject,
  } = useDropzone({accept: {'text/xml': []}});

  const style = useMemo(
    () => ({
      ...baseStyle,
      ...(isFocused ? focusedStyle : {}),
      ...(isDragAccept ? acceptStyle : {}),
      ...(isDragReject ? rejectStyle : {}),
    }),
    [isFocused, isDragAccept, isDragReject],
  );

  const [addWorkflow, {isLoading, isSuccess, isError, error}] =
    usePostNewWorkflowMutation();

  const dispatch = useAppDispatch();

  useEffect(() => {
    if (!(isSuccess || isError)) {
      return;
    }
    if (isError) {
      console.error(
        `Failed to post new workflow with version ${newVersionName}. Error is \n${error?.data}`,
        error,
      );
    }
    dispatch(
      showSnackbar({
        severity: isError ? 'error' : 'success',
        message: isError
          ? `Failed to post new workflow with version ${newVersionName}.`
          : `Workflow successfully imported with version ${newVersionName}.`,
      }),
    );
    if (isSuccess) {
      navigate(`/knowledge/${newVersionName}`);
    }
  }, [isSuccess, isError]);

  useEffect(() => {
    if (acceptedFiles.length === 0) {
      return;
    }
    setNewWorkflowFile(acceptedFiles[0] as File);
  }, [acceptedFiles]);

  useEffect(() => {
    setNodes(newWorkflow?.nodes ?? []);
    setEdges(newWorkflow?.edges ?? []);
  }, [newWorkflow]);

  useEffect(() => {
    if (!newWorkflowFile) {
      return;
    }
    const reader = new FileReader();
    reader.onerror = () => {
      dispatch(
        showSnackbar({
          severity: 'error',
          message: `Failed to load the workflow file.`,
        }),
      );
    };
    reader.onload = () => {
      setNewWorkflow(parseWorkflowXMLToObject(reader.result));
    };
    reader.readAsText(newWorkflowFile, 'utf8');
  }, [newWorkflowFile]);

  return (
    <Stack spacing={2} height="100%" sx={{height: '92vh', padding: '1em'}}>
      <Typography>Add workflow</Typography>
      <Paper sx={{height: '20vh', padding: '1em', overflow: 'auto'}}>
        <Stack spacing={2}>
          <TextField
            id="new-version-name-textfield"
            label="New version"
            variant="outlined"
            value={newVersionName}
            onChange={(e) => setNewVersionName(e.target.value)}
          />
          <Box {...getRootProps()} sx={style}>
            <input {...getInputProps()} />
            <Typography>
              Drag &apos;n&apos; drop a workflow file to import, or click to
              select file
            </Typography>
          </Box>
        </Stack>
      </Paper>
      <Paper sx={{height: '60vh', padding: '1em', overflow: 'auto'}}>
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
      </Paper>
      <Box
        sx={{textAlign: 'center', zIndex: 100, cursor: 'pointer'}}
        onClick={() =>
          addWorkflow({
            newVersion: newVersionName,
            workflowFile: newWorkflowFile,
          })
        }
      >
        <Fab
          color="primary"
          aria-label="add"
          variant="extended"
          sx={{position: 'absolute', bottom: '5vh'}}
          disabled={
            !newVersionName || newWorkflowFile === undefined || isLoading
          }
        >
          {isLoading && <CircularProgress />}
          <FileUploadIcon />
          Import
        </Fab>
      </Box>
    </Stack>
  );
}
