package com.example.online_program.service;

import com.example.online_program.entity.ProjInfo;
import com.example.online_program.entity.TreeNodeInfo;

import java.util.List;
import java.util.Map;


/**
 * @Author: wtt
 * @Date: 19-3-11
 * @Description:
 */
public interface TreeNodeOpt {
    /**
     *  create Project
     * @return
     */
    public boolean createProject(ProjInfo projInfo);

    /**
     * create directory and file
     * @param parentId
     * @return
     */
    public boolean createDireFile(TreeNodeInfo treeNodeInfo);
    /**
     * delete node
     * @return
     */
    public boolean deleteNode(List list);

    /**
     * renameNode
     * @return
     */
    boolean renameNode(Map<String, Object> params);

    /**
     * @param userId
     * @return
     */
    List<ProjInfo> queryProjectList(String userId);

    /**
     * @param nodeId
     * @return
     */
    List<TreeNodeInfo> queryNodeData(String nodeId);

    /**
     * @param projectId
     * @return
     */
    String queryProjectName(String projectId);

    /**
     * @param projectId
     */
    void deleteProject(String projectId);

    /**
     * @param list
     * @return
     */
    List<List> selectAllChildNodeId(List list);

    /**
     * @param treeNodeInfo
     * @return
     */
    boolean moveNode(TreeNodeInfo treeNodeInfo);
}
