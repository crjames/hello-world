package com.ctrip.tingting.business;

import com.alibaba.fastjson.JSONObject;
import com.ctrip.tingting.bean.LJRequestParam;
import com.ctrip.tingting.bean.LJScenicEntity;
import com.ctrip.tingting.bean.LJScenicExplainEntity;
import com.ctrip.tingting.bean.LJScenicValueEntity;
import com.ctrip.tingting.bean.MCtripPoi;
import com.ctrip.tingting.bean.MSupplierPoi;
import com.ctrip.tingting.bean.PocketCityEntity;
import com.ctrip.tingting.bean.PocketCityItemEntity;
import com.ctrip.tingting.bean.PocketSceneEntity;
import com.ctrip.tingting.bean.PocketSiteEntity;
import com.ctrip.tingting.bean.UploadAudioResponseEntity;
import com.ctrip.tingting.entity.CSPoiMapping;
import com.ctrip.tingting.entity.CSPoiMappingLocal;
import com.ctrip.tingting.util.MD5Util;
import com.ctrip.tingting.util.PositionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 匹配下载上传供应商的音频数据
 * Created by wang.donga on 2017/8/14.
 */
public class MatchAudioBusiness {

    //链景的访问密钥
    private static final String secretkey = "b534f8a9dc1a4e7e8284f6dd65ab56d5";
    //链景的访问tag标识
    private static final String tag = "1501134762";
    
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static ObjectMapper mapper = new ObjectMapper();
    private static FindDataFromRedisUtil util = new FindDataFromRedisUtil();
    //连接本地redis
    private static Jedis jedis = new Jedis("localhost");
    
    @Test
    public void test(){
        String audioUrl = "https://op.lianjinglx.com/Explain.ashx?cmd=PlayExplain&p=%7b%22Eid%22%3a%2277708205%22%2c%22Code%22%3a%2277704314%22%2c%22Type%22%3a%220%22%2c%22Jump%22%3a%22%22%7d&lang=1&version=1.0&Sign=4fbd9b24b3a65122a788a44ec8356e20&Tag=1501134762&salt=1502866199";
        String uploadResult = MatchUploadAudioBusiness.uploadAudioToCtripServer(audioUrl);
        //将uploadResult转成响应对象，并获取返回的文件名和新地址
        UploadAudioResponseEntity responseEntity = convert2UploadAudioResponseEntity(uploadResult);
        System.out.println(responseEntity);
    }

    public static String matchAudioData(MCtripPoi poi , long supplierType , long districtId){
        MSupplierPoi mSupplierPoi = null;
        int matchScore = 0;
        CSPoiMapping cspoiMapping = null;
        try {
            if(supplierType == 2017001){
                //根据poi对象的所在城市名称对应口的key文件找到对应城市key
                String cityKey = "8d49d262-a1d3-11e6-9a1b-00163e0005c0";//TODO 暂时以巴黎为例
                //调用接口获取该城市下的景点列表
                String unicodeCityResult = getSupplierData(getPocketAccessUrl("city",cityKey));
                System.out.println("调用口袋获取巴黎数据："+unicodeCityResult);
                if(unicodeCityResult!=null && !"".equals(unicodeCityResult)){
                	PocketCityEntity city = convert2PocketCityItem(unicodeCityResult);
                	if(city != null){
                		List<PocketCityItemEntity> itemList = city.getSite_list();
                        System.out.println("口袋巴黎数据集合大小："+itemList.size());
                        if(itemList!=null && itemList.size()>0){
                        	//遍历景点列表，并与poi对象进行相似度匹配    MacthRuleUtil  方法返回相似度得分
                            for(PocketCityItemEntity item : itemList){
                            	System.out.println(item.toString());
                                mSupplierPoi = new MSupplierPoi();
                                mSupplierPoi.setChName(item.getTITLE());
                                mSupplierPoi.setEnName(item.getTITLE_EN());
                                mSupplierPoi.setSupplierType("2017001");
                                mSupplierPoi.setDestinationName(poi.getCityName());
                                //TODO 用上面第一步匹配的城市名称
                                matchScore = MatchRuleUtil.getMatchScore(poi,mSupplierPoi);
                                //匹配相似度得分符合要求，调用口袋接口根据匹配上的景点key查询相应的语音信息
                                if(matchScore >10){
                                    //根据景点key查询景点信息
                                    String unicodeSiteResult = getSupplierData(getPocketAccessUrl("site",item.getKEY()));
                                    List<PocketSiteEntity> siteList = convert2PocketSiteList(unicodeSiteResult);
                                    if(siteList!=null && siteList.size()>0){
                                    	System.out.println("siteList:"+siteList.size());
                                    	for (PocketSiteEntity site : siteList) {
                                    		cspoiMapping = new CSPoiMapping();
                                        	System.out.println("POI:"+poi.getPoiID()+";name:"+poi.getChName());
                                        	System.out.println("site="+site);
                                        	cspoiMapping.setDistrictId(districtId);
                                        	cspoiMapping.setPoiId(poi.getPoiID());
                                        	if(poi.getChName()!=null && !"".equals(poi.getChName())){
                                        		cspoiMapping.setC_poiname(poi.getChName());
                                            }else{
                                            	cspoiMapping.setC_poiname(poi.getEnName());
                                            }
                                        	cspoiMapping.setMatchscore(matchScore);
                                        	cspoiMapping.setCh_name(item.getTITLE());
                                        	cspoiMapping.setChild_name(site.getTITLE());
                                            cspoiMapping.setSupplier_type("2017001");
                                            cspoiMapping.setKey_site(site.getKEY());
                                        	System.out.println("开始上传音频=============");
                                            //根据返回的信息中的图片和音频的url进行下载并上传至携程服务器，获取返回字符串
                                        	uploadAndInsertDB(site.getAUDIO(),cspoiMapping);
                                    	}
                                    }
                                }
                            }
                        }
                	}
                }
            }else if(supplierType == 2017002){
            	//表示调用的链景的哪个接口
            	int type = 1;
            	String responseScenic = "";//调用接口返回的json数据
            	LJRequestParam ljRequestParam = new LJRequestParam();
                if(poi.isInChina()){
                    ljRequestParam.setArea(poi.getCityName());
                }else{
                    ljRequestParam.setArea(poi.getCountryName());
                }
                if(poi.getChName()!=null && !"".equals(poi.getChName())){
                	ljRequestParam.setName(poi.getChName());
                }else{
                	ljRequestParam.setName(poi.getEnName());
                }
            	if(type == 3){
                    //根据poi所在城市名称和poi名称，调用链景接口查询景区数据 value集合
                    responseScenic = getSupplierData(getLJAccessUrl(ljRequestParam,3));
                    LJScenicValueEntity scenicValue = null;
                    System.out.println("调用链景接口获取字符串数据："+responseScenic);
                    if(responseScenic!=null && !"".equals(responseScenic)){
                        scenicValue = convert2ScenicValue(responseScenic);
                    }
                    if(scenicValue!=null){
                    	cspoiMapping = new CSPoiMapping();
                    	System.out.println("POI:"+poi.getPoiID()+";name:"+poi.getChName());
                    	cspoiMapping.setDistrictId(districtId);
                    	cspoiMapping.setPoiId(poi.getPoiID());
                    	if(poi.getChName()!=null && !"".equals(poi.getChName())){
                    		cspoiMapping.setC_poiname(poi.getChName());
                        }else{
                        	cspoiMapping.setC_poiname(poi.getEnName());
                        }
                    	cspoiMapping.setMatchscore(100);
                    	cspoiMapping.setCh_name(scenicValue.getName());
                        cspoiMapping.setSupplier_type("2017002");
                        cspoiMapping.setAudio_time(Integer.valueOf(scenicValue.getDateTime()));
                        cspoiMapping.setAudio_size(Long.valueOf(scenicValue.getMp3Size()));
                        cspoiMapping.setNarrator_nick(scenicValue.getNickName());
                        cspoiMapping.setNarrator_sex(scenicValue.getSex());
                        cspoiMapping.setNarrator_headurl(scenicValue.getHeadImg());
                        //上传音频，并且入库
                        uploadAndInsertDB(scenicValue.getMp3Url(),cspoiMapping);
                    }
            	}else if(type == 1){
            		//根据poi所在城市名称和poi名称，调用链景接口查询景区数据 value集合
            		ljRequestParam.setPageIndex("1");
            		ljRequestParam.setPageSize("10");
                    responseScenic = getSupplierData(getLJAccessUrl(ljRequestParam,1));
                    List<LJScenicValueEntity> out_list = null;
                    List<LJScenicExplainEntity> in_list = null;
                    if(responseScenic!=null && !"".equals(responseScenic)){
                    	out_list = convert2ScenicValueList(responseScenic);
                    }
                    if(out_list!=null && out_list.size()>0){
                    	for(LJScenicValueEntity scenicValue : out_list){
                    		//根据匹配规则进行匹配 TODO
                    		mSupplierPoi = new MSupplierPoi();
                            mSupplierPoi.setChName(scenicValue.getName());
                            mSupplierPoi.setDestinationName(scenicValue.getAreaName());
                            mSupplierPoi.setSupplierType(supplierType+"");
                            mSupplierPoi.setGdLT(scenicValue.getLat()+","+scenicValue.getLon());
                            mSupplierPoi.setPhone(scenicValue.getTelephone());
                            //用上面第一步匹配的城市名称
                            matchScore = MatchRuleUtil.getMatchScore(poi,mSupplierPoi);
                            System.out.println("matchScore="+matchScore);
//                    		if(matchScore > 0){
                    			//step1 先将外层景区数据进行下载上传
                    			//构造cspoiMapping对象
                    			/*cspoiMapping = new CSPoiMapping();
                            	System.out.println("POI:"+poi.getPoiID()+";name:"+poi.getChName());
                            	cspoiMapping.setDistrictId(districtId);
                            	cspoiMapping.setPoiId(poi.getPoiID());
                            	if(poi.getChName()!=null && !"".equals(poi.getChName())){
                            		cspoiMapping.setC_poiname(poi.getChName());
                                }else{
                                	cspoiMapping.setC_poiname(poi.getEnName());
                                }
                            	cspoiMapping.setCh_name(scenicValue.getName());
                            	cspoiMapping.setMatchscore(matchScore);
                            	cspoiMapping.setChild_name(scenicValue.getName());
                                cspoiMapping.setSupplier_type("2017002");
                                cspoiMapping.setAudio_time(Integer.valueOf(scenicValue.getDateTime()));
                                cspoiMapping.setNarrator_nick(scenicValue.getNickName());
                                cspoiMapping.setNarrator_sex(scenicValue.getSex());
                                cspoiMapping.setNarrator_headurl(scenicValue.getHeadImg());
                    			//上传音频并数据入库
                                uploadAndInsertDB(scenicValue.getMp3Url(),cspoiMapping);
                    			*/
                    			//step2 根据poi根据标识获取景区讲解集合
                    			ljRequestParam.setCode(String.valueOf(scenicValue.getCode()));
                    			ljRequestParam.setUnix(String.valueOf(scenicValue.getUnix()));
                                responseScenic = getSupplierData(getLJAccessUrl(ljRequestParam,2));
                                if(responseScenic!=null && !"".equals(responseScenic)){
                                	in_list = convert2ScenicExplainList(responseScenic);
                                	if(in_list!=null && in_list.size()>0){
                                		for(LJScenicExplainEntity scenicExplain : in_list){
                                			//构造cspoiMapping对象
                                			cspoiMapping = new CSPoiMapping();
                                        	System.out.println("POI:"+poi.getPoiID()+";name:"+poi.getChName());
                                        	cspoiMapping.setDistrictId(districtId);
                                        	cspoiMapping.setPoiId(poi.getPoiID());
                                        	if(poi.getChName()!=null && !"".equals(poi.getChName())){
                                        		cspoiMapping.setC_poiname(poi.getChName());
                                            }else{
                                            	cspoiMapping.setC_poiname(poi.getEnName());
                                            }
                                        	cspoiMapping.setMatchscore(matchScore);
                                        	cspoiMapping.setCh_name(scenicValue.getName());
                                        	cspoiMapping.setChild_name(scenicExplain.getName());
                                            cspoiMapping.setSupplier_type("2017002");
                                            cspoiMapping.setAudio_time(Integer.valueOf(scenicExplain.getDateTime()));
                                            //cspoiMapping.setAudio_size(Long.valueOf(scenicValue.getMp3Size()));
                                            cspoiMapping.setNarrator_nick(scenicValue.getNickName());
                                            cspoiMapping.setNarrator_sex(scenicExplain.getSex());
                                            cspoiMapping.setNarrator_headurl(scenicExplain.getHeadImg());
                                			//上传音频并数据入库
                                            uploadAndInsertDB(scenicExplain.getMp3Url(),cspoiMapping);
                                		}
                                	}
                                }
//                    		}
                    	}
                    }
            	}

            }else if(supplierType == 2017003){

            }else{
                //return "供应商类型参数supplierType不合法！";
            	System.out.println("MatchAudioBusiness-matchAudioData方法：供应商类型参数supplierType不合法！");
            }
        }catch(Exception ex){
        	System.out.println("MatchAudioBusiness-matchAudioData方法异常:"+ex.getMessage());
        }
        return "";
    }
    
    /**
     * 上传音频，入库
     * @param mp3Url
     * @param cspoiMapping
     */
    public static void uploadAndInsertDB(String mp3Url,CSPoiMapping cspoiMapping){
        //根据返回的信息中的图片和音频的url进行下载并上传至携程服务器，获取返回字符串
        System.out.println("开始上传音频文件=================");
        //String uploadResult = MatchUploadAudioBusiness.uploadAudioToCtripServer(mp3Url);
        String uploadResult = "9090909090";
        System.out.println("上传音频至服务器返回的字符串："+uploadResult);
        if(uploadResult!=null && !"".equals(uploadResult)){
        	//将uploadResult转成响应对象，并获取返回的文件名和新地址
            //UploadAudioResponseEntity responseEntity = convert2UploadAudioResponseEntity(uploadResult);
            //cspoiMapping.setAudio_filename(responseEntity.getFile_name());
            //cspoiMapping.setAudio_url(responseEntity.getUrl());
            cspoiMapping.setAudio_filename("testtest20170821.mp3");
            cspoiMapping.setAudio_url("testtest20170821.mp3");
            cspoiMapping.setDataChange_CreateTime(formatter.format(new Date()));
            cspoiMapping.setDataChange_LastTime(cspoiMapping.getDataChange_CreateTime());
            cspoiMapping.setIsDeleted("0");
            //保存或者更新数据库信息 
            int insertCount = insertData(cspoiMapping);
            if(insertCount > 0){
            	System.out.println("数据保存成功！");
            }
        }
    }
    
    /**
     * 上传音频，入库
     * 为产品保存需要的数据新建本地表 2017-08-22
     * @param mp3Url
     * @param cspoiMapping
     */
    public static void uploadAndInsertDBLocal(String mp3Url,CSPoiMappingLocal cspoiMappingLocal,int matchScore){
        //根据返回的信息中的图片和音频的url进行下载并上传至携程服务器，获取返回字符串
        System.out.println("开始上传音频文件=================");
        //String uploadResult = MatchUploadAudioBusiness.uploadAudioToCtripServer(mp3Url);
        String uploadResult = "0000000000";
        //System.out.println("上传音频至服务器返回的字符串："+uploadResult);
        if(uploadResult!=null && !"".equals(uploadResult)){
        	//将uploadResult转成响应对象，并获取返回的文件名和新地址
            //UploadAudioResponseEntity responseEntity = convert2UploadAudioResponseEntity(uploadResult);
            //cspoiMappingLocal.setAudio_filename(responseEntity.getFile_name());
            //cspoiMappingLocal.setAudio_url(responseEntity.getUrl());
        	cspoiMappingLocal.setAudio_filename("testtest20170830.mp3");
        	cspoiMappingLocal.setAudio_url("testtest20170830.mp3");
        	cspoiMappingLocal.setDataChange_CreateTime(new Timestamp(new Date().getTime()));
        	cspoiMappingLocal.setDataChange_LastTime(cspoiMappingLocal.getDataChange_CreateTime());
        	if(matchScore==21 || matchScore==31 || matchScore==0 || matchScore==10){
        		cspoiMappingLocal.setIsEffective("0");
        	}else{
        		cspoiMappingLocal.setIsEffective("1");
        	}
        	cspoiMappingLocal.setIsDeleted("0");
            //保存或者更新数据库信息 
            int insertCount = insertDataLocal(cspoiMappingLocal);
            if(insertCount > 0){
            	System.out.println("数据保存成功！");
            }
        }
    }

    /**
     * 调用供应商接口获取数据
     * @param url
     * @return
     */
    private static String getSupplierData(String url){
        String result = "";
        BufferedReader in = null;
        try {
            //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("ntproxy.qa.nt.ctripcorp.com", 8080));
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();//.openConnection(proxy);
            conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setRequestProperty("Content-Type","application/json;charset=utf-8");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception ex) {
        	System.out.println("MatchAudioBusiness-getSupplierData异常:url="+url+";"+ex.getMessage());
        }finally {
            //关闭流
            try {
                if(in != null){
                    in.close();
                }
            }catch(Exception ex){
            	System.out.println("MatchAudioBusiness-getSupplierData关流异常："+ex.getMessage());
            }
        }
        return result;
    }
    /**
     * 获取链景供应商的获取数据URL
     * 获取景区实体（type==3）
     * @param params  请求参数，用于生成sign的参数
     * @param type    用来判断是哪个接口，获取params中的不同参数
     * @return
     */
    private static String getLJAccessUrl(LJRequestParam params , int type){
        //unix时间戳
        Long unix = System.currentTimeMillis()/1000L;
        //签名sign的获取
        String param = "";
        String sign = "";
        StringBuffer url = new StringBuffer();
        url.append("https://op.lianjinglx.com/scenic.ashx");
        try {
            if(type == 1){
                //获取景区数据
                url.append("?cmd=LLScenicList");
                param = params.getArea()+params.getName()+params.getPageIndex()+params.getPageSize()+unix+secretkey;
                url.append("&p={\"Area\":\""+params.getArea()+"\",\"Name\":\""+params.getName()+"\",\"PageIndex\":\""+params.getPageIndex()+"\",\"PageSize\":\""+params.getPageSize()+"\"}");
            }else if(type == 2){
                //根据标识获取景区讲解集合
                url.append("?cmd=ScenicExplain");
                param = params.getCode()+params.getUnix()+unix+secretkey;
                url.append("&p={\"Code\":\""+params.getCode()+"\",\"Unix\":\""+params.getUnix()+"\"}");
            }else if(type == 3){
                //获取景区实体
                url.append("?cmd=FindScenic");
                param = params.getArea() +params.getName()+unix+secretkey;
                System.out.println(param);
                url.append("&p={\"Area\":\""+params.getArea()+"\",\"Name\":\""+params.getName()+"\"}");
            }else if(type == 4){
                //获取景点数据
                url.append("?cmd=LLScenicSpotList");
                unix = System.currentTimeMillis()/1000L;
                param = params.getCode()+params.getUnix()+params.getPageIndex()+params.getPageSize()+unix+secretkey;
                url.append("&p={\"Code\":\""+params.getCode()+"\",\"Unix\":\""+params.getUnix()+"\",\"PageIndex\":\""+params.getPageIndex()+"\",\"PageSize\":\""+params.getPageSize()+"\"}");
            }else if(type == 5){
                //根据标识获取景点讲解集合
                url.append("?cmd=ScenicSpotExplain");
                param = params.getCode()+params.getUnix()+unix+secretkey;
                url.append("&p={\"Code\":\""+params.getCode()+"\",\"Unix\":\""+params.getUnix()+"\"}");
            }
            url.append("&salt="+unix);
            sign = MD5Util.MD5Encode(param);
            url.append("&sign="+sign);
            url.append("&tag="+tag);
            url.append("&lang=1&version=1.0");

        }catch(Exception ex){

        }
        System.out.println("访问链景url："+url.toString());
        return url.toString();
    }

    /**
     * 获取口袋供应商的获取数据URL
     * country：获取国家景点语音讲解
     * city：获取城市景点语音讲解
     * site：获取某一个景点的语音介绍列表
     * scene：获取某一个语音介绍
     * @param level 查询级别 【country：国家key ；city：城市key ；site：景点key ；scene：景点key】
     * @param key 口袋的key值
     * @return
     */
    private static String getPocketAccessUrl(String level , String key){
        //返回口袋供应商接口url
        return "http://ctrip.api.kddaoyou.com/api/"+level+"/"+key;
    }

    /**
     * 口袋导游接口调用后返回unicode编码的json字符串
     * 需要转变为utf8编码的json字符串并且获取到对象集合
     * @param unicodeStr
     * @return
     */
    public static PocketCityEntity convert2PocketCityItem(String unicodeStr){
        List<PocketCityItemEntity> result = new ArrayList<>();
        PocketCityEntity pocketCityEntity = new PocketCityEntity();
        try {
            JSONObject jsonObject = JSONObject.parseObject(new String(unicodeStr.getBytes("UTF-8")));
            pocketCityEntity = jsonObject.toJavaObject(pocketCityEntity.getClass());
            if(pocketCityEntity!=null && pocketCityEntity.getSite_list()!=null && pocketCityEntity.getSite_list().size()>0){
                return pocketCityEntity;
            }else{
            	return null;
            }
        }catch(Exception ex){
            System.out.println("MatchAudioBusiness-convert2PocketCityItem异常:"+ex.getMessage());
            return null;
        }
    }
    /**
     * 口袋导游接口调用后返回unicode编码的json字符串
     * 需要转变为utf8编码的json字符串并且获取到对象集合
     * @param unicodeStr
     * @return
     */
    public static List<PocketSiteEntity> convert2PocketSiteList(String unicodeStr){
    	List<PocketSiteEntity> list = new ArrayList<PocketSiteEntity>();
        PocketSiteEntity pocketSiteEntity = new PocketSiteEntity();
        try {
            List<HashMap> listMap = mapper.readValue(new String(unicodeStr.getBytes("UTF-8")), ArrayList.class);
            for (HashMap map : listMap) {
            	pocketSiteEntity = (PocketSiteEntity) util.mapToObject(map, PocketSiteEntity.class);
            	list.add(pocketSiteEntity);
			}
        }catch(Exception ex){
        	System.out.println("MatchAudioBusiness-调用口袋site接口convert2PocketSiteList数据转换异常："+ex.getMessage());
        }
        return list;
    }
    
    public static PocketSceneEntity convert2PocketSceneEntity(String unicodeStr){
    	PocketSceneEntity entity = new PocketSceneEntity();
    	try {
    		System.out.println("scene:"+new String(unicodeStr.getBytes("UTF-8")));
    		if(!"[]".equals(new String(unicodeStr.getBytes("UTF-8")))){
    			entity = mapper.readValue(new String(unicodeStr.getBytes("UTF-8")), PocketSceneEntity.class);
    		}else{
    			return null;
    		}
		} catch (Exception e) {
			System.out.println("MatchAudioBusiness-调用口袋scene接口convert2PocketSceneEntity数据转换异常："+e.getMessage());
		}
    	return entity;
    }

    /**
     * 链景接口3调用后返回的字符串转变到scenicvalue对象
     * @param scenicStr
     * @return
     */
    public static LJScenicValueEntity convert2ScenicValue(String scenicStr){
        LJScenicValueEntity scenicValueEntity = null;
        LJScenicEntity scenicEntity = new LJScenicEntity();
        try {
            JSONObject jsonObject = JSONObject.parseObject(scenicStr);
            scenicEntity = jsonObject.toJavaObject(scenicEntity.getClass());
            System.out.println("解析的实体对象外层成功：");
            if(scenicEntity!=null && "0".equals(scenicEntity.getState()) && scenicEntity.getValue()!=null ){
            	HashMap map = mapper.readValue(scenicEntity.getValue(), HashMap.class);
            	scenicValueEntity = (LJScenicValueEntity) util.mapToObject(map, LJScenicValueEntity.class);
                System.out.println("解析内层对象成功：");
            }else{
            	System.out.println("此次查询结果："+scenicEntity.getMsg());
            }
        }catch(Exception ex){
        	System.out.println("MatchAudioBusiness-convert2ScenicValue方法解析调用接口返回的字符串成LJScenicEntity对象异常，异常信息："+ex.getMessage());
        }
        return scenicValueEntity;
    }
    
    /**
     * 调用链景第一个接口返回的数据，转变成LJScenicValueEntity集合
     * @param scenicStr
     * @return
     */
    public static List<LJScenicValueEntity> convert2ScenicValueList(String scenicStr){
    	List<LJScenicValueEntity> list = new ArrayList<LJScenicValueEntity>();
    	LJScenicValueEntity scenicValueEntity = null;
    	LJScenicEntity scenicEntity = null;
    	try {
    		scenicEntity = mapper.readValue(scenicStr, LJScenicEntity.class);
    		System.out.println("mapper:"+scenicEntity);
            System.out.println("解析的实体对象外层成功：");
            if(scenicEntity!=null && "0".equals(scenicEntity.getState()) && scenicEntity.getValue()!=null ){
            	List<HashMap> listMap = mapper.readValue(scenicEntity.getValue(), ArrayList.class);
            	for(HashMap map : listMap){
            		scenicValueEntity = (LJScenicValueEntity) util.mapToObject(map, LJScenicValueEntity.class);
            		list.add(scenicValueEntity);
            	}
                System.out.println("解析内层对象成功：");
            }else{
            	System.out.println("此次查询结果："+scenicEntity.getMsg());
            }
		} catch (Exception e) {
			System.out.println("MatchAudioBusiness-convert2ScenicValueList方法转换对象异常："+e.getMessage());
		}
    	
    	return list;
    }
    
    public static List<LJScenicExplainEntity> convert2ScenicExplainList(String scenicStr){
    	List<LJScenicExplainEntity> list = new ArrayList<LJScenicExplainEntity>();
    	LJScenicExplainEntity explainEntity = null;
    	LJScenicEntity scenicEntity = null;
    	try {
    		scenicEntity = mapper.readValue(scenicStr, LJScenicEntity.class);
    		System.out.println("mapper:"+scenicEntity);
            System.out.println("解析的实体对象外层成功：");
            if(scenicEntity!=null && "0".equals(scenicEntity.getState()) && scenicEntity.getValue()!=null ){
            	List<HashMap> listMap = mapper.readValue(scenicEntity.getValue(), ArrayList.class);
            	for(HashMap map : listMap){
            		explainEntity = (LJScenicExplainEntity) util.mapToObject(map, LJScenicExplainEntity.class);
            		list.add(explainEntity);
            	}
                System.out.println("解析内层对象成功：");
            }else{
            	System.out.println("此次查询结果："+scenicEntity.getMsg());
            }
		} catch (Exception e) {
			System.out.println("MatchAudioBusiness-convert2ScenicExplainList方法转换对象异常："+e.getMessage());
		}
    	return list;
    }

    /**
     * 将上传音频返回的字符串转成UploadAudioResponseEntity实体对象
     * @param scenicStr
     * @return
     */
    public static UploadAudioResponseEntity convert2UploadAudioResponseEntity(String scenicStr){
        UploadAudioResponseEntity uploadAudioResponseEntity = new UploadAudioResponseEntity();
        try {
            JSONObject jsonObject = JSONObject.parseObject(scenicStr);
            uploadAudioResponseEntity = jsonObject.toJavaObject(uploadAudioResponseEntity.getClass());
        }catch(Exception ex){
        	System.out.println("MatchAudioBusiness-convert2UploadAudioResponseEntity方法异常："+ex.getMessage());
        }
        return uploadAudioResponseEntity;
    }
    
    /**
     * 向数据库表插入数据
     * @param cspoiMapping
     * @return
     */
    public static int insertData(CSPoiMapping cspoiMapping){
    	MatualWithDbUtil dbutil = new MatualWithDbUtil();
        return dbutil.insertCSMappingData(cspoiMapping);
    }
    
    /**
     * 向本地数据库表插入数据
     * @param cspoiMapping
     * @return
     */
    public static int insertDataLocal(CSPoiMappingLocal cspoiMappingLocal){
    	MatualWithDbUtil dbutil = new MatualWithDbUtil();
        return dbutil.insertCSMappingDataLocal(cspoiMappingLocal);
    }
    
    public static void main(String[] args) throws Exception{
		
    }
    
    
    public static String matchAudioDataLocal(MCtripPoi poi , long supplierType , long districtId){
        MSupplierPoi mSupplierPoi = null;
        int matchScore = 0;
        CSPoiMappingLocal cspoiMappingLocal = null;
        try {
            if(supplierType == 2017001){
                //根据poi对象的所在城市名称对应的key文件找到对应城市key
                //String cityKey = "8d49d262-a1d3-11e6-9a1b-00163e0005c0";//暂时以巴黎为例
                //String cityKey = "8d408194-a1d3-11e6-9a1b-00163e0005c0";//上海
            	String cityKey = "";
                String cityName = poi.getCityName();
                if(cityName!=null && !"".equals(cityName)){
                	cityKey = jedis.get(cityName);
                	if(cityKey==null || "".equals(cityKey)) return "";
                }
                //调用接口获取该城市下的景点列表
                String unicodeCityResult = getSupplierData(getPocketAccessUrl("city",cityKey));
                if(unicodeCityResult!=null && !"".equals(unicodeCityResult)){
                	PocketCityEntity city = convert2PocketCityItem(unicodeCityResult);
                	if(city != null){
                		List<PocketCityItemEntity> itemList = city.getSite_list();
                        System.out.println("口袋"+city.getCity()+"城市包含景点数据集合大小："+itemList.size());
                        if(itemList!=null && itemList.size()>0){
                        	PocketSiteEntity site =null;
                        	//遍历景点列表，并与poi对象进行相似度匹配    MacthRuleUtil  方法返回相似度得分
                            for(PocketCityItemEntity item : itemList){
                            	System.out.println(item.toString());
                                mSupplierPoi = new MSupplierPoi();
                                mSupplierPoi.setChName(item.getTITLE());
                                mSupplierPoi.setEnName(item.getTITLE_EN());
                                mSupplierPoi.setSupplierType("2017001");
                                mSupplierPoi.setDestinationName(city.getCity());
                                //经纬度需要转换为高德经纬度
                                mSupplierPoi.setGdLT(PositionUtil.gps84_To_Gcj02(Double.parseDouble(item.getLATITUDE()), Double.parseDouble(item.getLONGITUDE())));
                                // 用上面第一步匹配的城市名称
                                matchScore = MatchRuleUtil.getMatchScore(poi,mSupplierPoi);
                                System.out.println("###################匹配得分："+matchScore);
                                //匹配相似度得分符合要求，调用口袋接口根据匹配上的景点key查询相应的语音信息
                                if(matchScore==29 || matchScore==23 || matchScore==39 || matchScore==34 || matchScore==90 
                                		|| matchScore==43 || matchScore==22|| matchScore==30|| matchScore==33){
                                    //根据景点key查询景点信息
                                    String unicodeSiteResult = getSupplierData(getPocketAccessUrl("site",item.getKEY()));
                                    List<PocketSiteEntity> siteList = convert2PocketSiteList(unicodeSiteResult);
                                    System.out.println(item.getTITLE()+"包含子景点个数="+siteList.size());
                                    if(siteList!=null && siteList.size()>0){
                                    	System.out.println("siteList:"+siteList.size());
                                    	for (int i=0;i<siteList.size();i++) {
                                    		site = siteList.get(i);
                                			cspoiMappingLocal = new CSPoiMappingLocal();
                                        	System.out.println("POI:"+poi.getPoiID()+";name:"+poi.getChName());
                                        	System.out.println("site="+site);
                                        	cspoiMappingLocal.setDistrictId(districtId);
                                        	cspoiMappingLocal.setPoiId(poi.getPoiID());
                                        	cspoiMappingLocal.setC_city_chname(poi.getCityName());
                                        	cspoiMappingLocal.setC_city_enname(poi.getCityEnName());
                                        	cspoiMappingLocal.setC_site_chname(poi.getChName());
                                        	cspoiMappingLocal.setC_site_enname(poi.getEnName());
                                        	cspoiMappingLocal.setC_site_localname(poi.getLocalName());
                                        	cspoiMappingLocal.setMatchscore(matchScore);
                                        	cspoiMappingLocal.setS_site_chname(item.getTITLE());
                                        	cspoiMappingLocal.setS_site_enname(item.getTITLE_EN());
                                        	cspoiMappingLocal.setChild_name(site.getTITLE());
                                        	cspoiMappingLocal.setSupplier_type("2017001");
                                        	cspoiMappingLocal.setC_isnewmodel(poi.isNewModel()?"1":"0");
                                        	cspoiMappingLocal.setC_site_address(poi.getChAddress());
                                        	cspoiMappingLocal.setC_site_phone(poi.getPhone());
                                        	cspoiMappingLocal.setAudio_text(item.getTEXT());
                                        	cspoiMappingLocal.setS_city_chname(city.getCity());
                                        	cspoiMappingLocal.setKey_site(site.getKEY());
                                        	cspoiMappingLocal.setAudio_text(site.getTEXT());
                                        	cspoiMappingLocal.setS_city_price(city.getCity_price()+"");
                                        	if(poi.isInChina()){
                                        		cspoiMappingLocal.setIsInChina("0");
                                            }else{
                                            	cspoiMappingLocal.setIsInChina("1");
                                            }
                                        	if(i == 0){
                                        		cspoiMappingLocal.setSite_level("1");
                                        	}else{
                                        		cspoiMappingLocal.setSite_level("2");
                                        	}
                                            //根据返回的信息中的图片和音频的url进行下载并上传至携程服务器，获取返回字符串
                                        	System.out.println("site:"+site);
                                        	uploadAndInsertDBLocal(site.getAUDIO(),cspoiMappingLocal,matchScore);
    									}
                                    }
                                }
                            }
                        }
                    }
                }
            }else if(supplierType == 2017002){
            	//表示调用的链景的哪个接口
            	int type = 1;
            	String responseScenic = "";//调用接口返回的json数据
            	LJRequestParam ljRequestParam = new LJRequestParam();
                if(poi.isInChina()){
                    ljRequestParam.setArea(poi.getCityName());
                }else{
                    ljRequestParam.setArea(poi.getCountryName());
                }
                ljRequestParam.setName("");
                /*if(poi.getChName()!=null && !"".equals(poi.getChName())){
                	ljRequestParam.setName(poi.getChName());
                }else{
                	ljRequestParam.setName(poi.getEnName());
                }*/
            	if(type == 3){
                    //根据poi所在城市名称和poi名称，调用链景接口查询景区数据 value集合
                    responseScenic = getSupplierData(getLJAccessUrl(ljRequestParam,3));
                    LJScenicValueEntity scenicValue = null;
                    System.out.println("调用链景接口获取字符串数据："+responseScenic);
                    if(responseScenic!=null && !"".equals(responseScenic)){
                        scenicValue = convert2ScenicValue(responseScenic);
                    }
                    if(scenicValue!=null){
                    	cspoiMappingLocal = new CSPoiMappingLocal();
                    	System.out.println("POI:"+poi.getPoiID()+";name:"+poi.getChName());
                    	cspoiMappingLocal.setDistrictId(districtId);
                    	cspoiMappingLocal.setPoiId(poi.getPoiID());
                    	cspoiMappingLocal.setC_city_chname(poi.getCityName());
                    	cspoiMappingLocal.setC_city_enname(poi.getCityEnName());
                    	cspoiMappingLocal.setC_site_chname(poi.getChName());
                    	cspoiMappingLocal.setC_site_enname(poi.getEnName());
                    	cspoiMappingLocal.setC_site_localname(poi.getLocalName());
                    	cspoiMappingLocal.setMatchscore(100);
                    	cspoiMappingLocal.setS_city_chname(scenicValue.getAreaName());
                    	cspoiMappingLocal.setS_site_chname(scenicValue.getName());
                    	cspoiMappingLocal.setSupplier_type("2017002");
                    	cspoiMappingLocal.setAudio_time(Integer.valueOf(scenicValue.getDateTime()));
                    	cspoiMappingLocal.setAudio_size(Long.valueOf(scenicValue.getMp3Size()));
                    	cspoiMappingLocal.setNarrator_nick(scenicValue.getNickName());
                    	cspoiMappingLocal.setNarrator_sex(scenicValue.getSex());
                    	cspoiMappingLocal.setNarrator_headurl(scenicValue.getHeadImg());
                    	if(poi.isInChina()){
                    		cspoiMappingLocal.setIsInChina("0");
                        }else{
                        	cspoiMappingLocal.setIsInChina("1");
                        }
                        //上传音频，并且入库
                        uploadAndInsertDBLocal(scenicValue.getMp3Url(),cspoiMappingLocal,90);
                    }
            	}else if(type == 1){
        		
        			//根据poi所在城市名称和poi名称，调用链景接口查询景区数据 value集合
            		
            		ljRequestParam.setPageSize("20");
                    
                    List<LJScenicValueEntity> out_list = new ArrayList<LJScenicValueEntity>();
                    List<LJScenicExplainEntity> in_list = null;
                    List<LJScenicValueEntity> out_list_sub = null;
                    for(int n=1;n<4;n++){
                    	ljRequestParam.setPageIndex(n+"");
                    	responseScenic = getSupplierData(getLJAccessUrl(ljRequestParam,1));
                    	if(responseScenic!=null && !"".equals(responseScenic)){
                    		out_list_sub = convert2ScenicValueList(responseScenic);
                        }
                    	if(out_list_sub!=null && out_list_sub.size()>0){
                    		out_list.addAll(out_list_sub);
                    	}else{
                    		continue;
                    	}
                    }
                    
                    if(out_list!=null && out_list.size()>0){
                    	System.out.println("获取链景对应"+ljRequestParam.getArea()+"数据:"+out_list.size());
                    	LJScenicExplainEntity scenicExplain = null;
                    	for(LJScenicValueEntity scenicValue : out_list){
                    		//根据匹配规则进行匹配
                    		mSupplierPoi = new MSupplierPoi();
                            mSupplierPoi.setChName(scenicValue.getName());
                            mSupplierPoi.setDestinationName(scenicValue.getAreaName());
                            mSupplierPoi.setSupplierType(supplierType+"");
                            mSupplierPoi.setGdLT(scenicValue.getLat()+","+scenicValue.getLon());
                            //System.out.println("mSupplierPoi:GLT="+mSupplierPoi.getGdLT());
                            mSupplierPoi.setPhone(scenicValue.getTelephone());
                            //用上面第一步匹配的城市名称
                            matchScore = MatchRuleUtil.getMatchScore(poi,mSupplierPoi);
                            System.out.println("matchScore="+matchScore);
                   		    if(matchScore==29 || matchScore==23 || matchScore==39 || matchScore==34 || matchScore==90 
                    				|| matchScore==43 || matchScore==22|| matchScore==30|| matchScore==33){
                    			//step2 根据poi根据标识获取景区讲解集合
                    			ljRequestParam.setCode(String.valueOf(scenicValue.getCode()));
                    			ljRequestParam.setUnix(String.valueOf(scenicValue.getUnix()));
                                responseScenic = getSupplierData(getLJAccessUrl(ljRequestParam,2));
                                if(responseScenic!=null && !"".equals(responseScenic)){
                                	in_list = convert2ScenicExplainList(responseScenic);
                                	if(in_list!=null && in_list.size()>0){
                                		for(int i=0;i<in_list.size();i++){
                                			scenicExplain = in_list.get(i);
                                			//构造cspoiMapping对象
                                			cspoiMappingLocal = new CSPoiMappingLocal();
                                        	//System.out.println("POI:"+poi.getPoiID()+";name:"+poi.getChName());
                                        	cspoiMappingLocal.setDistrictId(districtId);
                                        	cspoiMappingLocal.setPoiId(poi.getPoiID());
                                        	cspoiMappingLocal.setC_city_chname(poi.getCityName());
                                        	cspoiMappingLocal.setC_city_enname(poi.getCityEnName());
                                        	cspoiMappingLocal.setC_site_chname(poi.getChName());
                                        	cspoiMappingLocal.setC_site_enname(poi.getEnName());
                                        	cspoiMappingLocal.setC_site_localname(poi.getLocalName());
                                        	cspoiMappingLocal.setMatchscore(matchScore);
                                        	cspoiMappingLocal.setS_city_chname(scenicValue.getAreaName());
                                        	cspoiMappingLocal.setS_site_chname(scenicValue.getName());
                                        	cspoiMappingLocal.setChild_name(scenicExplain.getName());
                                        	cspoiMappingLocal.setSupplier_type("2017002");
                                        	cspoiMappingLocal.setC_isnewmodel(poi.isNewModel()?"1":"0");
                                        	cspoiMappingLocal.setC_site_address(poi.getChAddress());
                                        	cspoiMappingLocal.setC_site_phone(poi.getPhone());
                                        	cspoiMappingLocal.setS_site_phone(scenicValue.getTelephone());
                                        	cspoiMappingLocal.setAudio_text(scenicValue.getIntroduce());
                                        	cspoiMappingLocal.setAudio_time(Integer.valueOf(scenicExplain.getDateTime()));
                                            //cspoiMapping.setAudio_size(Long.valueOf(scenicValue.getMp3Size()));
                                        	cspoiMappingLocal.setNarrator_nick(scenicValue.getNickName());
                                        	cspoiMappingLocal.setNarrator_sex(scenicExplain.getSex());
                                        	cspoiMappingLocal.setNarrator_headurl(scenicExplain.getHeadImg());
                                        	if(poi.isInChina()){
                                        		cspoiMappingLocal.setIsInChina("0");
                                            }else{
                                            	cspoiMappingLocal.setIsInChina("1");
                                            }
                                        	if(scenicExplain.getName().equals(scenicValue.getName())){
                                        		cspoiMappingLocal.setSite_level("1");
                                        	}else{
                                        		cspoiMappingLocal.setSite_level("2");
                                        	}
                                			//上传音频并数据入库
                                            uploadAndInsertDBLocal(scenicExplain.getMp3Url(),cspoiMappingLocal,matchScore);
                                		}
                                	}
                                }
                    		}
                    	}
                    }
            	}
            }else if(supplierType == 2017003){

            }else{
                //return "供应商类型参数supplierType不合法！";
            	System.out.println("MatchAudioBusiness-matchAudioData方法：供应商类型参数supplierType不合法！");
            }
        }catch(Exception ex){
        	System.out.println("MatchAudioBusiness-matchAudioData方法异常:"+ex.getMessage());
        }
        return "";
    }
    /**===================================================================================================================================================**/
    /**
     * 匹配口袋导游数据：在本地不做真正的音频下载上传时全量匹配完后，由产品确认哪些保留，确认保留的整理poiId-site_key对应关系
     * @param poi
     * @param supplierType
     * @param districtId
     * @return
     */
    public static String matchPackageSupplierData(MCtripPoi poi , long supplierType , long districtId){
    	MSupplierPoi mSupplierPoi = null;
        int matchScore = 0;
        CSPoiMappingLocal cspoiMappingLocal = null;
        try {
        	if(supplierType == 2017001){
                //根据poi对象的所在城市名称对应的key文件找到对应城市key
                //String cityKey = "8d49d262-a1d3-11e6-9a1b-00163e0005c0";//暂时以巴黎为例
                //String cityKey = "8d408194-a1d3-11e6-9a1b-00163e0005c0";//上海
        		String cityKey = "";
                String cityName = poi.getCityName();
                if(cityName!=null && !"".equals(cityName)){
                	cityKey = jedis.get(cityName);
                	if(cityKey==null || "".equals(cityKey)) return "";
                }
                //调用接口获取该城市下的景点列表
                String unicodeCityResult = getSupplierData(getPocketAccessUrl("city",cityKey));
                if(unicodeCityResult!=null && !"".equals(unicodeCityResult)){
                	PocketCityEntity city = convert2PocketCityItem(unicodeCityResult);
                	if(city != null){
                		List<PocketCityItemEntity> itemList = city.getSite_list();
                        System.out.println("口袋"+city.getCity()+"城市包含景点数据集合大小："+itemList.size());
                        if(itemList!=null && itemList.size()>0){
                        	PocketSiteEntity site = null;
                        	//遍历景点列表，并与poi对象进行相似度匹配    MacthRuleUtil  方法返回相似度得分
                            for(PocketCityItemEntity item : itemList){
                            	System.out.println(item.toString());
                                mSupplierPoi = new MSupplierPoi();
                                mSupplierPoi.setChName(item.getTITLE());
                                mSupplierPoi.setEnName(item.getTITLE_EN());
                                mSupplierPoi.setSupplierType("2017001");
                                mSupplierPoi.setDestinationName(city.getCity());
                                //经纬度需要转换为高德经纬度
                                mSupplierPoi.setGdLT(PositionUtil.gps84_To_Gcj02(Double.parseDouble(item.getLATITUDE()), Double.parseDouble(item.getLONGITUDE())));
                                // 用上面第一步匹配的城市名称
                                matchScore = MatchRuleUtil.getMatchScore(poi,mSupplierPoi);
                                System.out.println("匹配得分："+matchScore);
                                //匹配相似度得分符合要求，调用口袋接口根据匹配上的景点key查询相应的语音信息
                                if(matchScore==29 || matchScore==23 || matchScore==39 || matchScore==34 || matchScore==90 
                                		|| matchScore==43 || matchScore==22|| matchScore==30|| matchScore==33){
                                    //根据景点key查询景点信息
                                    String unicodeSiteResult = getSupplierData(getPocketAccessUrl("site",item.getKEY()));
                                    List<PocketSiteEntity> siteList = convert2PocketSiteList(unicodeSiteResult);
                                    System.out.println(item.getTITLE()+"包含子景点个数="+siteList.size());
                                    if(siteList!=null && siteList.size()>0){
                                    	System.out.println("siteList:"+siteList.size());
                                    	for (int i=0;i<siteList.size();i++) {
                                    		site = siteList.get(i);
                                			cspoiMappingLocal = new CSPoiMappingLocal();
                                        	System.out.println("POI:"+poi.getPoiID()+";name:"+poi.getChName());
                                        	System.out.println("site="+site);
                                        	cspoiMappingLocal.setDistrictId(districtId);
                                        	cspoiMappingLocal.setPoiId(poi.getPoiID());
                                        	cspoiMappingLocal.setC_city_chname(poi.getCityName());
                                        	cspoiMappingLocal.setC_city_enname(poi.getCityEnName());
                                        	cspoiMappingLocal.setC_site_chname(poi.getChName());
                                        	cspoiMappingLocal.setC_site_enname(poi.getEnName());
                                        	cspoiMappingLocal.setC_site_localname(poi.getLocalName());
                                        	cspoiMappingLocal.setMatchscore(matchScore);
                                        	cspoiMappingLocal.setS_site_chname(item.getTITLE());
                                        	cspoiMappingLocal.setS_site_enname(item.getTITLE_EN());
                                        	cspoiMappingLocal.setChild_name(site.getTITLE());
                                        	cspoiMappingLocal.setSupplier_type("2017001");
                                        	cspoiMappingLocal.setC_isnewmodel(poi.isNewModel()?"1":"0");
                                        	cspoiMappingLocal.setC_site_address(poi.getChAddress());
                                        	cspoiMappingLocal.setC_site_phone(poi.getPhone());
                                        	cspoiMappingLocal.setAudio_text(item.getTEXT());
                                        	cspoiMappingLocal.setS_city_chname(city.getCity());
                                        	cspoiMappingLocal.setKey_site(site.getKEY());
                                        	cspoiMappingLocal.setAudio_text(site.getTEXT());
                                        	cspoiMappingLocal.setS_city_price(city.getCity_price()+"");
                                        	if(i == 0){
                                        		cspoiMappingLocal.setSite_level("1");
                                        	}else{
                                        		cspoiMappingLocal.setSite_level("2");
                                        	}
                                        	System.out.println("开始上传音频=============");
                                            //根据返回的信息中的图片和音频的url进行下载并上传至携程服务器，获取返回字符串
                                        	uploadAndInsertDBLocal(site.getAUDIO(),cspoiMappingLocal,matchScore);
    									}
                                    }
                                }
                            }
                        }
                    }
                }
            }
		} catch (Exception e) {
			System.out.println("匹配口袋到有数据异常："+districtId+"-"+poi.getPoiID()+";"+e.getMessage());
		}
        return "";
    }
    /**
     * 匹配链景的数据
     * @param poi
     * @param supplierType
     * @param districtId
     * @return
     */
    public static String matchLJSupplierData(MCtripPoi poi , long supplierType , long districtId){
    	MSupplierPoi mSupplierPoi = null;
        int matchScore = 0;
        CSPoiMappingLocal cspoiMappingLocal = null;
    	try {
        	//表示调用的链景的哪个接口
        	int type = 1;
        	String responseScenic = "";//调用接口返回的json数据
        	LJRequestParam ljRequestParam = new LJRequestParam();
            if(poi.isInChina()){
                ljRequestParam.setArea(poi.getCityName());
            }else{
                ljRequestParam.setArea(poi.getCountryName());
            }
            if(poi.getChName()!=null && !"".equals(poi.getChName())){
            	ljRequestParam.setName(poi.getChName());
            }else{
            	ljRequestParam.setName(poi.getEnName());
            }
        	if(type == 3){
                //根据poi所在城市名称和poi名称，调用链景接口查询景区数据 value集合
                responseScenic = getSupplierData(getLJAccessUrl(ljRequestParam,3));
                LJScenicValueEntity scenicValue = null;
                System.out.println("调用链景接口获取字符串数据："+responseScenic);
                if(responseScenic!=null && !"".equals(responseScenic)){
                    scenicValue = convert2ScenicValue(responseScenic);
                }
                if(scenicValue!=null){
                	cspoiMappingLocal = new CSPoiMappingLocal();
                	System.out.println("POI:"+poi.getPoiID()+";name:"+poi.getChName());
                	cspoiMappingLocal.setDistrictId(districtId);
                	cspoiMappingLocal.setPoiId(poi.getPoiID());
                	cspoiMappingLocal.setC_city_chname(poi.getCityName());
                	cspoiMappingLocal.setC_city_enname(poi.getCityEnName());
                	cspoiMappingLocal.setC_site_chname(poi.getChName());
                	cspoiMappingLocal.setC_site_enname(poi.getEnName());
                	cspoiMappingLocal.setC_site_localname(poi.getLocalName());
                	cspoiMappingLocal.setMatchscore(100);
                	cspoiMappingLocal.setS_city_chname(scenicValue.getAreaName());
                	cspoiMappingLocal.setS_site_chname(scenicValue.getName());
                	cspoiMappingLocal.setSupplier_type("2017002");
                	cspoiMappingLocal.setAudio_time(Integer.valueOf(scenicValue.getDateTime()));
                	cspoiMappingLocal.setAudio_size(Long.valueOf(scenicValue.getMp3Size()));
                	cspoiMappingLocal.setNarrator_nick(scenicValue.getNickName());
                	cspoiMappingLocal.setNarrator_sex(scenicValue.getSex());
                	cspoiMappingLocal.setNarrator_headurl(scenicValue.getHeadImg());
                    //上传音频，并且入库
                    uploadAndInsertDBLocal(scenicValue.getMp3Url(),cspoiMappingLocal,90);
                }
        	}else if(type == 1){
        		//根据poi所在城市名称和poi名称，调用链景接口查询景区数据 value集合
        		ljRequestParam.setPageIndex("1");
        		ljRequestParam.setPageSize("20");
                responseScenic = getSupplierData(getLJAccessUrl(ljRequestParam,1));
                List<LJScenicValueEntity> out_list = null;
                List<LJScenicExplainEntity> in_list = null;
                if(responseScenic!=null && !"".equals(responseScenic)){
                	out_list = convert2ScenicValueList(responseScenic);
                }
                if(out_list!=null && out_list.size()>0){
                	LJScenicExplainEntity scenicExplain = null;
                	for(LJScenicValueEntity scenicValue : out_list){
                		//根据匹配规则进行匹配
                		mSupplierPoi = new MSupplierPoi();
                        mSupplierPoi.setChName(scenicValue.getName());
                        mSupplierPoi.setDestinationName(scenicValue.getAreaName());
                        mSupplierPoi.setSupplierType(supplierType+"");
                        mSupplierPoi.setGdLT(scenicValue.getLat()+","+scenicValue.getLon());
                        System.out.println("mSupplierPoi:GLT="+mSupplierPoi.getGdLT());
                        mSupplierPoi.setPhone(scenicValue.getTelephone());
                        //用上面第一步匹配的城市名称
                        matchScore = MatchRuleUtil.getMatchScore(poi,mSupplierPoi);
                        System.out.println("matchScore="+matchScore);
                		if(matchScore==29 || matchScore==23 || matchScore==39 || matchScore==34 || matchScore==90
                				|| matchScore==43 || matchScore==22|| matchScore==30|| matchScore==33){
                			//step2 根据poi根据标识获取景区讲解集合
                			ljRequestParam.setCode(String.valueOf(scenicValue.getCode()));
                			ljRequestParam.setUnix(String.valueOf(scenicValue.getUnix()));
                            responseScenic = getSupplierData(getLJAccessUrl(ljRequestParam,2));
                            if(responseScenic!=null && !"".equals(responseScenic)){
                            	in_list = convert2ScenicExplainList(responseScenic);
                            	if(in_list!=null && in_list.size()>0){
                            		for(int i=0;i<in_list.size();i++){
                            			scenicExplain = in_list.get(i);
                            			//构造cspoiMapping对象
                            			cspoiMappingLocal = new CSPoiMappingLocal();
                                    	System.out.println("POI:"+poi.getPoiID()+";name:"+poi.getChName());
                                    	cspoiMappingLocal.setDistrictId(districtId);
                                    	cspoiMappingLocal.setPoiId(poi.getPoiID());
                                    	cspoiMappingLocal.setC_city_chname(poi.getCityName());
                                    	cspoiMappingLocal.setC_city_enname(poi.getCityEnName());
                                    	cspoiMappingLocal.setC_site_chname(poi.getChName());
                                    	cspoiMappingLocal.setC_site_enname(poi.getEnName());
                                    	cspoiMappingLocal.setC_site_localname(poi.getLocalName());
                                    	cspoiMappingLocal.setMatchscore(matchScore);
                                    	cspoiMappingLocal.setS_city_chname(scenicValue.getAreaName());
                                    	cspoiMappingLocal.setS_site_chname(scenicValue.getName());
                                    	cspoiMappingLocal.setChild_name(scenicExplain.getName());
                                    	cspoiMappingLocal.setSupplier_type("2017002");
                                    	cspoiMappingLocal.setC_isnewmodel(poi.isNewModel()?"1":"0");
                                    	cspoiMappingLocal.setC_site_address(poi.getChAddress());
                                    	cspoiMappingLocal.setC_site_phone(poi.getPhone());
                                    	cspoiMappingLocal.setS_site_phone(scenicValue.getTelephone());
                                    	cspoiMappingLocal.setAudio_text(scenicValue.getIntroduce());
                                    	cspoiMappingLocal.setAudio_time(Integer.valueOf(scenicExplain.getDateTime()));
                                        //cspoiMapping.setAudio_size(Long.valueOf(scenicValue.getMp3Size()));
                                    	cspoiMappingLocal.setNarrator_nick(scenicValue.getNickName());
                                    	cspoiMappingLocal.setNarrator_sex(scenicExplain.getSex());
                                    	cspoiMappingLocal.setNarrator_headurl(scenicExplain.getHeadImg());
                                    	if(i == 0){
                                    		cspoiMappingLocal.setSite_level("1");
                                    	}else{
                                    		cspoiMappingLocal.setSite_level("2");
                                    	}
                            			//上传音频并数据入库
                                        uploadAndInsertDBLocal(scenicExplain.getMp3Url(),cspoiMappingLocal,matchScore);
                            		}
                            	}
                            }
                		}
                	}
                }
        	}
		} catch (Exception e) {
			System.out.println("匹配链景数据异常："+districtId+"-"+poi.getPoiID()+";"+e.getMessage());
		}
    	return "";
    }
    
    /**
     * 匹配美景的数据
     * @param poi
     * @param supplierType
     * @param districtId
     * @return
     */
    public static String matchMJSupplierData(MCtripPoi poi , long supplierType , long districtId){
    	MSupplierPoi mSupplierPoi = null;
        int matchScore = 0;
        CSPoiMappingLocal cspoiMappingLocal = null;
    	try {
    		
		} catch (Exception e) {
			System.out.println("匹配美景数据异常："+districtId+"-"+poi.getPoiID()+";"+e.getMessage());
		}
    	return "";
    }
    
}
