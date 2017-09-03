package com.ctrip.tingting.bean;

public class PocketSceneEntity {

	
	public PocketSceneEntity() {}
    private String TITLE;
    private String TITLE_EN;
    private String PIC;
    private String AUDIO;
    private String SITE_TITLE;
    private String SITE_TITLE_EN;
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}
	public String getTITLE_EN() {
		return TITLE_EN;
	}
	public void setTITLE_EN(String tITLE_EN) {
		TITLE_EN = tITLE_EN;
	}
	public String getPIC() {
		return PIC;
	}
	public void setPIC(String pIC) {
		PIC = pIC;
	}
	public String getAUDIO() {
		return AUDIO;
	}
	public void setAUDIO(String aUDIO) {
		AUDIO = aUDIO;
	}
	public String getSITE_TITLE() {
		return SITE_TITLE;
	}
	public void setSITE_TITLE(String sITE_TITLE) {
		SITE_TITLE = sITE_TITLE;
	}
	public String getSITE_TITLE_EN() {
		return SITE_TITLE_EN;
	}
	public void setSITE_TITLE_EN(String sITE_TITLE_EN) {
		SITE_TITLE_EN = sITE_TITLE_EN;
	}
	@Override
	public String toString() {
		return "PocketSceneEntity [TITLE=" + TITLE
				+ ", TITLE_EN=" + TITLE_EN + ", PIC=" + PIC + ", AUDIO="
				+ AUDIO + ", SITE_TITLE=" + SITE_TITLE + ", SITE_TITLE_EN="
				+ SITE_TITLE_EN + "]";
	}
}
