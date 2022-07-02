package com.ml2wf.v3.app.controllers;

import com.ml2wf.v3.app.business.components.ConfigurationsComponent;
import com.ml2wf.v3.app.configurations.Configuration;
import com.ml2wf.v3.app.xml.XMLObjectMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuration")
public class ConfigurationsController {

    private final ConfigurationsComponent configurationsComponent;

    @Autowired
    public ConfigurationsController(@Autowired ConfigurationsComponent configurationsComponent) {
        this.configurationsComponent = configurationsComponent;
    }

    @PostMapping(value = {"/{name}"},
            consumes = {MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<String> importBPMNWorkflow(@PathVariable String name, @RequestBody String configurationString)
            throws Exception {
        // TODO: use jackson to automatically convert requestbody to configuration
        Configuration configuration = XMLObjectMapperFactory.getInstance()
                .createNewObjectMapper()
                .readValue(configurationString, Configuration.class);
        configurationsComponent.importConfiguration(name, configuration); // TODO: check result
        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }
}
