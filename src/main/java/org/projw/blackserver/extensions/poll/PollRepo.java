package org.projw.blackserver.extensions.poll;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollRepo extends CrudRepository<Poll, Long> {
}
