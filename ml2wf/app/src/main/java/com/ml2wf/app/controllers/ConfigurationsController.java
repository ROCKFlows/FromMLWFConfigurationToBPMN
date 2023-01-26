package com.ml2wf.app.controllers;

import com.ml2wf.contract.business.IConfigurationComponent;
import com.ml2wf.contract.mapper.IObjectMapperFactory;
import com.ml2wf.core.configurations.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuration")
public class ConfigurationsController {

    private final IObjectMapperFactory objectMapperFactory;
    private final IConfigurationComponent configurationsComponent;

    public ConfigurationsController(@Autowired IObjectMapperFactory objectMapperFactory,
                                    @Autowired IConfigurationComponent configurationsComponent) {
        this.objectMapperFactory = objectMapperFactory;
        this.configurationsComponent = configurationsComponent;
    }

    @PostMapping(value = {"", "/"}, consumes = {MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<String> importBPMNWorkflow(@RequestParam String configurationName,
                                              @RequestBody String configurationString)
            throws Throwable {
        // TODO: use jackson to automatically convert requestbody to configuration
        Configuration configuration = objectMapperFactory.createNewObjectMapper()
                .readValue(configurationString, Configuration.class);
        configurationsComponent.importConfiguration(configurationName, configuration); // TODO: check result
        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }
}
