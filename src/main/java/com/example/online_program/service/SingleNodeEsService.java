package com.example.online_program.service;

import com.example.online_program.constants.OnlineIde;
import com.example.online_program.utils.Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wtt
 * @Date: 19-4-5
 * @Description:
 */
public class SingleNodeEsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleNodeEsService.class);

    public static TransportClient client;
    static {
        client = getEsCli();
    }
    public static TransportClient getEsCli() {
        TransportClient client = null;
        try {
            while (true) {
                client = new PreBuiltTransportClient(Settings.EMPTY)
                        .addTransportAddress(new TransportAddress(InetAddress
                                .getByName(OnlineIde.ES_IP), OnlineIde.ES_TCP_PORT));
                if (client!=null){
                    break;
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

    public static void closeEsCli(TransportClient client) {
        if (client != null) {
            client.close();
        }
    }

    /**
     * @param codeText
     * @return
     * 保存数据到Es中
     */
    public static boolean saveDataToEs(String codeText) {
        try {
            if (isExists(codeText)){
                /*//todo 存在则更新
                for (SearchHit hit: hits){
                    UpdateResponse response = client.prepareUpdate(hit.getIndex(),hit.getType(),hit.getId()).setDoc(
                            XContentFactory.jsonBuilder()
                            .startObject().field("codeText",codeText)
                            .endObject()
                    ).execute().actionGet();
                    Integer integer = response.getShardInfo().getSuccessful();
                    LOGGER.debug(integer>0 ? true:false);
                }*/
                return true;
            }else {
                client.prepareIndex(OnlineIde.ES_INDEX, OnlineIde.ES_TABLE)
                        .setSource(jsonBuilder()
                                .startObject()
                                .field("codeId", Utils.getUUIDString())
                                .field("codeText", codeText)
                                .field("encod",DigestUtils.sha1Hex(codeText))
                                .endObject())
                        .get();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            closeEsCli(client);
        }
        return true;
    }

    /**
     * @param keyWord
     * @param page
     * @param num
     * @return
     * 全文搜索高亮关键字片段
     */
    public static Map queryDataFromEs(String keyWord, int page, int num) {
        Map result = new HashMap();
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("codeText", keyWord);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<em>");
        highlightBuilder.postTags("</em>");
        highlightBuilder.field("codeText");
        // 搜索数据
        if (client != null) {
            client=getEsCli();
        }
        SearchResponse response = client.prepareSearch(OnlineIde.ES_INDEX)
                .setQuery(queryBuilder)
                .setFrom(Utils.getLimitStartPoint(page, num))
                .setSize(num)
                .highlighter(highlightBuilder)
                .execute().actionGet();
        //获取查询结果集
        SearchHits searchHits = response.getHits();
        LOGGER.debug("共搜到:" + searchHits.getTotalHits() + "条结果!");
        result.put("count", searchHits.getTotalHits());
        result.put("pages", Utils.countPages(searchHits.getTotalHits(), num));
        //遍历结果
        List list = new ArrayList();
        for (SearchHit hit : searchHits) {
            LOGGER.debug("score : "+hit.getScore());
            Map map = new HashMap();
            String codeId = hit.getSourceAsMap().get("codeId").toString();
            String fullText = hit.getSourceAsMap().get("codeText").toString();
            Text[] text = hit.getHighlightFields().get("codeText").getFragments();
            StringBuilder builder = new StringBuilder();
            for (Text str : text) {
                builder.append(str.string());
                builder.append("...");
                LOGGER.debug(str.string());
            }
            map.put("codeId", codeId);
            map.put("codeText", builder.toString());
            map.put("fullText",fullText);
            list.add(map);
        }
        result.put("list", list);
        return result;
    }

    /**
     * @param codeText
     * @return
     * 存在返回结果集object
     * 不存在则返回null
     */
    public static boolean isExists(String codeText){
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("encod", DigestUtils.sha1Hex(codeText));
        SearchResponse response = client.prepareSearch(OnlineIde.ES_INDEX)
                .setQuery(queryBuilder)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();
        LOGGER.debug("getTotalHits : "+hits.getTotalHits());
        if (hits.getTotalHits()>0){
            return true;
        }
        return false;
    }

    /**
     * @param codeId
     * @return
     * query code content by codeId
     */
    public static String queryCodeContentByCodeId(String codeId){
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("codeId", codeId);
        SearchResponse response = client.prepareSearch(OnlineIde.ES_INDEX)
                .setQuery(queryBuilder)
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();
        LOGGER.debug("getTotalHits : "+hits.getTotalHits());
        if (hits!=null){
            for (SearchHit hit: hits){
                LOGGER.debug(hit.getSourceAsMap().get("codeText").toString());
                return hit.getSourceAsMap().get("codeText").toString();
            }
        }
        return null;
    }

    public static void main(String[] args) {
//        queryDataFromEs("print",1,15);
//        queryCodeContentByCodeId("1112");
//        saveDataToEs("12345678");
        queryCodeContentByCodeId("8be3efe133c742e6a79f67fbdef5a27e");
    }
}
