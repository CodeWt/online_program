package com.example.online_program.service.impl;

import com.example.online_program.dao.LogiRegiMapper;
import com.example.online_program.entity.UserInfo;
import com.example.online_program.service.LogiRegi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: wtt
 * @Date: 20-5-7
 * @Description:
 */
@Service
public class LogiRegiImpl implements LogiRegi {

    @Autowired
    private LogiRegiMapper logiRegiMapper;
    @Override
    public boolean regist(UserInfo userInfo) {
        if (logiRegiMapper.isExist(userInfo.getName())!=null){
            return false;
        }
        if (logiRegiMapper.regist(userInfo)>0){
            return true;
        }
        return false;
    }

    @Override
    public String login(UserInfo userInfo) {
        return logiRegiMapper.login(userInfo);
    }
}
