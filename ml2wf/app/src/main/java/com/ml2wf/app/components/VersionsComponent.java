package com.ml2wf.app.components;

import com.ml2wf.contract.business.IVersionsComponent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VersionsComponent {

    // TODO: to improve

    private final IVersionsComponent<?> versionsComponent;

    public VersionsComponent(IVersionsComponent<?> versionsComponent) {
        this.versionsComponent = versionsComponent;
    }

    public Optional<?> getLastVersion() {
        return versionsComponent.getLastVersion();
    }
}
