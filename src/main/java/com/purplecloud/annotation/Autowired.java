package com.purplecloud.annotation;

import java.lang.annotation.*;

/**
 * @author 15517
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {


    boolean value() default true;

    String name() default "";
}
