package com.example.online_program.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.online_program.entity.ProjInfo;
import com.example.online_program.entity.TreeNodeInfo;
import com.example.online_program.service.CodeStorage;
import com.example.online_program.service.TreeNodeOpt;
import com.example.online_program.utils.Utils;
import com.example.online_program.utils.result_utils.Result;
import com.example.online_program.utils.result_utils.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: wtt
 * @Date: 19-3-10
 * @Description: treenode curd
 */
@RestController
@RequestMapping("/treenode")
public class TreeNodeOptController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TreeNodeOptController.class);

    @Autowired
    private TreeNodeOpt opt;

    @Autowired
    private CodeStorage storage;
    /**
     * TODO createProject
     *
     * @param object
     * @return
     */
    @RequestMapping(value = "/create/proj")
    public Result createProject(@RequestBody JSONObject object) {
        ProjInfo projInfo = new ProjInfo();
        if (object != null && object.containsKey("userId")) {
            if (object.getString("userId")!=null&&!object.getString("userId").trim().equals("")){
                projInfo.setProjectId(Utils.getUUIDString("p"));
                //default project name is "Project"
                projInfo.setProjectName("Project");
                projInfo.setUserId(object.getString("userId"));
            }
        }
        if (opt.createProject(projInfo)) {
            return ResultGenerator.genSuccessResult(projInfo);
        }
        return ResultGenerator.genFailResult("create proj is failed !");

    }

    /**
     * TODO create directory or file
     *
     * @param object
     * @return
     */
    @RequestMapping(value = "/create/dirfile")
    public Result createDirFile(@RequestBody JSONObject object) {
//        System.out.println("[ createDirFile  :  "+object.toJSONString());
        String type = "";
        if (object != null && object.containsKey("type")) {
            type = object.getString("type");
        }
        if (object.containsKey("parentId")) {
            String parentId = object.getString("parentId");
            if (Utils.adjustIsProject(parentId) || storage.selectLabel(parentId)) {
                TreeNodeInfo tni = new TreeNodeInfo();
                tni.setParentId(parentId);
                tni.setChildId(Utils.getUUIDString("n"));
                if (type.trim().equals("dir")) {
                    //初次创建目录,default="dir"
                    tni.setNodeName("dir");
                    tni.setLabel("0");
                } else {
                    //初次创建文件,default="file"
                    tni.setNodeName("file");
                    tni.setLabel("1");
                }
                if (opt.createDireFile(tni)) {
                    return ResultGenerator.genSuccessResult(tni);
                }
            }
        }
        return ResultGenerator.genFailResult("创建位置不正确");
    }

    /**
     * TODO rename node by nodeId
     *
     * @param object
     * @return
     */
    @PostMapping(value = "/rename")
    public Result renameNode(@RequestBody JSONObject object) {
        String nodeId = object.getString("id");
        String nodeName = object.getString("text");
        String type = object.getString("type");
        System.out.println(nodeId + "\t" + nodeName + "\t" + type);
        Map map = new HashMap();
        map.put("nodeId", nodeId);
        map.put("nodeName", nodeName);
        map.put("type", type);
        map.put("updateTime", Utils.getTimeStamp());
        if (opt.renameNode(map)) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("rename node is failed !");
    }

    @PostMapping(value = "/move")
    public Result moveNode(@RequestBody JSONObject object) {
        System.out.println("[----------moveNodeController--------]");
        String sourceId = object.getString("sid");
        String targetId = object.getString("tid");
        String nodeName = object.getString("text");
        System.out.println(sourceId + "\t" + targetId + "\t" + nodeName);
        if (!storage.selectLabel(targetId)) {
            return ResultGenerator.genFailResult("target should be dir!");
        }
        TreeNodeInfo tni = new TreeNodeInfo();
        tni.setParentId(targetId);
        tni.setChildId(sourceId);
        tni.setNodeName(nodeName);
        if (storage.selectLabel(sourceId)) {
            tni.setLabel("0");
        } else {
            tni.setLabel("1");
        }
        if (opt.moveNode(tni)) {
            return ResultGenerator.genSuccessResult();
        }
        return ResultGenerator.genFailResult("move failed");
    }

    /**
     * TODO import my project list by userId
     *
     * @param object
     * @return
     */
    @PostMapping(value = "/import/list")
    public Result ImportProjectsList(@RequestBody JSONObject object) {
        System.out.println("[-------import ImportProjectsList----]");
        String userId = null;
        if (object != null) {
            userId = object.getString("userId");
        }
        if (userId != null && !userId.trim().equals("")) {
            List list = opt.queryProjectList(userId);
            return ResultGenerator.genSuccessResult(list);
        }
        return ResultGenerator.genFailResult("importList is failed !");
    }

    /**
     * TODO delete treecode includes data
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete")
    public Result deleteNode(@RequestBody JSONObject object) {
        if (object != null) {
            String nodeId = object.getString("id");
            if (Utils.adjustIsProject(nodeId)) {
                opt.deleteProject(nodeId);
            }
            List list = new ArrayList();
            list.add(nodeId);
            List list1 = Utils.mergeList(opt.selectAllChildNodeId(list));
            list1.add(nodeId);
            System.out.println("list1 --> : "+list1.size()+"\t"+list1.get(0));
            if (opt.deleteNode(list1)){
                return ResultGenerator.genSuccessResult();
            }
        }
        return ResultGenerator.genFailResult("delete failed");
    }

    /**
     * TODO import project
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/getNode")
    public List getTreeNodes(String id) {
        System.out.println("[--------into getTreeNodes-------nodeID:] \t: " + id);
        List result = new ArrayList();
        if (id != null && !id.trim().equals("")) {
            String[] ids = id.split(",");
            if (ids.length != 2) {
                String projName = opt.queryProjectName(id);
                JSONObject node = new JSONObject();
                node.put("id", id);
                node.put("text", projName);
                node.put("state", "closed");
                result.add(node);
            } else {
                System.out.println("front :" + ids[0] + "\t" + " behind : " + ids[1]);
                List<TreeNodeInfo> list = opt.queryNodeData(ids[1]);
                for (TreeNodeInfo tni : list) {
                    String state = tni.getLabel().equals("0") ? "closed" : "open";
                    JSONObject node = new JSONObject();
                    node.put("id", tni.getChildId());
                    node.put("text", tni.getNodeName());
                    node.put("state", state);
                    result.add(node);
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/adjust/isFile")
    public Result adjustIsTxtFile(@RequestBody JSONObject object) {
        System.out.println("[--------adjustIsTxtFile-------]");
        if (object!=null){
            String id = object.getString("nodeId");
            if (id != null && !id.trim().equals("")) {
                if (Utils.adjustIsProject(id)){
                    return ResultGenerator.genSuccessResult(false);
                }
                if (!storage.selectLabel(id)) {
                    return ResultGenerator.genSuccessResult(true);
                } else {
                    return ResultGenerator.genSuccessResult(false);
                }
            }
        }
        return ResultGenerator.genFailResult("adjustIsTxtFile failed!");
    }
}
