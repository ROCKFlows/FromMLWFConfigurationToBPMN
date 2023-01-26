package com.ml2wf.core.configurations;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Configuration {

    private List<ConfigurationFeature> features;
}
