import {createApi} from '@reduxjs/toolkit/query/react';
import {gql} from 'graphql-request';
import {graphqlRequestBaseQuery} from '@rtk-query/graphql-request-base-query';

export enum FeatureSelectionStatus {
  SELECTED = 'SELECTED',
  UNSELECTED = 'UNSELECTED',
  UNDEFINED = 'UNDEFINED',
}

export type ConfigurationFeature = {
  name: string;
  automatic: FeatureSelectionStatus;
  manual: FeatureSelectionStatus;
};

export type Configuration = {
  name: string;
  features: ConfigurationFeature[];
};

type GetConfigurationResponse = {
  configuration: Configuration;
};

type GetAllConfigurationsResponse = {
  configurations: Configuration[];
};

type PostConfigurationResponse = {
  configuration: Configuration;
};

export const configurationApi = createApi({
  reducerPath: 'configurationApi',
  baseQuery: graphqlRequestBaseQuery({
    url: 'http://localhost:8080/ml2wf/api/v1/graphql',
  }),
  tagTypes: ['Configuration'],
  endpoints: (builder) => ({
    getConfiguration: builder.query<GetConfigurationResponse, {name: string}>({
      query: ({name}) => ({
        document: gql`
          query GetConfiguration {
            configuration(name: "${name}") {
              name
              features {
                name
                automatic
                manual
              }
            }
          }
        `,
      }),
    }),
    getAllConfigurations: builder.query<GetAllConfigurationsResponse, void>({
      query: () => ({
        document: gql`
          query GetConfigurations {
            configurations {
              name
              features {
                name
                automatic
                manual
              }
            }
          }
        `,
      }),
    }),
    postConfiguration: builder.mutation<
      PostConfigurationResponse,
      {configuration: Configuration}
    >({
      query: ({configuration}) => ({
        document: gql`
          mutation AddConfiguration($configuration: InputConfiguration!) {
            configuration(configuration: $configuration) {
              name
              features {
                name
                automatic
                manual
              }
            }
          }
        `,
        variables: {
          configuration,
        },
      }),
      invalidatesTags: ['Configuration'],
    }),
  }),
});

export const {
  useGetConfigurationQuery,
  useGetAllConfigurationsQuery,
  usePostConfigurationMutation,
} = configurationApi;
