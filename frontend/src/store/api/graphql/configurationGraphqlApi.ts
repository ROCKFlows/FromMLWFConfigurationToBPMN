import {gql} from 'graphql-request';
import baseGraphqlApi from './baseGraphqlApi';
import type {Configuration} from '../../types';

type GetConfigurationResponse = {
  configuration: Configuration;
};

type GetAllConfigurationsResponse = {
  configurations: Configuration[];
};

type PostConfigurationResponse = {
  configuration: Configuration;
};

const taggedBaseGraphqlApi = baseGraphqlApi.enhanceEndpoints({
  addTagTypes: ['Configuration'],
});

const configurationGraphqlApi = taggedBaseGraphqlApi.injectEndpoints({
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
      providesTags: ['Configuration'],
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
      providesTags: ['Configuration'],
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
  overrideExisting: false,
});

export const {
  useGetConfigurationQuery,
  useGetAllConfigurationsQuery,
  usePostConfigurationMutation,
} = configurationGraphqlApi;

export default configurationGraphqlApi;
