package com.jjx.esclient.util;

import com.jjx.esclient.annotation.ESMapping;
import com.jjx.esclient.annotation.ESMetaData;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * program: esdemo
 * description: 索引信息操作工具类
 *
 * @author admin
 * create: 2019-01-29 14:29
 **/
public class IndexTools {
    /**
     * 获取索引元数据：indexname、indextype
     *
     * @param clazz
     * @return
     */
    public static MetaData getIndexType(Class<?> clazz) {
        String indexname;
        String indextype;
        if (clazz.getAnnotation(ESMetaData.class) != null) {
            indexname = clazz.getAnnotation(ESMetaData.class).indexName();
            indextype = clazz.getAnnotation(ESMetaData.class).indexType();
            if ("".equals(indextype)) {
                indextype = indexname;
            }
            MetaData metaData = new MetaData(indexname, indextype);
            metaData.setPrintLog(clazz.getAnnotation(ESMetaData.class).printLog());
            if (Tools.arrayISNULL(clazz.getAnnotation(ESMetaData.class).searchIndexNames())) {
                metaData.setSearchIndexNames(new String[]{indexname});
            } else {
                metaData.setSearchIndexNames((clazz.getAnnotation(ESMetaData.class).searchIndexNames()));
            }
            return metaData;
        }
        throw new RuntimeException("检测到数据类型未使用ESMetaData注解,无法自动构建索引");
    }

    /**
     * 获取索引元数据：主分片、备份分片数的配置
     *
     * @param clazz
     * @return
     */
    public static MetaData getShardsConfig(Class<?> clazz) {
        int number_of_shards = 0;
        int number_of_replicas = 0;
        if (clazz.getAnnotation(ESMetaData.class) != null) {
            number_of_shards = clazz.getAnnotation(ESMetaData.class).number_of_shards();
            number_of_replicas = clazz.getAnnotation(ESMetaData.class).number_of_replicas();
            MetaData metaData = new MetaData(number_of_shards, number_of_replicas);
            metaData.setPrintLog(clazz.getAnnotation(ESMetaData.class).printLog());
            return metaData;
        }
        return null;
    }

    /**
     * 获取索引元数据：indexname、indextype、主分片、备份分片数的配置
     *
     * @param clazz
     * @return
     */
    public static MetaData getMetaData(Class<?> clazz) {
        String indexname = "";
        String indextype = "";
        int number_of_shards = 0;
        int number_of_replicas = 0;
        if (clazz.getAnnotation(ESMetaData.class) != null) {
            indexname = clazz.getAnnotation(ESMetaData.class).indexName();
            indextype = clazz.getAnnotation(ESMetaData.class).indexType();
            if (indextype == null || indextype.equals("")) {
                indextype = "_doc";
            }
            number_of_shards = clazz.getAnnotation(ESMetaData.class).number_of_shards();
            number_of_replicas = clazz.getAnnotation(ESMetaData.class).number_of_replicas();
            MetaData metaData = new MetaData(indexname, indextype, number_of_shards, number_of_replicas);
            metaData.setPrintLog(clazz.getAnnotation(ESMetaData.class).printLog());
            if (Tools.arrayISNULL(clazz.getAnnotation(ESMetaData.class).searchIndexNames())) {
                metaData.setSearchIndexNames(new String[]{indexname});
            } else {
                metaData.setSearchIndexNames((clazz.getAnnotation(ESMetaData.class).searchIndexNames()));
            }
            return metaData;
        }
        return null;
    }

    /**
     * 获取配置于Field上的mapping信息，如果未配置注解，则给出默认信息
     *
     * @param field
     * @return
     */
    public static MappingData getMappingData(Field field) {
        if (field == null) {
            return null;
        }
        field.setAccessible(true);
        MappingData mappingData = new MappingData();
        mappingData.setFieldName(field.getName());
        if (field.getAnnotation(ESMapping.class) != null) {
            ESMapping esMapping = field.getAnnotation(ESMapping.class);
            mappingData.setDataType(esMapping.datatype().toString().replaceAll("_type", ""));
//            mappingData.setAnalyzedtype(esMapping.analyzedtype().toString());
            mappingData.setAnalyzer(esMapping.analyzer().toString());
            mappingData.setNgram(esMapping.ngram());
            mappingData.setIgnoreAbove(esMapping.ignore_above());
            mappingData.setSearchAnalyzer(esMapping.search_analyzer().toString());
            mappingData.setKeyword(esMapping.keyword());
            mappingData.setSuggest(esMapping.suggest());
            mappingData.setAllow_search(esMapping.allow_search());
            mappingData.setCopy_to(esMapping.copy_to());
            if (!StringUtils.isEmpty(esMapping.null_value())) {
                mappingData.setNull_value(esMapping.null_value());
            }
        } else {
            mappingData.setDataType("text");
//            mappingData.setAnalyzedtype("analyzed");
            mappingData.setAnalyzer("standard");
            mappingData.setNgram(false);
            mappingData.setIgnoreAbove(256);
            mappingData.setSearchAnalyzer("standard");
            mappingData.setKeyword(true);
            mappingData.setSuggest(false);
            mappingData.setAllow_search(true);
            mappingData.setCopy_to("");
        }
        return mappingData;
    }

    /**
     * 批量获取配置于Field上的mapping信息，如果未配置注解，则给出默认信息
     *
     * @param clazz
     * @return
     */
    public static MappingData[] getMappingData(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        MappingData[] mappingDataList = new MappingData[fields.length];
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getName().equals("serialVersionUID")) {
                continue;
            }
            mappingDataList[i] = getMappingData(fields[i]);
        }
        return mappingDataList;
    }

}
