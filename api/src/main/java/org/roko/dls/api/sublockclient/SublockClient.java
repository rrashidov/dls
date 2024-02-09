package org.roko.dls.api.sublockclient;

import org.roko.dls.api.sublockclient.exc.AlreadyLockedException;
import org.roko.dls.api.sublockclient.exc.LockFailedException;

public interface SublockClient {

    public void lock(String id) throws AlreadyLockedException, LockFailedException;

    public void unlock(String id) throws LockFailedException;
}
