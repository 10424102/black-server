package org.team10424102.blackserver.config.propertyeditors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.team10424102.blackserver.models.User;

public class StringToUserConverter implements Converter<String, User> {
    private static final Logger logger = LoggerFactory.getLogger(StringToUserConverter.class);

    @Override
    public User convert(String source) {
        logger.debug(source);
        return new User();
    }
}
