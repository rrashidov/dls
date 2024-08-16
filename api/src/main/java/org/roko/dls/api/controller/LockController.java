package org.roko.dls.api.controller;

import org.roko.dls.api.service.LockService;
import org.roko.dls.api.sublock.client.LockResult;
import org.roko.dls.api.sublock.client.UnlockResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/lock")
public class LockController {

    private LockService svc;

    @Autowired
    public LockController(LockService svc) {
        this.svc = svc;
    }

    @PostMapping("/{lockId}")
    public ResponseEntity<String> lock(@PathVariable("lockId") String lockId) {
        LockResult lockResult = svc.lock(lockId);

        if (lockResult.equals(LockResult.OK)) {
            return ResponseEntity.ok().build();
        }

        if (lockResult.equals(LockResult.ALREADY_LOCKED)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{lockId}")
    public ResponseEntity<String> unlock(@PathVariable("lockId") String lockId) {
        UnlockResult unlockResult = svc.unlock(lockId);

        if (unlockResult.equals(UnlockResult.OK)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.internalServerError().build();
    }
}
