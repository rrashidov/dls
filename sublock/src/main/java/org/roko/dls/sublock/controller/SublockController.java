package org.roko.dls.sublock.controller;

import org.roko.dls.sublock.domain.LockResult;
import org.roko.dls.sublock.domain.UnlockResult;
import org.roko.dls.sublock.svc.SublockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sublock")
public class SublockController {

    private SublockService svc;

    @Autowired
    public SublockController(SublockService svc) {
        this.svc = svc;
    }

    @PostMapping("/{lockId}")
    public ResponseEntity<String> lock(@PathVariable("lockId") String id) {

        LockResult lockResult = svc.lock(id);

        return transformLockResultToHttpResponse(lockResult);
    }

    @DeleteMapping("/{lockId}")
    public ResponseEntity<String> unlock(@PathVariable("lockId") String id) {

        UnlockResult lockResult = svc.unlock(id);

        return transformUnlockResultToHttpResponse(lockResult);
    }

    private ResponseEntity<String> transformLockResultToHttpResponse(LockResult lockResult) {
        if (lockResult == LockResult.OK) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } else if (lockResult == LockResult.ALREADY_LOCKED) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        } else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }

    private ResponseEntity<String> transformUnlockResultToHttpResponse(UnlockResult lockResult) {
        if (lockResult == UnlockResult.OK) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }
}
