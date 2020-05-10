package com.example.online_program.service;

import com.example.online_program.entity.UserInfo;

/**
 * @Author: wtt
 * @Date: 20-5-7
 * @Description:
 */
public interface LogiRegi {
    /**
     * @param userInfo
     * @return
     */
    String login(UserInfo userInfo);

    /**
     * @param userInfo
     * @return
     */
    boolean regist(UserInfo userInfo);
}
