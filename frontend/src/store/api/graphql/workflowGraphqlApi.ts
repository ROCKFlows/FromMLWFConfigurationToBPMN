import {gql} from 'graphql-request';
import baseGraphqlApi from './baseGraphqlApi';
import type {BpmnWorkflow} from '../../types';

type GetWorkflowResponse = {
  workflow: BpmnWorkflow;
};

const taggedBaseGraphqlApi = baseGraphqlApi.enhanceEndpoints({
  addTagTypes: ['Workflow', 'Version'],
});

const workflowGraphqlApi = taggedBaseGraphqlApi.injectEndpoints({
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
  overrideExisting: false,
});

export const {useGetWorkflowQuery} = workflowGraphqlApi;

export default workflowGraphqlApi;
