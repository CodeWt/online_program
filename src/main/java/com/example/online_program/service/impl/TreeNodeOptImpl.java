package com.example.online_program.service.impl;

import com.example.online_program.controller.CodeSearchController;
import com.example.online_program.dao.TreeNodeOptMapper;
import com.example.online_program.entity.ProjInfo;
import com.example.online_program.entity.TreeNodeInfo;
import com.example.online_program.service.TreeNodeOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: wtt
 * @Date: 19-3-11
 * @Description:
 */
@Service
public class TreeNodeOptImpl implements TreeNodeOpt {
    private static final Logger LOGGER = LoggerFactory.getLogger(TreeNodeOptImpl.class);
    @Autowired
    private TreeNodeOptMapper nodeOptMapper;

    /**
     * createProject
     *
     * @param projInfo
     * @return
     */
    @Override
    @Transactional
    public boolean createProject(ProjInfo projInfo) {
        if (nodeOptMapper.isExistUserId(projInfo.getUserId())!=null){
            int i = nodeOptMapper.insertProj(projInfo);
            LOGGER.debug("[--- createProject --- 影响数据行 i 为：" + i);
            if (i>0){
                return true;
            }
        }
        return false;
    }

    /**
     * createDireFile
     *
     * @param treeNodeInfo
     * @return
     */
    @Override
    @Transactional
    public boolean createDireFile(TreeNodeInfo treeNodeInfo) {
        int j = nodeOptMapper.insertNode(treeNodeInfo);
        LOGGER.debug("影响数据行 j 为：" + j);
        return true;
    }

    @Override
    @Transactional
    public boolean renameNode(Map<String, Object> params) {
        Object object = params.get("type");
        String type = null;
        if (object instanceof String) {
            type = (String) object;
        }
        if (type != null && type.trim().equals("proj")) {
            LOGGER.debug("[-------rename proj---------]");
            nodeOptMapper.renameProj(params);
        } else {
            LOGGER.debug("[-------rename node---------]");
            nodeOptMapper.renameNode(params);
        }
        return true;
    }

    @Override
    public List<ProjInfo> queryProjectList(String userId) {
        List<ProjInfo> list = null;
        list = nodeOptMapper.queryProjList(userId);
        LOGGER.debug("size : "+list.size());
        for (ProjInfo info : list) {
            LOGGER.debug(info.getProjectId() + "\t" + info.getProjectName());
        }
        return list;
    }

    @Override
    public List<TreeNodeInfo> queryNodeData(String nodeId) {
        List list = null;
        if (nodeId != null && !nodeId.trim().equals("")) {
            list = nodeOptMapper.queryNode(nodeId);
            LOGGER.debug("[ --- queryNodeData Result : " + list);
        }
        return list;
    }

    @Override
    public String queryProjectName(String projectId) {
        return nodeOptMapper.queryProjName(projectId);
    }

    @Override
    public boolean deleteNode(List list) {
        if (list.size() > 0) {
            nodeOptMapper.deleteNode(list);
            return true;
        }
        return false;
    }

    @Override
    public void deleteProject(String projectId) {
            int i = nodeOptMapper.deleteProj(projectId);
            LOGGER.debug("[  deleteProject count is : " + i);
    }
    @Override
    public List<List> selectAllChildNodeId(List list) {
        if (list.size() > 0) {
            LOGGER.debug(list.size()+"\t"+list.get(0));
            List li = nodeOptMapper.queryAllchildNode(list);
            LOGGER.debug("[selectAllChildNodeId : ]" + li);
            if (li == null || li.size() < 1) {
                return null;
            }
            lists.add(li);
            selectAllChildNodeId(li);
        }
        return lists;
    }

    @Transactional
    @Override
    public boolean moveNode(TreeNodeInfo treeNodeInfo) {
        int i = nodeOptMapper.deleteRelative(treeNodeInfo);
        int j = nodeOptMapper.insertNode(treeNodeInfo);
        if (i>0&&j>0){
            return true;
        }
        return false;
    }

    private List<List> lists = new LinkedList<>();

    public TreeNodeOptImpl(List<List> lists) {
        this.lists = lists;
    }

    public TreeNodeOptImpl() {
    }

    public List<List> getLists() {
        return lists;
    }

    public void setLists(List<List> lists) {
        this.lists = lists;
    }
}
