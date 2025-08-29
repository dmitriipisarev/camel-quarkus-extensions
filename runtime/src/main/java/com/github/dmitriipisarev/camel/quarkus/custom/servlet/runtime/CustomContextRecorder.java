package com.github.dmitriipisarev.camel.quarkus.custom.servlet.runtime;

import org.apache.camel.CamelContext;
import org.apache.camel.Service;
import org.apache.camel.spi.CamelContextCustomizer;
import org.apache.camel.spi.RoutesBuilderLoader;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import org.jboss.logging.Logger;

@Recorder
public class CustomContextRecorder {
    private static final Logger LOG = Logger.getLogger(CustomContextRecorder.class);

    public void replaceXmlBuilder(RuntimeValue<CamelContext> camelContext) {
        LOG.info("Trying to replace xml loader");
        camelContext.getValue().getCamelContextExtension().addContextPlugin(RoutesBuilderLoader.class, new CustomXmlRoutesBuilderLoader());
    }

    public RuntimeValue<CamelContextCustomizer> createCustomizer() {
        LOG.info("Trying to create customizer");
        return new RuntimeValue<>(context -> {
            addServiceToContext(context);
        });
    }

    private void addServiceToContext(CamelContext context) {
        for (Service serv : context.getCamelContextExtension().getServices()) {
            LOG.info(String.format("Service id = %s", serv.toString()));
        }

        CustomXmlRoutesBuilderLoader service = new CustomXmlRoutesBuilderLoader();
        try {
            context.addService(service);
            service.start();
        } catch (Exception e) {
            LOG.error("Unable to add service", e);
        }
    }
}
