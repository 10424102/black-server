package org.projw.blackserver.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepo extends CrudRepository<PostLike, Long> {
    PostLike findOneByPostIdAndUser(Long postId, User user);
}
