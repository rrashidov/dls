package org.roko.dls.sublock.svc;

import java.util.Optional;

import org.roko.dls.sublock.domain.LockResult;
import org.roko.dls.sublock.domain.Sublock;
import org.roko.dls.sublock.domain.UnlockResult;
import org.roko.dls.sublock.repo.SublockRepo;
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
        synchronized(id.intern()){
            try {
                Optional<Sublock> sublockOptional = repo.findById(id);

                if (!sublockOptional.isPresent()) {
                    print("[lock] Sublock " + id + " not found");
                    Sublock sublock = new Sublock();
                    sublock.setId(id);
                    repo.save(sublock);
                    repo.flush();
                    print("[lock] Sublock " + id + " created");
                    return LockResult.OK;
                }

                print("[lock] Sublock " + id + " found");
    
                return LockResult.ALREADY_LOCKED;    
            } catch (DataAccessException e) {
                return LockResult.LOCK_FAILED;
            }
        }
    }

    public UnlockResult unlock(String id){
        synchronized(id.intern()){
            try {
                Optional<Sublock> sublockOptional = repo.findById(id);

                if (sublockOptional.isPresent()) {
                    print("[unlock] Sublock found, delete it");
                    repo.delete(sublockOptional.get());
                    repo.flush();
                    print("[unlock] Sublock deleted");
                    return UnlockResult.OK;
                }

                print("[unlock] Sublock not found");
    
                return UnlockResult.OK;
            } catch (DataAccessException e) {
                return UnlockResult.UNLOCK_FAILED;
            }
        }
    }

    private void print(String msg){
        Thread currThread = Thread.currentThread();

        long timeMillis = System.currentTimeMillis();

        System.out.println(currThread + " : " + timeMillis + " : " + msg);
    }
}
