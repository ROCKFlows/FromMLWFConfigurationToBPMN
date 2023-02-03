import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react';

const baseRestApi = createApi({
  baseQuery: fetchBaseQuery({baseUrl: 'http://localhost:8080/ml2wf/api/v1/'}),
  endpoints: () => ({}),
  reducerPath: 'restApi',
});

export default baseRestApi;
