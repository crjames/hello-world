package com.ctrip.tingting.business;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

import com.ctrip.tingting.bean.MCtripPoi;
import com.ctrip.tingting.bean.PoiCsvBean;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FindDataFromRedisUtil {

	/**
	 * 通过districtId获取对应的poi集合
	 * @param districtId
	 * @return
	 */
	public List<MCtripPoi> findDataFromRedisByDistrictId(long districtId){
		List<MCtripPoi> result = new ArrayList<MCtripPoi>();
		ObjectMapper mapper = new ObjectMapper();
		MCtripPoi mCtripPoi = null;
		try {
			Jedis jedis = new Jedis("localhost");//本地redis的连接
			String mapper_str = jedis.get(String.valueOf(districtId));//从redis中获取districtId对应的poi集合字符串
			System.out.println(mapper_str);
			List<LinkedHashMap> parserresult = mapper.readValue(mapper_str, ArrayList.class);
			for(int i=0;i<parserresult.size();i++){
				LinkedHashMap lkm = parserresult.get(i);
				PoiCsvBean p = (PoiCsvBean) mapToObject(lkm, PoiCsvBean.class);
				if(p != null){
					mCtripPoi = new MCtripPoi();
					mCtripPoi.setPoiID(Long.valueOf(p.getPoiid()));
					mCtripPoi.setChName(p.getPoiname());//poi中文名称
					mCtripPoi.setEnName(p.getEname());//poi英文名称
					mCtripPoi.setLocalName(p.getLocalname());//poi当地语言名称
					mCtripPoi.setCountryName(p.getCountryname());//poi所在国家名称
					mCtripPoi.setCountryEnName(p.getCountryenname());
					mCtripPoi.setCityName(p.getCityname());//poi所在城市名称
					mCtripPoi.setCityEnName(p.getCityenname());
					mCtripPoi.setChAddress(p.getPoiaddress());
					if(p.getIsNewModel()!=null && !"".equals(p.getIsNewModel())){
						if("1".equals(p.getIsNewModel())){
							mCtripPoi.setNewModel(true);
						}else{
							mCtripPoi.setNewModel(false);
						}
					}
					
					//mCtripPoi.setChAddress();
					if(p.getIsInChina()!=null && !"".equals(p.getIsInChina())){
						if("1".equals(p.getIsInChina())){
							mCtripPoi.setInChina(true);//是否在国内
						}else{
							mCtripPoi.setInChina(false);//是否在国内
						}
					}
					mCtripPoi.setPhone(p.getPhone());//poi联系电话
					mCtripPoi.setBdLT(p.getBlat()+","+p.getBlon());//百度纬度经度
					mCtripPoi.setGdLT(p.getGlat()+","+p.getGlon());//高德纬度经度
					
					result.add(mCtripPoi);
				}
				System.out.println(p.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	/**
	 * map转换为bean对象
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws  Exception{
		if (map == null) return null;    
  
        Object obj = beanClass.newInstance();  
  
        Field[] fields = obj.getClass().getDeclaredFields();   
        for (Field field : fields) {    
            int mod = field.getModifiers();    
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){    
                continue;    
            }    
  
            field.setAccessible(true);    
            field.set(obj, map.get(field.getName()));   
        }   
  
        return obj;   
	}

	/**
	 * 获取所有的districtId
	 * @return
	 */
	public List<Long> findAllDistrictId() {
		List<Long> result = new ArrayList<Long>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			Jedis jedis = new Jedis("localhost");//本地redis的连接
			String mapper_str = jedis.get("you_districId_list");//从redis中获取所有districtId
			List<String> list_str = mapper.readValue(mapper_str, ArrayList.class);
			if(list_str!=null && list_str.size()>0){
				for (String districtId_str : list_str) {
					result.add(Long.parseLong(districtId_str));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	public static void main(String[] args) {
		FindDataFromRedisUtil util = new FindDataFromRedisUtil();
		List<Long> result = util.findAllDistrictId();
		System.out.println(result.size());
		//System.out.println(result.toString());
	}
}
