package com.jjx.esclient.annotation;

import java.lang.annotation.*;

/**
 * program: esdemo
 * description: ES entity 标识ID的注解,在es entity field上添加
 * @author admin
 * create: 2019-01-18 16:092
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface ESID {
}
