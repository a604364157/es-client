package com.jjx.esclient.util;

import com.jjx.esclient.annotation.ESID;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 工具类
 *
 * @author admin
 * @date 2019-01-18 16:23
 **/
@SuppressWarnings("unused")
public class Tools {
    /**
     * 根据对象中的注解获取ID的字段值
     *
     * @param obj obj
     * @return str
     */
    public static String getEsId(Object obj) throws Exception {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            ESID esid = f.getAnnotation(ESID.class);
            if (esid != null) {
                Object value = f.get(obj);
                if (value == null) {
                    return null;
                } else {
                    return value.toString();
                }
            }
        }
        return null;
    }

    /**
     * 获取o中所有的字段有值的map组合
     *
     * @return map
     */
    public static Map<String, Object> getFieldValue(Object o) throws IllegalAccessException {
        Map<String, Object> retMap = new HashMap<>(16);
        Field[] fs = o.getClass().getDeclaredFields();
        for (Field f : fs) {
            f.setAccessible(true);
            if (f.get(o) != null) {
                retMap.put(f.getName(), f.get(o));
            }
        }
        return retMap;
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
     */
    public static <T, S> Class<T> getSuperClassGenericType(Class<S> clazz) {
        return getSuperClassGenericType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ,start from 0.
     */
    @SuppressWarnings("unchecked")
    public static <T, S> Class<T> getSuperClassGenericType(Class<S> clazz, int index) throws IndexOutOfBoundsException {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return (Class<T>) Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return (Class<T>) Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return (Class<T>) Object.class;
        }
        return (Class<T>) params[index];
    }

    public static String arrayToString(String[] strs) {
        if (StringUtils.isEmpty(strs)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Arrays.stream(strs).forEach(str -> sb.append(str).append(" "));
        return sb.toString();
    }

    public static boolean arrayIsNull(Object[] objs) {
        if (objs == null || objs.length == 0) {
            return true;
        }
        boolean flag = false;
        for (Object obj : objs) {
            if (!StringUtils.isEmpty(obj)) {
                flag = true;
                break;
            }
        }
        return !flag;
    }

    public static <T> List<List<T>> splitList(List<T> oriList, boolean isParallel) {
        if (oriList.size() <= Constant.BULK_COUNT) {
            List<List<T>> splitList = new ArrayList<>();
            splitList.add(oriList);
            return splitList;
        }
        int limit = (oriList.size() + Constant.BULK_COUNT - 1) / Constant.BULK_COUNT;
        if (isParallel) {
            return Stream.iterate(0, n -> n + 1).limit(limit).parallel().map(a -> oriList.stream().skip(a * Constant.BULK_COUNT).limit(Constant.BULK_COUNT).parallel().collect(Collectors.toList())).collect(Collectors.toList());
        } else {
            final List<List<T>> splitList = new ArrayList<>();
            Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> splitList.add(oriList.stream().skip(i * Constant.BULK_COUNT).limit(Constant.BULK_COUNT).collect(Collectors.toList())));
            return splitList;
        }
    }
}
