package com.purplecloud.annotation;

import java.lang.annotation.*;

/**
 * @author 15517
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GetMapping {


    String value();

}
