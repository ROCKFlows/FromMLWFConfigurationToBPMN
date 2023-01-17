import {createSlice} from '@reduxjs/toolkit';

// Define a type for the slice state
interface VersionSliceState {
  currentVersion: string;
}

// Define the initial state using that type
const initialState: VersionSliceState = {
  currentVersion: '',
};

export const versionSlice = createSlice({
  name: 'version',
  // `createSlice` will infer the state type from the `initialState` argument
  initialState,
  reducers: {
    changeVersion: (state, action) => {
      // TODO/ check if exists in api versions
      state.currentVersion = action.payload;
    },
  },
});

export const {changeVersion} = versionSlice.actions;

export default versionSlice.reducer;
