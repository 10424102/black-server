package org.team10424102.blackserver.daos;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.team10424102.blackserver.models.Friendship;

@Repository
public interface FriendshipRepo extends PagingAndSortingRepository<Friendship, Integer> {
}
