package com.ml2wf.core.configurations;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Locale;

@Data
@AllArgsConstructor
public class ConfigurationFeature {

    public enum Status {
        SELECTED, UNSELECTED, UNDEFINED;

        public String getLowercaseName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    private Status automatic;
    private Status manual;
    private String name;
}
