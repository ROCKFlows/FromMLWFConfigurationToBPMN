import {createApi} from '@reduxjs/toolkit/query/react';
import {gql} from 'graphql-request';
import {graphqlRequestBaseQuery} from '@rtk-query/graphql-request-base-query';

export type WorkflowTask = {
  name: string;
  description: string;
  isAbstract: boolean;
  isOptional: boolean;
};

export type Workflow = {
  tasks: WorkflowTask[];
};

type GetWorkflowResponse = {
  workflow: Workflow;
};

export const workflowApi = createApi({
  reducerPath: 'workflowApi',
  baseQuery: graphqlRequestBaseQuery({
    url: 'http://localhost:8080/ml2wf/api/v1/graphql',
  }),
  tagTypes: ['Workflow', 'Versioned'],
  endpoints: (builder) => ({
    getWorkflow: builder.query<GetWorkflowResponse, {versionName: string}>({
      query: ({versionName}) => ({
        document: gql`
          query GetWorkflow {
            workflow(versionName: "${versionName}") {
              tasks {
                name
                description
                isAbstract
                isOptional
              }
            }
          }
        `,
      }),
    }),
  }),
});

export const {useGetWorkflowQuery} = workflowApi;
