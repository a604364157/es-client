package com.jjx.esclient.auto.util;

import org.springframework.core.annotation.AnnotationAttributes;

/**
 * @author admin
 * @date 2019-09-05 10:37
 **/
public class EnableESTools {
    private static String[] basePackages;
    private static String[] value;
    private static String[] entityPath;
    private static boolean printRegMsg = false;

    public static void gainAnnoInfo(AnnotationAttributes attributes) {
        basePackages = attributes.getStringArray("basePackages");
        value = attributes.getStringArray("value");
        entityPath = attributes.getStringArray("entityPath");
        printRegMsg = attributes.getBoolean("printRegMsg");
    }

    public static String[] getBasePackages() {
        return basePackages;
    }

    public static String[] getValue() {
        return value;
    }

    public static String[] getEntityPath() {
        return entityPath;
    }

    public static boolean isPrintRegMsg() {
        return printRegMsg;
    }
}
