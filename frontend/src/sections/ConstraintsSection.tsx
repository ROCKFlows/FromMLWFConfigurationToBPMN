import * as React from 'react';
import {Stack, Typography} from '@mui/material';

type ConstraintsSectionProps = {
  constraints: any[] | undefined;
};

export default function ConstraintsSection(props: ConstraintsSectionProps) {
  const {constraints} = props;

  return (
    <Stack spacing={1} height="100%">
      <Typography>Constraints</Typography>
      {false &&
        constraints?.constraints &&
        constraints?.constraints.map((c) => <p>{c}</p>)}
    </Stack>
  );
}
