package com.example.online_program.service;

import com.example.online_program.constants.OnlineIde;
import com.example.online_program.controller.CodeSearchController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wtt
 * @Date: 19-4-6
 * @Description:
 */
public class SeaInsPac {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeaInsPac.class);

    /**
     * search python Package
     * @param pName (package name)
     * @return
     */
    public static List searchPackage(String pName) {
        LOGGER.debug("search package name : " + pName);
        List result = new ArrayList();
        InputStream in = null;
        try {
            if (pName != null && !pName.trim().equals("")) {
                String[] cmds = {"/bin/sh", "-c", "pip search " + pName};
                Process pro = Runtime.getRuntime().exec(cmds);
                pro.waitFor();
                in = pro.getInputStream();
                BufferedReader read = new BufferedReader(new InputStreamReader(in));
                String line = null;
                int id =0;
                while ((line = read.readLine()) != null) {
                    LOGGER.debug("exec output + \t" + line);
                    int index = line.lastIndexOf("- ");
//                    LOGGER.debug("line : "+line+"\tlen : "+line.length()+"\tindex : "+index);
                    if (index>0){
                        Map map = new HashMap();
                        map.put("id",++id);
                        map.put("text", line.substring(0, index).trim());
                        map.put("desc", "-" + line.substring(index));
                        result.add(map);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in!=null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * install python Package
     * @param pName(package name)
     * @param pipV(pip version)
     * @return
     */
    public static int installPackage(String pName, String pipV) {
        String searchName = pName.split("\\(")[0];
        String[] cmds3 = {"/bin/sh", "-c", "pip3 install " + searchName};
        InputStream in = null;
        if (pName != null && !pName.trim().equals("")) {
            try {
                if (pipV != null && pipV.trim().equals("v2")) {
                    String[] cmds2 = {"/bin/sh", "-c", "pip install " + searchName};
                    LOGGER.debug("pip2 cmd : "+cmds2);
                    Process pro = Runtime.getRuntime().exec(cmds2);
                    pro.waitFor();
                    in = pro.getInputStream();
                    BufferedReader read = new BufferedReader(new InputStreamReader(in));
                    String line = null;
                    while ((line = read.readLine()) != null) {
                        LOGGER.debug("pip2 install fun exe result : "+line);
                        int index = line.indexOf(" ");
                        if (index!=-1&&line.substring(0,index).equals("Successfully")){
                            return 0;
                        }
                    }
                } else {
                    LOGGER.debug("pip3 cmd : "+cmds3);
                    Process pro = Runtime.getRuntime().exec(cmds3);
                    pro.waitFor();
                    in = pro.getInputStream();
                    BufferedReader read = new BufferedReader(new InputStreamReader(in));
                    String line = null;
                    while ((line = read.readLine()) != null) {
                        LOGGER.debug("pip3 install fun exe result : "+line);
                        int index = line.indexOf(" ");
                        int satisfied = line.indexOf(":");
                        if (index!=-1&&line.substring(0,index).equals("Successfully")){
                            return 0;
                        }
                        if (satisfied!=-1&&line
                                .substring(0,satisfied)
                                .equalsIgnoreCase("Requirement already satisfied")){
                            return 1;
                        }
                    }
                }
                LOGGER.debug("------ install end -----");
            } catch (IOException e) {
                e.printStackTrace();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        List list = searchPackage("pika");

//        for (Object o : list) {
//            Map map = ((HashMap) o);
//            installPackage(map.get("pName").toString(), "22");
//            break;
//        }
//        String[] cmds = {"/bin/sh","-c","ps -ef|grep java"};
//        String[] cmds = {"/bin/sh", "-c", "pip search pika"};
//        String[] cmds = {"pwd"};
//        String[] cmds = {"/bin/sh","-c","pip3 install pika"};
        String runPy = "python "+"/home/wtt/IdeaProjects/online_program/src/main/resources/py/hi.py";
        String[] cmds = {"/bin/sh","-c",runPy};
        Process pro = Runtime.getRuntime().exec(cmds);
        pro.waitFor();
//        InputStream in = pro.getInputStream();
        InputStream in = pro.getErrorStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = read.readLine()) != null) {
            LOGGER.debug(line);
        }
    }
}
