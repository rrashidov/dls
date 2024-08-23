package org.roko.dls.client;

import java.util.function.Supplier;

import org.roko.dls.client.util.RestTemplateSupplier;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class DLSClient {

    private static final String LOCK_API_PATH = "/api/v1/lock/";

    private Supplier<RestTemplate> restTemplateSupplier;

    public DLSClient(String url) {
        this(new RestTemplateSupplier(url));
    }

    protected DLSClient(Supplier<RestTemplate> restTemplateSupplier) {
        this.restTemplateSupplier = restTemplateSupplier;
    }

    public LockResult lock(String id) {
        try {
            RestTemplate restTemplate = restTemplateSupplier.get();

            restTemplate.postForEntity(LOCK_API_PATH + id, id, String.class);

            return LockResult.OK;
        } catch (HttpClientErrorException e) {
            return LockResult.ALREADY_LOCKED;
        } catch (HttpServerErrorException e) {
            return LockResult.LOCK_FAILED;
        }
    }

    public UnlockResult unlock(String id) {
        try {
            RestTemplate restTemplate = restTemplateSupplier.get();

            restTemplate.exchange(LOCK_API_PATH + id, HttpMethod.DELETE, null, String.class);
    
            return UnlockResult.OK;
        } catch (HttpServerErrorException e) {
            return UnlockResult.UNLOCK_FAILED;
        }
    }
}
