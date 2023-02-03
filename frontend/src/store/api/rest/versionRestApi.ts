import baseRestApi from './baseRestApi';
import type {Version} from '../../types';

export type GetLastVersionApiResponse = Version;
export type GetLastVersionApiArg = void;
export type GetVersionsApiResponse = Version[];
export type GetVersionsApiArg = void;

const taggedBaseRestApi = baseRestApi.enhanceEndpoints({
  addTagTypes: ['Version'],
});

const versionRestApi = taggedBaseRestApi.injectEndpoints({
  endpoints: (build) => ({
    getLastVersion: build.query<
      GetLastVersionApiResponse,
      GetLastVersionApiArg
    >({
      query: () => ({
        url: `/fm/versions/last`,
        headers: {
          accept: 'application/json',
        },
      }),
      providesTags: ['Version'],
    }),
    getVersions: build.query<GetVersionsApiResponse, GetVersionsApiArg>({
      query: () => ({
        url: `/fm/versions/all`,
        headers: {
          accept: 'application/json',
        },
      }),
      providesTags: ['Version'],
    }),
  }),
  overrideExisting: false,
});

export const {useGetLastVersionQuery, useGetVersionsQuery} = versionRestApi;

export default versionRestApi;
