import {createSlice, PayloadAction} from '@reduxjs/toolkit';
import {Configuration, ConfigurationFeature} from "../api/configurationApi";

// Define a type for the slice state
interface ConfigurationSliceState {
  currentConfiguration: Configuration;
}

// Define the initial state using that type
const initialState: ConfigurationSliceState = {
  currentConfiguration: {name: 'new empty configuration', features: []},
};

export const configurationSlice = createSlice({
  name: 'configuration',
  // `createSlice` will infer the state type from the `initialState` argument
  initialState,
  reducers: {
    setConfigurationName: (state: ConfigurationSliceState, action: PayloadAction<{name: string}>) => {
      state.currentConfiguration.name = action.payload.name;
    },
    setConfigurationFeatures: (state: ConfigurationSliceState, action: PayloadAction<{features: ConfigurationFeature[]}>) => {
      state.currentConfiguration.features = [...action.payload.features];
    },
    updateConfigurationFeature: (state: ConfigurationSliceState, action: PayloadAction<ConfigurationFeature>) => {
      const {name, manual, automatic} = action.payload;
      const feature = state.currentConfiguration.features.find((f) => f.name === name);
      if (feature) {
        feature.manual = manual;
        feature.automatic = automatic;
      }
    }
  },
});

export const {setConfigurationName, setConfigurationFeatures, updateConfigurationFeature} = configurationSlice.actions;

export default configurationSlice.reducer;
