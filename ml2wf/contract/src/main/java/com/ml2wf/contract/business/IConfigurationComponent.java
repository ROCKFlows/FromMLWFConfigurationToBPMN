package com.ml2wf.contract.business;

import com.ml2wf.core.configurations.NamedConfiguration;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public interface IConfigurationComponent {

    Mono<Boolean> importConfiguration(NamedConfiguration configuration);

    Mono<NamedConfiguration> getConfiguration(String configurationName);

    Flux<NamedConfiguration> getAllConfigurations();
}
