package org.projw.blackserver.models;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademyRepo extends PagingAndSortingRepository<Academy, Integer> {
    Academy findOneByName(String name);
}
