package com.ctrip.tingting.util;

import java.text.DecimalFormat;

public class PositionUtil {

	private static double pi = 3.1415926535897932384626;
	private static double a = 6378245.0;
	private static double ee = 0.00669342162296594323;
	private static DecimalFormat df = new DecimalFormat("###.0000000000");
	
	/** 
     * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System 
     * 谷歌坐标 转 高德坐标
     * @param lat 
     * @param lon 
     * @return 
     */  
    public static String gps84_To_Gcj02(double lat, double lon) {
//        if (outOfChina(lat, lon)) {
//            return null;
//        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return (df.format(mgLat)+","+df.format(mgLon));
    }
    
    public static boolean outOfChina(double lat, double lon) {  
        if (lon < 72.004 || lon > 137.8347)  
            return true;  
        if (lat < 0.8293 || lat > 55.8271)  
            return true;  
        return false;  
    }
    
    public static double transformLat(double x, double y) {  
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y  
                + 0.2 * Math.sqrt(Math.abs(x));  
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;  
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;  
        return ret;  
    }  
  
    public static double transformLon(double x, double y) {  
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1  
                * Math.sqrt(Math.abs(x));  
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;  
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;  
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0  
                * pi)) * 2.0 / 3.0;  
        return ret;  
    }
    
    public static void main(String[] args) {
		String str = gps84_To_Gcj02(Double.parseDouble("48.8610092539475"),Double.parseDouble("2.33590364456177"));//卢浮宫
		String str2 = gps84_To_Gcj02(Double.parseDouble("48.8582775896404"),Double.parseDouble("2.29442596435547"));//埃菲尔铁塔
		System.out.println(str);
		System.out.println(str2);
	}
}
