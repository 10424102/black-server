package org.projw.blackserver.extensions.dota2;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Dota2MatchResultRepo extends CrudRepository<Dota2MatchResult, Long> {
}
