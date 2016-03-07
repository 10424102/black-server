package org.projw.blackserver.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.projw.blackserver.models.Image;
import org.projw.blackserver.services.TokenService;

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
