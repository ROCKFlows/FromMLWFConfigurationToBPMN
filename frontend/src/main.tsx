import {createTheme, CssBaseline, ThemeProvider} from '@mui/material';
import {blue, pink, purple} from '@mui/material/colors';
import React from 'react';
import ReactDOM from 'react-dom/client';
import {Provider} from 'react-redux';
import App from './App';
import store from './store';

const theme = createTheme({
  // see https://www.welcomedeveloper.com/react-mui-theme
  palette: {
    mode: 'dark',
    primary: {
      light: blue[300],
      main: purple[500],
      dark: blue[700],
    },
    secondary: {
      light: pink[300],
      main: '#11cb5f',
      dark: pink[700],
    },
  },
});

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <CssBaseline enableColorScheme />
      <Provider store={store}>
        <App />
      </Provider>
    </ThemeProvider>
  </React.StrictMode>,
);
