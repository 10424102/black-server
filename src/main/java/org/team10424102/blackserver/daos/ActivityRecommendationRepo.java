package org.team10424102.blackserver.daos;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.team10424102.blackserver.models.ActivityRecommendation;


@Repository
@Transactional
public interface ActivityRecommendationRepo extends PagingAndSortingRepository<ActivityRecommendation, Long> {
}
