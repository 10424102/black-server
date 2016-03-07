package org.projw.blackserver.models;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepo extends PagingAndSortingRepository<Post, Long> {

    List<Post> findByCommentativeFalseAndSenderCollege(College college, Pageable pageable);

    List<Post> findByCommentativeFalseAndSenderIn(Collection<User> users, Pageable pageable);

    List<Post> findByCommentativeFalseAndSender(User sender, Pageable pageable);

    List<Post> findCommentsById(Long id, Pageable pageable);

    List<PostLike> findLikesById(Long id, Pageable pageable);
}
