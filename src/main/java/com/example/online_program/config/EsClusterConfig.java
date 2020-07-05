package com.example.online_program.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Data getter,setter,toString
 */
@Data
@Component
public class EsClusterConfig {

    @Value("${elasticsearch.node1.host}")
    private String host_1;
    @Value("${elasticsearch.node1.port}")
    private Integer port_1;
    @Value("${elasticsearch.node1.scheme}")
    private String scheme_1;

    @Value("${elasticsearch.node2.host}")
    private String host_2;
    @Value("${elasticsearch.node2.port}")
    private Integer port_2;
    @Value("${elasticsearch.node2.scheme}")
    private String scheme_2;

    @Value("${elasticsearch.index}")
    private String index;
    @Value("${elasticsearch.table}")
    private String table;

}
