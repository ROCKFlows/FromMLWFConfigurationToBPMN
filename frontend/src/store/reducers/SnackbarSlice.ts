import {createSlice, PayloadAction} from '@reduxjs/toolkit';
import {AlertColor} from '@mui/material';

// Define a type for the slice state
interface SnackbarSliceState {
  severity: AlertColor | undefined;
  message: string | undefined;
  show: boolean;
}

// Define the initial state using that type
const initialState: SnackbarSliceState = {
  severity: undefined,
  message: undefined,
  show: false,
};

export const snackbarSlice = createSlice({
  name: 'snackbar',
  // `createSlice` will infer the state type from the `initialState` argument
  initialState,
  reducers: {
    showSnackbar: (
      state: SnackbarSliceState,
      action: PayloadAction<{severity: AlertColor; message: string}>,
    ) => {
      const {severity, message} = action.payload;
      state.severity = severity;
      state.message = message;
      state.show = true;
    },
    hideSnackbar: (state: SnackbarSliceState) => {
      state.severity = undefined;
      state.message = undefined;
      state.show = false;
    },
  },
});

export const {showSnackbar, hideSnackbar} = snackbarSlice.actions;

export default snackbarSlice.reducer;
