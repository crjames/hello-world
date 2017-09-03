package com.ctrip.tingting.bean;

/**
 * 调用口袋供应商http://ctrip.api.kddaoyou.com/api/site/[景点key] 返回的对象
 * Created by wang.donga on 2017/8/14.
 */
public class PocketSiteEntity {
    private String KEY;
    private String TITLE;
    private String TITLE_EN;
    private String PIC;
    private String AUDIO;
    private String FEATURED;
    private Object LATITUDE;// 经度 (该字段仅针对景点介绍提供)
	private Object LONGITUDE;// 维度(该字段仅针对景点介绍提供)
	private String TEXT;// 文字介绍(该字段仅针对景点介绍提供)

	public String getKEY() {
        return KEY;
    }
    public void setKEY(String kEY) {
        KEY = kEY;
    }
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
    public String getFEATURED() {
        return FEATURED;
    }
    public void setFEATURED(String fEATURED) {
        FEATURED = fEATURED;
    }
    public Object getLATITUDE() {
		return LATITUDE;
	}
	public void setLATITUDE(Object lATITUDE) {
		LATITUDE = lATITUDE;
	}
	public Object getLONGITUDE() {
		return LONGITUDE;
	}
	public void setLONGITUDE(Object lONGITUDE) {
		LONGITUDE = lONGITUDE;
	}
	public String getTEXT() {
		return TEXT;
	}
	public void setTEXT(String tEXT) {
		TEXT = tEXT;
	}
	@Override
	public String toString() {
		return "PocketSiteEntity [KEY=" + KEY + ", TITLE=" + TITLE
				+ ", TITLE_EN=" + TITLE_EN + ", PIC=" + PIC + ", AUDIO="
				+ AUDIO + ", FEATURED=" + FEATURED + ", LATITUDE=" + LATITUDE
				+ ", LONGITUDE=" + LONGITUDE + ", TEXT=" + TEXT + "]";
	}
    
}
