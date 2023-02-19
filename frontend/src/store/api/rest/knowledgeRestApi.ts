import baseRestApi from './baseRestApi';
import type {FeatureModel, FeatureModelTask} from '../../types';

export type GetFeatureModelPreviewApiResponse = FeatureModel;
export type GetFeatureModelPreviewApiArg = {
  body: Blob;
};
export type GetFeatureModelApiResponse = FeatureModel;
export type GetFeatureModelApiArg = {
  versionName: string;
};
export type ImportFeatureModelApiResponse = string;
export type ImportFeatureModelApiArg = {
  versionName: string;
  body: Blob;
};
export type GetFeatureModelTaskApiResponse = FeatureModelTask;
export type GetFeatureModelTaskApiArg = {
  name: string;
  versionName: string;
};

const taggedBaseRestApi = baseRestApi.enhanceEndpoints({
  addTagTypes: ['Knowledge', 'Version'],
});

const knowledgeRestApi = taggedBaseRestApi.injectEndpoints({
  endpoints: (build) => ({
    getFeatureModelPreview: build.query<
      GetFeatureModelPreviewApiResponse,
      GetFeatureModelPreviewApiArg
    >({
      query: (queryArg) => ({
        url: `/preview/fm`,
        method: 'POST',
        body: queryArg.body,
        headers: {
          accept: 'application/json',
          'Content-Type': 'application/xml',
        },
      }),
    }),
    getFeatureModel: build.query<
      GetFeatureModelApiResponse,
      GetFeatureModelApiArg
    >({
      query: (queryArg) => ({
        url: `/fm`,
        params: {versionName: queryArg.versionName},
        headers: {
          accept: 'application/json',
          'Content-Type': 'application/json',
        },
      }),
      providesTags: ['Knowledge', 'Version'],
    }),
    importFeatureModel: build.mutation<
      ImportFeatureModelApiResponse,
      ImportFeatureModelApiArg
    >({
      query: (queryArg) => ({
        url: `/fm`,
        method: 'POST',
        body: queryArg.body,
        params: {versionName: queryArg.versionName},
        headers: {
          accept: 'application/json',
          'Content-Type': 'application/xml',
        },
        responseHandler: (response) => response.text(),
      }),
      invalidatesTags: ['Knowledge', 'Version'],
    }),
    getFeatureModelTask: build.query<
      GetFeatureModelTaskApiResponse,
      GetFeatureModelTaskApiArg
    >({
      query: (queryArg) => ({
        url: `/fm/${queryArg.name}`,
        params: {versionName: queryArg.versionName},
        headers: {
          accept: 'application/json',
          'Content-Type': 'application/json',
        },
      }),
      providesTags: ['Knowledge', 'Version'],
    }),
  }),
  overrideExisting: false,
});

export const {
  useGetFeatureModelPreviewQuery,
  useGetFeatureModelQuery,
  useImportFeatureModelMutation,
  useGetFeatureModelTaskQuery,
} = knowledgeRestApi;

export default knowledgeRestApi;
