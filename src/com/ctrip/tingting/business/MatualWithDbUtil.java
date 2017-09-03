package com.ctrip.tingting.business;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ctrip.tingting.entity.CSPoiMapping;
import com.ctrip.tingting.entity.CSPoiMappingLocal;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

/**
 * 与数据库交互的工具类
 * @author wang.donga
 *
 */
public class MatualWithDbUtil {

	private static Connection connection = null;
	private static PreparedStatement ps = null;
	//本地数据库连接信息
	private static final String url = "jdbc:mysql://localhost:3306/tingting?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8";
	private static final String driverName = "com.mysql.jdbc.Driver";
	private static final String username = "root";
	private static final String password = "root";
	//dev环境的数据库连接信息
	/*
	private static final String url = "jdbc:mysql://pub.mysql.db.dev.sh.ctripcorp.com:28747/htlvoicenavigationdb?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8";
	private static final String driverName = "com.mysql.jdbc.Driver";
	private static final String username = "us_dev_wangdong";
	private static final String password = "Wang@123";
	*/
	/**
	 * 向数据库插入数据
	 * @param mapping
	 * @return
	 */
	public int insertCSMappingData(CSPoiMapping mapping){
		int insertCount = 0;
		try {
			connection = getConnection();//获取数据库连接
			StringBuffer sql = new StringBuffer();
			sql.append("insert into c_s_poimapping (ch_name,supplier_type,audio_time,audio_size,");
			sql.append("narrator_nick,narrator_sex,narrator_headurl,audio_filename,audio_url,DataChange_CreateTime,");
			sql.append("DataChange_LastTime,IsDeleted,poiid,key_site,child_name,districtId,c_poiname,matchscore) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps = (PreparedStatement) connection.prepareStatement(sql.toString());
			ps.setString(1, mapping.getCh_name());
			ps.setString(2, mapping.getSupplier_type());
			ps.setLong(3, mapping.getAudio_time());
			ps.setLong(4, mapping.getAudio_size());
			ps.setString(5, mapping.getNarrator_nick());
			ps.setString(6, mapping.getNarrator_sex());
			ps.setString(7, mapping.getNarrator_headurl());
			ps.setString(8, mapping.getAudio_filename());
			ps.setString(9, mapping.getAudio_url());
			ps.setString(10, mapping.getDataChange_CreateTime());
			ps.setString(11, mapping.getDataChange_LastTime());
			ps.setString(12, mapping.getIsDeleted());
			ps.setLong(13, mapping.getPoiId());
			ps.setString(14, mapping.getKey_site());
			ps.setString(15, mapping.getChild_name());
			ps.setLong(16, mapping.getDistrictId());
			ps.setString(17, mapping.getC_poiname());
			ps.setInt(18, mapping.getMatchscore());
			insertCount = ps.executeUpdate();
			
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return insertCount;
	}
	
	/**
	 * 向本地数据库插入数据
	 * 本地数据库表名  local_mapping
	 * dev环境数据库表名  c_s_poimapping
	 * @param mapping
	 * @return   
	 */
	public int insertCSMappingDataLocal(CSPoiMappingLocal mapping){
		int insertCount = 0;
		try {
			connection = getConnection();//获取数据库连接
			StringBuffer sql = new StringBuffer();
			sql.append("insert into local_mapping (s_site_chname,supplier_type,audio_time,audio_size, ");
			sql.append("narrator_nick,narrator_sex,narrator_headurl,audio_filename,audio_url,DataChange_CreateTime, ");
			sql.append("DataChange_LastTime,IsDeleted,poiid,key_site,child_name,districtId,match_score, ");
			sql.append("c_city_chname,c_city_enname,c_site_chname,c_site_enname,c_site_localname,c_isnewmodel,c_site_phone,c_site_address, ");
			sql.append("s_site_phone,audio_text,s_site_enname,s_city_price,site_level,IsEffective,s_city_chname,IsInChina ) ");
			sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps = (PreparedStatement) connection.prepareStatement(sql.toString());
			ps.setString(1, mapping.getS_site_chname());
			ps.setString(2, mapping.getSupplier_type());
			ps.setLong(3, mapping.getAudio_time());
			ps.setLong(4, mapping.getAudio_size());
			ps.setString(5, mapping.getNarrator_nick());
			ps.setString(6, mapping.getNarrator_sex());
			ps.setString(7, mapping.getNarrator_headurl());
			ps.setString(8, mapping.getAudio_filename());
			ps.setString(9, mapping.getAudio_url());
			ps.setTimestamp(10, mapping.getDataChange_CreateTime());
			ps.setTimestamp(11, mapping.getDataChange_LastTime());
			ps.setString(12, mapping.getIsDeleted());
			ps.setLong(13, mapping.getPoiId());
			ps.setString(14, mapping.getKey_site());
			ps.setString(15, mapping.getChild_name());
			ps.setLong(16, mapping.getDistrictId());
			ps.setInt(17, mapping.getMatchscore());
			ps.setString(18,mapping.getC_city_chname());
			ps.setString(19,mapping.getC_city_enname());
			ps.setString(20,mapping.getC_site_chname());
			ps.setString(21,mapping.getC_site_enname());
			ps.setString(22,mapping.getC_site_localname());
			ps.setString(23,mapping.getC_isnewmodel());
			ps.setString(24,mapping.getC_site_phone());
			ps.setString(25,mapping.getC_site_address());
			ps.setString(26,mapping.getS_site_phone());
			ps.setString(27,mapping.getAudio_text());
			ps.setString(28,mapping.getS_site_enname());
			ps.setString(29,mapping.getS_city_price());
			ps.setString(30,mapping.getSite_level());
			ps.setString(31,mapping.getIsEffective());
			ps.setString(32,mapping.getS_city_chname());
			ps.setString(33,mapping.getIsInChina());
			insertCount = ps.executeUpdate();
			
			ps.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return insertCount;
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 * @throws Exception
	 */
	private static Connection getConnection() throws Exception{
		Class.forName(driverName);
		Connection connection = (Connection) DriverManager.getConnection(url, username, password);
		return connection;
	}
}
