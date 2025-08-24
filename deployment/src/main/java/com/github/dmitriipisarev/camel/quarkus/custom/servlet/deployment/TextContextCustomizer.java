package com.github.dmitriipisarev.camel.quarkus.custom.servlet.deployment;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spi.CamelContextCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextContextCustomizer implements CamelContextCustomizer {
    private static final Logger LOG = LoggerFactory.getLogger(TextContextCustomizer.class);

    @Override
    public void configure(CamelContext camelContext) {
        DefaultCamelContext defaultCamelContext = (DefaultCamelContext) camelContext;

        for (RouteDefinition routeDefinition : defaultCamelContext.getRouteDefinitions()) {
            LOG.info("Route definition is: {}", routeDefinition.toString());
        }
    }
}
