import {createApi} from '@reduxjs/toolkit/query/react';
import {gql} from 'graphql-request';
import {graphqlRequestBaseQuery} from '@rtk-query/graphql-request-base-query';

export enum FeatureSelectionStatus {
  SELECTED,
  UNSELECTED,
  UNDEFINED,
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

type Pagination = {
  page: number;
  per_page: number;
  total: number;
  total_pages: number;
};

export type GetConfigurationResponse = {
  data: {
    configuration: Configuration;
  };
} & Pagination;

export type GetAllConfigurationsResponse = {
  data: {
    configurations: Configuration[];
  };
} & Pagination;

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
            configuration(name: $name) {
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
          name,
        },
      }),
    }),
    getAllConfigurations: builder.query<GetAllConfigurationsResponse, {}>({
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
  }),
});

export const {useGetConfigurationQuery, useGetAllConfigurationsQuery} =
  configurationApi;
