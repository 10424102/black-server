package org.team10424102.blackserver.config;

import org.hibernate.cfg.ImprovedNamingStrategy;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomNamingStrategy extends ImprovedNamingStrategy {
    public static final String SPLIT_REGEX = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";

    /**
     * 将驼峰法命名的类名 MyEntityClass，改成 T_MY_ENTITY_CLASS 这样的表名
     */
    @Override
    public String classToTableName(String className) {
        String[] words = className.split(SPLIT_REGEX);
        return "t_" + Arrays.stream(words).map(String::toLowerCase).collect(Collectors.joining("_"));
    }
}
