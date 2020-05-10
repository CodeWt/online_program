package com.example.online_program.dao;

import com.example.online_program.entity.ProjInfo;
import com.example.online_program.entity.TreeNodeInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author: wtt
 * @Date: 20-5-6
 * @Description:
 */
@Repository
public interface TreeNodeOptMapper {
    /**
     * @param projInfo
     * @return
     * insert data of a project to db
     */
    int insertProj(ProjInfo projInfo);

    /**
     * @param treeNodeInfo
     * @return
     * insert a node to db
     */
    int insertNode(TreeNodeInfo treeNodeInfo);


    /**
     * @param map
     * @return
     * rename Project
     */
    int renameProj(Map map);

    /**
     * @param map
     * @return
     */
    int renameNode(Map map);

    /**
     * @param userId
     * @return
     * query Project List
     */
    List queryProjList(String userId);

    /**
     * @param nodeId
     * @return
     */
    List queryNode(String nodeId);

    /**
     * @param projectId
     * @return
     */
    String  queryProjName(String projectId);

    /**
     * @param list
     * @return
     */
    int deleteNode(List list);

    /**
     * @param projectId
     * @return
     */
    int deleteProj(String projectId);

    /**
     * @param list
     * @return
     */
    List queryAllchildNode(List list);

    /**
     * @param treeNodeInfo
     * @return
     */
    int deleteRelative(TreeNodeInfo treeNodeInfo);

    /**
     * @param userId
     * @return
     */
    String isExistUserId(String userId);
}
