package ru.terraobjects.entity.annotations;

import java.lang.annotation.*;

/**
 * @author terranz
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(
        {
                ElementType.METHOD
        })
public @interface PropSetter {
    String name() default "";

    int id();

    int type();
}
