package org.roko.dls.api.providers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

import org.roko.dls.api.lockclient.DistributedLockClient;
import org.roko.dls.api.providers.config.SublockClientConfigObject;
import org.roko.dls.api.sublockclient.SublockClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

@Configuration
public class DistributedClientProvider {

    @Value("${sublock.client.config.path}")
    private String sublockClientConfigPath;

    @Bean
    public DistributedLockClient getDistributedLockClient()
            throws JsonSyntaxException, JsonIOException, FileNotFoundException {
        Gson gson = new Gson();

        SublockClientConfigObject sublockClientConfigObject = gson
                .fromJson(new FileReader(new File(sublockClientConfigPath)), SublockClientConfigObject.class);

        List<SublockClient> sublockClients = sublockClientConfigObject.getSublockClients().stream()
                .map(c -> {
                    RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
                    return new SublockClient(restTemplateBuilder.rootUri(c.getRootUri()).build());
                })
                .collect(Collectors.toList());

        return new DistributedLockClient(sublockClients);
    }
}
