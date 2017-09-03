package com.ctrip.tingting.bean;

/**
 * 用于数据匹配的携程poi对象
 * Created by wang.donga on 2017/8/14.
 */
public class MCtripPoi {

    public MCtripPoi(){}
    private Long poiID;
    private String chName;//中文景点名称
    private String enName;//英文景点名称
    private String localName;//当地语言景点名称
    private String destinationName;//所属目的地名称（城市级别-中文）
    private String bdLT;//百度经纬度
    private String gdLT;//高德经纬度
    private String chAddress;//中文景点地址
    private String phone;//联系电话
    private String cityName;//所在城市名称
    private String countryName;//所属国家名称
    private String countryEnName;//所在国家英文名
    private boolean isInChina;//判断是否国内
    private String cityEnName;//城市英文名
	private boolean isNewModel;//是否使用前台新模板
	public Long getPoiID() {
        return poiID;
    }
    public void setPoiID(Long poiID) {
        this.poiID = poiID;
    }
    public String getChName() {
        return chName;
    }
    public void setChName(String chName) {
        this.chName = chName;
    }
    public String getEnName() {
        return enName;
    }
    public void setEnName(String enName) {
        this.enName = enName;
    }
    public String getLocalName() {
        return localName;
    }
    public void setLocalName(String localName) {
        this.localName = localName;
    }
    public String getDestinationName() {
        return destinationName;
    }
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
    public String getBdLT() {
        return bdLT;
    }
    public void setBdLT(String bdLT) {
        this.bdLT = bdLT;
    }
    public String getGdLT() {
        return gdLT;
    }
    public void setGdLT(String gdLT) {
        this.gdLT = gdLT;
    }
    public String getChAddress() {
        return chAddress;
    }
    public void setChAddress(String chAddress) {
        this.chAddress = chAddress;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    public String getCountryEnName() {
		return countryEnName;
	}
	public void setCountryEnName(String countryEnName) {
		this.countryEnName = countryEnName;
	}
    public boolean isInChina() {
        return isInChina;
    }
    public void setInChina(boolean isInChina) {
        this.isInChina = isInChina;
    }
    public String getCityEnName() {
		return cityEnName;
	}
	public void setCityEnName(String cityEnName) {
		this.cityEnName = cityEnName;
	}
	public boolean isNewModel() {
		return isNewModel;
	}
	public void setNewModel(boolean isNewModel) {
		this.isNewModel = isNewModel;
	}
    @Override
    public String toString() {
        return "CtripPOI [poiID=" + poiID + ", cityName=" + cityName
                + ", chName=" + chName + ", enName=" + enName + ", localName="
                + localName + ", destinationName=" + destinationName
                + ", bdLT=" + bdLT + ", gdLT=" + gdLT + ", chAddress="
                + chAddress + ", phone=" + phone + ", countryName="
                + countryName + ", isInChina=" + isInChina + ", cityEnName=" + cityEnName
                + ", isNewModel=" + isNewModel +"]";
    }
    public MCtripPoi(Long poiID, String cityName, String countryName ,String chName, String enName,
                    String localName, String destinationName, String bdLT, String gdLT,
                    String chAddress, String phone, boolean isInChina) {
        super();
        this.poiID = poiID;
        this.cityName = cityName;
        this.countryName = countryName;
        this.chName = chName;
        this.enName = enName;
        this.localName = localName;
        this.destinationName = destinationName;
        this.bdLT = bdLT;
        this.gdLT = gdLT;
        this.chAddress = chAddress;
        this.phone = phone;
        this.isInChina = isInChina;
    }

}
