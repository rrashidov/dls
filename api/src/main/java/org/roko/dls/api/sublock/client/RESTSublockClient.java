package org.roko.dls.api.sublock.client;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RESTSublockClient implements SublockClient {

    private final RestTemplate restClient;

    public RESTSublockClient(RestTemplate restClient) {
        this.restClient = restClient;
    }

    @Override
    public LockResult lock(String id) {
        ResponseEntity<String> response = restClient.postForEntity("/api/v1/sublock/" + id, id, String.class);

        if (response.getStatusCode().is4xxClientError()) {
            return LockResult.ALREADY_LOCKED;
        }

        if (response.getStatusCode().is5xxServerError()) {
            return LockResult.LOCK_FAILED;
        }

        return LockResult.OK;
    }

    @Override
    public UnlockResult unlock(String id) {
        ResponseEntity<String> response = restClient.exchange("/api/v1/sublock/" + id, HttpMethod.DELETE, null,
                String.class);

        if (response.getStatusCode().is5xxServerError()) {
            return UnlockResult.UNLOCK_FAILED;
        }

        return UnlockResult.OK;
    }

}