import * as React from 'react';
import {
  CircularProgress,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
} from '@mui/material';
import {useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import {useAppSelector} from '../../store/hooks';
import {useGetVersionsQuery} from '../../store/api/graphql/versionGraphqlApi';

export default function VersionsSelect() {
  const {currentVersion} = useAppSelector((state) => state.version);
  const {
    data: versions,
    isSuccess,
    isError,
    error,
    isFetching,
  } = useGetVersionsQuery();

  const navigate = useNavigate();

  useEffect(() => {
    if (isError && error) {
      console.error(
        `Failed to retrieve versions. Error is ${error.status}: ${error.message}`,
      );
    }
    if (isError) {
      // TODO: snackbar
    }
  }, [isError, error]);

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
        value={currentVersion}
        onChange={(e) => navigate(`/knowledge/${e.target.value}`)}
      >
        {isSuccess &&
          versions.versions
            ?.slice()
            ?.sort(
              (vA, vB) =>
                vB.major * 100 +
                vB.minor * 10 +
                vB.patch -
                (vA.major * 100 + vA.minor * 10 + vA.patch),
            )
            .map((v) => (
              <MenuItem value={v.name} key={v.name}>
                {v.name} ({v.major}.{v.minor}.{v.patch})
              </MenuItem>
            ))}
      </Select>
    </FormControl>
  );
}
