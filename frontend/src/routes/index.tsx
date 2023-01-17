import React, {Suspense} from 'react';
import {Route, Routes} from 'react-router';
import {CircularProgress} from '@mui/material';

const HomePage = React.lazy(() => import('../pages/HomePage'));
const NotFoundPage = React.lazy(() => import('../pages/NotFoundPage'));
const WorkflowImportPage = React.lazy(() =>
  import('../pages/WorkflowImportPage'),
);

export type RoutePage = {
  name: string;
  url: string;
};

export const pages: RoutePage[] = [
  {
    name: 'Home',
    url: '/',
  },
  {
    name: 'Import Workflow',
    url: '/workflow/new',
  },
];

export const routes = (
  <Suspense fallback={<CircularProgress />}>
    <Routes>
      <Route path="/" element={<HomePage />} key="versionRoute" />
      <Route
        path="/workflow/new"
        element={<WorkflowImportPage />}
        key="workflowImportRoute"
      />
      <Route path="*" element={<NotFoundPage />} key="notFoundRoute" />
    </Routes>
  </Suspense>
);