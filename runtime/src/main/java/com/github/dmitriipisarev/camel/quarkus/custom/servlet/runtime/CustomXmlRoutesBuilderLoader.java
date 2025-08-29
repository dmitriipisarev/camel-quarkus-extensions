package com.github.dmitriipisarev.camel.quarkus.custom.servlet.runtime;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dsl.xml.io.XmlRoutesBuilderLoader;
import org.apache.camel.spi.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("routes-builder-loader-xml")
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
