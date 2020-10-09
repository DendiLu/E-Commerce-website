package com.qingcheng.service.factory;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
public class ESRestHighLevelClient {

    public static RestHighLevelClient getRestHighLevelClient(String hostname, int port){
        HttpHost http = new HttpHost(hostname,port,"http");
        RestClientBuilder builder = RestClient.builder(http);
        return new RestHighLevelClient(builder);
    }
}
