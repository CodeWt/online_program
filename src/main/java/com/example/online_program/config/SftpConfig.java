package com.example.online_program.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(ignoreUnknownFields = false, prefix = "sftp.client")
public class SftpConfig {
    private String host;
    private Integer port;
    private String userName;
    private String passWord;
    private String desDirPath;
}
