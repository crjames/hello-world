package com.ctrip.tingting.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;

import com.csvreader.CsvReader;
import com.ctrip.tingting.bean.PoiCsvBean;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 读取csv文件数据的工具类，并将读取的数据存入本地redis
 * 需要导入javacsv.jar
 * 或者依赖Maven坐标：
 * <dependency>
 *   <groupId>net.sourceforge.javacsv</groupId>
 *   <artifactId>javacsv</artifactId>
 *   <version>2.0</version>
 * </dependency>
 * @author wang.donga
 *
 */
public class ReadFromCsvUtil {

	public static void main(String[] args) throws Exception {
		ReadFromCsvUtil csvUtil = new ReadFromCsvUtil();
		//读取csv文件获取数据    d:/Users/wang.donga/Desktop/口袋导游语音讲解/2017-08-23/poi信息.csv
		//List<String[]> list = csvUtil.readCsv("d:/Users/wang.donga/Desktop/口袋导游语音讲解/2017-08-16/2394156.csv");
		List<String[]> list = csvUtil.readCsv("d:/Users/wang.donga/Desktop/口袋导游语音讲解/2017-08-23/poi信息.csv");
		System.out.println(list.size());
		System.out.println(list.get(0).length);
		Map<String,List<PoiCsvBean>> map = new HashMap<String,List<PoiCsvBean>>();
		List<PoiCsvBean> sublist = null;
		PoiCsvBean poiCsvBean = null;
		if(list!=null && list.size()>0){
			//统计key（districtId）--value（poi集合）
			for (int i = 0; i < list.size(); i++) {
				if(!map.containsKey(list.get(i)[0])){
					poiCsvBean = csvUtil.ConstructPoiCsvBean(list.get(i));
					sublist = new ArrayList<PoiCsvBean>();
					sublist.add(poiCsvBean);
					map.put(list.get(i)[0], sublist);
				}else{
					poiCsvBean = csvUtil.ConstructPoiCsvBean(list.get(i));
					sublist = map.get(list.get(i)[0]);
					sublist.add(poiCsvBean);
					map.put(list.get(i)[0], sublist);
				}
			}
			System.out.println(map.size());
//			System.out.println(map.get("1744").size());
			//连接本地redis
			Jedis jedis = new Jedis("localhost");
//			jedis.set("who","hello");
//			System.out.println(jedis.get("who"));
			ObjectMapper mapper = new ObjectMapper();
			String mapper_str = "";//map中的value值序列化后的字符串
			//遍历map，将数据集合存入本地redis
			Set<String> keySet = map.keySet();
			List<String> districtId_list = new ArrayList<String>();
			for (String districtId : keySet) {
				sublist = map.get(districtId);
				mapper_str = mapper.writeValueAsString(sublist);
				jedis.set(districtId, mapper_str);
				districtId_list.add(districtId);
			}
			
			//将所有districtId作为value存入redis
			String districtIds = mapper.writeValueAsString(districtId_list);
			jedis.set("you_districId_list", districtIds);
		}
	}
	/**
	 * 构造PoiCsvBean对象
	 * @param data
	 * @return
	 */
	public PoiCsvBean ConstructPoiCsvBean(String[] data){
		PoiCsvBean bean = new PoiCsvBean();
		bean.setPoiid(data[1]);
		bean.setCountryname(data[2]);
		bean.setCountryenname(data[3]);
		bean.setCityname(data[4]);
		bean.setCityenname(data[5]);
		bean.setPoiname(data[6]);
		bean.setEname(data[7]);
		bean.setLocalname(data[8]);
		bean.setBlon(data[9]);
		bean.setBlat(data[10]);
		bean.setGlon(data[11]);
		bean.setGlat(data[12]);
		bean.setIsInChina(data[13]);
		bean.setPoiaddress(data[14]);
		bean.setPhone(data[15]);
		bean.setIsNewModel(data[16]);
		return bean;
	}
	/**
	 * 读取csv文件 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
    public List<String[]> readCsv(String filePath) throws Exception { 
        List<String[]> csvList = new ArrayList<String[]>(); 
        if (isCsv(filePath)) { 
            CsvReader reader = new CsvReader(filePath, ',', Charset.forName("UTF-8")); 
            reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。 
            while (reader.readRecord()) { //逐行读入除表头的数据 
                csvList.add(reader.getValues()); 
            } 
            reader.close(); 
        } else { 
            System.out.println("此文件不是CSV文件！"); 
        } 
        return csvList; 
    } 
    /**
     * 判断是否是csv文件 
     * @param fileName
     * @return
     */
    private boolean isCsv(String fileName) { 
        return fileName.matches("^.+\\.(?i)(csv)$"); 
    }
    
    @Test
    public void readPackageKeyFromCsv(){
    	ReadFromCsvUtil csvUtil = new ReadFromCsvUtil();
		Jedis jedis = new Jedis("localhost");
		//读取csv文件获取数据    d:/Users/wang.donga/Desktop/口袋导游语音讲解/2017-08-23/poi信息.csv
		//List<String[]> list = csvUtil.readCsv("d:/Users/wang.donga/Desktop/口袋导游语音讲解/2017-08-16/2394156.csv");
		try {
			List<String[]> list = csvUtil.readCsv("d:/Users/wang.donga/Desktop/口袋导游语音讲解/2017-08-24/package-key-all.csv");
			System.out.println("========="+list.size());
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					System.out.println("key="+list.get(i)[0]+";value="+list.get(i)[1]);
					jedis.set(list.get(i)[0], list.get(i)[1]);
				}
			}
		} catch (Exception e) {
			System.out.println("读取口袋城市对应key的csv文件，存入本地redis异常："+e.getMessage());
		}
    }
    
    @Test
    public void testGet(){
    	Jedis jedis = new Jedis("localhost");
    	System.out.println(jedis.get("重庆"));
    }
    
}
