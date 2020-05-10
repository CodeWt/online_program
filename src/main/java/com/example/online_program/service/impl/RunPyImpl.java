package com.example.online_program.service.impl;

import com.example.online_program.controller.CodeSearchController;
import com.example.online_program.service.RunPy;
import com.example.online_program.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: wtt
 * @Date: 20-5-8
 * @Description:
 */
@Component
public class RunPyImpl implements RunPy {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunPyImpl.class);

    @Override
    public String runPy(String string) {
        String uuid = Utils.getUUIDString();
        String fileName = new StringBuffer()
                .append("./").append(uuid)
                .append(".py").toString();

        RandomAccessFile raf = null;
        FileChannel channel = null;
        try {
            File file = new File(fileName);
            raf = new RandomAccessFile(file, "rw");
            channel = raf.getChannel();
            byte[] bytes = string.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            channel.write(buffer);
            String[] cmds = {"/bin/sh", "-c",
                    new StringBuffer("python3 ").append(fileName).toString()};
            Process process = Runtime.getRuntime().exec(cmds);
            process.waitFor();
            InputStream inputStream1 = process.getInputStream();
            InputStream inputStream2 = process.getErrorStream();
            byte[] bytes1 = new byte[1024];
            int len;
            StringBuffer sb = new StringBuffer();
            while ((len = inputStream1.read(bytes1)) != -1) {
                sb.append(new String(bytes1, 0, len));
            }
            while ((len = inputStream2.read(bytes1)) != -1) {
                sb.append(new String(bytes1, 0, len));
            }
            if (file.isFile() && file.delete()) {
                LOGGER.debug(fileName + " is deleted !");
            }
            return sb.toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
                if (channel != null) {

                    channel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
