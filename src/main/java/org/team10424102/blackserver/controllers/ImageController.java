package org.team10424102.blackserver.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.team10424102.blackserver.daos.ImageRepo;
import org.team10424102.blackserver.models.Image;
import org.team10424102.blackserver.services.ImageService;
import org.team10424102.blackserver.services.UserService;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ImageController {
    static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired ImageRepo imageRepo;

    @Autowired ImageService imageService;

    @Autowired UserService userService;

    /**
     * 获取图片
     */
    @RequestMapping(value = "/api/image", method = GET)
    public ResponseEntity getImage(@RequestParam String q) {
        //logger.debug("获取图片 " + q);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        Long id = imageService.getImageIdFromAccessToken(q);
        if (id != null) {
            Image image = imageRepo.findOne(id);
            if (image == null) {
                return new ResponseEntity<>(null, headers, NOT_FOUND);
            } else {
                // image data will never be null
                return new ResponseEntity<>(image.getData(), headers, OK);
            }
        }
        return new ResponseEntity<>(null, headers, UNAUTHORIZED);
    }

    /**
     * 上传图片，成功返回图片的 accessToken，text/plain
     */
    @RequestMapping(value = "/api/image", method = POST)
    public ResponseEntity uploadImage(@RequestParam MultipartFile file) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        try {
            if (!file.isEmpty()) {
                Image image = imageService.saveImage(file.getBytes(), null);
                String token = imageService.generateAccessToken(image);
                return new ResponseEntity<>(token, headers, CREATED);
            }
        } catch (IOException e) {
            logger.warn("上传图片出错", e);
        }
        return new ResponseEntity<>(null, headers, BAD_REQUEST);
    }

}
