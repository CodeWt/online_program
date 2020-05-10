package com.example.online_program.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.online_program.entity.UserInfo;
import com.example.online_program.service.LogiRegi;
import com.example.online_program.utils.Utils;
import com.example.online_program.utils.result_utils.Result;
import com.example.online_program.utils.result_utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wtt
 * @Date: 20-5-7
 * @Description:
 */
@RestController
public class LogiRegiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeSearchController.class);

    @Autowired
    private LogiRegi regi;

    @PostMapping(value = "/login")
    public Result login(@RequestBody JSONObject object){
        UserInfo userInfo = new UserInfo();
        userInfo.setName(object.getString("name"));
        userInfo.setPass(object.getString("pass"));
        String data = regi.login(userInfo);
        if (data!=null){
            return ResultGenerator.genSuccessResult(data);
        }
        return ResultGenerator.genFailResult("login failed !");

    }

    @PostMapping(value = "/regist")
    public Result regist(@RequestBody JSONObject object){
        UserInfo userInfo = new UserInfo();
        userInfo.setID(Utils.getUUIDString());
        userInfo.setName(object.getString("name"));
        userInfo.setPass(object.getString("pass"));
        if (regi.regist(userInfo)){
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("regist failed !");
    }
}
