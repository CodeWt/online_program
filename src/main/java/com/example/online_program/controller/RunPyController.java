package com.example.online_program.controller;

import com.example.online_program.service.impl.RunPyImpl;
import com.example.online_program.utils.result_utils.Result;
import com.example.online_program.utils.result_utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @Author: wtt
 * @Date: 20-5-8
 * @Description:
 */
@RestController
public class RunPyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunPyController.class);

    @Autowired
    private RunPyImpl runPy;

    @PostMapping(value = "/run",consumes = MediaType.TEXT_PLAIN_VALUE)
    public Result runPy(HttpServletRequest request){
        BufferedReader in = null;
        try {
            in = request.getReader();
            char[] buf = new char[1024];
            int len;
            StringBuffer buffer = new StringBuffer();
            while ((len = in.read(buf)) != -1) {
                buffer.append(buf, 0, len);
            }
            String code = buffer.toString();
            LOGGER.debug("run code :\n "+code);
            String data = runPy.runPy(code);
            return ResultGenerator.genSuccessResult(data);
        }catch (IOException e){
            e.printStackTrace();
            return ResultGenerator.genFailResult("run failed");
        }finally {
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
