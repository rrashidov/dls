package org.roko.dls.sublock.service;

import java.util.Optional;

import org.roko.dls.sublock.model.Sublock;
import org.roko.dls.sublock.repository.SublockRepository;
import org.roko.dls.sublock.service.util.SublockDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SublockService {

    private SublockRepository repo;
    private SublockDateUtil util;

    @Autowired
    public SublockService(SublockRepository repo, SublockDateUtil util) {
        this.repo = repo;
        this.util = util;
    }

    public SublockLockResult lock(String lockId) {
        Optional<Sublock> sublockOptional = repo.findById(lockId);

        if (sublockOptional.isPresent()) {
            Sublock sublock = sublockOptional.get();
            if (sublock.isLocked()) {
                return SublockLockResult.ALREADY_LOCKED;
            } else {
                sublock.setLocked(true);
                sublock.setTimestamp(System.currentTimeMillis());
                sublock.setDateFlag(util.getDateFlag());
                repo.save(sublock);
                return SublockLockResult.OK;
            }
        } else {
            Sublock sublock = new Sublock();
            sublock.setId(lockId);
            sublock.setLocked(true);
            sublock.setTimestamp(System.currentTimeMillis());
            sublock.setDateFlag(util.getDateFlag());
            repo.save(sublock);
            return SublockLockResult.OK;
        }
    }
}
