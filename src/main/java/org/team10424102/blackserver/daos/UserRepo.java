package org.team10424102.blackserver.daos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.team10424102.blackserver.models.User;

@Repository
@Transactional
public interface UserRepo extends PagingAndSortingRepository<User, Integer> {

    @Query("select case when count(u)>0 then true else false end from User u where u.profile.phone = ?1")
    boolean existsByPhone(String phone);

    @Query("select u from User u where u.profile.phone = ?1")
    User findByPhone(String phone);

    @Query("select case when count(u)>0 then true else false end from User u inner join u.friendshipSet f where u.id = :uid1 and exists (select f from f where f.friend.id = :uid2)")
    boolean friendOf(int uid1, int uid2);

    @Query("select case when count(u)>0 then true else false end from User u inner join u.focuses f where u.id = :uid1 and exists (select f from f where f.id = :uid2)")
    boolean fanOf(int uid1, int uid2);

    @Query("select case when count(u)>0 then true else false end from User u inner join u.fans f where u.id = :uid1 and exists (select f from f where f.id = :uid2)")
    boolean focusOf(int uid1, int uid2);
}
