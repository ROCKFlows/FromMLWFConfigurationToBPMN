import {gql} from 'graphql-request';
import baseGraphqlApi from './baseGraphqlApi';
import type {Version} from '../../types';

type GetVersionsResponse = {
  versions: Version[];
};

type GetLastVersion = {
  lastVersion: Version;
};

const taggedBaseGraphqlApi = baseGraphqlApi.enhanceEndpoints({
  addTagTypes: ['Version'],
});

const versionGraphqlApi = taggedBaseGraphqlApi.injectEndpoints({
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
    getLastVersion: builder.query<GetLastVersion, void>({
      query: () => ({
        document: gql`
          query GetLastVersion {
            lastVersion {
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
  overrideExisting: false,
});

export const {useGetVersionsQuery, useGetLastVersionQuery} = versionGraphqlApi;

export default versionGraphqlApi;
