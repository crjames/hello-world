package com.ctrip.tingting.business;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

import com.ctrip.tingting.util.MD5Util;

/**
 * 音频上传至携程服务器
 * Created by wang.donga on 2017/8/15.
 */
public class MatchUploadAudioBusiness {

    //上传至服务器的域名 UAT环境和PROD生产环境
    private static final String UAT_domain = "ws.voice.upload.fx.uat.qa.nt.ctripcorp.com";
    private static final String PROD_domain = "ws.voice.upload.fx.ctripcorp.com";
    //调用上传音频接口所用的channel参数
    private static final String channel = "VoiceNavigation";
    //用来生成调用服务获取token的clientid
    private static final String forClientid = "GS_MATCHAUDIO_NBNB";

    /**
     * 根据供应商/第三方的音频路径下载音频并上传至服务器（MP3）
     * @param audioUrl  供应商/第三方的音频路径
     * @return
     */
    public static String uploadAudioToCtripServer(String audioUrl){
    	System.out.println("供应商音频地址："+audioUrl);
    	if(audioUrl==null || "".equals(audioUrl)) return "###############audio-url不存在";
        String uploadResult = "";
        BufferedReader in = null;
        try {
            HttpURLConnection conn = getHttpURLConnection(audioUrl,"GET",5000,"application/json;charset=UTF-8");
            if(conn != null && conn.getResponseCode()==200){
                InputStream inputStream = conn.getInputStream();
                //将获取到的音频inputStream转变成数组
                byte[] downAudio = readInstream(inputStream);
                //获取token
                String token = getTokenForUpload();
                //调用上传方法进行音频上传
                uploadResult = uploadToRemote(downAudio , token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                //关闭输入流
                if(in != null){
                    in.close();
                }
            }catch(Exception ex){

            }
        }
        return uploadResult;
    }

    private static byte[] readInstream(InputStream inputStream) throws Exception{
        ByteArrayOutputStream byteArrayOutPutStream = null;
        try {
            //创建ByteArrayOutputStream类
            byteArrayOutPutStream = new ByteArrayOutputStream();
            //声明缓存区
            byte[] buffer = new byte[1024];
            //定义读取的默认长度
            int length = -1;
            //把缓存区中的输入到内存中
            while((length = inputStream.read(buffer))!= -1){
                byteArrayOutPutStream.write(buffer,0,length);
            }
        }catch(Exception ex){

        }finally {
            try {
                if(byteArrayOutPutStream != null){
                    byteArrayOutPutStream.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            }catch(Exception ex){

            }
        }
        return byteArrayOutPutStream.toByteArray();//返回这个输入流的字节数组
    }

    public static String getTokenForUpload(){
        String token = "";
        try {
            //生成一个id：clientid；根据定义的全局变量forClientid进行md5加密生成
            String clientid = MD5Util.MD5Encode(forClientid);
            //构造获取token的访问url
            //String url = "http://"+UAT_domain+"/voice/v1/api/gettoken?clientid="+clientid+"&ts="+System.currentTimeMillis();//UAT
            String url = "http://"+PROD_domain+"/voice/v1/api/gettoken?clientid="+clientid+"&ts="+System.currentTimeMillis();//PROD
            
            HttpURLConnection conn = getHttpURLConnection(url,"GET",1000,"text/plain");
            if(conn != null){
                InputStream inputStream = conn.getInputStream();
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
               token = result.toString("UTF-8");
                try {
                    result.close();
                }catch(Exception ex){

                }
            }
        }catch(Exception ex){
        	System.out.println("MatchUploadAudioBusiness-getTokenForUpload异常："+ex.getMessage());
        }
        return token;
    }

    /**
     * 根据传入参数获取HttpURLConnection
     * @param url
     * @param requestType
     * @param timeOut
     * @param contentType
     * @return
     */
    public static HttpURLConnection getHttpURLConnection(String url , String requestType , int timeOut ,
                                                  String contentType){
        HttpURLConnection conn = null;
        try {
            //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("ntproxy.qa.nt.ctripcorp.com", 8080));
            conn = (HttpURLConnection) new URL(url).openConnection();//.openConnection(proxy);
            conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
            conn.setRequestMethod(requestType);
            conn.setConnectTimeout(timeOut);
            conn.addRequestProperty("Content-Type",contentType);
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Accept-Ranges", "bytes");
        } catch (Exception e) {
            System.out.println("MatchUploadAudioBusiness-getHttpURLConnection获取http连接异常："+e.getMessage());
        }
        return conn;
    }

    /**
     * 上传至远程服务器
     * @param buffer
     * @param token
     */
    private static String uploadToRemote(byte[] buffer , String token){
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        //远程服务器地址
        //String uploadUrl = "http://"+UAT_domain+"/voice/v1/api/upload?channel="+channel+"&token="+token;//UAT
        String uploadUrl = "http://"+PROD_domain+"/voice/v1/api/upload?channel="+channel+"&token="+token;//PROD
        try {
            //获取Crc校验码
            String crc = getCrc(buffer);
            HttpURLConnection conn = getHttpURLConnection(uploadUrl,"POST",1000,"audio/mp3");
            if(conn != null){
                //设置补充参数Content-Length   Crc
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.addRequestProperty("Content-Length",String.valueOf(buffer.length));
                conn.addRequestProperty("Crc",crc);

                //音频数据上传
                OutputStream stream = new DataOutputStream(conn.getOutputStream());
                stream.write(buffer,0,buffer.length);
                stream.close();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode==206) {
                    inputStream = conn.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream);
                    reader = new BufferedReader(inputStreamReader);
                    String tempLine = null;
                    while((tempLine=reader.readLine())!=null) {
                        resultBuffer.append(tempLine);
                    }
                }else if (responseCode != 200 && responseCode != 206){
                	System.out.println("音频上传失败："+responseCode);
                }
            }else{
                //打印日志：获取连接失败
            	System.out.println("音频上传获取HttpURLConnection失败："+uploadUrl);
            }
        } catch (Exception e) {
        	System.out.println("音频上传异常："+e.getMessage());
        }finally {
            //关流
            try {
                if(inputStream != null){
                    inputStream.close();
                }
                if(inputStreamReader != null){
                    inputStreamReader.close();
                }
                if(reader != null){
                    reader.close();
                }
            }catch(Exception ex){
            	System.out.println("音频上传关闭流异常："+ex.getMessage());
            }
        }
        return resultBuffer.toString();
    }

    /**
     * java Crc检验码算法：
     */
    private static String getCrc(byte[] fileBytes) throws Exception {
        byte[] tmp;
        int size5M = 5 * 1024 * 1024;
        int size10M = 10 * 1024 * 1024;
        if (fileBytes.length > size10M) {
            tmp = new byte[size10M];
            System.arraycopy(fileBytes, 0, tmp, 0, size5M);
            System.arraycopy(fileBytes, fileBytes.length - size5M, tmp, size5M, size5M);
        }
        else
        {
            tmp = fileBytes;
        }
        MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] digest = m.digest(tmp);
        String hash = new BigInteger(1, digest).toString(16);
        return hash;
    }
}
