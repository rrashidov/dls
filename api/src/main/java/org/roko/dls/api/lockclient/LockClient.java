package org.roko.dls.api.lockclient;

public interface LockClient {

    public LockResult lock(String id);

    public UnlockResult unlock(String id);
}
