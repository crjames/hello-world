package com.ctrip.tingting.entity;

import java.sql.Timestamp;

public class CSPoiMappingLocal {

	private long districtId;
	private long poiId;
	private String c_city_chname = "";//携程中文城市名
	private String c_city_enname = "";//携程英文城市名
	private String c_site_chname = "";//携程中文景点名
	private String c_site_enname = "";//携程英文景点名
	private String c_site_localname = "";//携程本地语言景点名
	private String c_isnewmodel = "";//携程是否使用前台新模板
	private String c_site_phone = "";//携程景点联系电话
	private String c_site_address = "";//携程景点地址
	private int matchscore = 0;//匹配得分
	private String child_name = "";//子景点名称
	private String s_city_chname = "";//供应商中文城市名
	private String s_city_enname = "";//供应商英文城市名
	private String s_site_chname = "";//供应商中文景点名
	private String s_site_enname = "";//供应商英文城市名
	private String s_site_localname = "";//供应商当地语言城市名
	private String s_ishot = "";//供应商是否热门景点
	private String s_site_phone = "";//供应商景点联系电话
	private String s_site_address = "";//供应商景点地址
	private String audio_filename = "";//音频文件名称
	private String audio_text = "";//音频文本
	private String audio_url = "";//音频地址
	private int audio_time;//音频时长
	private long audio_size;//音频大小
	private String narrator_nick = "";//音频解说昵称
	private String narrator_sex = "";//音频解说性别
	private String narrator_headurl = "";//音频解说头像url
	private String supplier_type = "";//供应商类别
	private Timestamp DataChange_LastTime;//最后修改时间
	private Timestamp DataChange_CreateTime;//创建时间
	private String IsDeleted = "";//是否已删除
	private String s_city_price = "";//供应商城市解锁价格
	private String key_site = "";//供应商key值
	private String site_level = "";//景点级别：1-父景点；2-子景点
	private String IsEffective = "1";//数据是否有效  0-无效；1-有效
	private String IsInChina ;//是否国内景点  0-国内；1-国外
	
	public long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}
	public long getPoiId() {
		return poiId;
	}
	public void setPoiId(long poiId) {
		this.poiId = poiId;
	}
	public String getC_city_chname() {
		return c_city_chname;
	}
	public void setC_city_chname(String c_city_chname) {
		this.c_city_chname = c_city_chname;
	}
	public String getC_city_enname() {
		return c_city_enname;
	}
	public void setC_city_enname(String c_city_enname) {
		this.c_city_enname = c_city_enname;
	}
	public String getC_site_chname() {
		return c_site_chname;
	}
	public void setC_site_chname(String c_site_chname) {
		this.c_site_chname = c_site_chname;
	}
	public String getC_site_enname() {
		return c_site_enname;
	}
	public void setC_site_enname(String c_site_enname) {
		this.c_site_enname = c_site_enname;
	}
	public String getC_site_localname() {
		return c_site_localname;
	}
	public void setC_site_localname(String c_site_localname) {
		this.c_site_localname = c_site_localname;
	}
	public String getC_isnewmodel() {
		return c_isnewmodel;
	}
	public void setC_isnewmodel(String c_isnewmodel) {
		this.c_isnewmodel = c_isnewmodel;
	}
	public String getC_site_phone() {
		return c_site_phone;
	}
	public void setC_site_phone(String c_site_phone) {
		this.c_site_phone = c_site_phone;
	}
	public String getC_site_address() {
		return c_site_address;
	}
	public void setC_site_address(String c_site_address) {
		this.c_site_address = c_site_address;
	}
	public int getMatchscore() {
		return matchscore;
	}
	public void setMatchscore(int matchscore) {
		this.matchscore = matchscore;
	}
	public String getChild_name() {
		return child_name;
	}
	public void setChild_name(String child_name) {
		this.child_name = child_name;
	}
	public String getS_city_chname() {
		return s_city_chname;
	}
	public void setS_city_chname(String s_city_chname) {
		this.s_city_chname = s_city_chname;
	}
	public String getS_city_enname() {
		return s_city_enname;
	}
	public void setS_city_enname(String s_city_enname) {
		this.s_city_enname = s_city_enname;
	}
	public String getS_site_chname() {
		return s_site_chname;
	}
	public void setS_site_chname(String s_site_chname) {
		this.s_site_chname = s_site_chname;
	}
	public String getS_site_enname() {
		return s_site_enname;
	}
	public void setS_site_enname(String s_site_enname) {
		this.s_site_enname = s_site_enname;
	}
	public String getS_site_localname() {
		return s_site_localname;
	}
	public void setS_site_localname(String s_site_localname) {
		this.s_site_localname = s_site_localname;
	}
	public String getS_ishot() {
		return s_ishot;
	}
	public void setS_ishot(String s_ishot) {
		this.s_ishot = s_ishot;
	}
	public String getS_site_phone() {
		return s_site_phone;
	}
	public void setS_site_phone(String s_site_phone) {
		this.s_site_phone = s_site_phone;
	}
	public String getS_site_address() {
		return s_site_address;
	}
	public void setS_site_address(String s_site_address) {
		this.s_site_address = s_site_address;
	}
	public String getAudio_filename() {
		return audio_filename;
	}
	public void setAudio_filename(String audio_filename) {
		this.audio_filename = audio_filename;
	}
	public String getAudio_text() {
		return audio_text;
	}
	public void setAudio_text(String audio_text) {
		this.audio_text = audio_text;
	}
	public String getAudio_url() {
		return audio_url;
	}
	public void setAudio_url(String audio_url) {
		this.audio_url = audio_url;
	}
	public int getAudio_time() {
		return audio_time;
	}
	public void setAudio_time(int audio_time) {
		this.audio_time = audio_time;
	}
	public long getAudio_size() {
		return audio_size;
	}
	public void setAudio_size(long audio_size) {
		this.audio_size = audio_size;
	}
	public String getNarrator_nick() {
		return narrator_nick;
	}
	public void setNarrator_nick(String narrator_nick) {
		this.narrator_nick = narrator_nick;
	}
	public String getNarrator_sex() {
		return narrator_sex;
	}
	public void setNarrator_sex(String narrator_sex) {
		this.narrator_sex = narrator_sex;
	}
	public String getNarrator_headurl() {
		return narrator_headurl;
	}
	public void setNarrator_headurl(String narrator_headurl) {
		this.narrator_headurl = narrator_headurl;
	}
	public String getSupplier_type() {
		return supplier_type;
	}
	public void setSupplier_type(String supplier_type) {
		this.supplier_type = supplier_type;
	}
	public Timestamp getDataChange_LastTime() {
		return DataChange_LastTime;
	}
	public void setDataChange_LastTime(Timestamp dataChange_LastTime) {
		DataChange_LastTime = dataChange_LastTime;
	}
	public Timestamp getDataChange_CreateTime() {
		return DataChange_CreateTime;
	}
	public void setDataChange_CreateTime(Timestamp dataChange_CreateTime) {
		DataChange_CreateTime = dataChange_CreateTime;
	}
	public String getIsDeleted() {
		return IsDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		IsDeleted = isDeleted;
	}
	public String getS_city_price() {
		return s_city_price;
	}
	public void setS_city_price(String s_city_price) {
		this.s_city_price = s_city_price;
	}
	public String getKey_site() {
		return key_site;
	}
	public void setKey_site(String key_site) {
		this.key_site = key_site;
	}
	public String getSite_level() {
		return site_level;
	}
	public void setSite_level(String site_level) {
		this.site_level = site_level;
	}
	public String getIsEffective() {
		return IsEffective;
	}
	public void setIsEffective(String isEffective) {
		IsEffective = isEffective;
	}
	public String getIsInChina() {
		return IsInChina;
	}
	public void setIsInChina(String isInChina) {
		IsInChina = isInChina;
	}
	@Override
	public String toString() {
		return "CSPoiMappingLocal [districtId=" + districtId + ", poiId="
				+ poiId + ", c_city_chname=" + c_city_chname
				+ ", c_city_enname=" + c_city_enname + ", c_site_chname="
				+ c_site_chname + ", c_site_enname=" + c_site_enname
				+ ", c_site_localname=" + c_site_localname + ", c_isnewmodel="
				+ c_isnewmodel + ", c_site_phone=" + c_site_phone
				+ ", c_site_address=" + c_site_address + ", matchscore="
				+ matchscore + ", child_name=" + child_name
				+ ", s_city_chname=" + s_city_chname + ", s_city_enname="
				+ s_city_enname + ", s_site_chname=" + s_site_chname
				+ ", s_site_enname=" + s_site_enname + ", s_site_localname="
				+ s_site_localname + ", s_ishot=" + s_ishot + ", s_site_phone="
				+ s_site_phone + ", s_site_address=" + s_site_address
				+ ", audio_filename=" + audio_filename + ", audio_text="
				+ audio_text + ", audio_url=" + audio_url + ", audio_time="
				+ audio_time + ", audio_size=" + audio_size
				+ ", narrator_nick=" + narrator_nick + ", narrator_sex="
				+ narrator_sex + ", narrator_headurl=" + narrator_headurl
				+ ", supplier_type=" + supplier_type + ", DataChange_LastTime="
				+ DataChange_LastTime + ", DataChange_CreateTime="
				+ DataChange_CreateTime + ", IsDeleted=" + IsDeleted
				+ ", s_city_price=" + s_city_price + ", key_site=" + key_site
				+ ", site_level=" + site_level + ", IsEffective=" + IsEffective
				+ ", IsInChina=" + IsInChina + "]";
	}
	
}
