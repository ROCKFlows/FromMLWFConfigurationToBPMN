import React, {Suspense} from 'react';
import {Navigate, Route, Routes} from 'react-router';
import {CircularProgress} from '@mui/material';
import ConfigurationPage from '../pages/ConfigurationPage';

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
    url: '/knowledge',
  },
  {
    name: 'Import Workflow',
    url: '/workflow/new',
  },
  {
    name: 'Configuration',
    url: '/configuration',
  },
];

export const routes = (
  <Suspense fallback={<CircularProgress />}>
    <Routes>
      <Route path="/" element={<Navigate to="/knowledge" replace />} />
      <Route
        path="/knowledge/:version?"
        element={<HomePage />}
        key="versionRoute"
      />
      <Route
        path="/workflow/new"
        element={<WorkflowImportPage />}
        key="workflowImportRoute"
      />
      <Route
        path="/configuration"
        element={<ConfigurationPage />}
        key="configurationRoute"
      />
      <Route path="*" element={<NotFoundPage />} key="notFoundRoute" />
    </Routes>
  </Suspense>
);
