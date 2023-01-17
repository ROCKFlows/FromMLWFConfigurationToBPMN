import * as React from 'react';
import {
  Alert,
  AlertColor,
  Snackbar,
  Stack,
  TextField,
  Typography,
} from '@mui/material';
import Dropzone from 'react-dropzone';
import {useState} from 'react';
import axios from 'axios';
import {useAppSelector} from '../store/hooks';

type WorkflowImportSectionProps = {
  onImported: () => void;
};

export default function WorkflowImportSection(
  props: WorkflowImportSectionProps,
) {
  const {onImported} = props;

  const [isSnackbarOpen, setSnackbarOpen] = useState<boolean>(false);
  const [snackbarSeverity, setSnackbarSeverity] = useState<
    AlertColor | undefined
  >(undefined);
  const [snackbarMessage, setSnackbarMessage] = useState<string | undefined>(
    undefined,
  );
  const [newVersionName, setNewVersionName] = useState<string>('');

  const {currentVersion} = useAppSelector((state) => state.version);

  const importWorkflow = (worflowFile: File) => {
    const reader = new FileReader();
    reader.onerror = () => {
      setSnackbarSeverity('error');
      setSnackbarMessage('Failed to import the workflow.');
    };
    reader.onload = () => {
      axios
        .post(
          `http://localhost:8080/ml2wf/api/v1/bpmn?versionName=${currentVersion}`,
          reader.result,
          {
            headers: {
              'Content-Type': 'application/xml',
            },
          },
        )
        .then(() => {
          setSnackbarSeverity('success');
          setSnackbarMessage(
            'Workflow successfully imported. Refreshing tree knowledge...',
          );
          onImported();
        });
    };
    reader.readAsArrayBuffer(worflowFile);
  };

  return (
    <Stack spacing={2} height="100%">
      <Typography>Add workflow</Typography>
      <TextField
        id="new-version-name-textfield"
        label="New version"
        variant="standard"
        value={newVersionName}
        onChange={(e) => setNewVersionName(e.target.value)}
      />
      <Dropzone
        multiple={false}
        disabled={!currentVersion}
        onDrop={(acceptedFiles) => importWorkflow(acceptedFiles[0])}
      >
        {({getRootProps, getInputProps}) => (
          <section>
            <div {...getRootProps()}>
              <input {...getInputProps()} />
              <p>
                Drag `&apos;`n`&apos;` drop some workflow file to import, or
                click to select file
              </p>
            </div>
          </section>
        )}
      </Dropzone>
      <Snackbar
        open={isSnackbarOpen}
        autoHideDuration={6000}
        onClose={() => setSnackbarOpen(false)}
      >
        <Alert
          onClose={() => setSnackbarOpen(false)}
          severity={snackbarSeverity}
          sx={{width: '100%'}}
        >
          {snackbarMessage}
        </Alert>
      </Snackbar>
    </Stack>
  );
}