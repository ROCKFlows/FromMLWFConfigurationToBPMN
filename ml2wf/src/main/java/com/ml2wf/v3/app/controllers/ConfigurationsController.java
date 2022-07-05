package com.ml2wf.v3.app.controllers;

import com.ml2wf.v2.xml.XMLObjectMapperFactory;
import com.ml2wf.v3.app.business.components.IConfigurationComponent;
import com.ml2wf.v3.app.configurations.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuration")
public class ConfigurationsController {

    private final IConfigurationComponent configurationsComponent;

    public ConfigurationsController(@Autowired IConfigurationComponent configurationsComponent) {
        this.configurationsComponent = configurationsComponent;
    }

    @PostMapping(value = {"/{name}"}, consumes = {MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<String> importBPMNWorkflow(@PathVariable String name, @RequestBody String configurationString)
            throws Throwable {
        // TODO: use jackson to automatically convert requestbody to configuration
        Configuration configuration = XMLObjectMapperFactory.getInstance()
                .createNewObjectMapper()
                .readValue(configurationString, Configuration.class);
        configurationsComponent.importConfiguration(name, configuration); // TODO: check result
        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }
}
