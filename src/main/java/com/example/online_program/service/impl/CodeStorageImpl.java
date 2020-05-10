package com.example.online_program.service.impl;

import com.example.online_program.controller.CodeSearchController;
import com.example.online_program.dao.CodeStorageMapper;
import com.example.online_program.entity.CodeInfo;
import com.example.online_program.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wtt
 * @Date: 19-4-1
 * @Description:
 */
@Service
public class CodeStorageImpl implements CodeStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodeStorageImpl.class);

    @Autowired
    private CodeStorageMapper storageMapper;

    @Override
    @Transactional
    public boolean save(CodeInfo codeInfo) {
        if ((codeInfo != null) && (codeInfo.getCodeId() != null)
                && (!codeInfo.getCodeId().trim().equals(""))) {
            int i = storageMapper.isExistsCode(codeInfo.getCodeId());
            LOGGER.debug("isExistsCode -- i : " + i);
            storageMapper.saveCode(codeInfo);
            return true;
        }
        return false;
    }

    @Override
    public String show(String codeId) {
        String result = null;
        if (codeId != null && !codeId.trim().equals("")) {
            result = storageMapper.showCode(codeId);
        }
        return result;
    }

    @Override
    public List getHistoryCode(String userId) {
        List result = new ArrayList();
        List<CodeInfo> list = storageMapper.showHistory(userId);
        if (list.size() > 0) {
            LOGGER.debug("history code List size : " + list.size());
            for (CodeInfo info : list) {
                result.add(info);
            }
        }
        return result;
    }

    @Override
    public boolean deleteCode(String codeId) {
        if (codeId != null && !codeId.trim().equals("")) {
            int i = storageMapper.deleteCode(codeId);
            LOGGER.debug("del code i : " + i);
            if (i > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delMutiCode(List list) {
        if (list!=null&&list.size()>0){
            if (storageMapper.delMutiCode(list)>0){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean selectLabel(String parentId) {
        String label = storageMapper.selectLabel(parentId);
        if (label != null && label.equals("0")) {
            return true;
        }
        return false;
    }
}
