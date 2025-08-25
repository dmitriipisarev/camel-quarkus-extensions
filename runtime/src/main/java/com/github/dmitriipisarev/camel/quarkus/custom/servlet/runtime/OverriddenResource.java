package com.github.dmitriipisarev.camel.quarkus.custom.servlet.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.camel.spi.Resource;
import org.apache.camel.support.CachedResource;
import org.apache.commons.io.IOUtils;

public class OverriddenResource extends CachedResource {
    private final InputStream overriddenRouteBody;

    public OverriddenResource(Resource sourceResource) {
        super(sourceResource);
        try {
            this.overriddenRouteBody = overrideRouteBody(sourceResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Unable to instantiate custom resouces");
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.overriddenRouteBody;
    }

    private InputStream overrideRouteBody(InputStream sourceBody) {
        String body = streamToString(sourceBody).replaceAll("here", "replaced");

        return stringToStream(body);
    }

    private String streamToString(InputStream stream) {
        try {
            return IOUtils.toString(stream, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            return null;
        }
    }

    private InputStream stringToStream(String string) {
        return IOUtils.toInputStream(string);
    }
}
