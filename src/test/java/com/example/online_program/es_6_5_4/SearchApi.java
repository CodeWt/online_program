package com.example.online_program.es_6_5_4;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: wtt
 * @Date: 20-5-16
 * @Description:
 */
public class SearchApi {
    @Test
    public void searchTest(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("182.92.107.205", 9200, "http")));
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //todo Set the from option that determines the result index to start searching from. Defaults to 0.
        searchSourceBuilder.from();
        //todo Set the size option that determines the number of search hits to return. Defaults to 10.
        searchSourceBuilder.size();
        //todo 模糊匹配设置
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("codeText", "operation")
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
            System.out.println("status : "+status.getStatus());
            System.out.println("totalShards : "+searchResponse.getTotalShards());
            System.out.println("successfulShards : "+searchResponse.getSuccessfulShards());
            System.out.println("getFailedShards :"+searchResponse.getFailedShards());
            for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
                //todo  failures should be handled here
                System.out.println(failure.getCause());
            }
            SearchHits hits = searchResponse.getHits();
            System.out.println("getTotalHits : "+hits.getTotalHits()+"\t -- getMaxScore : "+hits.getMaxScore());
            for (SearchHit hit : hits) {
                // do something with the SearchHit
                System.out.println("index :"+hit.getIndex());
                System.out.println("type :"+hit.getType());
                System.out.println("id :"+hit.getId());
                System.out.println("score :"+hit.getScore());
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                String codeText = (String) sourceAsMap.get("codeText");
                System.out.println("codeText : \n "+codeText);
                //todo 高亮处理
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField highlight = highlightFields.get("codeText");
                Text[] fragments = highlight.fragments();
                for (Text text:fragments){
                    System.out.println("fragmentString -->"+text.string());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
