package com.ctrip.tingting.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class Test816 {

	/**
	 * 测试音频下载方法
	 */
	@org.junit.Test
	public void testAudio(){
		String picUrl = "http://voice.uat.qa.nt.ctripcorp.com/voices/v20f0j00000002oys757F.mp3";
		try {
	        //HttpURLConnection conn = getHttpURLConnection(picUrl,"GET",60000,"application/x-www-form-urlencoded");
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("ntproxy.qa.nt.ctripcorp.com", 8080));
			HttpURLConnection conn = (HttpURLConnection) new URL(picUrl).openConnection(proxy);
			conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
		    conn.setRequestMethod("GET");
		    conn.setConnectTimeout(1000);
		    conn.addRequestProperty("ContentType","application/x-www-form-urlencoded");
			
			if(conn != null && conn.getResponseCode()==200){
	        	InputStream inputStream = conn.getInputStream();
	        	System.out.println("打印inputStream大小");
	        	System.out.println(inputStream.available());
	        	System.out.println("start writing=============");
	        	byte[] data = readInstream(inputStream);
	            File file = new File("D:/Users/Public/Pictures/Sample Pictures/demo81603.mp3");
	            FileOutputStream outputStream = new FileOutputStream(file);
	            outputStream.write(data);
	            outputStream.close();       
	        	System.out.println("ending==============");
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private byte[] readInstream(InputStream inputStream) throws Exception{
		System.out.println("-=-=-=-=-=-=-=-=");
        ByteArrayOutputStream byteArrayOutPutStream = new ByteArrayOutputStream();//创建ByteArrayOutputStream类
        byte[] buffer = new byte[1024];//声明缓存区
        int length = -1;//定义读取的默认长度
        while((length = inputStream.read(buffer))!= -1){
            byteArrayOutPutStream.write(buffer,0,length);//把缓存区中的输入到内存中         
        };
        byteArrayOutPutStream.close();//关闭输入流
        inputStream.close();//关闭输入流

        return byteArrayOutPutStream.toByteArray();//返回这个输入流的字节数组
    }
}
