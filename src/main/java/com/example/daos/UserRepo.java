package com.example.daos;

import com.example.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepo extends PagingAndSortingRepository<User, Integer> {

    User findOneByPhone(String phone);

    @Query("select case when count(u)>0 then true else false end from User u where u.phone = ?1")
    boolean existsByPhone(String phone);

    @Query("select u.id from User u where u.phone = ?1")
    Integer findUserIdByphone(String phone);

    @Query("select case when count(u)>0 then true else false end from User u inner join u.friendshipSet f where u.id = :uid1 and exists (select f from f where f.friend.id = :uid2)")
    boolean friendOf(int uid1, int uid2);

    @Query("select case when count(u)>0 then true else false end from User u inner join u.focuses f where u.id = :uid1 and exists (select f from f where f.id = :uid2)")
    boolean fanOf(int uid1, int uid2);

    @Query("select case when count(u)>0 then true else false end from User u inner join u.fans f where u.id = :uid1 and exists (select f from f where f.id = :uid2)")
    boolean followingOf(int uid1, int uid2);
}
