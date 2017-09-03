package com.ctrip.tingting.bean;

/**
 * 调用链景接口
 * 4.3获取景区实体
 * 举例：https://op.lianjinglx.com/scenic.ashx?cmd=FindScenic&p=
 * {"Area":"北京","Name":"天坛"}&salt=1469415549&sign=130f4d3bdc2b991c011e0bb0dc313e2d&tag=10000&lang=1&version=1.0
 * 获取的对象 具体value值
 * Created by wang.donga on 2017/8/14.
 */
public class LJScenicValueEntity {
    private long Code;//数据的唯一标识
    private long Unix;//数据的唯一标识
    private String Name;//景区名称
    private String Mp3Url;//音频url
    private String Mp3Size;//音频大小，kb
    private String AreaName;//地区名称
    private String Introduce;//景区介绍
    private String Telephone;//联系电话
    private String PriceDesc;//价格描述
    private String Suitable;//旅游时节
    private String BigImg;//多张,分开
    private String PicImg;//单张
    private String DateTime;//录音时长 s
    private String NickName;//录制人
    private String Sex;//录制人1-男，2-女
    private String HeadImg;//录制人头像
    private String Lat;//纬度
    private String Lon;//经度
	public long getCode() {
        return Code;
    }
    public void setCode(long code) {
        Code = code;
    }
    public long getUnix() {
        return Unix;
    }
    public void setUnix(long unix) {
        Unix = unix;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getMp3Url() {
        return Mp3Url;
    }
    public void setMp3Url(String mp3Url) {
        Mp3Url = mp3Url;
    }
    public String getMp3Size() {
        return Mp3Size;
    }
    public void setMp3Size(String mp3Size) {
        Mp3Size = mp3Size;
    }
    public String getAreaName() {
        return AreaName;
    }
    public void setAreaName(String areaName) {
        AreaName = areaName;
    }
    public String getIntroduce() {
        return Introduce;
    }
    public void setIntroduce(String introduce) {
        Introduce = introduce;
    }
    public String getTelephone() {
        return Telephone;
    }
    public void setTelephone(String telephone) {
        Telephone = telephone;
    }
    public String getPriceDesc() {
        return PriceDesc;
    }
    public void setPriceDesc(String priceDesc) {
        PriceDesc = priceDesc;
    }
    public String getSuitable() {
        return Suitable;
    }
    public void setSuitable(String suitable) {
        Suitable = suitable;
    }
    public String getBigImg() {
        return BigImg;
    }
    public void setBigImg(String bigImg) {
        BigImg = bigImg;
    }
    public String getPicImg() {
        return PicImg;
    }
    public void setPicImg(String picImg) {
        PicImg = picImg;
    }
    public String getDateTime() {
        return DateTime;
    }
    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }
    public String getNickName() {
        return NickName;
    }
    public void setNickName(String nickName) {
        NickName = nickName;
    }
    public String getSex() {
        return Sex;
    }
    public void setSex(String sex) {
        Sex = sex;
    }
    public String getHeadImg() {
        return HeadImg;
    }
    public void setHeadImg(String headImg) {
        HeadImg = headImg;
    }
    public String getLat() {
		return Lat;
	}
	public void setLat(String lat) {
		Lat = lat;
	}
	public String getLon() {
		return Lon;
	}
	public void setLon(String lon) {
		Lon = lon;
	}
    @Override
    public String toString() {
        return "LJScenicValueEntity [Code=" + Code + ", Unix=" + Unix
                + ", Name=" + Name + ", Mp3Url=" + Mp3Url + ", Mp3Size="
                + Mp3Size + ", AreaName=" + AreaName + ", Introduce="
                + Introduce + ", Telephone=" + Telephone + ", PriceDesc="
                + PriceDesc + ", Suitable=" + Suitable + ", BigImg=" + BigImg
                + ", PicImg=" + PicImg + ", DateTime=" + DateTime
                + ", NickName=" + NickName + ", Sex=" + Sex + ", HeadImg="
                + HeadImg + ", Lat=" + Lat + ", Lon=" + Lon + "]";
    }
}
