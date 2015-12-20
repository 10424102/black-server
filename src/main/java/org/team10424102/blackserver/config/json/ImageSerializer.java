package org.team10424102.blackserver.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.team10424102.blackserver.models.Image;
import org.team10424102.blackserver.models.ImageRepo;
import org.team10424102.blackserver.models.Page;
import org.team10424102.blackserver.services.TokenService;

import java.io.IOException;

@Component
public class ImageSerializer extends JsonSerializer<Image> {

    @Autowired TokenService tokenService;
    @Autowired ImageRepo imageRepo;

    @Override
    public void serialize(Image image, JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
//        long id = 0;
//        if (image instanceof HibernateProxy) {
//            HibernateProxy proxy = (HibernateProxy) image;
//            LazyInitializer init = proxy.getHibernateLazyInitializer();
//            if (init.isUninitialized()) {
//                id  = (long)init.getIdentifier();
//            }
//        } else {
//            id = image.getId();
//        }
//        image = imageRepo.findOne(id);
//        String token = tokenService.generateToken(image); // no session
//        jg.writeString(token + "~" + image.getHash());
        jg.writeString("fuck!!!");
    }
}
