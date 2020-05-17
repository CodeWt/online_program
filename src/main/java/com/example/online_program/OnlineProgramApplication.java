package com.example.online_program;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.util.TimeZone;

@MapperScan("com.example.online_program.dao")//使用MapperScan批量扫描所有的Mapper/Repository接口
@SpringBootApplication
@EnableTransactionManagement
public class OnlineProgramApplication extends SpringBootServletInitializer {


    protected SpringApplicationBuilder builder() {
        return builder().sources(OnlineProgramApplication.class);
    }

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");// 解决netty冲突
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8:00")); //时区设置
        SpringApplication.run(OnlineProgramApplication.class, args);
    }
}


