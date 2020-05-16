package com.example.online_program.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.online_program.service.ClusterEsService;
import com.example.online_program.service.SeaInsPac;
import com.example.online_program.utils.result_utils.Result;
import com.example.online_program.utils.result_utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: wtt
 * @Date: 19-3-10
 * @Description:
 */
@RestController
@RequestMapping(value = "/search")
public class CodeSearchController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeSearchController.class);
    /**
     * TODO fullTextSearchCode
     * kw 为搜索关键字,
     * page第几页,
     * num每页数据条数
     *
     * @param object
     * @return
     */
    @PostMapping(value = "/fulltext")
    public Result fullTextSearchCode(@RequestBody JSONObject object) {
        String kw = null;
        int page = 1;
        int num = 0;
        if (object != null) {
            if (object.getString("keyword") != null) {
                kw = object.getString("keyword");
            }
            if (object.getString("num") != null) {
                num = Integer.valueOf(object.getString("num"));
            }
            if (object.getString("page") != null) {
                page = Integer.valueOf(object.getString("page"));
            }
            LOGGER.debug("page : " + page + "\tkw : " + kw + "\tnum : " + num);
            Map data = ClusterEsService.queryDataFromEs(kw, page, num);
            return ResultGenerator.genSuccessResult(data);
        }
        return ResultGenerator.genFailResult("search full failed");
    }

    /**
     * @param object
     * @return
     * search code content from ES
     */
    @PostMapping(value = "/show")
    public Result searchCodeContentFromEs(@RequestBody JSONObject object){
        if ((object.getString("codeId")!=null)&&(!object.getString("codeId").trim().equals(""))){
            String data = ClusterEsService.queryCodeContentByCodeId(object.getString("codeId"));
            if (data!=null){
                return ResultGenerator.genSuccessResult(data);
            }
        }
        return ResultGenerator.genFailResult("es show failed by codeId");
    }
    /**
     * TODO getSearchPackageMsg
     *
     * @param q(package name)
     * @return
     */
    @RequestMapping(value = "/spac")
    public List getSearchPackageMsg(String q) {
        LOGGER.debug("[ ---getSearchPackageMsg ---: " + q);
        List result = SeaInsPac.searchPackage(q);
        return result;
    }

    /**
     * TODO install Package of Python On Linux OS
     *
     * @param object(package name,pip version)
     * @return
     */
    @RequestMapping(value = "/ipac")
    public Result installPyPackageOnLinux(@RequestBody JSONObject object) {
        LOGGER.debug("-----installPyPackageOnLinux-----");
        if (object != null) {
            String pName = object.getString("pName");
            String pipV = object.getString("pipV");
            LOGGER.debug("pName : " + pName + " pipV : " + pipV);
            int re = SeaInsPac.installPackage(pName, pipV);
            if (re == 0) {
                return ResultGenerator.genSuccessResult(0);
            }
            if (re == 1) {
                return ResultGenerator.genSuccessResult(1);
            }
        }
        return ResultGenerator.genFailResult("install package failed ..");
    }
}
