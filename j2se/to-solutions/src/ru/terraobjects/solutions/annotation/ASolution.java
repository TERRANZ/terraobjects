package ru.terraobjects.solutions.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author korostelev
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(
{
    ElementType.TYPE
})
public @interface ASolution
{
    String name();
    String port();
}
