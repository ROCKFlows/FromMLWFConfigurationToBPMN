import baseRestApi from './baseRestApi';
import type {RawConfiguration} from '../../types';

export type GetConfigurationPreviewApiResponse = RawConfiguration;
export type GetConfigurationPreviewApiArg = {
  body: Blob;
};
export type GetConfigurationApiResponse = RawConfiguration;
export type GetConfigurationApiArg = {
  configurationName: string;
};
export type ImportConfigurationApiResponse = string;
export type ImportConfigurationApiArg = {
  configurationName: string;
  body: Blob;
};

const taggedBaseRestApi = baseRestApi.enhanceEndpoints({
  addTagTypes: ['Configuration'],
});

const configurationRestApi = taggedBaseRestApi.injectEndpoints({
  endpoints: (build) => ({
    getConfigurationPreview: build.query<
      GetConfigurationPreviewApiResponse,
      GetConfigurationPreviewApiArg
    >({
      query: (queryArg) => ({
        url: `/preview/configuration`,
        method: 'POST',
        body: queryArg.body,
        headers: {
          accept: 'application/json',
          'Content-Type': 'application/xml',
        },
      }),
    }),
    getConfiguration: build.query<
      GetConfigurationApiResponse,
      GetConfigurationApiArg
    >({
      query: (queryArg) => ({
        url: `/configuration`,
        params: {configurationName: queryArg.configurationName},
        headers: {
          accept: 'application/json',
        },
      }),
      providesTags: ['Configuration'],
    }),
    importConfiguration: build.mutation<
      ImportConfigurationApiResponse,
      ImportConfigurationApiArg
    >({
      query: (queryArg) => ({
        url: `/configuration`,
        method: 'POST',
        body: queryArg.body,
        params: {configurationName: queryArg.configurationName},
        headers: {
          accept: 'application/json',
          'Content-Type': 'application/xml',
        },
      }),
      invalidatesTags: ['Configuration'],
    }),
  }),
  overrideExisting: false,
});

export const {
  useGetConfigurationPreviewQuery,
  useGetConfigurationQuery,
  useImportConfigurationMutation,
} = configurationRestApi;

export default configurationRestApi;
