package org.projw.blackserver.notifications;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HandleNotificationType {
    int[] value();
}
