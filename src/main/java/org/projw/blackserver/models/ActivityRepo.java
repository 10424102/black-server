package org.projw.blackserver.models;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface ActivityRepo extends PagingAndSortingRepository<Activity, Long> {
    List<Activity> findByPromoterCollege(College college, Pageable pageable);
    List<Activity> findByPromoterIn(Collection<User> users, Pageable pageable);
    List<Activity> findByPromoter(User user, Pageable pageable);

    List<Post> findCommentsById(Long id, Pageable pageable);

}
