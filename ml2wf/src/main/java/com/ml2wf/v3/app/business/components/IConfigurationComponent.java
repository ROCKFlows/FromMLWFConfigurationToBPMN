package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.configurations.Configuration;
import org.springframework.stereotype.Component;

@Component
public interface IConfigurationComponent {

    boolean importConfiguration(String name, Configuration configuration) throws Throwable;
}
