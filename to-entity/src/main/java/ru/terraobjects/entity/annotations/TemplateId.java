package ru.terraobjects.entity.annotations;

import java.lang.annotation.*;

/**
 * @author terranz
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TemplateId {
    String name() default "";

    int id();
}
