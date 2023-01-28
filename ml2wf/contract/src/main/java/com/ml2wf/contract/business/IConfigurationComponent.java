package com.ml2wf.contract.business;

import com.ml2wf.core.configurations.NamedConfiguration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IConfigurationComponent {

    boolean importConfiguration(NamedConfiguration configuration);

    NamedConfiguration getConfiguration(String configurationName);

    List<NamedConfiguration> getAllConfigurations();
}
