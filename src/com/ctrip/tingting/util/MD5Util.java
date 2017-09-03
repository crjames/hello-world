package com.ctrip.tingting.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wang.donga on 2017/8/14.
 */
public class MD5Util {

    /**
     * MD5加密方法
     * @param input
     * @return
     */
    public static String MD5Encode(String input) {
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes("UTF-8"));
            byte bb[] = md.digest();
            int i;

            for (byte b : bb) {
                i = b;
                if (i < 0) i += 256;
                if (i < 16) sb.append("0");
                sb.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            //Vnlogger.error(e.getMessage()+";加密字符串="+input, "MD5Util.MD5Encode");
        } catch (UnsupportedEncodingException e) {
            //Vnlogger.error(e.getMessage()+";加密字符串转utf-8错误="+input, "MD5Util.MD5Encode");
        }

        return sb.toString();
    }
}
