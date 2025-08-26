package com.github.dmitriipisarev.camel.quarkus.custom.servlet.runtime;

import org.apache.camel.CamelContext;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.dsl.xml.io.XmlRoutesBuilderLoader;
import org.apache.camel.spi.CamelContextCustomizer;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import org.jboss.logging.Logger;

@Recorder
public class CustomContextRecorder {
    private static final Logger LOG = Logger.getLogger(CustomContextRecorder.class);

    public void replaceXmlBuilder(RuntimeValue<CamelContext> camelContext) {
        LOG.info("Trying to replace xml loader");
        throw new RuntimeException("Unable to replace xml loader");
        //((ExtendedCamelContext) camelContext).addContextPlugin(XmlRoutesBuilderLoader.class, new CustomXmlRoutesBuilderLoader());
    }

    public RuntimeValue<CamelContextCustomizer> createCustomizer() {
        LOG.info("Trying to create customizer");
        return new RuntimeValue<>(context -> {
            ((ExtendedCamelContext) context).addContextPlugin(XmlRoutesBuilderLoader.class, new CustomXmlRoutesBuilderLoader());
        });
    }

    public void testRuntimStep() {
        LOG.info("Test runtime step");
        throw new RuntimeException("Unable to execute test runtime step");
    }
}
