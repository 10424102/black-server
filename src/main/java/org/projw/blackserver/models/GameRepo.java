package org.projw.blackserver.models;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepo extends PagingAndSortingRepository<Game, Integer> {

    Game findOneByNameKey(String nameKey);
}
