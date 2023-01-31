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
import {parseConfigurationXMLToObject} from '../../xml/parser';
import {
  Configuration,
  usePostConfigurationMutation,
} from '../../store/api/configurationApi';
import ConfigurationComponent from '../../sections/configuration/components/ConfigurationComponent';

const focusedStyle = {
  borderColor: '#2196f3',
};

const acceptStyle = {
  borderColor: '#00e676',
};

const rejectStyle = {
  borderColor: '#ff1744',
};

export default function ConfigurationImportPage() {
  const [newConfigurationFile, setNewConfigurationFile] = useState<
    File | undefined
  >(undefined);
  const [newConfiguration, setNewConfiguration] = useState<Configuration>({
    name: '',
    features: [],
  });

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

  const [addConfiguration, {isLoading, isSuccess, isError, error}] =
    usePostConfigurationMutation();

  const dispatch = useAppDispatch();

  useEffect(() => {
    if (!(isSuccess || isError)) {
      return;
    }
    if (isError) {
      console.error(
        `Failed to post new configuration with name ${newConfiguration.name}. Error is \n${error?.message}`,
        error,
      );
    }
    dispatch(
      showSnackbar({
        severity: isError ? 'error' : 'success',
        message: isError
          ? `Failed to post new configuration with name ${newConfiguration.name}.`
          : `Configuration successfully imported with name ${newConfiguration.name}.`,
      }),
    );
    if (isSuccess) {
      navigate(`/configuration`);
    }
  }, [isSuccess, isError]);

  useEffect(() => {
    if (acceptedFiles.length === 0) {
      return;
    }
    setNewConfigurationFile(acceptedFiles[0] as File);
  }, [acceptedFiles]);

  useEffect(() => {
    if (!newConfigurationFile) {
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
      setNewConfiguration({
        ...newConfiguration,
        features: parseConfigurationXMLToObject(reader.result),
      });
    };
    reader.readAsText(newConfigurationFile, 'utf8');
  }, [newConfigurationFile]);

  return (
    <Stack spacing={2} height="100%" sx={{height: '92vh', padding: '1em'}}>
      <Typography>Add Configuration</Typography>
      <Paper sx={{height: '20vh', padding: '1em', overflow: 'auto'}}>
        <Stack spacing={2}>
          <TextField
            id="new-version-name-textfield"
            label="Configuration name"
            variant="outlined"
            value={newConfiguration.name}
            onChange={(e) =>
              setNewConfiguration({...newConfiguration, name: e.target.value})
            }
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
        {newConfiguration && (
          <ConfigurationComponent configuration={newConfiguration} />
        )}
      </Paper>
      <Box
        sx={{textAlign: 'center', zIndex: 100, cursor: 'pointer'}}
        onClick={() => {
          if (newConfiguration.name) {
            addConfiguration({configuration: newConfiguration});
          }
        }}
      >
        <Fab
          color="primary"
          aria-label="add"
          variant="extended"
          sx={{position: 'absolute', bottom: '5vh'}}
          disabled={!newConfiguration.name || isLoading}
        >
          {isLoading && <CircularProgress />}
          <FileUploadIcon />
          Import
        </Fab>
      </Box>
    </Stack>
  );
}
