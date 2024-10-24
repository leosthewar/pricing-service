package com.capitole.pricingservice.common.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
/**
 * Annotation that marks a class as a Spring component which is a web REST adapter.
 *
 * @author Leonardo Rincon - leo.sthewar.rincon@gmail.com
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RestAdapter {

    /*
     * The value may indicate a suggestion for a logical component name,
     * @return the suggested component name, if any (or empty String otherwise)
     */
    @AliasFor(annotation = Component.class)
    String value() default "";

}

