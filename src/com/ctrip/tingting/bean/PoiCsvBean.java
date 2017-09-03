package com.ctrip.tingting.bean;

public class PoiCsvBean {

	
	public PoiCsvBean() {}
	private String districtid;
	private String poiid;
	private String countryname;
	private String countryenname;
	private String cityname;
	private String cityenname;
	private String poiname;
	private String ename;
	private String localname;
	private String blon;
	private String blat;
	private String glon;
	private String glat;
	private String isInChina;
	private String poiaddress;
	private String phone;
	private String isNewModel;
	
	public String getCountryenname() {
		return countryenname;
	}
	public void setCountryenname(String countryenname) {
		this.countryenname = countryenname;
	}
	public String getCityenname() {
		return cityenname;
	}
	public void setCityenname(String cityenname) {
		this.cityenname = cityenname;
	}
	public String getLocalname() {
		return localname;
	}
	public void setLocalname(String localname) {
		this.localname = localname;
	}
	public String getIsNewModel() {
		return isNewModel;
	}
	public void setIsNewModel(String isNewModel) {
		this.isNewModel = isNewModel;
	}
	public String getPoiaddress() {
		return poiaddress;
	}
	public void setPoiaddress(String poiaddress) {
		this.poiaddress = poiaddress;
	}
	public String getDistrictid() {
		return districtid;
	}
	public void setDistrictid(String districtid) {
		this.districtid = districtid;
	}
	public String getPoiid() {
		return poiid;
	}
	public void setPoiid(String poiid) {
		this.poiid = poiid;
	}
	public String getCountryname() {
		return countryname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getPoiname() {
		return poiname;
	}
	public void setPoiname(String poiname) {
		this.poiname = poiname;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getBlat() {
		return blat;
	}
	public void setBlat(String blat) {
		this.blat = blat;
	}
	public String getBlon() {
		return blon;
	}
	public void setBlon(String blon) {
		this.blon = blon;
	}
	public String getGlat() {
		return glat;
	}
	public void setGlat(String glat) {
		this.glat = glat;
	}
	public String getGlon() {
		return glon;
	}
	public void setGlon(String glon) {
		this.glon = glon;
	}
	public String getIsInChina() {
		return isInChina;
	}
	public void setIsInChina(String isInChina) {
		this.isInChina = isInChina;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString() {
		return "PoiCsvBean [districtid=" + districtid + ", poiid=" + poiid
				+ ", countryname=" + countryname + ", countryenname="
				+ countryenname + ", cityname=" + cityname + ", cityenname="
				+ cityenname + ", poiname=" + poiname + ", ename=" + ename
				+ ", localname=" + localname + ", blat=" + blat + ", blon="
				+ blon + ", glat=" + glat + ", glon=" + glon + ", isInChina="
				+ isInChina + ", phone=" + phone + ", isNewModel=" + isNewModel
				+ ", poiaddress=" + poiaddress + "]";
	}
	
	
}
