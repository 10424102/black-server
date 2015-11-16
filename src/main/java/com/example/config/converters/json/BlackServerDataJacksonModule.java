package com.example.config.converters.json;

import com.example.models.Activity;
import com.example.models.Profile;
import com.example.models.User;
import com.example.models.UserGroup;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class BlackServerDataJacksonModule extends SimpleModule {

    public static final int MAJOR_VERSION = 0;
    public static final int MINOR_VERSION = 0;
    public static final int PATCH_VERSION = 1;

    public BlackServerDataJacksonModule() {
        super("BlackServerData", new Version(MAJOR_VERSION, MINOR_VERSION, PATCH_VERSION, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(User.class, UserMixin.class);
        context.setMixInAnnotations(Profile.class, ProfileMixin.class);
        context.setMixInAnnotations(Activity.class, ActivityMixin.class);
        context.setMixInAnnotations(UserGroup.class, GroupMixin.class);
    }

}
