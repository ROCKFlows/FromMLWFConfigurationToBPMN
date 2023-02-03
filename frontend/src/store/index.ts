import {configureStore} from '@reduxjs/toolkit';
import versionSlice from './reducers/VersionSlice';
import snackbarSlice from './reducers/SnackbarSlice';
import configurationSlice from './reducers/ConfigurationSlice';
import baseGraphqlApi from './api/graphql/baseGraphqlApi';
import baseRestApi from './api/rest/baseRestApi';
import configurationRestApi from './api/rest/configurationRestApi';
import knowledgeRestApi from './api/rest/knowledgeRestApi';
import versionRestApi from './api/rest/versionRestApi';
import workflowRestApi from './api/rest/workflowRestApi';
import configurationGraphqlApi from './api/graphql/configurationGraphqlApi';
import versionGraphqlApi from './api/graphql/versionGraphqlApi';
import workflowGraphqlApi from './api/graphql/workflowGraphqlApi';

const store = configureStore({
  reducer: {
    // slices
    version: versionSlice,
    snackbar: snackbarSlice,
    configuration: configurationSlice,
    // rest api
    [baseRestApi.reducerPath]: baseRestApi.reducer,
    [configurationRestApi.reducerPath]: configurationRestApi.reducer,
    [knowledgeRestApi.reducerPath]: knowledgeRestApi.reducer,
    [versionRestApi.reducerPath]: versionRestApi.reducer,
    [workflowRestApi.reducerPath]: workflowRestApi.reducer,
    // graphql api
    [baseGraphqlApi.reducerPath]: baseGraphqlApi.reducer,
    [configurationGraphqlApi.reducerPath]: configurationGraphqlApi.reducer,
    [versionGraphqlApi.reducerPath]: versionGraphqlApi.reducer,
    [workflowGraphqlApi.reducerPath]: workflowGraphqlApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(
      // rest api
      baseRestApi.middleware,
      // graphql api
      baseGraphqlApi.middleware,
    ),
  // TODO: devTools: process.env.NODE_ENV === 'development',
});

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>;
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch;

export default store;
