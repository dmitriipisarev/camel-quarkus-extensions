package com.github.dmitriipisarev.camel.quarkus.custom.servlet.runtime;

import org.apache.camel.CamelContext;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.dsl.xml.io.XmlRoutesBuilderLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class CustomContextRecorder {
    private static final Logger LOG = LoggerFactory.getLogger(CustomContextRecorder.class);

    public void replaceXmlBuilder(RuntimeValue<CamelContext> camelContext) {
        LOG.info("Trying to replace xml loader");
        ((ExtendedCamelContext) camelContext).addContextPlugin(XmlRoutesBuilderLoader.class, new CustomXmlRoutesBuilderLoader());
    }
}
