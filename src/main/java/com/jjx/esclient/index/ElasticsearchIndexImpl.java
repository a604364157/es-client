package com.jjx.esclient.index;

import com.jjx.esclient.util.IndexTools;
import com.jjx.esclient.util.MappingData;
import com.jjx.esclient.util.MetaData;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 索引结构基础方法实现类
 *
 * @author admin
 * @date 2019-01-29 10:05
 **/
@Component
public class ElasticsearchIndexImpl<T> implements ElasticsearchIndex<T> {
    @Autowired
    private RestHighLevelClient client;

    @Override
    public void createIndex(Class<T> clazz) {
        MetaData metaData = IndexTools.getMetaData(clazz);
        CreateIndexRequest request = new CreateIndexRequest(metaData.getIndexname());
        StringBuilder source = new StringBuilder();
        source.append("  {\n" + "    \"").append(metaData.getIndextype()).append("\": {\n").append("      \"properties\": {\n");
        MappingData[] mappingDataList = IndexTools.getMappingData(clazz);
        boolean isNgram = false;
        for (int i = 0; i < mappingDataList.length; i++) {
            MappingData mappingData = mappingDataList[i];
            if (mappingData == null || mappingData.getField_name() == null) {
                continue;
            }
            source.append(" \"").append(mappingData.getField_name()).append("\": {\n");
            source.append(" \"type\": \"").append(mappingData.getDatatype()).append("\"\n");
            if (!StringUtils.isEmpty(mappingData.getCopy_to())) {
                source.append(" ,\"copy_to\": \"").append(mappingData.getCopy_to()).append("\"\n");
            }
            if (!StringUtils.isEmpty(mappingData.getNull_value())) {
                source.append(" ,\"null_value\": \"").append(mappingData.getNull_value()).append("\"\n");
            }
            if (!mappingData.isAllow_search()) {
                source.append(" ,\"index\": false\n");
            }
            if (mappingData.isNgram() && ("text".equals(mappingData.getDatatype()) || "keyword".equals(mappingData.getDatatype()))) {
                source.append(" ,\"analyzer\": \"autocomplete\"\n");
                source.append(" ,\"search_analyzer\": \"standard\"\n");
                isNgram = true;
            } else if ("text".equals(mappingData.getDatatype())) {
                source.append(" ,\"analyzer\": \"").append(mappingData.getAnalyzer()).append("\"\n");
                source.append(" ,\"search_analyzer\": \"").append(mappingData.getSearch_analyzer()).append("\"\n");
            }
            if (mappingData.isKeyword() && !"keyword".equals(mappingData.getDatatype()) && mappingData.isSuggest()) {
                source.append(" \n");
                source.append(" ,\"fields\": {\n");
                source.append(" \"keyword\": {\n");
                source.append(" \"type\": \"keyword\",\n");
                source.append(" \"ignore_above\": ").append(mappingData.getIgnore_above());
                source.append(" },\n");
                source.append(" \"suggest\": {\n");
                source.append(" \"type\": \"completion\",\n");
                source.append(" \"analyzer\": \"").append(mappingData.getAnalyzer()).append("\"\n");
                source.append(" }\n");
                source.append(" }\n");
            } else if (mappingData.isKeyword() && !"keyword".equals(mappingData.getDatatype()) && !mappingData.isSuggest()) {
                source.append(" \n");
                source.append(" ,\"fields\": {\n");
                source.append(" \"keyword\": {\n");
                source.append(" \"type\": \"keyword\",\n");
                source.append(" \"ignore_above\": ").append(mappingData.getIgnore_above());
                source.append(" }\n");
                source.append(" }\n");
            } else if (!mappingData.isKeyword() && mappingData.isSuggest()) {
                source.append(" \n");
                source.append(" ,\"fields\": {\n");
                source.append(" \"suggest\": {\n");
                source.append(" \"type\": \"completion\",\n");
                source.append(" \"analyzer\": \"").append(mappingData.getAnalyzer()).append("\"\n");
                source.append(" }\n");
                source.append(" }\n");
            }
            if (i == mappingDataList.length - 1) {
                source.append(" }\n");
            } else {
                source.append(" },\n");
            }
        }
        source.append(" }\n");
        source.append(" }\n");
        source.append(" }\n");
        if (isNgram) {
            request.settings(Settings.builder()
                    .put("index.number_of_shards", metaData.getNumber_of_shards())
                    .put("index.number_of_replicas", metaData.getNumber_of_replicas())
                    .put("analysis.filter.autocomplete_filter.type", "edge_ngram")
                    .put("analysis.filter.autocomplete_filter.min_gram", 1)
                    .put("analysis.filter.autocomplete_filter.max_gram", 20)
                    .put("analysis.analyzer.autocomplete.type", "custom")
                    .put("analysis.analyzer.autocomplete.tokenizer", "standard")
                    .putList("analysis.analyzer.autocomplete.filter", "lowercase", "autocomplete_filter")
            );
        } else {
            request.settings(Settings.builder()
                    .put("index.number_of_shards", metaData.getNumber_of_shards())
                    .put("index.number_of_replicas", metaData.getNumber_of_replicas())
            );
        }
        //类型定义
        request.mapping(metaData.getIndextype(), source.toString(), XContentType.JSON);
        try {
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            //返回的CreateIndexResponse允许检索有关执行的操作的信息，如下所示：
            //指示是否所有节点都已确认请求
            boolean acknowledged = createIndexResponse.isAcknowledged();
            System.out.println(acknowledged);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dropIndex(Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexname();
        DeleteIndexRequest request = new DeleteIndexRequest(indexname);
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    @Override
    public boolean exists(Class<T> clazz) throws Exception {
        MetaData metaData = IndexTools.getIndexType(clazz);
        String indexname = metaData.getIndexname();
        GetIndexRequest request = new GetIndexRequest(indexname);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }
}
