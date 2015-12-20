package org.team10424102.blackserver.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.team10424102.blackserver.models.Image;
import org.team10424102.blackserver.services.TokenService;

import java.io.IOException;

@Component
public class ImageDeserializer extends JsonDeserializer<Image> {

    @Autowired TokenService tokenService;

    @Override
    public Image deserialize(JsonParser jp, DeserializationContext context) throws IOException, JsonProcessingException {
        String token = jp.getText();
        return (Image)tokenService.getObjectFromToken(token);
    }
}
