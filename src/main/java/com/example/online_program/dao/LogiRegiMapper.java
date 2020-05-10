package com.example.online_program.dao;

import com.example.online_program.entity.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * @Author: wtt
 * @Date: 20-5-7
 * @Description:
 */
@Repository
public interface LogiRegiMapper {
    /**
     * @param userInfo
     * @return
     */
    int regist(UserInfo userInfo);

    /**
     * @param userInfo
     * @return
     */
    String login(UserInfo userInfo);


    /**
     * @param name
     * @return
     */
    String isExist(String name);
}
