package com.ml2wf.app.controllers;

import com.ml2wf.contract.business.IConfigurationComponent;
import com.ml2wf.core.configurations.NamedConfiguration;
import com.ml2wf.core.configurations.RawConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/configuration")
public class ConfigurationsController {

    private final IConfigurationComponent configurationsComponent;

    public ConfigurationsController(@Autowired IConfigurationComponent configurationsComponent) {
        this.configurationsComponent = configurationsComponent;
    }

    @PostMapping(value = {"", "/"}, consumes = {MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<String> importConfiguration(@RequestParam String configurationName,
                                              @RequestBody RawConfiguration configuration) {
        NamedConfiguration namedConfiguration = new NamedConfiguration(configurationName, configuration.getFeatures());
        return configurationsComponent.importConfiguration(namedConfiguration)
                .thenReturn("OK"); // TODO: check result
    }

    @GetMapping(value = {"", "/"}, produces = {MediaType.APPLICATION_XML_VALUE})
    Mono<RawConfiguration> importConfiguration(@RequestParam String configurationName) {
        return configurationsComponent.getConfiguration(configurationName)
                .map((c) -> new RawConfiguration(c.getFeatures()));
    }
}
