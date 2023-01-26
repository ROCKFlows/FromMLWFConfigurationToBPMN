package com.ml2wf.graphql.controllers;

import com.ml2wf.contract.business.IConfigurationComponent;
import com.ml2wf.core.configurations.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ConfigurationController {

    private final IConfigurationComponent configurationComponent;

    ConfigurationController(@Autowired IConfigurationComponent configurationComponent) {
        this.configurationComponent = configurationComponent;
    }

    @QueryMapping
    public Configuration configurationById(@Argument String id) {
        return configurationComponent.getConfiguration(id);
    }
}
