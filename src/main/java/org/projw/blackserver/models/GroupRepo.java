package org.projw.blackserver.models;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends PagingAndSortingRepository<UserGroup, Long> {

    @Query("select case when count(g) >0 then true else false end from UserGroup g inner join g.members m where g.id = :gid and :uid in (select m.id from m)")
    boolean isUserInGroup(int uid, long gid);

}
