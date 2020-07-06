package com.example.online_program.es_6_5_4;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author: wtt
 * @Date: 20-5-16
 * @Description:
 */
public class InitialCli {
    @Test
    public  void  getCli() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));
        client.close();
    }
}
