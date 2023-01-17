import * as React from 'react';
import {Alert, Snackbar, Stack} from '@mui/material';
import {BrowserRouter as Router} from 'react-router-dom';
import {useAppDispatch, useAppSelector} from './store/hooks';
import {hideSnackbar} from './store/reducers/SnackbarSlice';
import {routes, pages} from './routes';
import TopToolbar from './TopToolbar';

function App() {
  const snackbar = useAppSelector((state) => state.snackbar);

  const dispatch = useAppDispatch();

  return (
    <Stack>
      <Router>
        <TopToolbar pages={pages} />
        {routes}
      </Router>
      <Snackbar
        open={snackbar.show}
        autoHideDuration={6000}
        onClose={() => dispatch(hideSnackbar())}
      >
        <Alert
          onClose={() => dispatch(hideSnackbar())}
          severity={snackbar.severity}
          sx={{width: '100%'}}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Stack>
  );
}

export default App;
