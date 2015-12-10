package org.team10424102.blackserver.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.team10424102.blackserver.models.Image;
import org.team10424102.blackserver.services.ImageService;

import javax.persistence.EntityManager;
import java.io.IOException;

@Component
public class ImageDeserializer extends JsonDeserializer<Image> {
    private static final Logger logger = LoggerFactory.getLogger(ImageDeserializer.class);

    @Autowired ImageService imageService;

    @Autowired EntityManager em;

    @Override
    public Image deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
        String token = jp.getText();
        logger.trace("获得图片的访问 token = " + token);
        Long id = imageService.getImageIdFromAccessToken(token);
        if (id == null) return null;
        Session session = em.unwrap(Session.class);
        return (Image) session.load(Image.class, id);
    }
}
