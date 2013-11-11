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
public @interface PropGetter {
    String name() default "";

    int id();

    boolean autoincrement() default false;

    int startNum() default 0;

    int type();
}
