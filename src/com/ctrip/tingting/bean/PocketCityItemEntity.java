package com.ctrip.tingting.bean;

/**
 * 调用口袋http://ctrip.api.kddaoyou.com/api/city/[城市key] 接口获取的返回对象中list的子对象
 * Created by wang.donga on 2017/8/14.
 */
public class PocketCityItemEntity {
    private String KEY;//景点唯一标示号
    private String TITLE;//景点名称
    private String TITLE_EN;//景点英文名称
    private int COUNT_SCENE;//景点内讲解个数
    private String IS_HOT;//是否热门
    private String PIC;//景点图片小图320*320
    private String LATITUDE;// 经度 (该字段仅针对景点介绍提供)
	private String LONGITUDE;// 维度(该字段仅针对景点介绍提供)
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
    public int getCOUNT_SCENE() {
        return COUNT_SCENE;
    }
    public void setCOUNT_SCENE(int cOUNT_SCENE) {
        COUNT_SCENE = cOUNT_SCENE;
    }
    public String getIS_HOT() {
        return IS_HOT;
    }
    public void setIS_HOT(String iS_HOT) {
        IS_HOT = iS_HOT;
    }
    public String getPIC() {
        return PIC;
    }
    public void setPIC(String pIC) {
        PIC = pIC;
    }
    public String getLATITUDE() {
		return LATITUDE;
	}
	public void setLATITUDE(String lATITUDE) {
		LATITUDE = lATITUDE;
	}
	public String getLONGITUDE() {
		return LONGITUDE;
	}
	public void setLONGITUDE(String lONGITUDE) {
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
		return "PocketCityItemEntity [KEY=" + KEY + ", TITLE=" + TITLE
				+ ", TITLE_EN=" + TITLE_EN + ", COUNT_SCENE=" + COUNT_SCENE
				+ ", IS_HOT=" + IS_HOT + ", PIC=" + PIC + ", LATITUDE="
				+ LATITUDE + ", LONGITUDE=" + LONGITUDE + ", TEXT=" + TEXT
				+ "]";
	}

}
