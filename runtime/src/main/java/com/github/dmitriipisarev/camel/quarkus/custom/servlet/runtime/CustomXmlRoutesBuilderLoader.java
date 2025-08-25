package com.github.dmitriipisarev.camel.quarkus.custom.servlet.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dsl.xml.io.XmlRoutesBuilderLoader;
import org.apache.camel.spi.Resource;
import org.apache.camel.spi.annotations.RoutesLoader;
import org.apache.commons.io.IOUtils;

@ManagedResource(description = "Managed XML RoutesBuilderLoader")
@RoutesLoader(XmlRoutesBuilderLoader.EXTENSION)
public class CustomXmlRoutesBuilderLoader extends XmlRoutesBuilderLoader {
    @Override
    public void preParseRoute(Resource resource) throws Exception {
        super.preParseRoute(new OverriddenResource(resource));
    }

    @Override
    public RouteBuilder doLoadRouteBuilder(Resource input) throws Exception {
        return super.doLoadRouteBuilder(new OverriddenResource(input));
    } 
}
