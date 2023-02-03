import {createApi} from '@reduxjs/toolkit/query/react';
import {graphqlRequestBaseQuery} from '@rtk-query/graphql-request-base-query';

const baseGraphqlApi = createApi({
  baseQuery: graphqlRequestBaseQuery({
    url: 'http://localhost:8080/ml2wf/api/v1/graphql',
  }),
  endpoints: () => ({}),
  reducerPath: 'graphqlApi',
});

export default baseGraphqlApi;
