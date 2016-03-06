package org.projw.blackserver.models;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends PagingAndSortingRepository<Notification, Long> {
    List<Notification> findAllByTarget(User target);
}
