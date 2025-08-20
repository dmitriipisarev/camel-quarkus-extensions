package com.github.dmitriipisarev.camel.quarkus.custom.servlet.runtime;

import java.util.UUID;

import org.apache.camel.spi.PropertiesSource;
import org.apache.camel.spi.annotations.JdkService;

import jakarta.enterprise.context.ApplicationScoped;

@JdkService("properties-source-factory")
@ApplicationScoped
public class CustomPropertiesSource implements PropertiesSource {

    @Override
    public String getName() {
        return "CustomPropertiesSource";
    }

    @Override
    public String getProperty(String name) {
        if ("test.prop".equals(name)){
            return UUID.randomUUID().toString();
        } else {
            return "nothing";
        }
    }
}
