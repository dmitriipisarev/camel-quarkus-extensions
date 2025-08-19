package com.github.dmitriipisarev.camel.quarkus.custom.servlet.runtime;

import org.apache.camel.BindToRegistry;
import org.apache.camel.spi.ComponentCustomizer;

import com.github.dmitriipisarev.HandlingHttpBinding;
import com.github.dmitriipisarev.ServletCustomComponent;
import com.github.dmitriipisarev.ServletCustomFilterStrategy;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class CamelCustomServletConfiguration {
    @Produces
    @ApplicationScoped
    public ServletCustomFilterStrategy servletCustomFilterStrategy() {
        return new ServletCustomFilterStrategy();
    }
    
    @BindToRegistry("handlingHttpBinding")
    public HandlingHttpBinding handlingHttpBinding(ServletCustomFilterStrategy servletCustomFilterStrategy) {
        return new HandlingHttpBinding(servletCustomFilterStrategy);
    }

    @Produces
    @ApplicationScoped
    public ComponentCustomizer servletCustomComponentCustomizer(ServletCustomFilterStrategy servletCustomFilterStrategy) {
        return ComponentCustomizer.builder(ServletCustomComponent.class)
                .build((component) -> {
                    component.setHeaderFilterStrategy(servletCustomFilterStrategy);
                });
    }
}
