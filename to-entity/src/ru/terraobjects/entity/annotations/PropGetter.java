package ru.terraobjects.entity.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author terranz
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(
{
    ElementType.METHOD
})
public @interface PropGetter
{
    String name() default "";
    int id();
    boolean autoincrement() default false;
    int startNum() default 0;    
    int type();
}
