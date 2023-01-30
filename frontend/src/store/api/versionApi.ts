import {createApi} from '@reduxjs/toolkit/query/react';
import {gql} from 'graphql-request';
import {graphqlRequestBaseQuery} from '@rtk-query/graphql-request-base-query';

export type Version = {
  name: string;
  major: number;
  minor: number;
  patch: number;
};

type GetVersionsResponse = {
  versions: Version[];
};

export const versionApi = createApi({
  reducerPath: 'versionApi',
  baseQuery: graphqlRequestBaseQuery({
    url: 'http://localhost:8080/ml2wf/api/v1/graphql',
  }),
  tagTypes: ['Versioned'],
  endpoints: (builder) => ({
    getVersions: builder.query<GetVersionsResponse, void>({
      query: () => ({
        document: gql`
          query GetVersions {
            versions {
              name
              major
              minor
              patch
            }
          }
        `,
      }),
    }),
  }),
});

export const {useGetVersionsQuery} = versionApi;
