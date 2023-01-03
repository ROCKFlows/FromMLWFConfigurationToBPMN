import * as React from 'react';
import {AlertColor, Button, Grid, Paper, Snackbar, Stack, TextField, Typography} from "@mui/material";
import {Alert, TreeItem, TreeView} from "@mui/lab";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import {ReactElement, useEffect, useState} from "react";
import axios from "axios";
import {XMLParser} from "fast-xml-parser";
import Dropzone from 'react-dropzone'

function App() {

    const [knowledgeTree, setKnowledgeTree] = useState<{ FeatureModel: {constraints: any, structure: any }} | undefined>(undefined);
    const [tmpVersion, setTmpVersion] = useState<string>('');
    const [version, setVersion] = useState<string>('');
    const [isSnackbarOpen, setSnackbarOpen] = useState<boolean>(false);
    const [snackbarSeverity, setSnackbarSeverity] = useState<AlertColor | undefined>(undefined);
    const [snackbarMessage, setSnackbarMessage] = useState<string | undefined>(undefined);

    const getTree = () => {
        axios.get(`http://localhost:8080/ml2wf/api/v1/fm?versionName=${version}`)
            .then((r) => {
                setKnowledgeTree(new XMLParser().parse(r.data));
                setSnackbarSeverity('success');
                setSnackbarMessage('Knowledge tree successfully retrieved.');
            })
            .catch(() => {
                setSnackbarSeverity('error');
                setSnackbarMessage('Failed to retrieve knowledge tree.');
            })
            .finally(() => {
                setSnackbarOpen(true);
            });
    }

    useEffect(() => {
        if (!version) {
            return;
        }
        getTree();
    }, [version]);

    const getNodes = (node: any): ReactElement => {
        if (!node.children) {
            return <TreeItem nodeId={node.name} label={node.name} />;
        }
        if (!node.name) {
            return Array.isArray(node.children) ? node.children.map(getNodes) : getNodes(node.children);
        }
        return (
            <TreeItem nodeId={node.name} label={node.name}>
                {Array.isArray(node.children) ? node.children.map(getNodes) : getNodes(node.children)}
            </TreeItem>
        );
    }

    const importWorkflow = (worflowFile: File) => {
        const reader = new FileReader();
        reader.onerror = () => {
            setSnackbarSeverity('error');
            setSnackbarMessage('Failed to import the workflow.');
        }
        reader.onload = () => {
            axios.post(`http://localhost:8080/ml2wf/api/v1/bpmn?versionName=${version}`, reader.result, {
                headers: {
                    'Content-Type': 'application/xml'
                }
            }).then(() => {
                setSnackbarSeverity('success');
                setSnackbarMessage('Workflow successfully imported. Refreshing tree knowledge...');
                getTree();
            })
        }
        reader.readAsArrayBuffer(worflowFile);
    }

    return (
        <Stack height="100%">
            <Typography>ml2wf</Typography>
            <Stack direction='row' padding={1} height="100%">
                <TextField
                    label="Version"
                    variant="outlined"
                    value={tmpVersion}
                    onChange={(e) => setTmpVersion(e.target.value)}
                />
                <Button
                    variant="outlined"
                    onClick={() => setVersion(tmpVersion)}
                >
                    Confirm
                </Button>
            </Stack>
            <Grid container spacing={2} height="100%">
                <Grid item xs={6} height="100%">
                    <Paper>
                        <TreeView
                            aria-label="knowledge-tree"
                            defaultCollapseIcon={<ExpandMoreIcon />}
                            defaultExpandIcon={<ChevronRightIcon />}
                        >
                            {knowledgeTree && getNodes(knowledgeTree.FeatureModel.structure.children)}
                        </TreeView>
                    </Paper>
                </Grid>
                <Grid item xs={6} height="100%">
                    <Paper style={{height: "50%"}}>
                        <Stack height="50%" >
                            <Typography>Constraints</Typography>
                        </Stack>
                    </Paper>
                    <Paper>
                        <Stack height="50%">
                            <Typography>Workflows</Typography>
                            <Dropzone
                                multiple={false}
                                disabled={!version}
                                onDrop={(acceptedFiles) => importWorkflow(acceptedFiles[0])}
                            >
                                {({getRootProps, getInputProps}) => (
                                    <section>
                                        <div {...getRootProps()}>
                                            <input {...getInputProps()} />
                                            <p>Drag `&apos;`n`&apos;` drop some workflow file to import, or click to select file</p>
                                        </div>
                                    </section>
                                )}
                            </Dropzone>
                        </Stack>
                    </Paper>
                </Grid>
            </Grid>
            <Snackbar
                open={isSnackbarOpen}
                autoHideDuration={6000}
                onClose={() => setSnackbarOpen(false)}
            >
                <Alert
                    onClose={() => setSnackbarOpen(false)}
                    severity={snackbarSeverity}
                    sx={{ width: '100%' }}
                >
                    {snackbarMessage}
                </Alert>
            </Snackbar>
        </Stack>
  )
}

export default App
