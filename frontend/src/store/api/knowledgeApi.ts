import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react';
import {XMLParser} from 'fast-xml-parser';

export type Version = {
  name: string;
  major: number;
  minor: number;
  patch: number;
};

type VersionResponse = {
  List: {
    item: {
      name: string;
      major: number;
      minor: number;
      patch: number;
    }[];
  };
};

type KnowledgeTree = {
  extendedFeatureModel: {
    constraints: any;
    struct: any;
  };
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

type Workflow = {
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
    getVersions: builder.query<Version[], void>({
      query: () => ({
        url: 'fm/versions/all',
        responseHandler: (response) => response.text(),
      }),
      transformResponse: (response: VersionResponse) => {
        const apiReturnedVersions = new XMLParser({
          isArray: (name, jpath) => jpath === 'List.item',
        }).parse(response).List;
        return apiReturnedVersions ? apiReturnedVersions.item : [];
      },
      providesTags: ['Versioned'],
    }),
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
    getWorkflow: builder.query<Workflow, string>({
      // TODO: any
      query: (version) => ({
        url: `bpmn?versionName=${version}`,
        responseHandler: (response) => response.text(),
      }),
      transformResponse: (
        response: any, // TODO: any
      ) => {
        const tasks = new XMLParser({
          ignoreAttributes: false,
          attributeNamePrefix: '@_',
          allowBooleanAttributes: true,
          preserveOrder: true,
        })
          .parse(response)[0]
          ['bpmn2:definitions'][0]['bpmn2:process'].filter(
            (t) => t['bpmn2:task'],
          );
        // TODO: move conversion to dedicated util
        return {
          nodes: tasks.map((t, i) => ({
            id: t[':@']['@_id'],
            position: {x: i * 200, y: 0},
            data: {label: t[':@']['@_name']},
            sourcePosition: 'right',
            targetPosition: 'left',
          })),
          edges: tasks.slice(0, -1).map((n, i) => ({
            id: `e${i}`,
            source: tasks[i][':@']['@_id'],
            target: tasks[i + 1][':@']['@_id'],
          })),
        };
      },
      providesTags: ['Versioned'],
    }),
  }),
});

// Export hooks for usage in functional components, which are
// auto-generated based on the defined endpoints
export const {
  useGetVersionsQuery,
  useGetKnowledgeTreeQuery,
  useGetWorkflowQuery,
} = knowledgeApi;
