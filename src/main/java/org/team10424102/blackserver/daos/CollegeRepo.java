package org.team10424102.blackserver.daos;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.team10424102.blackserver.models.College;

@Repository
@Transactional
public interface CollegeRepo extends PagingAndSortingRepository<College, Integer> {
    College findOneByName(String name);
}
