package com.github.dmitriipisarev.camel.quarkus.custom.servlet.deployment;

import java.util.List;
import java.util.Optional;

import com.github.dmitriipisarev.camel.quarkus.custom.servlet.runtime.CamelCustomServletConfig;
import com.github.dmitriipisarev.camel.quarkus.custom.servlet.runtime.CamelCustomServletConfig.ServletConfig;
import com.github.dmitriipisarev.camel.quarkus.custom.servlet.runtime.CamelCustomServletConfig.ServletConfig.MultipartConfig;

import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

import java.util.Map.Entry;
import io.quarkus.undertow.deployment.ServletBuildItem;
import io.quarkus.undertow.deployment.ServletBuildItem.Builder;
import io.quarkus.undertow.deployment.WebMetadataBuildItem;
import jakarta.servlet.MultipartConfigElement;
import org.jboss.metadata.web.spec.WebMetaData;

import static com.github.dmitriipisarev.camel.quarkus.custom.servlet.runtime.CamelCustomServletConfig.ServletConfig.DEFAULT_SERVLET_CLASS;


class CamelQuarkusCustomServletProcessor {

    private static final String FEATURE = "camel-quarkus-custom-servlet";

    CamelCustomServletConfig camelServletConfig;

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void build(BuildProducer<ServletBuildItem> servlet, WebMetadataBuildItem webMetadata) {
        boolean servletCreated = false;

        WebMetaData metaData = webMetadata.getWebMetaData();
        if (metaData != null && metaData.getServlets() != null) {
            servletCreated = metaData.getServlets()
                    .stream()
                    .anyMatch(meta -> meta.getServletClass().equals(DEFAULT_SERVLET_CLASS));
        }

        if (camelServletConfig.defaultServlet().isValid()) {
            servlet.produce(
                    newServlet(ServletConfig.DEFAULT_SERVLET_NAME, camelServletConfig.defaultServlet()));
            servletCreated = true;
        }

        for (Entry<String, ServletConfig> e : camelServletConfig.namedServlets().entrySet()) {
            if (ServletConfig.DEFAULT_SERVLET_NAME.equals(e.getKey())) {
                throw new IllegalStateException(
                        String.format("Use quarkus.camel.servlet.url-patterns instead of quarkus.camel.servlet.%s.url-patterns",
                                ServletConfig.DEFAULT_SERVLET_NAME));
            }
            servlet.produce(newServlet(e.getKey(), e.getValue()));
            servletCreated = true;
        }

        if (!servletCreated) {
            throw new IllegalStateException(
                    "Map at least one servlet to a path using quarkus.camel.servlet.url-patterns or quarkus.camel.servlet.[your-servlet-name].url-patterns");
        }
    }

    static ServletBuildItem newServlet(String key, ServletConfig servletConfig) {
        final String servletName = servletConfig.getEffectiveServletName(key);
        final Optional<List<String>> urlPatterns = servletConfig.urlPatterns();
        if (!urlPatterns.isPresent() || urlPatterns.get().isEmpty()) {
            throw new IllegalStateException(
                    String.format("Missing quarkus.camel.servlet%s.url-patterns",
                            ServletConfig.DEFAULT_SERVLET_NAME.equals(servletName) ? "" : "." + servletName));
        }

        final Builder builder = ServletBuildItem.builder(servletName, servletConfig.servletClass());
        for (String pattern : urlPatterns.get()) {
            builder.addMapping(pattern);
        }

        // NOTE: We only configure loadOnStartup, async & forceAwait if the default values were overridden
        if (servletConfig.loadOnStartup() > -1) {
            builder.setLoadOnStartup(servletConfig.loadOnStartup());
        }

        if (servletConfig.async()) {
            builder.setAsyncSupported(servletConfig.async());
            builder.addInitParam("async", "true");
        }

        if (servletConfig.forceAwait()) {
            builder.addInitParam("forceAwait", "true");
        }

        servletConfig.executorRef().ifPresent(executorRef -> {
            builder.addInitParam("executorRef", executorRef);
        });

        MultipartConfig multipartConfig = servletConfig.multipart();
        if (multipartConfig != null) {
            builder.setMultipartConfig(new MultipartConfigElement(
                    multipartConfig.location(),
                    multipartConfig.maxFileSize(),
                    multipartConfig.maxRequestSize(),
                    multipartConfig.fileSizeThreshold()));
        }

        return builder.build();
    }
}
