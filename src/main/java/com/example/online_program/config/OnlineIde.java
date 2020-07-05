package com.example.online_program.config;

/**
 * @Author: wtt
 * @Date: 19-4-5
 * @Description:
 */
public interface OnlineIde {
    String ES_IP = "182.92.107.205";
    String ES_IP_2 = "127.0.0.1";

    int ES_TCP_PORT = 9300;//TCP port
    int ES_HTTP_PORT = 9200;
    int ES_HTTP_PORT_1 = 9201;
    String ES_SCHEME = "http";
    String ES_INDEX = "ide_code_index";
    String ES_TABLE = "ide_code_table";
    /**
     *   用　docker-compose 部署整个项目的IP需要改为服务名
     *   String ES_IP = "elasticsearch";
     *   String ES_IP_2 = "elasticsearch2";
     */
}
