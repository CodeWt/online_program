package com.example.online_program.constants;

/**
 * @Author: wtt
 * @Date: 19-4-5
 * @Description:
 */
public interface OnlineIde {
    String ES_IP = "localhost";
    String ES_IP_2 = "127.0.0.1";
    /*用　docker-compose 部署整个项目需要改为服务名
    public static final String ES_IP = "elasticsearch";
    public static final String ES_IP_2 = "elasticsearch2";*/
    int ES_TCP_PORT = 9300;
    int ES_HTTP_PORT = 9200;
    String ES_SCHEME = "http";
    String ES_INDEX = "ide_code_index";
    String ES_TABLE = "ide_code_table";

}
