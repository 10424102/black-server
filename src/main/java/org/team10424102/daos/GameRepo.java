package org.team10424102.daos;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.team10424102.models.Game;

@Repository
public interface GameRepo extends PagingAndSortingRepository<Game, Integer> {

    Game findOneByIdentifier(String identifier);
}
