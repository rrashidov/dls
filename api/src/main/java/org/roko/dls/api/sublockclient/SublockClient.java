package org.roko.dls.api.sublockclient;

import org.roko.dls.api.sublockclient.exc.AlreadyLockedException;
import org.roko.dls.api.sublockclient.exc.LockFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class SublockClient {

    private RestTemplate restTemplate;

    @Autowired
    public SublockClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void lock(String id) throws AlreadyLockedException, LockFailedException {
        try  {
            restTemplate.postForObject("/api/v1/sublock/" + id, null, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                throw new AlreadyLockedException();
            }

            throw new LockFailedException();
        }
    }

    public void unlock(String id) throws LockFailedException {
        try {
            restTemplate.delete("/api/v1/sublock/" + id);
        } catch (HttpClientErrorException e) {
            throw new LockFailedException();
        }
    }

}
