package com.ml2wf.graphql.controllers;

import com.ml2wf.contract.business.IConfigurationComponent;
import com.ml2wf.core.configurations.NamedConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ConfigurationController {

    private final IConfigurationComponent configurationComponent;

    ConfigurationController(@Autowired IConfigurationComponent configurationComponent) {
        this.configurationComponent = configurationComponent;
    }

    @QueryMapping
    public NamedConfiguration configuration(@Argument String name) {
        return configurationComponent.getConfiguration(name);
    }

    @QueryMapping
    public List<NamedConfiguration> configurations() {
        return configurationComponent.getAllConfigurations();
    }
}
