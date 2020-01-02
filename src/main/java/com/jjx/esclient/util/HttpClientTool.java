package com.jjx.esclient.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author admin
 **/
public class HttpClientTool {
    private static HttpClient mHttpClient = null;

    private static CloseableHttpClient getHttpClient(HttpClientBuilder httpClientBuilder) {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        ConnectionSocketFactory factory = new PlainConnectionSocketFactory();
        registryBuilder.register("http", factory);
        //指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //信任任何链接
            TrustStrategy anyTrustStrategy = (x509Certificates, s) -> true;
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException | KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        //设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        //构建客户端
        return httpClientBuilder.setConnectionManager(connManager).build();
    }

    private synchronized static HttpClient getESHttpClient() {
        if (mHttpClient == null) {
            mHttpClient = getHttpClient(HttpClientBuilder.create());
        }
        return mHttpClient;
    }

    private synchronized static HttpClient getESHttpClient(String username, String password) {
        if (mHttpClient == null) {
            mHttpClient = getHttpClientWithBasicAuth(username, password);
        }
        return mHttpClient;
    }

    private static HttpClientBuilder credential(String username, String password) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CredentialsProvider provider = new BasicCredentialsProvider();
        AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        provider.setCredentials(scope, credentials);
        httpClientBuilder.setDefaultCredentialsProvider(provider);
        return httpClientBuilder;
    }

    /**
     * 获取支持basic Auth认证的HttpClient
     *
     * @param username
     * @param password
     * @return
     */
    private static CloseableHttpClient getHttpClientWithBasicAuth(String username, String password) {
        return getHttpClient(credential(username, password));
    }

    /**
     * 设置头信息，e.g. content-type 等
     *
     * @param req     req
     * @param headers headers
     */
    private static void setHeaders(HttpRequestBase req, Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        for (Map.Entry<String, String> header : headers.entrySet()) {
            req.setHeader(header.getKey(), header.getValue());
        }
    }

    /**
     * 执行http请求
     *
     * @param url
     * @param obj
     * @return
     * @throws Exception
     */
    public static String execute(String url, String obj) throws Exception {
        HttpClient httpClient;
        HttpResponse response;
        httpClient = HttpClientTool.getESHttpClient();
        HttpUriRequest request = postMethod(url, obj);
        response = httpClient.execute(request);
        HttpEntity entity1 = response.getEntity();
        return EntityUtils.toString(entity1, "utf-8").trim();
    }

    /**
     * 执行http请求
     *
     * @param url
     * @param obj
     * @return
     * @throws Exception
     */
    public static String execute(String url, String obj, String username, String password) throws Exception {
        HttpClient httpClient;
        HttpResponse response;
        httpClient = HttpClientTool.getESHttpClient(username, password);
        HttpUriRequest request = postMethod(url, obj);
        response = httpClient.execute(request);
        HttpEntity entity1 = response.getEntity();
        return EntityUtils.toString(entity1, "utf-8").trim();
    }

    private static HttpUriRequest postMethod(String url, String data) {
        HttpPost httpPost = new HttpPost(url);
        if (data != null) {
            httpPost.setEntity(new StringEntity(data, "UTF-8"));
        }
        httpPost.addHeader("Content-Type", "application/json");
        return httpPost;
    }

}
