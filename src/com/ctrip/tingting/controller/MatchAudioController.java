package com.ctrip.tingting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;

import com.ctrip.tingting.bean.MCtripPoi;
import com.ctrip.tingting.business.FindDataFromRedisUtil;
import com.ctrip.tingting.business.MatchAudioBusiness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * 匹配供应商音频数据
 * Created by wang.donga on 2017/8/14.
 */
@Controller
@ResponseBody
@RequestMapping(value="/match")
public class MatchAudioController {

    /**
     * 与供应商的音频数据进行匹配
     * @param matchId        districtId/poiId
     * @param matchType      1-districtId;2-poiId;
     * @param supplierType  2017001-口袋;2017002-链景;2017003美景;
     * @return
     * @throws Exception 
     */
	
	@RequestMapping(value="/audio/{matchId}/{matchType}/{supplierType}",method= RequestMethod.GET)
    @ResponseBody
    public String syncAudioData(@PathVariable int matchId,
                              @PathVariable int matchType,
                              @PathVariable long supplierType){
		FindDataFromRedisUtil fromRedisUtil = new FindDataFromRedisUtil();
        String result = "";
        /*if(matchType == 1){
        	List<MCtripPoi> poiList = fromRedisUtil.findDataFromRedisByDistrictId(matchId);
            //根据matchId调用攻略服务获取当前matchId对应的目的地详情
            SightPoiDetail district = MGlobalPoiClientSoa.getDistrictDetailByDistrictId(matchId);
            if(district != null){
                //根据matchId调用攻略服务获取当前matchId下关联的poi集合poi_list
                List<MCtripPoi> poiList = MGlobalPoiClientSoa.getPoiListByDistricId(matchId);
                //遍历poi_list，获取poi对象的详情
                if(poiList!=null && poiList.size()>0){
                    for(MCtripPoi poi : poiList){
                        //poi所属城市名称
                        poi.setCityName(district.getName());
                        //调用MatchAudioBusiness的方法
                        MatchAudioBusiness.matchAudioData(poi,supplierType);
                    }
                }
            }
        }else if(matchType == 2){
            //调用攻略服务获取poi对象详情
            MCtripPoi poi = MGlobalPoiClientSoa.getPoiDetailByPoiId(matchId);
            if(poi != null){
                //调用MatchAudioBusiness的方法
                MatchAudioBusiness.matchAudioData(poi,supplierType);
            }
        }else{
            response.setMessage("matchType参数不合法！");
        }*/
        return result;
    }
    

	/*
    public static void main(String[] args){
        int matchId = 308;
        int matchType = 1;
        long supplierType = 2017002;
        if(matchType == 1){
            //根据matchId调用攻略服务获取当前matchId对应的目的地详情
            SightPoiDetail district = MGlobalPoiClientSoa.getDistrictDetailByDistrictId(matchId);
            if(district != null){
                //根据matchId调用攻略服务获取当前matchId下关联的poi集合poi_list
                List<MCtripPoi> poiList = MGlobalPoiClientSoa.getPoiListByDistricId(matchId);
                System.out.println(poiList.size());
                //遍历poi_list，获取poi对象的详情
                if(poiList!=null && poiList.size()>0){
                    for(MCtripPoi poi : poiList){
                        //poi所属城市名称
                        poi.setCityName(district.getName());
                        //调用MatchAudioBusiness的方法
                        MatchAudioBusiness.matchAudioData(poi,supplierType);
                    }
                    MCtripPoi poi = poiList.get(0);
                    poi.setCityName(district.getName());
                    //调用MatchAudioBusiness的方法
                    MatchAudioBusiness.matchAudioData(poi,supplierType);
                }
            }
        }else if(matchType == 2){
            //调用攻略服务获取poi对象详情
            MCtripPoi poi = MGlobalPoiClientSoa.getPoiDetailByPoiId(matchId);
            if(poi != null){
                //调用MatchAudioBusiness的方法
                MatchAudioBusiness.matchAudioData(poi,supplierType);
            }
        }
    }*/
	
	/**
     * 与供应商的音频数据进行匹配
     * @param matchId        districtId
     * @param supplierType  2017001-口袋;2017002-链景;2017003美景;
     * @return
     * @throws Exception 
     */
	public static void main(String[] args) throws Exception {
		FindDataFromRedisUtil fromRedisUtil = new FindDataFromRedisUtil();
		
		long start = System.currentTimeMillis();
		long matchId = 0;//districtId
        long supplierType = 2017002;//对应供应商
        List<Long> districtId_list = fromRedisUtil.findAllDistrictId();
        //List<Long> districtId_list = new ArrayList<Long>();
        //districtId_list.add(308L);
        //districtId_list.add(2L);
        //districtId_list.add(39L);
        //districtId_list.add(38L);
        //districtId_list.add(53L);
        int matchtotal =0;
        if(districtId_list!=null && districtId_list.size()>0){
        	for (Long id : districtId_list) {
        		long start_in = System.currentTimeMillis();
        		matchId = id;
        		List<MCtripPoi> poiList = fromRedisUtil.findDataFromRedisByDistrictId(matchId);
                int matchCount = 0;
                //遍历poi_list，获取poi对象的详情
                if(poiList!=null && poiList.size()>0){
                    for(MCtripPoi poi : poiList){
                        //调用MatchAudioBusiness的方法    && "巴黎".equals(poi.getCityName())
                    	if(poi.getChName()!=null && !"".equals(poi.getChName()) ){
                    		//MatchAudioBusiness.matchAudioData(poi,supplierType,matchId);//
                    		//System.out.println("poi:BLT="+poi.getBdLT()+";GLT="+poi.getGdLT());
                    		MatchAudioBusiness.matchAudioDataLocal(poi,supplierType,matchId);//为产品输出所需格式的数据2017-08-22
                    		matchCount++;
                    	}else if(poi.getEnName()!=null && !"".equals(poi.getEnName()) ){
                    		MatchAudioBusiness.matchAudioDataLocal(poi,supplierType,matchId);//为产品输出所需格式的数据2017-08-22
                    		matchCount++;
                    	}
                    }
                }
                long time_consume = System.currentTimeMillis()-start_in;
                System.out.println("本次匹配districtId:"+matchId+",耗时"+time_consume+";poi总个数:"+poiList.size()+";");
                matchtotal++;
			}
        }
        long time_total = System.currentTimeMillis()-start;
        System.out.println("本次共计匹配districtId:"+matchtotal+"个,耗时"+time_total+";");
	}
}
