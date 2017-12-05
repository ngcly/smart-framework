package org.cly.annotation;

import java.lang.annotation.*;

/**
 * 切面注解
 *
 * @author chen
 * @date 2017-12-05 11:29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 注解
     */
    Class<? extends Annotation> value();
}
