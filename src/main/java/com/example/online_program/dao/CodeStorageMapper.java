package com.example.online_program.dao;

import com.example.online_program.entity.CodeInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: wtt
 * @Date: 20-5-6
 * @Description:
 */
@Repository
public interface CodeStorageMapper {
    /**
     * @param codeInfo
     * save code
     */
    boolean saveCode(CodeInfo codeInfo);

    /**
     * @param codeId
     * @return
     * get code file content via codeId
     */
    String showCode(String codeId);

    /**
     * @param userId
     * @return
     * get sb history code by userId
     */
    List showHistory(String userId);

    /**
     * @param codeId
     * @return
     * delete code file by codeId
     */
    int deleteCode(String codeId);

    /**
     * @param list
     * @return
     */
    int delMutiCode(List list);
    /**
     * @param codeInfo
     * @return
     * update code file
     */

    int updateCode(CodeInfo codeInfo);

    /**
     * @param codeId
     * @return
     * judge code file is exist or not
     */
    int isExistsCode(String codeId);

    /**
     * @param parentId
     * @return
     */
    String selectLabel(String parentId);
}
