package org.roko.dls.api.sublock.client;

import org.roko.dls.dto.LockRequest;

public interface SublockClient {

    public LockResult lock(String id);

    public LockResult lock(String id, LockRequest request);

    public UnlockResult unlock(String id);
}
