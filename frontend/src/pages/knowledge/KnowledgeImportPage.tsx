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
import {useEffect, useMemo, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {useAppDispatch} from '../../store/hooks';
import {showSnackbar} from '../../store/reducers/SnackbarSlice';
import {parseKnowledgeTreeXMLToObject} from '../../xml/parser';
import {
  KnowledgeTree,
  usePostKnowledgeTreeMutation,
} from '../../store/api/knowledgeApi';
import KnowledgeTreeComponent from '../../sections/knowledge/components/KnowledgeTreeComponent';

const focusedStyle = {
  borderColor: '#2196f3',
};

const acceptStyle = {
  borderColor: '#00e676',
};

const rejectStyle = {
  borderColor: '#ff1744',
};

export default function KnowledgeImportPage() {
  const [newVersionName, setNewVersionName] = useState<string>('');
  const [newKnowledgeTreeFile, setNewKnowledgeTreeFile] = useState<
    File | undefined
  >(undefined);
  const [newKnowledgeTree, setNewKnowledgeTree] = useState<
    KnowledgeTree | undefined
  >(undefined);

  const navigate = useNavigate();

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

  const [addKnowledgeTree, {isLoading, isSuccess, isError, error}] =
    usePostKnowledgeTreeMutation();

  const dispatch = useAppDispatch();

  useEffect(() => {
    if (!(isSuccess || isError)) {
      return;
    }
    if (isError) {
      console.error(
        `Failed to post new knowledge tree with version ${newVersionName}. Error is \n${error?.data}`,
        error,
      );
    }
    dispatch(
      showSnackbar({
        severity: isError ? 'error' : 'success',
        message: isError
          ? `Failed to post new knowledge tree with version ${newVersionName}.`
          : `Knowledge tree successfully imported with version ${newVersionName}. You will be redirected soon...`,
      }),
    );
    if (isSuccess) {
      // TODO: navigate(`/knowledge/${newVersionName}`);
    }
  }, [isSuccess, isError]);

  useEffect(() => {
    if (acceptedFiles.length === 0) {
      return;
    }
    setNewKnowledgeTreeFile(acceptedFiles[0] as File);
  }, [acceptedFiles]);

  useEffect(() => {
    if (!newKnowledgeTreeFile) {
      return;
    }
    const reader = new FileReader();
    reader.onerror = () => {
      dispatch(
        showSnackbar({
          severity: 'error',
          message: `Failed to load the configuration file.`,
        }),
      );
    };
    reader.onload = () => {
      setNewKnowledgeTree(parseKnowledgeTreeXMLToObject(reader.result));
    };
    reader.readAsText(newKnowledgeTreeFile, 'utf8');
  }, [newKnowledgeTreeFile]);

  return (
    <Stack spacing={2} height="100%" sx={{height: '92vh', padding: '1em'}}>
      <Typography>Add Knowledge Tree</Typography>
      <Paper sx={{height: '20vh', padding: '1em', overflow: 'auto'}}>
        <Stack spacing={2}>
          <TextField
            id="new-version-name-textfield"
            label="Knowledge Tree version"
            variant="outlined"
            value={newVersionName}
            onChange={(e) => setNewVersionName(e.target.value)}
          />
          <Box {...getRootProps()} sx={style}>
            <input {...getInputProps()} />
            <Typography>
              Drag &apos;n&apos; drop a configuration file to import, or click
              to select file
            </Typography>
          </Box>
        </Stack>
      </Paper>
      <Paper sx={{height: '60vh', padding: '1em', overflow: 'auto'}}>
        {newKnowledgeTree && (
          <KnowledgeTreeComponent knowledgeTree={newKnowledgeTree} />
        )}
      </Paper>
      <Box
        sx={{textAlign: 'center', zIndex: 100, cursor: 'pointer'}}
        onClick={() => {
          if (newKnowledgeTree?.extendedFeatureModel.length) {
            addKnowledgeTree({
              versionName: newVersionName,
              knowledgeFile: newKnowledgeTreeFile,
            });
          }
        }}
      >
        <Fab
          color="primary"
          aria-label="add"
          variant="extended"
          sx={{position: 'absolute', bottom: '5vh'}}
          disabled={
            !newVersionName ||
            !newKnowledgeTree?.extendedFeatureModel.length ||
            isLoading
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
