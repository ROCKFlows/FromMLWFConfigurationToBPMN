import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react';
import {XMLParser} from 'fast-xml-parser';

type KnowledgeTree = {
  extendedFeatureModel: [
    {
      constraints: any;
      struct: any;
    },
  ];
};

type WorkflowTask = {
  id: string;
  position: {x: number; y: number};
  data: {label: string};
  sourcePosition: 'right' | 'left';
  targetPosition: 'right' | 'left';
};

type WorkflowEdge = {
  id: string;
  source: string;
  target: string;
};

export type Workflow = {
  nodes: WorkflowTask[];
  edges: WorkflowEdge[];
};

// Define a service using a base URL and expected endpoints
export const knowledgeApi = createApi({
  reducerPath: 'knowledgeApi',
  baseQuery: fetchBaseQuery({
    baseUrl: 'http://localhost:8080/ml2wf/api/v1/',
  }),
  tagTypes: ['Versioned'],
  endpoints: (builder) => ({
    getKnowledgeTree: builder.query<KnowledgeTree, string>({
      // TODO: any
      query: (version) => ({
        url: `fm?versionName=${version}`,
        responseHandler: (response) => response.text(),
      }),
      transformResponse: (
        response: any, // TODO: any
      ) =>
        new XMLParser({
          ignoreAttributes: false,
          attributeNamePrefix: '@_',
          allowBooleanAttributes: true,
          preserveOrder: true,
        }).parse(response)[0],
      providesTags: ['Versioned'],
    }),
    postNewWorkflow: builder.mutation<
      File,
      Partial<{newVersion: string; workflowFile: File}>
    >({
      // note: an optional `queryFn` may be used in place of `query`
      query: ({newVersion, workflowFile}) => ({
        url: `bpmn/?newVersionName=${newVersion}`,
        method: 'POST',
        body: workflowFile,
        headers: {
          accept: 'application/json',
          'Content-Type': 'application/xml',
        },
        responseHandler: (response) => response.text(),
      }),
      invalidatesTags: ['Versioned'],
    }),
  }),
});

export const {useGetKnowledgeTreeQuery, usePostNewWorkflowMutation} =
  knowledgeApi;
