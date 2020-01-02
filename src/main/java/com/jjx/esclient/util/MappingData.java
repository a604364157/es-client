package com.jjx.esclient.util;

/**
 * mapping注解对应的数据载体类
 *
 * @author admin
 * @date 2019-01-29 15:09
 **/
@SuppressWarnings("unused")
public class MappingData {

    private String fieldName;
    /**
     * 数据类型（包含 关键字类型）
     */
    private String dataType;
    /**
     * 间接关键字
     */
    private boolean keyword;

    /**
     * 关键字忽略字数
     */
    private int ignoreAbove;
    /**
     * 是否支持ngram，高效全文搜索提示
     */
    private boolean ngram;
    /**
     * 是否支持suggest，高效前缀搜索提示
     */
    private boolean suggest;
    /**
     * 索引分词器设置
     */
    private String analyzer;
    /**
     * 搜索内容分词器设置
     */
    private String searchAnalyzer;

    private boolean allowSearch;

    private String copyTo;

    private String nullValue;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isKeyword() {
        return keyword;
    }

    public void setKeyword(boolean keyword) {
        this.keyword = keyword;
    }

    public int getIgnoreAbove() {
        return ignoreAbove;
    }

    public void setIgnoreAbove(int ignoreAbove) {
        this.ignoreAbove = ignoreAbove;
    }

    public boolean isNgram() {
        return ngram;
    }

    public void setNgram(boolean ngram) {
        this.ngram = ngram;
    }

    public boolean isSuggest() {
        return suggest;
    }

    public void setSuggest(boolean suggest) {
        this.suggest = suggest;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }

    public String getSearchAnalyzer() {
        return searchAnalyzer;
    }

    public void setSearchAnalyzer(String searchAnalyzer) {
        this.searchAnalyzer = searchAnalyzer;
    }

    public boolean isAllowSearch() {
        return allowSearch;
    }

    public void setAllowSearch(boolean allowSearch) {
        this.allowSearch = allowSearch;
    }

    public String getCopyTo() {
        return copyTo;
    }

    public void setCopyTo(String copyTo) {
        this.copyTo = copyTo;
    }

    public String getNullValue() {
        return nullValue;
    }

    public void setNullValue(String nullValue) {
        this.nullValue = nullValue;
    }
}
