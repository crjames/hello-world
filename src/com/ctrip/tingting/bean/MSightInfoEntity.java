package com.ctrip.tingting.bean;

import java.util.Map;

public class MSightInfoEntity {

	private long id;
    private String name;
    private String address;
    private long coverImageId;
    private double bdlat;
    private double bdlon;
    private double gdlat;
    private double gdlon;
    private double gglat;
    private double gglon;
    private Map<String, String> imgUrl;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getCoverImageId() {
		return coverImageId;
	}
	public void setCoverImageId(long coverImageId) {
		this.coverImageId = coverImageId;
	}
	public double getBdlat() {
		return bdlat;
	}
	public void setBdlat(double bdlat) {
		this.bdlat = bdlat;
	}
	public double getBdlon() {
		return bdlon;
	}
	public void setBdlon(double bdlon) {
		this.bdlon = bdlon;
	}
	public double getGdlat() {
		return gdlat;
	}
	public void setGdlat(double gdlat) {
		this.gdlat = gdlat;
	}
	public double getGdlon() {
		return gdlon;
	}
	public void setGdlon(double gdlon) {
		this.gdlon = gdlon;
	}
	public double getGglat() {
		return gglat;
	}
	public void setGglat(double gglat) {
		this.gglat = gglat;
	}
	public double getGglon() {
		return gglon;
	}
	public void setGglon(double gglon) {
		this.gglon = gglon;
	}
	public Map<String, String> getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(Map<String, String> imgUrl) {
		this.imgUrl = imgUrl;
	}
}
