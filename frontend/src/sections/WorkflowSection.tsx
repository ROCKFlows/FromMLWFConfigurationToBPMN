import * as React from 'react';
import {
  Alert,
  AlertColor,
  Paper,
  Snackbar,
  Stack,
  Typography,
} from '@mui/material';
import Dropzone from 'react-dropzone';
import {useState} from 'react';
import axios from 'axios';

type WorkflowSectionProps = {
  version: string;
  onImported: () => void;
};

export default function WorkflowSection(props: WorkflowSectionProps) {
  const {version, onImported} = props;

  const [isSnackbarOpen, setSnackbarOpen] = useState<boolean>(false);
  const [snackbarSeverity, setSnackbarSeverity] = useState<
    AlertColor | undefined
  >(undefined);
  const [snackbarMessage, setSnackbarMessage] = useState<string | undefined>(
    undefined,
  );

  const importWorkflow = (worflowFile: File) => {
    const reader = new FileReader();
    reader.onerror = () => {
      setSnackbarSeverity('error');
      setSnackbarMessage('Failed to import the workflow.');
    };
    reader.onload = () => {
      axios
        .post(
          `http://localhost:8080/ml2wf/api/v1/bpmn?versionName=${version}`,
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
            'WorkflowSection successfully imported. Refreshing tree knowledge...',
          );
          onImported();
        });
    };
    reader.readAsArrayBuffer(worflowFile);
  };

  return (
    <Stack spacing={2} height="100%">
      <Typography>Workflow</Typography>
      <Dropzone
        multiple={false}
        disabled={!version}
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
