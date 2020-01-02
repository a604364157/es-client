package com.jjx.esclient.config;

import com.jjx.esclient.util.Constant;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

/**
 * 自动配置注入restHighLevelClient
 *
 * @author admin
 * @date 2019-01-07 14:09
 **/
@Configuration
@ComponentScan("com.jjx.esclient")
public class ElasticSearchConfiguration {

    @Autowired
    private ElasticsearchProperties elasticsearchProperties;

    private RestHighLevelClient restHighLevelClient;

    @Bean(destroyMethod = "close")
    @Scope("singleton")
    public RestHighLevelClient createInstance() {
        String host = elasticsearchProperties.getHost();
        String username = elasticsearchProperties.getUsername();
        String password = elasticsearchProperties.getPassword();
        try {
            if (StringUtils.isEmpty(host)) {
                host = Constant.DEFAULT_ES_HOST;
            }
            String[] hosts = host.split(",");
            HttpHost[] httpHosts = new HttpHost[hosts.length];
            for (int i = 0; i < httpHosts.length; i++) {
                String h = hosts[i];
                httpHosts[i] = new HttpHost(h.split(":")[0], Integer.parseInt(h.split(":")[1]), "http");
            }
            if (!StringUtils.isEmpty(username)) {
                final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
                restHighLevelClient = new RestHighLevelClient(
                        RestClient.builder(httpHosts).setHttpClientConfigCallback(httpClientBuilder -> {
                            httpClientBuilder.disableAuthCaching();
                            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                        })
                );
            } else {
                restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHosts));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return restHighLevelClient;
    }
}
