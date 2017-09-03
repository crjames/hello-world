package com.ctrip.tingting.bean;

/**
 * 上传音频至服务器返回的响应对象
 * Created by wang.donga on 2017/8/15.
 */
public class UploadAudioResponseEntity {
    private String file_name;//文件名
    private String url;//新的音频地址
    public String getFile_name() {
        return file_name;
    }
    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public String toString() {
        return "UploadAudioResponseEntity [file_name=" + file_name + ", url="
                + url + "]";
    }
}
