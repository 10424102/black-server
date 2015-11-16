package com.example.daos;

import com.example.models.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepo extends CrudRepository<Image, Long>{
}
