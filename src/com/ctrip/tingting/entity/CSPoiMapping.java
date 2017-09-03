package com.ctrip.tingting.entity;

import java.util.Date;

/**
 * 对应数据库tingting表c_s_poimapping的实体对象
 * @author wang.donga
 *
 */
public class CSPoiMapping {

	private long districtId;
	private long poiId;
	private String c_poiname="";//携程景点中文名称
	private String key_site = "";//口袋对应的景点key
	private String ch_name = "";//poi中文名称
	private int matchscore = 0;//匹配得分
	private String child_name = "";//子景点名称
	private String audio_filename = "";//音频文件名称
	private String audio_url = "";//音频地址
	private int audio_time;//音频时长
	private long audio_size;//音频大小
	private String narrator_nick = "";//音频解说昵称
	private String narrator_sex = "";//音频解说性别
	private String narrator_headurl = "";//音频解说头像url
	private String supplier_type = "";//供应商类别
	private String DataChange_LastTime;//最后修改时间
	private String DataChange_CreateTime;//创建时间
	private String IsDeleted = "";//是否已删除
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
	public String getC_poiname() {
		return c_poiname;
	}
	public void setC_poiname(String c_poiname) {
		this.c_poiname = c_poiname;
	}
	public String getKey_site() {
		return key_site;
	}
	public void setKey_site(String key_site) {
		this.key_site = key_site;
	}
	public String getCh_name() {
		return ch_name;
	}
	public void setCh_name(String ch_name) {
		this.ch_name = ch_name;
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
	public String getAudio_filename() {
		return audio_filename;
	}
	public void setAudio_filename(String audio_filename) {
		this.audio_filename = audio_filename;
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
	public String getDataChange_LastTime() {
		return DataChange_LastTime;
	}
	public void setDataChange_LastTime(String dataChange_LastTime) {
		DataChange_LastTime = dataChange_LastTime;
	}
	public String getDataChange_CreateTime() {
		return DataChange_CreateTime;
	}
	public void setDataChange_CreateTime(String dataChange_CreateTime) {
		DataChange_CreateTime = dataChange_CreateTime;
	}
	public String getIsDeleted() {
		return IsDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		IsDeleted = isDeleted;
	}
	@Override
	public String toString() {
		return "CSPoiMapping [districtId=" + districtId + ", poiId=" + poiId + ", c_poiname=" + c_poiname
				+ ", key_site=" + key_site + ", matchscore=" + matchscore
				+ ", ch_name=" + ch_name + ", child_name=" + child_name + ", audio_filename=" + audio_filename
				+ ", audio_url=" + audio_url + ", audio_time=" + audio_time
				+ ", audio_size=" + audio_size + ", narrator_nick="
				+ narrator_nick + ", narrator_sex=" + narrator_sex
				+ ", narrator_headurl=" + narrator_headurl + ", supplier_type="
				+ supplier_type + ", DataChange_LastTime="
				+ DataChange_LastTime + ", DataChange_CreateTime="
				+ DataChange_CreateTime + ", IsDeleted=" + IsDeleted + "]";
	}
	
}
