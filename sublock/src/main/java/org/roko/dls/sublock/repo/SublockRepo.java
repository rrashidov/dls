package org.roko.dls.sublock.repo;

import org.roko.dls.sublock.domain.Sublock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SublockRepo extends JpaRepository<Sublock, String> {

}
