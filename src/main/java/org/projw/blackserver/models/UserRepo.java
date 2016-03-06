package org.projw.blackserver.models;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends PagingAndSortingRepository<User, Long> {

    @Query("select case when count(u)>0 then true else false end from User u where u.phone = ?1")
    boolean isPhoneExists(String phone);

    @Query("select u from User u where u.phone = ?1")
    User findByPhone(String phone);

    @Query("select case when count(u)>0 then true else false end from User u inner join u.friendshipSet f where u.id = :uid1 and exists (select f from f where f.friend.id = :uid2)")
    boolean friendOf(int uid1, int uid2);

    @Query("select case when count(u)>0 then true else false end from User u inner join u.focuses f where u.id = :uid1 and exists (select f from f where f.id = :uid2)")
    boolean fanOf(int uid1, int uid2);

    @Query("select case when count(u)>0 then true else false end from User u inner join u.fans f where u.id = :uid1 and exists (select f from f where f.id = :uid2)")
    boolean focusOf(int uid1, int uid2);
}
