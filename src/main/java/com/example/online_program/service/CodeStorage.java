package com.example.online_program.service;

import com.example.online_program.entity.CodeInfo;

import java.util.List;

/**
 * @Author: wtt
 * @Date: 19-4-1
 * @Description:
 */

public interface CodeStorage {
    /**
     * @param codeInfo
     * save code
     */
    boolean save(CodeInfo codeInfo);

    /**
     * @param codeId
     * @return
     * get code file content via codeId
     */
    String show(String codeId);

    /**
     * @param userId
     * @return
     * get sb history code by userId
     */
    List getHistoryCode(String userId);

    /**
     * @param codeId
     * @return
     * delete code file by codeId
     */

    boolean deleteCode(String codeId);

    /**
     * @param list
     * @return
     */
    boolean delMutiCode(List list);

    /**
     * @param parentId
     * @return
     * 0为目录文件,1为普通文件
     */
    boolean selectLabel(String parentId);
}
