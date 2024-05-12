package com.purplecloud.annotation;

import java.lang.annotation.*;

/**
 * @author 15517
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {


    String value() default "";

}
