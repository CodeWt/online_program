package com.example.online_program.es_6_5_4;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: wtt
 * @Date: 20-5-16
 * @Description:
 */
public class GetApi {
    @Test
    public void getRequest() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        GetRequest getRequest = new GetRequest(
                "posts",
                "doc",
                "1");
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        String index = getResponse.getIndex();
        String type = getResponse.getType();
        String id = getResponse.getId();
        System.out.println(index+"\t"+type+"\t"+id);
        if (getResponse.isExists()) {
            long version = getResponse.getVersion();
            System.out.println("version : "+version);
            String sourceAsString = getResponse.getSourceAsString();
            System.out.println(sourceAsString);
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
            System.out.println("codeText : \n"+sourceAsMap.get("message"));
//            byte[] sourceAsBytes = getResponse.getSourceAsBytes();
        } else {
            System.out.println("not found ...");
        }
        client.close();
    }
}
