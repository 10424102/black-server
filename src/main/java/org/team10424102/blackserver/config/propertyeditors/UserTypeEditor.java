package org.team10424102.blackserver.config.propertyeditors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;

public class UserTypeEditor extends PropertyEditorSupport {
    private static final Logger logger = LoggerFactory.getLogger(UserTypeEditor.class);

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        logger.debug(text);
    }
}
