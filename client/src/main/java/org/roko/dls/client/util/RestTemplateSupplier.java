package org.roko.dls.client.util;

import java.util.function.Supplier;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class RestTemplateSupplier implements Supplier<RestTemplate> {

    private String url;

    private RestTemplate restTemplate;

    public RestTemplateSupplier(String url) {
        this.url = url;
    }

    @Override
    public RestTemplate get() {
        if (restTemplate == null) {
            restTemplate = buildRestTemplate();
        }

        return restTemplate;
    }

    private RestTemplate buildRestTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        builder = builder.rootUri(url);
        return builder.build();
    }
}
