package org.projw.blackserver.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo extends CrudRepository<Image, Long> {
    Image findOneByTags(String tags);
    Image findOneByHash(String hash);
}
