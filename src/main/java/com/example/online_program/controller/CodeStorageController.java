package com.example.online_program.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.online_program.entity.CodeInfo;
import com.example.online_program.service.CodeStorage;
import com.example.online_program.service.EsOptService;
import com.example.online_program.utils.Utils;
import com.example.online_program.utils.result_utils.Result;
import com.example.online_program.utils.result_utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * @Author: wtt
 * @Date: 19-3-10
 * @Description: 关于代码数据的存储
 */
@RestController
@RequestMapping("/code")
public class CodeStorageController {
    private final static Logger LOGGER = LoggerFactory.getLogger(CodeStorageController.class);
    
    @Autowired
    private CodeStorage storage;
    /**
     * TODO save code to DB
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/save", consumes = MediaType.TEXT_PLAIN_VALUE)
    public Result saveCodeToDB(HttpServletRequest request) {
        Utils.disRequestData(request);
        String codeId = null;
        String userId = null;
        String code = null;
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.trim().equals("")) {
            String[] arr = queryString.split("&");
            codeId = arr[0].split("=")[1];
            userId = arr[1].split("=")[1];
        }
        BufferedReader in = null;
        try {
            in = request.getReader();
            char[] buf = new char[1024];
            int len = 0;
            StringBuffer buffer = new StringBuffer();
            while ((len = in.read(buf)) != -1) {
                buffer.append(buf, 0, len);
            }
            code = buffer.toString();
            LOGGER.debug("save code \n: "+code);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (codeId != null && code != null
                && !codeId.trim().equals("") && !userId.trim().equals("")) {
            CodeInfo codeInfo = new CodeInfo();
            codeInfo.setUserId(userId);
            codeInfo.setCodeId(codeId);
            codeInfo.setCodeText(code);
            if (!storage.save(codeInfo)){
                return ResultGenerator.genFailResult("save db failed");
            }
            if (!EsOptService.saveDataToEs(code)){
                return ResultGenerator.genFailResult("[save es failed]");
            }
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("[ save failed !]");
    }


    /**
     * TODO show code
     *
     * @param object
     * @return
     */
    @PostMapping(value = "/show")
    public Result showCode(@RequestBody JSONObject object) {
        LOGGER.debug("[---------showCode-------codeId : ]");
        String codeId = null;
        if (object != null) {
            codeId = object.getString("codeId");
            LOGGER.debug("[ ----codeId----: " + codeId);
        }
        if (codeId != null && !codeId.trim().equals("")) {
            String result = storage.show(codeId);
            return ResultGenerator.genSuccessResult(result);
        }
        return ResultGenerator.genFailResult("show code failed");
    }

    /**
     * TODO showHistoryCode
     * @param object
     * @return
     */
    @PostMapping(value = "/history")
    public Result showHistoryCode(@RequestBody JSONObject object){
        String userId = null;
        if (object != null) {
            userId = object.getString("userId");
            LOGGER.debug("[----into showHistoryCode Controller : ]"+userId);
            if (userId!=null&&!userId.trim().equals("")){
                List list= storage.getHistoryCode(userId);
                return ResultGenerator.genSuccessResult(list);
            }
        }
        return ResultGenerator.genFailResult("get history failed ~");
    }

    /**
     * TODO del code
     * @param object
     * @return
     */
    @PostMapping(value = "/delete")
    public Result deleteCode(@RequestBody JSONObject object){
        String codeId = null;
        if (object != null) {
            codeId = object.getString("codeId");
            if (codeId!=null&&!codeId.trim().equals("")){
                if (storage.deleteCode(codeId)){
                    return ResultGenerator.genSuccessResult();
                }
            }
        }
        return ResultGenerator.genFailResult("del code failed");
    }

   /* @PostMapping(value = "/save")
    public Result saveCodeToDB(String nodeId, String code) {
        LOGGER.debug("[---------saveCodeToDB--------]");
        LOGGER.debug("nodeId : " + nodeId + "\n" + "code : " + code);
        if (nodeId != null && !nodeId.trim().equals("")) {
            if (code!=null){
                CodeStorage codeStorage = new CodeStorageImpl();
                CodeInfo codeInfo = new CodeInfo();
                codeInfo.setCodeId(nodeId);
                codeInfo.setCodeText(code);
                codeInfo.setUserId("1111");
                if (codeStorage.save(codeInfo)) {
                    return ResultGenerator.genSuccessResult();
                }
            }else {
                return ResultGenerator.genFailResult("code is null !!");
            }
        }
        return ResultGenerator.genFailResult("save code failed !!");
    }*/
   /*@PostMapping(value = "/save")
    public Result saveCodeToDB(@RequestBody JSONObject object) {
        LOGGER.debug("[---------saveCodeToDB--------]");
        String nodeId = null;
        String code = null;
        if (object!=null){
            nodeId = object.getString("nodeId");
            code = object.getString("code");
            LOGGER.debug("nodeId : " + nodeId + "\n" + "code : " + code);
        }
        if (nodeId != null && !nodeId.trim().equals("")) {
            if (code!=null){
                CodeStorage codeStorage = new CodeStorageImpl();
                CodeInfo codeInfo = new CodeInfo();
                codeInfo.setCodeId(nodeId);
                codeInfo.setCodeText(code);
                codeInfo.setUserId("1111");
                if (codeStorage.save(codeInfo)) {
                    return ResultGenerator.genSuccessResult();
                }
            }else {
                return ResultGenerator.genFailResult("code is null !!");
            }
        }
        return ResultGenerator.genFailResult("save code failed !!");
    }
*/
}
