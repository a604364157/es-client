package com.jjx.esclient.util;

/**
 * 元数据载体类
 *
 * @author jiangjx
 */
public class MetaData {
    public MetaData(String indexName, String indexType) {
        this.indexName = indexName;
        this.indexType = indexType;
    }

    String indexName = "";
    String indexType = "";

    String[] searchIndexNames;

    public String[] getSearchIndexNames() {
        return searchIndexNames;
    }

    public void setSearchIndexNames(String[] searchIndexNames) {
        this.searchIndexNames = searchIndexNames;
    }

    boolean printLog = false;

    public boolean isPrintLog() {
        return printLog;
    }

    public void setPrintLog(boolean printLog) {
        this.printLog = printLog;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    int numberOfShards;
    int numberOfReplicas;

    public int getNumberOfShards() {
        return numberOfShards;
    }

    public void setNumberOfShards(int numberOfShards) {
        this.numberOfShards = numberOfShards;
    }

    public int getNumberOfReplicas() {
        return numberOfReplicas;
    }

    public void setNumberOfReplicas(int numberOfReplicas) {
        this.numberOfReplicas = numberOfReplicas;
    }

    public MetaData(String indexName, String indexType, int numberOfShards, int numberOfReplicas) {
        this.indexName = indexName;
        this.indexType = indexType;
        this.numberOfShards = numberOfShards;
        this.numberOfReplicas = numberOfReplicas;
    }

    public MetaData(int numberOfShards, int numberOfReplicas) {
        this.numberOfShards = numberOfShards;
        this.numberOfReplicas = numberOfReplicas;
    }
}