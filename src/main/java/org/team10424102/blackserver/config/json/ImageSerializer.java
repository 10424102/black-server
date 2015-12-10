package org.team10424102.blackserver.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.team10424102.blackserver.models.Image;
import org.team10424102.blackserver.services.ImageService;

import java.io.IOException;

@Component
public class ImageSerializer extends JsonSerializer<Image> {
    private static final Logger logger = LoggerFactory.getLogger(ImageSerializer.class);

    @Autowired ImageService imageService;

    @Override
    public void serialize(Image image, JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        String token = imageService.generateAccessToken(image);
        jg.writeString(token);
    }
}
