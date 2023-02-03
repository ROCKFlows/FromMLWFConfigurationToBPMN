package com.ml2wf.app.controllers;

import com.ml2wf.contract.business.IConfigurationComponent;
import com.ml2wf.core.configurations.NamedConfiguration;
import com.ml2wf.core.configurations.RawConfiguration;
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

    @PostMapping(value = {""}, consumes = {MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> importConfiguration(@RequestParam String configurationName,
                                              @RequestBody RawConfiguration configuration) {
        NamedConfiguration namedConfiguration = new NamedConfiguration(configurationName, configuration.getFeatures());
        configurationsComponent.importConfiguration(namedConfiguration); // TODO: check result
        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }

    @GetMapping(value = {""}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    RawConfiguration getConfiguration(@RequestParam String configurationName) {
        return new RawConfiguration(configurationsComponent.getConfiguration(configurationName).getFeatures());
    }
}
