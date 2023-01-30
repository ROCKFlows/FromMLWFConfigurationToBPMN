import {configureStore} from '@reduxjs/toolkit';
import versionSlice from './reducers/VersionSlice';
import snackbarSlice from './reducers/SnackbarSlice';
import configurationSlice from './reducers/ConfigurationSlice';
import {knowledgeApi} from './api/knowledgeApi';
import {configurationApi} from './api/configurationApi';
import {workflowApi} from './api/workflowApi';

const store = configureStore({
  reducer: {
    version: versionSlice,
    snackbar: snackbarSlice,
    configuration: configurationSlice,
    [knowledgeApi.reducerPath]: knowledgeApi.reducer,
    [configurationApi.reducerPath]: configurationApi.reducer,
    [workflowApi.reducerPath]: workflowApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(
      knowledgeApi.middleware,
      configurationApi.middleware,
      workflowApi.middleware,
    ),
  // TODO: devTools: process.env.NODE_ENV === 'development',
});

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>;
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch;

export default store;
