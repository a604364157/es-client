package com.jjx.esclient.index;

/**
 * 索引结构基础方法接口
 *
 * @author admin
 * @date 2019-01-25 16:52
 **/
public interface ElasticsearchIndex<T> {
    /**
     * 创建索引
     *
     * @param clazz clazz
     * @throws Exception ex
     */
    void createIndex(Class<T> clazz) throws Exception;

    /**
     * 删除索引
     *
     * @param clazz clazz
     * @throws Exception ex
     */
    void dropIndex(Class<T> clazz) throws Exception;

    /**
     * 索引是否存在
     *
     * @param clazz clazz
     * @throws Exception ex
     */
    boolean exists(Class<T> clazz) throws Exception;

}
