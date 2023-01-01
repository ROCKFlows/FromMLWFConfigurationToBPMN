package com.ml2wf.contract.business;

import com.ml2wf.core.configurations.Configuration;
import org.springframework.stereotype.Component;

@Component
public interface IConfigurationComponent {

    boolean importConfiguration(String name, Configuration configuration);
}
