package org.roko.sublock.repo;

import org.roko.sublock.domain.Sublock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SublockRepo extends JpaRepository<Sublock, String> {

}
