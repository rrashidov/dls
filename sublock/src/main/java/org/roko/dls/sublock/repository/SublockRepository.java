package org.roko.dls.sublock.repository;

import org.roko.dls.sublock.Sublock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SublockRepository extends JpaRepository<Sublock, String> {

}
