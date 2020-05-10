package com.example.online_program;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.example.online_program.dao")//使用MapperScan批量扫描所有的Mapper/Repository接口
@SpringBootApplication
@EnableTransactionManagement
public class OnlineProgramApplication extends SpringBootServletInitializer {


    protected SpringApplicationBuilder builder() {
        return builder().sources(OnlineProgramApplication.class);
    }

    public static void main(String[] args) {
        //解决netty冲突
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(OnlineProgramApplication.class, args);
    }
}


