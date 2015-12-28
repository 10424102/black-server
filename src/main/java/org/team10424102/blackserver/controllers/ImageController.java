package org.team10424102.blackserver.controllers;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.team10424102.blackserver.App;
import org.team10424102.blackserver.models.ImageRepo;
import org.team10424102.blackserver.models.Image;
import org.team10424102.blackserver.services.TokenService;
import org.team10424102.blackserver.utils.Api;
import org.team10424102.blackserver.utils.Cryptor;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(App.API_IMAGE)
public class ImageController {

    @Autowired ImageRepo imageRepo;
    @Autowired TokenService tokenService;

    /**
     * 获取图片
     */
    @RequestMapping(method = GET, produces = MediaType.IMAGE_PNG_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> getImage(@RequestParam String q) {
        Image image = (Image)tokenService.getObjectFromToken(q);
        if (image == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Long id;
        if (image instanceof HibernateProxy) {
            HibernateProxy proxy = (HibernateProxy) image;
            LazyInitializer init = proxy.getHibernateLazyInitializer();
            id  = (long)init.getIdentifier();
        } else {
            id = image.getId();
        }
        image = imageRepo.findOne(id);
        if (image == null) return null;
        return ResponseEntity.ok(image.getData());
    }

    /**
     * 上传图片
     *
     * @return
     * {
     *     "token": token-string
     * }
     */
    @RequestMapping(method = POST)
    public Api.Result uploadImage(@RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty()) return null;

        byte[] bytes = file.getBytes();
        bytes = convertToPNGImageData(bytes);
        String hash = Cryptor.md5(bytes);
        Image image = imageRepo.findOneByHash(hash);
        if (image == null) {
            image = new Image();
            image.setData(bytes);
            image.setHash(hash);
            image.setTags("用户上传");
            image = imageRepo.save(image);
        }

        return Api.result().param("token", tokenService.generateToken(image));
    }

    private byte[] convertToPNGImageData(byte[] bytes) {
        // TODO convert to png image data
        return bytes;
    }

}
