package org.team10424102.blackserver.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.team10424102.blackserver.models.Image;

@Repository
public interface ImageRepo extends CrudRepository<Image, Long> {
    Image findOneByTags(String tags);
    Image findOneByHash(String hash);
}
