import React from 'react';
import {Route, Routes} from 'react-router';

const HomePage = React.lazy(() => import('../pages/HomePage'));
const NotFoundPage = React.lazy(() => import('../pages/NotFoundPage'));

export type RoutePage = {
  name: string;
  url: string;
};

export const pages: RoutePage[] = [
  {
    name: 'Home',
    url: '/',
  },
];

export const routes = (
  <Routes>
    <Route path="/" element={<HomePage />} key="versionRoute" />
    <Route path="*" element={<NotFoundPage />} key="notFoundRoute" />
  </Routes>
);
