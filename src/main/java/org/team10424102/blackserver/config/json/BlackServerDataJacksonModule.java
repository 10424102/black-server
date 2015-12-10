package org.team10424102.blackserver.config.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlackServerDataJacksonModule extends SimpleModule {

    public static final int MAJOR_VERSION = 0;
    public static final int MINOR_VERSION = 0;
    public static final int PATCH_VERSION = 1;

    @Autowired
    public BlackServerDataJacksonModule(ImageDeserializer imageDeserializer, ImageSerializer imageSerializer) {
        super("BlackServerData", new Version(MAJOR_VERSION, MINOR_VERSION, PATCH_VERSION, null, null, null));
        //assert imageDeserializer != null;
        //addDeserializer(Image.class, imageDeserializer);
        //assert imageSerializer != null;
        //addSerializer(Image.class, imageSerializer);
        //addSerializer(HibernateProxy.class, new HibernateProxySerializer());
    }

    @Override
    public void setupModule(SetupContext context) {
        // context.setMixInAnnotations(User.class, UserMixin.class);
        //context.setMixInAnnotations(Profile.class, ProfileMixin.class);
        //context.setMixInAnnotations(Activity.class, ActivityMixin.class);
        //context.setMixInAnnotations(UserGroup.class, GroupMixin.class);
    }

}
