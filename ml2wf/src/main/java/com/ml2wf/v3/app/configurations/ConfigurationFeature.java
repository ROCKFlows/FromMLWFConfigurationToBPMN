package com.ml2wf.v3.app.configurations;

import lombok.Data;

import java.util.Locale;

@Data
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
