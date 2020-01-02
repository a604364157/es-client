package com.jjx.esclient.auto.util;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 获取basepackage列表
 *
 * @author admin
 * @date 2019-09-02 22:12
 **/
public class GetBasePackage {
    /**
     * 缓存的entityPaths
     */
    private static Map<Class, List<String>> entityPathsMap;

    static {
        entityPathsMap = new HashMap<>();
    }

    private Class<? extends Annotation> annotation;

    public GetBasePackage(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    /**
     * 获取repository的路径，如果获取不到就取main启动路径
     *
     * @param annotationMetadata metadata
     * @return stream
     */
    public Stream<String> getBasePackage(AnnotationMetadata annotationMetadata) {
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(annotation.getName());
        AnnotationAttributes attributes = new AnnotationAttributes(annotationAttributes);
        EnableESTools.gainAnnoInfo(attributes);
        //annotationg中的注解
        String[] value = EnableESTools.getValue();
        //annotationg中的注解
        String[] basePackages = EnableESTools.getBasePackages();
        //annotationg中的注解
        String[] entityPaths = EnableESTools.getEntityPath();
        //没配注解参数
        if (value.length == 0 && basePackages.length == 0) {
            String className = annotationMetadata.getClassName();
            return Stream.of(ClassUtils.getPackageName(className));
        }
        //配了注解
        return Stream.of(Arrays.asList(value), Arrays.asList(basePackages), Arrays.asList(entityPaths)).flatMap(Collection::stream);
    }

    /**
     * 获取实体类的路径，如果获取不到就取main启动路径
     *
     * @param annotationMetadata metadata
     * @return stream
     */
    public Stream<String> getEntityPackage(AnnotationMetadata annotationMetadata) {
        //缓存entityPaths
        if (entityPathsMap.containsKey(annotation)) {
            return Stream.of(entityPathsMap.get(annotation)).flatMap(Collection::stream);
        }
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(annotation.getName());
        AnnotationAttributes attributes = new AnnotationAttributes(annotationAttributes);
        //annotationg中的注解
        String[] entityPaths = attributes.getStringArray("entityPath");
        //没配注解参数
        if (entityPaths.length == 0) {
            String className = annotationMetadata.getClassName();
            entityPathsMap.put(annotation, Collections.singletonList(ClassUtils.getPackageName(className)));
            return Stream.of(ClassUtils.getPackageName(className));
        }
        entityPathsMap.put(annotation, Arrays.asList(entityPaths));
        //配了注解
        return Stream.of(Arrays.asList(entityPaths)).flatMap(Collection::stream);
    }

    public static Map<Class, List<String>> getEntityPathsMap() {
        return entityPathsMap;
    }
}
