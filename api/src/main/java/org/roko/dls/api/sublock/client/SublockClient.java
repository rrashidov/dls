package org.roko.dls.api.sublock.client;

public interface SublockClient {

    public LockResult lock(String id);

    public UnlockResult unlock(String id);
}
