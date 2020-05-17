package com.example.online_program.es_6_5_4;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wtt
 * @Date: 20-5-16
 * @Description:
 */
public class IndexApi {
    @Test
    public void indexSynReqest() throws IOException {
        //todo 获取client
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        IndexRequest request = new IndexRequest(
                "posts",
                "doc",
                "1");
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        request.source(jsonString, XContentType.JSON);

        //todo 等待主分片变得可用的时间
        //request.timeout(TimeValue.timeValueSeconds(3));
        //todo Refresh policy as a WriteRequest.RefreshPolicy instance
        //request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        //request.opType("create"); //can be create or update (default)
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);

        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        String id = indexResponse.getId();
        long version = indexResponse.getVersion();
        System.out.println(index+"\t"+type+"\t"+id+"\t"+version);
        if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
            System.out.println("----处理（如果需要）首次创建文档的情况----");
        } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
            System.out.println("-----处理（如果需要）已存在的文档被重写的情况----");
        }
        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            System.out.println("----处理成功分片数量少于总分片数量的情况----");
        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure :
                    shardInfo.getFailures()) {
                String reason = failure.reason();
                System.out.println("reason"+reason);
            }
        }
        client.close();
    }
    @Test
    public void AsyIndexRequest() throws IOException, InterruptedException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("182.92.107.205", 9200, "http")));
        //todo 提供source的第二种方式
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out asyn request Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("posts", "doc", "2")
                .source(jsonMap);
        client.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                System.out.println(indexResponse.getIndex()+"\t"+indexResponse.getType()+"\t"+indexResponse.getId());
                System.out.println(indexResponse.getResult());
                System.out.println(indexResponse.getShardInfo());
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("=========failed========");
            }});
        client.close();

    }
}
