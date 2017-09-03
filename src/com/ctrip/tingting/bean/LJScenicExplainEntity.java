package com.ctrip.tingting.bean;
/**
 * 调用链景接口2获取景区讲解集合对应实体对象
 * @author wang.donga
 *
 */
public class LJScenicExplainEntity {

	private String Name;//子景点名称
	private String Mp3Url;//音频地址
	private String DateTime;//音频时长
	private String Sex;//性别：1-男；2-女
	private String HeadImg;//头像地址
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
	public String getDateTime() {
		return DateTime;
	}
	public void setDateTime(String dateTime) {
		DateTime = dateTime;
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
	@Override
	public String toString() {
		return "LJScenicExplainEntity [Name=" + Name + ", Mp3Url=" + Mp3Url
				+ ", DateTime=" + DateTime + ", Sex=" + Sex + ", HeadImg="
				+ HeadImg + "]";
	}
	
}
