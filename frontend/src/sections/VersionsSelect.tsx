import * as React from 'react';
import {
  CircularProgress,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
} from '@mui/material';
import {useEffect} from 'react';
import {useAppDispatch} from '../store/hooks';
import {changeVersion} from '../store/reducers/VersionSlice';
import {useGetVersionsQuery} from '../store/api/knowledgeApi';

export default function VersionsSelect() {
  const {
    data: versions,
    isSuccess,
    isError,
    error,
    isFetching,
  } = useGetVersionsQuery();

  const dispatch = useAppDispatch();

  useEffect(() => {
    if (isError && error) {
      console.error(
        `Failed to retrieve versions. Error is ${error.status}: ${error.error}`,
      );
    }
  }, [isError, error]);

  if (isError) {
    // TODO: snackbar
  }

  if (isFetching) {
    return <CircularProgress />;
  }

  return (
    <FormControl fullWidth>
      <InputLabel id="version-select-label">Version</InputLabel>
      <Select
        labelId="versions-select-label"
        id="versions-select"
        label="Version"
        onChange={(e) => dispatch(changeVersion(e.target.value))}
      >
        {isSuccess &&
          versions.map((v) => (
            <MenuItem value={v.name} key={v.name}>
              {v.name} ({v.major}.{v.minor}.{v.patch})
            </MenuItem>
          ))}
      </Select>
    </FormControl>
  );
}
