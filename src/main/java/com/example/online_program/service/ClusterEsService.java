package com.example.online_program.service;

import com.example.online_program.constants.OnlineIde;
import com.example.online_program.utils.Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wtt
 * @Date: 20-5-16
 * @Description:
 */
public class ClusterEsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterEsService.class);
    private static RestHighLevelClient client;
    static {
        client = getClient();
    }
    public static RestHighLevelClient getClient(){
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(OnlineIde.ES_IP, OnlineIde.ES_HTTP_PORT, OnlineIde.ES_SCHEME)));
    }

    public static boolean saveDataToEsCluster(String codeText){
        String sha1 = DigestUtils.sha1Hex(codeText);
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("codeId", sha1);
        jsonMap.put("codeText", codeText);
        IndexRequest request = new IndexRequest(OnlineIde.ES_INDEX, OnlineIde.ES_TABLE, sha1).source(jsonMap);
        IndexResponse indexResponse = null;
        try {
            indexResponse = client.index(request, RequestOptions.DEFAULT);
            String index = indexResponse.getIndex();
            String type = indexResponse.getType();
            String id = indexResponse.getId();
            long version = indexResponse.getVersion();
            LOGGER.debug(index+"\t"+type+"\t"+id+"\t"+version);
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                LOGGER.debug("----处理（如果需要）首次创建文档的情况----");
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                LOGGER.debug("-----处理（如果需要）已存在的文档被重写的情况----");
            }
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                LOGGER.warn("----处理成功分片数量少于总分片数量的情况----");
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure :
                        shardInfo.getFailures()) {
                    String reason = failure.reason();
                    LOGGER.error("reason : \n "+reason);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeEsCli(client);
            return false;
        }
        return true;
    }

    public static String queryCodeContentByCodeId(String codeId){
        GetRequest request = new GetRequest(
                OnlineIde.ES_INDEX,
                OnlineIde.ES_TABLE,
                codeId);
        GetResponse getResponse = null;
        try {
            getResponse = client.get(request, RequestOptions.DEFAULT);
            LOGGER.debug(getResponse.getIndex()+"\t"+getResponse.getType()+"\t"+getResponse.getId());
            if (getResponse.isExists()) {
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
                return sourceAsMap.get("codeText").toString();
            } else {
                LOGGER.debug("not found code...");
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeEsCli(client);
        }
        return null;
    }
    public static Map queryDataFromEs(String keyWord, int page, int num){
        Map result = new HashMap();
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //todo Set the from option that determines the result index to start searching from. Defaults to 0.
        searchSourceBuilder.from(Utils.getLimitStartPoint(page, num));
        //todo Set the size option that determines the number of search hits to return. Defaults to 10.
        searchSourceBuilder.size();
        //todo 模糊匹配设置
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("codeText", keyWord)
                .fuzziness(Fuzziness.AUTO) //Enable fuzzy matching on the match query
                .prefixLength(3)//Set the prefix length option on the match query
                .maxExpansions(10);//设置最大扩展选项以控制查询的模糊过程
        searchSourceBuilder.query(matchQueryBuilder);
        //todo 高亮突出显示设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightUser = new HighlightBuilder.Field("codeText");
        highlightBuilder.field(highlightUser);
        searchSourceBuilder.highlighter(highlightBuilder);
        highlightBuilder.preTags("<em>");
        highlightBuilder.postTags("</em>");
        //todo Add the SearchSourceBuilder to the SeachRequest.
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            RestStatus status = searchResponse.status();
            LOGGER.debug("status : "+status.getStatus());
            LOGGER.debug("totalShards : "+searchResponse.getTotalShards());
            LOGGER.debug("successfulShards : "+searchResponse.getSuccessfulShards());
            LOGGER.warn("getFailedShards :"+searchResponse.getFailedShards());
            for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
                //todo  failures should be handled here
                LOGGER.error(String.valueOf(failure.getCause()));
            }
            SearchHits hits = searchResponse.getHits();
            LOGGER.debug("getTotalHits : "+hits.getTotalHits()+"\t -- getMaxScore : "+hits.getMaxScore());
            List list = new ArrayList();
            for (SearchHit hit : hits) {
                // do something with the SearchHit
                LOGGER.debug("index:"+hit.getIndex()+"  type :"+hit.getType()+"  id :"+hit.getId());
                LOGGER.debug("score : "+hit.getScore());
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                String codeId = (String) sourceAsMap.get("codeId");
                String codeText = (String) sourceAsMap.get("codeText");
                LOGGER.debug("codeText : \n "+codeText);
                //todo 高亮处理
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField highlight = highlightFields.get("codeText");
                Text[] fragments = highlight.fragments();
                StringBuilder builder = new StringBuilder();
                Map map = new HashMap();
                for (Text text:fragments){
                    LOGGER.debug("fragmentString -->"+text.string());
                    builder.append(text.string());
                    builder.append("...");
                }
                map.put("codeId", codeId);
                map.put("codeText", builder.toString());
                map.put("fullText",codeText);
                list.add(map);
            }
            result.put("count",hits.getTotalHits());
            result.put("pages",Utils.countPages(hits.getTotalHits(),num));
            result.put("list",list);
        } catch (IOException e) {
            e.printStackTrace();
            closeEsCli(client);
        }
        return result;
    }
    public static void closeEsCli(RestHighLevelClient client) {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        saveDataToEsCluster("trying out Elasticsearch");
        saveDataToEsCluster("trying out high rest client save Elasticsearch");
        saveDataToEsCluster("The SearchRequest is used for any operation that has to do with " +
                "searching documents, aggregations, suggestions and also Elasticsearch offers ways of requesting highlighting on the resulting documents.");
        LOGGER.debug("===============");
//        LOGGER.debug(queryCodeContentByCodeId("3861e582e01c20e3cdcfd54a10f0b15272c43ad3"));
    }
}
