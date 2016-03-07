package org.projw.blackserver.models;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeRepo extends PagingAndSortingRepository<College, Integer> {
    College findOneByName(String name);
}
