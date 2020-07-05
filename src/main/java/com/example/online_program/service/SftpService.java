package com.example.online_program.service;

import com.example.online_program.config.SftpConfig;
import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Vector;

@Service
public class SftpService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SftpService.class);

    @Autowired
    private SftpConfig config;

    public Session getSession() {
        Session session = null;
        JSch jSch = new JSch();
        try {
            session = jSch.getSession(config.getUserName(), config.getHost(), config.getPort());
            if (StringUtils.isNotBlank(config.getPassWord())) {
                session.setPassword(config.getPassWord());
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(3 * 1000);
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }


    public void uploadFile(String fromFile, String toFile) {
        Session session = this.getSession();
        ChannelSftp channel = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
        } catch (JSchException e) {
            e.printStackTrace();
        }
        try {
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        try {
            //判断是否已存在该目录文件
            Vector vector = channel.ls(config.getDesDirPath());
            if (!(vector.size() > 0)) {
                channel.mkdir(config.getDesDirPath());
            }
        } catch (SftpException e) {
            e.printStackTrace();
            try {
                channel.mkdir(config.getDesDirPath());
            } catch (SftpException ex) {
                ex.printStackTrace();
            }
        }
        try {
            channel.put(new FileInputStream(fromFile), config.getDesDirPath() + toFile);
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeChannel(channel, session);
        }
    }

    public void exeShell(String cmd) {
        Session session = getSession();
        ChannelShell channelShell = null;
        try {
            channelShell = (ChannelShell) session.openChannel("shell");
        } catch (JSchException e) {
            e.printStackTrace();
        }
        channelShell.setPty(true);
        try {
            channelShell.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        OutputStream outputStream = null;//写入该流的数据  都将发送到远程端
        InputStream inputStream = null;
        try {
            outputStream = channelShell.getOutputStream();
            inputStream = channelShell.getInputStream();//从远端到达的数据  都能从这个流读取到
            //使用PrintWriter 就是为了使用println 这个方法,好处就是不需要每次手动给字符加\n
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.println("cd " + config.getDesDirPath());
            printWriter.println(cmd);
            printWriter.println("exit");//为了结束本次交互
            printWriter.flush();//把缓冲区的数据强行输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                System.out.println(++i + " :: " + line);
            }
            printWriter.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            closeChannel(channelShell,session);
        }
    }

    public void closeChannel(Channel channel, Session session) {
        try {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
