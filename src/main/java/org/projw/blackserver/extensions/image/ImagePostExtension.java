package org.projw.blackserver.extensions.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.projw.blackserver.models.ImageRepo;
import org.projw.blackserver.models.Image;
import org.projw.blackserver.extensions.PostExtension;
import org.projw.blackserver.services.TokenService;

import java.util.ArrayList;
import java.util.List;

@Component("image")
public class ImagePostExtension implements PostExtension {

    @Autowired ImageRepo imageRepo;

    @Autowired TokenService tokenService;

    @Override
    public Object getData(String stub) {
        String[] ids = stub.split(",");
        List<String> imageTokens = new ArrayList<>();
        for (String id : ids) {
            Long imageId = Long.parseLong(id);
            Image image = imageRepo.findOne(imageId);
            imageTokens.add(tokenService.generateToken(image));
        }
        return imageTokens;
    }
}
