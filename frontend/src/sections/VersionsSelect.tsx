import * as React from 'react';
import {FormControl, InputLabel, MenuItem, Select} from '@mui/material';
import axios from 'axios';
import {useEffect, useState} from 'react';
import {XMLParser} from 'fast-xml-parser';

const xmlParser = new XMLParser({
  isArray: (name, jpath) => jpath === 'List.item',
});

type VersionsSelectProps = {
  onSelectedVersion: (version: string) => void;
};

export default function VersionsSelect(props: VersionsSelectProps) {
  const {onSelectedVersion} = props;

  const [versions, setVersions] = useState<string[]>([]);

  const getVersions = async () =>
    axios
      .get(`http://localhost:8080/ml2wf/api/v1/fm/versions/all`)
      .then((r) => {
        setVersions(xmlParser.parse(r.data).List.item.map((v) => v.name));
      });

  useEffect(() => {
    getVersions();
  }, []);

  return (
    <FormControl fullWidth>
      <InputLabel id="version-select-label">Version</InputLabel>
      <Select
        labelId="versions-select-label"
        id="versions-select"
        label="Version"
        onChange={(e) => onSelectedVersion(e.target.value)}
      >
        {versions.map((v) => (
          <MenuItem value={v} key={v}>
            {v}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
}
