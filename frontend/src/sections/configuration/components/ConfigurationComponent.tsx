import * as React from 'react';
import {
  Checkbox,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
} from '@mui/material';
import {
  Configuration,
  ConfigurationFeature,
  FeatureSelectionStatus,
} from '../../../store/api/configurationApi';

type ConfigurationProps = {
  configuration: Configuration;
  onSelected?: (f: ConfigurationFeature) => void;
};

export default function ConfigurationComponent(props: ConfigurationProps) {
  const {configuration, onSelected} = props;

  return (
    <List dense>
      {configuration.features.map((f) => (
        <ListItem
          key={`list-item-feature-${f.name}`}
          secondaryAction={
            <Checkbox
              edge="end"
              checked={
                f.manual === FeatureSelectionStatus.SELECTED ||
                f.automatic === FeatureSelectionStatus.SELECTED
              }
              disabled={f.automatic === FeatureSelectionStatus.SELECTED}
              onChange={(_, checked) => {
                onSelected &&
                  onSelected({
                    ...f,
                    manual: checked
                      ? FeatureSelectionStatus.SELECTED
                      : FeatureSelectionStatus.UNSELECTED,
                  });
              }}
            />
          }
        >
          <ListItemButton>
            <ListItemText
              id={`list-item-text-feature-${f.name}`}
              key={`list-item-text-feature-${f.name}`}
              primary={f.name}
            />
          </ListItemButton>
        </ListItem>
      ))}
    </List>
  );
}

ConfigurationComponent.defaultProps = {
  onSelected: undefined,
};
