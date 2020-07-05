package com.example.online_program;

import com.example.online_program.config.EsClusterConfig;
import com.example.online_program.service.ClusterEsService;
import com.example.online_program.service.SftpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: wtt
 * @Date: 20-5-8
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OnlineProgramApplication.class)
public class ConfigTest {

    @Autowired
    private EsClusterConfig config;

    @Test
    public void test01(){
        System.out.println(config.getHost_1());
        System.out.println(config.getPort_1());
        System.out.println(config.getScheme_1());
        System.out.println(config.getIndex());
        System.out.println(config.getTable());
    }

    @Autowired
    private ClusterEsService service;
    @Test
    public void test02(){
        System.out.println(service.saveDataToEsCluster("print(\"hello world\")"));
    }

    @Autowired
    private SftpService utils;
    @Test
    public void test03() throws Exception {
        String dstFilePath = "D:\\package\\hi.py";
        utils.uploadFile(dstFilePath,"hi.py");
        utils.exeShell("python3 hi.py");
    }

}
