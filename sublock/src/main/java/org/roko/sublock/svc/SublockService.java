package org.roko.sublock.svc;

import java.util.Optional;

import org.roko.sublock.domain.LockResult;
import org.roko.sublock.domain.Sublock;
import org.roko.sublock.domain.UnlockResult;
import org.roko.sublock.repo.SublockRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class SublockService {

    private SublockRepo repo;

    @Autowired
    public SublockService(SublockRepo repo) {
        this.repo = repo;
    }

    public LockResult lock(String id) {
        synchronized(id){
            try {
                Optional<Sublock> sublockOptional = repo.findById(id);

                if (!sublockOptional.isPresent()) {
                    Sublock sublock = new Sublock();
                    sublock.setId(id);
                    repo.save(sublock);
                    return LockResult.OK;
                }
    
                return LockResult.ALREADY_LOCKED;    
            } catch (DataAccessException e) {
                return LockResult.LOCK_FAILED;
            }
        }
    }

    public UnlockResult unlock(String id){
        synchronized(id){
            try {
                Optional<Sublock> sublockOptional = repo.findById(id);

                if (sublockOptional.isPresent()) {
                    repo.delete(sublockOptional.get());
                }
    
                return UnlockResult.OK;
            } catch (DataAccessException e) {
                return UnlockResult.UNLOCK_FAILED;
            }
        }
    }
}
