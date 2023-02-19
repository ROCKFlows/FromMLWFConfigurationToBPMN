import baseRestApi from './baseRestApi';
import type {BpmnWorkflow} from '../../types';

export type GetBpmnWorkflowPreviewApiResponse = BpmnWorkflow;
export type GetBpmnWorkflowPreviewApiArg = {
  body: Blob;
};
export type GetBpmnWorkflowApiResponse = BpmnWorkflow;
export type GetBpmnWorkflowApiArg = {
  versionName: string;
};
export type ImportBpmnWorkflowApiResponse = string;
export type ImportBpmnWorkflowApiArg = {
  newVersionName: string;
  body: Blob;
};
export type IsBpmnWorkflowConsistentApiResponse = string;
export type IsBpmnWorkflowConsistentApiArg = {
  versionName: string;
  body: Blob;
};

const taggedBaseRestApi = baseRestApi.enhanceEndpoints({
  addTagTypes: ['Workflow', 'Version'],
});

const workflowRestApi = taggedBaseRestApi.injectEndpoints({
  endpoints: (build) => ({
    getBpmnWorkflowPreview: build.query<
      GetBpmnWorkflowPreviewApiResponse,
      GetBpmnWorkflowPreviewApiArg
    >({
      query: (queryArg) => ({
        url: `/preview/bpmn`,
        method: 'POST',
        body: queryArg.body,
        headers: {
          accept: 'application/json',
          'Content-Type': 'application/xml',
        },
      }),
    }),
    getBpmnWorkflow: build.query<
      GetBpmnWorkflowApiResponse,
      GetBpmnWorkflowApiArg
    >({
      query: (queryArg) => ({
        url: `/bpmn`,
        params: {versionName: queryArg.versionName},
        headers: {
          accept: 'application/json',
          'Content-Type': 'application/json',
        },
      }),
      providesTags: ['Workflow', 'Version'],
    }),
    importBpmnWorkflow: build.mutation<
      ImportBpmnWorkflowApiResponse,
      ImportBpmnWorkflowApiArg
    >({
      query: (queryArg) => ({
        url: `/bpmn`,
        method: 'POST',
        body: queryArg.body,
        params: {newVersionName: queryArg.newVersionName},
        headers: {
          accept: 'application/json',
          'Content-Type': 'application/xml',
        },
        responseHandler: (response) => response.text(),
      }),
      invalidatesTags: ['Workflow'],
    }),
    isBpmnWorkflowConsistent: build.query<
      IsBpmnWorkflowConsistentApiResponse,
      IsBpmnWorkflowConsistentApiArg
    >({
      query: (queryArg) => ({
        url: `/bpmn/consistency`,
        method: 'POST',
        body: queryArg.body,
        params: {versionName: queryArg.versionName},
        headers: {
          accept: 'application/json',
          'Content-Type': 'application/json',
        },
      }),
    }),
  }),
  overrideExisting: false,
});

export const {
  useGetBpmnWorkflowPreviewQuery,
  useGetBpmnWorkflowQuery,
  useImportBpmnWorkflowMutation,
  useIsBpmnWorkflowConsistentQuery,
} = workflowRestApi;

export default workflowRestApi;
