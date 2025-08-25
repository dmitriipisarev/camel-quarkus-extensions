package com.github.dmitriipisarev.camel.quarkus.custom.servlet.runtime;

import org.apache.camel.CamelContext;
import org.apache.camel.ExtendedCamelContext;
import org.apache.camel.dsl.xml.io.XmlRoutesBuilderLoader;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class CustomContextRecorder {
    public void replaceXmlBuilder(RuntimeValue<CamelContext> camelContext) {
        ((ExtendedCamelContext) camelContext).addContextPlugin(XmlRoutesBuilderLoader.class, null);
    }
}
