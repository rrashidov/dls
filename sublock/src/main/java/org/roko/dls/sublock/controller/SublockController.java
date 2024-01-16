package org.roko.dls.sublock.controller;

import org.roko.dls.sublock.service.SublockLockResult;
import org.roko.dls.sublock.service.SublockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> lock(@PathVariable("lockId")String lockId) {
        SublockLockResult lockResult = svc.lock(lockId);

        if (lockResult == SublockLockResult.OK) {
            return ResponseEntity.status(HttpStatus.OK).body("OK");
        } else if (lockResult == SublockLockResult.ALREADY_LOCKED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already locked");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }

}
