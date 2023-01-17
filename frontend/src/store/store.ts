import {configureStore} from '@reduxjs/toolkit';
import versionSlice from './reducers/VersionSlice';
import snackbarSlice from './reducers/SnackbarSlice';
import {knowledgeApi} from './api/knowledgeApi';

const store = configureStore({
  reducer: {
    version: versionSlice,
    snackbar: snackbarSlice,
    [knowledgeApi.reducerPath]: knowledgeApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(knowledgeApi.middleware),
  // TODO: devTools: process.env.NODE_ENV === 'development',
});

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>;
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch;

export default store;
