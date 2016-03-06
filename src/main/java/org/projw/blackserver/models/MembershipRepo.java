package org.projw.blackserver.models;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepo extends PagingAndSortingRepository<Membership, Long> {
}
