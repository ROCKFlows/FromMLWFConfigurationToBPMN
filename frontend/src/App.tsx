import * as React from 'react';
import {AlertColor, Button, Snackbar, Stack, TextField, Typography} from "@mui/material";
import {Alert, TreeItem, TreeView} from "@mui/lab";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import {ReactElement, useEffect, useState} from "react";
import axios from "axios";
import {XMLParser} from "fast-xml-parser";

function App() {

    const [knowledgeTree, setKnowledgeTree] = useState<{ FeatureModel: {constraints: any, structure: any }} | undefined>(undefined);
    const [tmpVersion, setTmpVersion] = useState<string>('');
    const [version, setVersion] = useState<string>('');
    const [isSnackbarOpen, setSnackbarOpen] = useState<boolean>(false);
    const [snackbarSeverity, setSnackbarSeverity] = useState<AlertColor | undefined>(undefined);
    const [snackbarMessage, setSnackbarMessage] = useState<string | undefined>(undefined);

    useEffect(() => {
        if (!version) {
            return;
        }
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
    }, [version]);

    const getNodes = (node: any): ReactElement => {
        if (!node.children) {
            return <TreeItem nodeId={node.name} label={node.name} />;
        }
        if (!node.name) {
            return Array.isArray(node.children) ? node.children.map((c) => getNodes(c)) : getNodes(node.children);
        }
        return (
            <TreeItem nodeId={node.name} label={node.name}>
                {Array.isArray(node.children) ? node.children.map((c) => getNodes(c)) : getNodes(node.children)}
            </TreeItem>
        );
    }

    return (
        <Stack>
            <Typography>ml2wf</Typography>
            <Stack direction='row' padding={1}>
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
            <TreeView
                aria-label="knowledge-tree"
                defaultCollapseIcon={<ExpandMoreIcon />}
                defaultExpandIcon={<ChevronRightIcon />}
            >
                {knowledgeTree && getNodes(knowledgeTree.FeatureModel.structure.children)}
            </TreeView>
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
