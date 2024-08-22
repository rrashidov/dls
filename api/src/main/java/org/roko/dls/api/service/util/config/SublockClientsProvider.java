package org.roko.dls.api.service.util.config;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.roko.dls.api.sublock.client.RESTSublockClient;
import org.roko.dls.api.sublock.client.SublockClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SublockClientsProvider {

    @Value("${sublock.config.path}")
    private String sublockConfigPath;

    private SublockConfigSupplier configSupplier;

    @Autowired
    public SublockClientsProvider(SublockConfigSupplier configSupplier) {
        this.configSupplier = configSupplier;
    }

    @Bean
    public List<SublockClient> getSublockClients(){
        SublockConfig sublockConfig = configSupplier.get(sublockConfigPath);

        return sublockConfig.getSublockClients().stream()
            .map(x -> {
                RestTemplateBuilder builder = new RestTemplateBuilder();
                builder = builder.setConnectTimeout(Duration.ofMillis(x.getConnectTimeout()));
                builder = builder.setReadTimeout(Duration.ofMillis(x.getReadTimeout()));
                builder = builder.rootUri(x.getUrl());

                RestTemplate restTemplate = builder.build();

                return new RESTSublockClient(restTemplate);
            })
            .collect(Collectors.toList());
    }
}
