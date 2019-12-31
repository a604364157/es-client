package com.jjx.esclient.auto.intfproxy;

import com.jjx.esclient.enums.AggsType;
import com.jjx.esclient.repository.PageList;
import com.jjx.esclient.repository.PageSortHighLight;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @date 2019-09-02 17:29
 **/
@SuppressWarnings("unused")
public interface ESCRepository<T, M> {

    /**
     * 通过Low Level REST Client 查询
     * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.6/java-rest-low-usage-requests.html
     *
     * @param request req
     * @return res
     * @throws Exception ex
     */
    Response request(Request request) throws Exception;


    /**
     * 新增索引
     *
     * @param t t
     * @return b
     * @throws Exception ex
     */
    boolean save(T t) throws Exception;

    /**
     * 新增索引集合
     *
     * @param list t
     * @return res
     * @throws Exception ex
     */
    BulkResponse save(List<T> list) throws Exception;

    /**
     * 按照有值字段更新索引
     *
     * @param t t
     * @return b
     * @throws Exception ex
     */
    boolean update(T t) throws Exception;


    /**
     * 覆盖更新索引
     *
     * @param t t
     * @return b
     * @throws Exception ex
     */
    boolean updateCover(T t) throws Exception;


    /**
     * 删除索引
     *
     * @param t t
     * @return b
     * @throws Exception ex
     */
    boolean delete(T t) throws Exception;

    /**
     * 删除索引
     *
     * @param id id
     * @return b
     * @throws Exception ex
     */
    boolean deleteById(M id) throws Exception;

    /**
     * 根据ID查询
     *
     * @param id id
     * @return t
     * @throws Exception ex
     */
    T getById(M id) throws Exception;

    /**
     * 【最原始】查询
     *
     * @param searchRequest req
     * @return res
     * @throws Exception ex
     */
    SearchResponse search(SearchRequest searchRequest) throws Exception;


    /**
     * 非分页查询
     * 目前暂时传入类类型
     *
     * @param queryBuilder queryBuilder
     * @return t
     * @throws Exception ex
     */
    List<T> search(QueryBuilder queryBuilder) throws Exception;


    /**
     * 查询数量
     *
     * @param queryBuilder queryBuilder
     * @return l
     * @throws Exception ex
     */
    long count(QueryBuilder queryBuilder) throws Exception;


    /**
     * 支持分页、高亮、排序的查询
     *
     * @param queryBuilder      queryBuilder
     * @param pageSortHighLight hl
     * @return t
     * @throws Exception ex
     */
    PageList<T> search(QueryBuilder queryBuilder, PageSortHighLight pageSortHighLight) throws Exception;

    /**
     * 非分页查询，指定最大返回条数
     * 目前暂时传入类类型
     *
     * @param queryBuilder queryBuilder
     * @param limitSize    最大返回条数
     * @return t
     * @throws Exception ex
     */
    List<T> searchMore(QueryBuilder queryBuilder, int limitSize) throws Exception;

    /**
     * 搜索建议
     *
     * @param fieldName  f
     * @param fieldValue f
     * @return s
     * @throws Exception ex
     */
    List<String> completionSuggest(String fieldName, String fieldValue) throws Exception;

    /**
     * 普通聚合查询
     * 以bucket分组以aggstypes的方式metric度量
     *
     * @param metricName   m
     * @param aggsType     a
     * @param queryBuilder q
     * @param bucketName   b
     * @return map
     * @throws Exception ex
     */
    Map<?, ?> aggs(String metricName, AggsType aggsType, QueryBuilder queryBuilder, String bucketName) throws Exception;

}
