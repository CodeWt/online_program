package com.example.online_program;

import com.example.online_program.service.CodeStorage;
import com.example.online_program.service.impl.CodeStorageImpl;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @Author: wtt
 * @Date: 20-5-8
 * @Description:
 */
public class MultiWriteFileTest {
    static String fileName = "/home/wtt/IdeaProjects/" +
            "online_program/src/test/java/com/" +
            "example/online_program/utils/t.txt";
    @Test
    public void writeFileWithBufferWriter() throws IOException {
        Method[] methods = MultiWriteFileTest.class.getDeclaredMethods();
        System.out.println("methods : "+methods);
        String str = Arrays.toString(methods);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true));
        System.out.println("------追加信息-----");
        writer.write(str);
        writer.append("追加信息1");
        writer.append("追加信息2");
        writer.close();
    }

    @Test
    public void writeFileWithFileOutputStream() throws IOException {
        Method[] methods = CodeStorageImpl.class.getDeclaredMethods();
        System.out.println("methods : "+methods);
        String s = Arrays.toString(methods);
        FileOutputStream outputStream = new FileOutputStream(fileName);
        byte[] bytes = s.getBytes();
        outputStream.write(bytes);
        outputStream.close();
    }

    @Test
    public void writeFileWithDataOutputStream() throws IOException {
        Method[] methods = CodeStorage.class.getDeclaredMethods();
        String s = Arrays.toString(methods);

        FileOutputStream fos = new FileOutputStream(fileName);
        DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(fos));
        outputStream.writeUTF(s);
        outputStream.close();
        System.out.println("----read  and  verify result--------");
        String result;
        FileInputStream fis = new FileInputStream(fileName);
        DataInputStream reader = new DataInputStream(fis);
        result = reader.readUTF();
        reader.close();
        System.out.println(result.equals(s));

    }
    @Test
    public void writeFileWithRAF() throws IOException {
        RandomAccessFile writer = new RandomAccessFile(fileName,"rw");
        writer.seek(2);
        writer.writeUTF("向指定位置插入新内容");
        writer.close();
    }

    @Test
    public void writeFileWithFileChannel() throws IOException {
        RandomAccessFile stream = new RandomAccessFile(fileName,"rw");
        FileChannel channel = stream.getChannel();

        String val = MultiWriteFileTest.class.getSimpleName();
        byte[] bytes = val.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        channel.write(buffer);
        stream.close();
        channel.close();
    }

    @Test
    public void writeWithFiles() throws IOException {
        String str = "Hello";
        Path path = Paths.get(fileName);
        byte[] bytes = str.getBytes();
        Files.write(path,bytes);
        String readStr = Files.readAllLines(path).get(0);
        System.out.println(str.equals(readStr));
    }

    @Test
    public void writeFile() throws IOException{
        File file = new File("./xxx.py");
        file.getParentFile().setWritable(true,false);
        if (!file.exists()){
            file.createNewFile();
        }
        System.out.println(file.getParent());
        System.out.println(file.getParentFile().canWrite());
        System.out.println("---------");
        System.out.println(file.getName());
        System.out.println(file.getPath());
        System.out.println(file.getAbsoluteFile());
        System.out.println(file.getCanonicalPath());
        System.out.println(file.canWrite());
    }

    @Test
    public void disAccess() throws IOException, InterruptedException {
        File file = new File("./xxxx.py");
        System.out.println("abs --->"+file.getCanonicalPath());
        File file1 = new File(file.getCanonicalPath());
        while (file1!=null){
            System.out.println("=============");
            System.out.println(file1.getCanonicalPath());
            System.out.println(file1.canWrite());
            if (!file1.canWrite()){
                file1.setWritable(true,false);
            }
            System.out.println(file1.canWrite());
            System.out.println("=============");
            file1 = file1.getParentFile();
        }
    }
}
//https://www.jianshu.com/p/2ee6f0b9e484