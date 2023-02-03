export enum FeatureSelectionStatus {
  SELECTED = 'SELECTED',
  UNSELECTED = 'UNSELECTED',
  UNDEFINED = 'UNDEFINED',
}

export type ConfigurationFeature = {
  name: string;
  automatic?: FeatureSelectionStatus;
  manual?: FeatureSelectionStatus;
};

export type Configuration = {
  name: string;
  features: ConfigurationFeature[];
};

export type RawConfiguration = {
  features?: ConfigurationFeature[];
};

export type Version = {
  name: string;
  major: number;
  minor: number;
  patch: number;
};

export type Description = {
  content?: string;
};

export type FeatureModelTask = {
  parent?: FeatureModelTask;
  name: string;
  version?: string;
  descriptions?: Description[];
  children?: FeatureModelTask[];
  abstract?: boolean;
  mandatory?: boolean;
  identity?: string;
};

export type FeatureModelStructure = {
  children?: FeatureModelTask[];
};
export type AbstractOperand =
  | AbstractOperator & {
      value?: string;
    };

export type AbstractOperator = {
  operands?: AbstractOperand[];
  operandName?: string;
};

export type ConstraintTree = {
  operator?: AbstractOperator;
};

export type FeatureModelRule = {
  constraint?: ConstraintTree;
  description?: Description;
};

export type FeatureModel = {
  structure?: FeatureModelStructure;
  constraints?: FeatureModelRule[];
};
export type Documentation = {
  id?: string;
  content?: string;
};

export type BpmnWorkflowTask = {
  id?: string;
  name: string;
  documentation?: Documentation;
  abstract: boolean;
  optional: boolean;
};

export type BPMNSequenceFlow = {
  id?: string;
  sourceRef?: string;
  targetRef?: string;
};

export type BpmnProcess = {
  id?: string;
  name?: string;
  tasks?: BpmnWorkflowTask[];
  sequenceFlows?: BPMNSequenceFlow[];
};

export type BpmnWorkflow = {
  processes?: BpmnProcess[];
};
