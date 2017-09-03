package com.ctrip.tingting.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test814 {
	public static void main(String[] args) {
		Long time = System.currentTimeMillis()/1000L;
		System.out.println(time);
		String param = "774343341502688728110"+time+"b534f8a9dc1a4e7e8284f6dd65ab56d5";
		System.out.println(param);
		System.out.println(encode(param));
	}
	
	public static String encode(String input) {
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte bb[] = md.digest();
            int i;

            for (byte b : bb) {
                i = b;
                if (i < 0) i += 256;
                if (i < 16) sb.append("0");
                sb.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
        }

        return sb.toString();
    }

}
