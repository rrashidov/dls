package org.roko.dls.api.controller;

import org.roko.dls.api.lockclient.LockResult;
import org.roko.dls.api.lockclient.UnlockResult;
import org.roko.dls.api.svc.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lock")
public class LockController {

    private LockService svc;

    @Autowired
    public LockController(LockService svc) {
        this.svc = svc;
    }

    @PostMapping("/{lockid}")
    public HttpStatus lock(@PathVariable("lockid") String lockid) {
        LockResult lockResult = svc.lock(lockid);

        if (lockResult.equals(LockResult.ALREADY_LOCKED)) {
            return HttpStatus.CONFLICT;
        }

        if (lockResult.equals(LockResult.LOCK_FAILED)) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.OK;
    }

    @DeleteMapping("/{lockid}")
    public HttpStatus unlock(@PathVariable("lockid") String lockid ){
        UnlockResult unlockResult = svc.unlock(lockid);

        if (unlockResult.equals(UnlockResult.UNLOCK_FAILED)) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.OK;
    }
}
