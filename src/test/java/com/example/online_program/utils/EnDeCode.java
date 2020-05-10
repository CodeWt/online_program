package com.example.online_program.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * @Author: wtt
 * @Date: 20-5-8
 * @Description:
 */
public class EnDeCode {
    @Test
    public void MD5Gen(){
        System.out.println("md5 val : "+DigestUtils.md5Hex("123456"));
    }

    @Test
    public void Base64Gen(){
        Base64 base64 = new Base64();
        String str = base64.encodeToString("123456".getBytes());
        System.out.println("base64 val : "+str);
        System.out.println("-------base64 decode-------");
        String s = new String(base64.decode(str));
        System.out.println(s);
    }
    @Test
    public void  SHA1Gen(){
        String s = DigestUtils.sha1Hex("123456");
        System.out.println("----------sha1---------");
        System.out.println("sha1 val : "+s);
    }
    @Test
    public void SHA256Gen(){
        System.out.println("-----------sha256---------");
        System.out.println("sha256 val : "+DigestUtils.sha256Hex("123456"));
    }

}
