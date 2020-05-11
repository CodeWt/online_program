package com.example.online_program.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: wtt
 * @Date: 19-3-29
 * @Description:
 */
public class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * @return
     * 49777483d41644af91cfa11aaa3e94e7  32
     */
    public static String getUUIDString() {
        return UUID
                .randomUUID()
                .toString()
                .replaceAll("-", "");
    }

    /**
     * @return
     * 2020-05-11 22:35:03.509
     */
    public static String getTimeStamp() {
        return new Timestamp(
                System.currentTimeMillis()
        ).toString();
    }

    /**
     * @param replacement n 为普通节点
     *                    p 为工程节点
     * @return
     */
    public static String getUUIDString(String replacement) {
        return new StringBuffer(getUUIDString())
                .replace(0, 1, replacement)
                .toString();
    }

    /**
     * adjust node is Project or not by nodeId
     *
     * @param id
     * @return
     */
    public static boolean adjustIsProject(String id) {
        if (id == null || id.trim().equals("")) {
            return false;
        }
        if (id.trim().substring(0, 1).equals("p")) {
            return true;
        }
        return false;
    }


    /**
     * merge many inner list of list to a list
     *
     * @param lists
     * @return
     */
    public static List mergeList(List<List> lists) {
        List list = new ArrayList();
        if (lists != null && lists.size() > 0) {
            for (List li : lists) {
                list.addAll(li);
            }
            return list;
        }
        return list;
    }

    public static void disRequestData(HttpServletRequest request) {
        LOGGER.debug("req uri : " + request.getRequestURI());
        LOGGER.debug("req url : " + request.getRequestURL());
        LOGGER.debug("req ContextPath" + request.getContextPath());
        LOGGER.debug("req ServletPath : " + request.getServletPath());
        LOGGER.debug("req getQueryString : " + request.getQueryString());
    }

    /**
     * 返回起始查询数据的起始点from
     * @param page 第几页
     * @param num  每页的数据条数
     * @return
     */
    public static int getLimitStartPoint(int page,int num){
        if (page>0&&num>0){
            if (page>1){
                return (page-1)*num;
            }
            return 0;
        }
        //参数不正确返回-1
        return -1;
    }

    public static int countPages(Number sum,Number num){
        double s = sum.doubleValue();
        double n = num.doubleValue();
//        LOGGER.debug("s : "+s +"\t n : "+n + "\t res : "+Math.ceil(s/n));
        if (s>0&&n>0){
            return (int) Math.ceil(s/n);
        }
        return -1;
    }


    public static void main(String[] args) {
        LOGGER.debug(getUUIDString()+"\t" +getUUIDString().length());
//        for (int i = 0; i < 10; i++) {
//            LOGGER.debug(new StringBuffer(getUUIDString()).replace(0, 1, "p"));
//        }
        LOGGER.debug(getTimeStamp());
    }
}
/*
向上取整用Math.ceil(double a)
向下取整用Math.floor(double a)*/
