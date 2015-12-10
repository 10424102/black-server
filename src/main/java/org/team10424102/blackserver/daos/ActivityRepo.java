package org.team10424102.blackserver.daos;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.team10424102.blackserver.models.*;

import java.util.Collection;
import java.util.List;


@Repository
@Transactional
public interface ActivityRepo extends PagingAndSortingRepository<Activity, Long> {
    List<Activity> findByPromoterProfileCollege(College college, Pageable pageable);
    List<Activity> findByPromoterIn(Collection<User> users, Pageable pageable);
    List<Activity> findByPromoter(User user, Pageable pageable);

    List<Post> findCommentsById(Long id, Pageable pageable);
    List<ActivityLike> findLikesById(Long id, Pageable pageable);

}
