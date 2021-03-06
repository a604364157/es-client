package com.jjx.esclient.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * es索引元数据的注解，在es entity class上添加
 *
 * @author admin
 * @date 2019-01-18 16:12
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface ESMetaData {
    /**
     * 检索时的索引名称，如果不配置则默认为和indexName一致，该注解项仅支持搜索
     * 并不建议这么做，建议通过特定方法来做跨索引查询
     */
    String[] searchIndexNames() default {};

    /**
     * 索引名称，必须配置
     */
    String indexName();

    /**
     * 索引类型，可以不配置，不配置默认和indexName相同，墙裂建议每个index下只有一个type
     */
    String indexType() default "";

    /**
     * 主分片数量
     */
    int number_of_shards() default 1;

    /**
     * 备份分片数量
     */
    int number_of_replicas() default 1;

    /**
     * 是否打印日志
     */
    boolean printLog() default false;
}
