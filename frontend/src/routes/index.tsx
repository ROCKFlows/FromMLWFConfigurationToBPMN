import React, {Suspense} from 'react';
import {Navigate, Route, Routes} from 'react-router';
import {CircularProgress} from '@mui/material';

const KnowledgePage = React.lazy(() => import('../pages/KnowledgePage'));
const NotFoundPage = React.lazy(() => import('../pages/NotFoundPage'));
const WorkflowImportPage = React.lazy(() =>
  import('../pages/workflow/WorkflowImportPage'),
);
const ConfigurationPage = React.lazy(() =>
  import('../pages/configuration/ConfigurationPage'),
);
const ConfigurationImportPage = React.lazy(() =>
  import('../pages/configuration/ConfigurationImportPage'),
);

export type RoutePage = {
  name: string;
  url: string;
};

export const pages: RoutePage[] = [
  {
    name: 'Knowledge',
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
  {
    name: 'Import Configuration',
    url: '/configuration/new',
  },
];

export const routes = (
  <Suspense fallback={<CircularProgress />}>
    <Routes>
      <Route path="/" element={<Navigate to="/knowledge" replace />} />
      <Route
        path="/knowledge/:version?"
        element={<KnowledgePage />}
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
      <Route
        path="/configuration/new"
        element={<ConfigurationImportPage />}
        key="configurationImportRoute"
      />
      <Route path="*" element={<NotFoundPage />} key="notFoundRoute" />
    </Routes>
  </Suspense>
);
