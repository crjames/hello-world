package com.ctrip.tingting.business;

import java.util.regex.Pattern;

import com.ctrip.tingting.bean.MCtripPoi;
import com.ctrip.tingting.bean.MSupplierPoi;

/**
 * Created by wang.donga on 2017/8/14.
 */
public class MatchRuleUtil {
    private static final String POINAME = "POINAME";
    private static final String POIDESTINATION = "POIDESTINATION";
    private static final String POIADDRESS = "POIADDRESS";
    private static final String STRING_REGIX_COMMA = ",";//字符串分割标识符-供应商景区联系电话分隔符
    private static final String STRING_REGIX_SEMICOLON = ";";//字符串分割标识符-ctrip景区联系电话分隔符
    
    private static String regEx="[^0-9]";   
    private static Pattern pattern = Pattern.compile(regEx); 
    
    private static String regExCh = "[^\u4e00-\u9fa5]";

    /**
     * 比较本地景点与供应商景点信息，获取匹配分值
     * @return
     */
    public static int getMatchScore(MCtripPoi ctripPOI,MSupplierPoi supplierPOI){
        if(ctripPOI == null || supplierPOI == null){
            return 0;
        }
        //System.out.println("ctripPOI="+ctripPOI.toString()+";supplierPOI="+supplierPOI);
        int score_chName=0,score_ename=0,score_chAddress=0,score_destination=0,score_lt=0,score_phone=0;
        //2017002-链景；2017003-美景；2017001-口袋旅游
        if("2017002".equals(supplierPOI.getSupplierType())){
            //比较字段：中文景点名称，中文所处城市名称，高德经纬度，中文景点地址，联系电话
        	if(ctripPOI.isInChina()){
        		score_destination = getPoiInfoMatchScore(ctripPOI.getCityName(),supplierPOI.getDestinationName(),"POIDESTINATION");//目的地名称（城市级别）
        	}else{
        		score_destination = getPoiInfoMatchScore(ctripPOI.getCountryName(),supplierPOI.getDestinationName(),"POIDESTINATION");//目的地名称（城市级别）
        	}
        	score_chName = getPoiInfoMatchScore(ctripPOI.getChName(),supplierPOI.getChName(),"POICHNAME",ctripPOI.getCityName(),supplierPOI.getDestinationName());//中文名称
        	score_ename = getPoiInfoMatchScore(ctripPOI.getEnName(),supplierPOI.getEnName(),"POIENNAME",ctripPOI.getCityName(),supplierPOI.getDestinationName());//英文名称
        	int nameScore = score_chName>=score_ename?score_chName:score_ename;
        	if(score_destination<90){
        		if(nameScore>=75){
        			return 43;
        		}
        		return 0;
        	}
        	if(nameScore!=5){
        		if(nameScore<60){
            		return 10;
            	}else if(nameScore>90){
            		return 90;
            	}else if(nameScore>75){
            		score_phone = getPoiPhoneMatchScore(ctripPOI.getPhone(),supplierPOI.getPhone());//联系电话
            		if(score_phone == 100){
            			return 39;
            		}else{
            			score_lt = getPoiLTMatchScore(ctripPOI.getGdLT(),supplierPOI.getGdLT(),3);//高德经纬度
            			return score_lt;  
            		}
            	}else{
            		score_phone = getPoiPhoneMatchScore(ctripPOI.getPhone(),supplierPOI.getPhone());//联系电话
            		if(score_phone == 100){
            			return 29;
            		}else{
            			score_lt = getPoiLTMatchScore(ctripPOI.getGdLT(),supplierPOI.getGdLT(),2);//高德经纬度
            			return score_lt; 
            		}
            	}
        	}else{
        		return 10;
        	}
        }else if("2017003".equals(supplierPOI.getSupplierType())){
            //比较字段：中文景点名称，英文景点名称，中文所处城市名称，高德经纬度
            score_destination = getPoiInfoMatchScore(ctripPOI.getCityName(),supplierPOI.getDestinationName(),"POIDESTINATION");//目的地名称（城市级别）
            if(score_destination<90){
        		return 0;
        	}
        	score_chName = getPoiInfoMatchScore(ctripPOI.getChName(),supplierPOI.getChName(),"POICHNAME",ctripPOI.getCityName(),supplierPOI.getDestinationName());//中文名称
        	score_ename = getPoiInfoMatchScore(ctripPOI.getEnName(),supplierPOI.getEnName(),"POIENNAME",ctripPOI.getCityName(),supplierPOI.getDestinationName());//英文名称
        	if(score_chName!=5){
        		if(score_chName<60){
            		return 10;
            	}else if(score_chName>90){
            		return 90;
            	}else if(score_chName>75){
            		score_lt = getPoiLTMatchScore(ctripPOI.getGdLT(),supplierPOI.getGdLT(),3);//高德经纬度
            	}else{
            		score_lt = getPoiLTMatchScore(ctripPOI.getGdLT(),supplierPOI.getGdLT(),2);//高德经纬度
            	}
        		return score_lt; 
        	}else{
        		if(score_ename<60){
            		return 10;
            	}else if(score_ename>90){
            		return 90;
            	}else if(score_ename>75){
            		score_lt = getPoiLTMatchScore(ctripPOI.getGdLT(),supplierPOI.getGdLT(),3);//高德经纬度
            	}else{
            		score_lt = getPoiLTMatchScore(ctripPOI.getGdLT(),supplierPOI.getGdLT(),2);//高德经纬度
            	}
        		return score_lt;  
        	}  
        }else if("2017001".equals(supplierPOI.getSupplierType())){
            //比较字段：中文景点名称，英文景点名称，当地语言景点名称，中文所处城市名称，高德经纬度
        	score_destination = getPoiInfoMatchScore(ctripPOI.getCityName(),supplierPOI.getDestinationName(),"POIDESTINATION");//目的地名称（城市级别）
        	System.out.println("Ctrip-chname="+ctripPOI.getChName()+";supplier-chname="+supplierPOI.getChName());
        	System.out.println("Ctrip-enname="+ctripPOI.getEnName()+";supplier-enname="+supplierPOI.getEnName());
        	score_chName = getPoiInfoMatchScore(ctripPOI.getChName(),supplierPOI.getChName(),"POICHNAME",ctripPOI.getCityName(),supplierPOI.getDestinationName());//中文名称
        	score_ename = getPoiInfoMatchScore(ctripPOI.getEnName(),supplierPOI.getEnName(),"POIENNAME",ctripPOI.getCityName(),supplierPOI.getDestinationName());//英文名称
        	System.out.println("score_chname="+score_chName+";score_ename="+score_ename);
        	int nameScore = score_chName>=score_ename?score_chName:score_ename;
        	if(score_destination<90){
        		if(nameScore>=75){
        			return 43;
        		}
        		return 0;
        	}
        	if(nameScore!=5){
        		if(nameScore<60){
            		return 10;
            	}else if(nameScore>90){
            		return 90;
            	}else if(nameScore>75){
            		score_lt = getPoiLTMatchScore(ctripPOI.getGdLT(),supplierPOI.getGdLT(),3);//高德经纬度
            	}else{
            		score_lt = getPoiLTMatchScore(ctripPOI.getGdLT(),supplierPOI.getGdLT(),2);//高德经纬度
            	}
        		return score_lt; 
        	}else{
        		return 10;
        	}
        }else{
        	return 10;
        }
    }

    /**
     * 获取 所处目的地城市名称    匹配分值
     * @param ctripPoiInfo 携程 /所处目的地城市名称/
     * @param supplierPoiInfo 供应商 /所处目的地城市名称/
     * @return
     */
    public static int getPoiInfoMatchScore(String ctripPoiInfo,String supplierPoiInfo,String strType){
        int infoScore = 0;
        if(strType==null || ctripPoiInfo==null || supplierPoiInfo==null || "".equals(ctripPoiInfo) || "".equals(supplierPoiInfo)){
            return 5;
        }
        double similarityRatio;
        if(supplierPoiInfo.contains("·")){
        	similarityRatio = getSimilarityRatio(ctripPoiInfo,supplierPoiInfo.substring(supplierPoiInfo.indexOf("·")+1));
        }else{
        	similarityRatio = getSimilarityRatio(ctripPoiInfo,supplierPoiInfo);
        }
        System.out.println(ctripPoiInfo+" 与 "+supplierPoiInfo+"-similarityRatio="+similarityRatio);
        infoScore = similarityRatio > 0.90 ? 95:similarityRatio > 0.75 ? 80:similarityRatio >= 0.50 ? 70:0;
        return infoScore;
    }
    /**
     * 获取 景点名称/景点地址    匹配分值
     * @param ctripPoiInfo 携程 景点名称/景点地址
     * @param supplierPoiInfo 供应商 景点名称/景点地址
     * @return
     */
    public static int getPoiInfoMatchScore(String ctripPoiInfo,String supplierPoiInfo,String strType,String ctripCityName,String supplierCityName){
        int infoScore = 0;
        if(strType==null || ctripPoiInfo==null || supplierPoiInfo==null || "".equals(ctripPoiInfo) || "".equals(supplierPoiInfo)){
            return 5;
        }
        double similarityRatio;
        //判断是中文名称还是英文名称
        if("POIENNAME".equals(strType)){
        	ctripPoiInfo = ctripPoiInfo.toLowerCase();
        	supplierPoiInfo = supplierPoiInfo.toLowerCase();
        	//处理  of
        	if(ctripPoiInfo.contains(" of ")){
        		ctripPoiInfo.replace(" of ", " ");
        	}
        	if(supplierPoiInfo.contains(" of ")){
        		supplierPoiInfo.replace(" of ", " ");
        	}
        }else{
        	//只保留汉字
        	ctripPoiInfo = ctripPoiInfo.replaceAll(regExCh,"");
        	supplierPoiInfo = supplierPoiInfo.replaceAll(regExCh,"");
            //景点名称都以 以下特殊词语结束时，容易影响匹配度合规性，特此判断，若携程自己数据与供应商数据景点名称均已相同特殊词语结尾时，先将特殊词语去掉再进行相似度匹配
        	//如果名称以所在城市开头，将开头城市名去掉
            if(ctripPoiInfo.startsWith(ctripCityName)){
            	ctripPoiInfo = ctripPoiInfo.substring(ctripCityName.length());
            }
            if(supplierPoiInfo.startsWith(supplierCityName)){
            	supplierPoiInfo = supplierPoiInfo.substring(supplierCityName.length());
            }
            //如果两个名称都截取掉城市名称后均为空字符串，则判定失败；因为如果城市名称相同就不会来判断景点名称
            if("".equals(ctripPoiInfo)&&"".equals(supplierPoiInfo)){
            	return 0;
            }
        	if(ctripPoiInfo.endsWith("大学") && supplierPoiInfo.endsWith("大学")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-2);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-2);
        	}
        	if(ctripPoiInfo.endsWith("博物馆") && supplierPoiInfo.endsWith("博物馆")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-3);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-3);
        	}
        	if(ctripPoiInfo.endsWith("故居纪念馆") && supplierPoiInfo.endsWith("故居纪念馆")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-5);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-5);
        	}
        	if(ctripPoiInfo.endsWith("天主堂") && supplierPoiInfo.endsWith("天主堂")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-3);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-3);
        	}
        	if(ctripPoiInfo.endsWith("公园") && supplierPoiInfo.endsWith("公园")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-2);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-2);
        	}
        	if(ctripPoiInfo.endsWith("王府") && supplierPoiInfo.endsWith("王府")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-2);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-2);
        	}
        	if(ctripPoiInfo.endsWith("大厦") && supplierPoiInfo.endsWith("大厦")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-2);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-2);
        	}
        	if(ctripPoiInfo.endsWith("广场") && supplierPoiInfo.endsWith("广场")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-2);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-2);
        	}
        	if(ctripPoiInfo.endsWith("教堂") && supplierPoiInfo.endsWith("教堂")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-2);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-2);
        	}
        	if(ctripPoiInfo.endsWith("码头") && supplierPoiInfo.endsWith("码头")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-2);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-2);
        	}
        	if(ctripPoiInfo.endsWith("大街") && supplierPoiInfo.endsWith("大街")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-2);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-2);
        	}
        	if(ctripPoiInfo.endsWith("艺术宫") && supplierPoiInfo.endsWith("艺术宫")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-3);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-3);
        	}
        	if(ctripPoiInfo.endsWith("市政厅") && supplierPoiInfo.endsWith("市政厅")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-3);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-3);
        	}
        	if(ctripPoiInfo.endsWith("纪念碑") && supplierPoiInfo.endsWith("纪念碑")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-3);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-3);
        	}
        	if(ctripPoiInfo.endsWith("纪念堂") && supplierPoiInfo.endsWith("纪念堂")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-3);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-3);
        	}
        	if(ctripPoiInfo.endsWith("修道院") && supplierPoiInfo.endsWith("修道院")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-3);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-3);
        	}
        	/*
        	*（5）生态园
       	    *（7）古镇
       	    *（8）民俗村
       	    */
        	if(ctripPoiInfo.endsWith("生态园") && supplierPoiInfo.endsWith("生态园")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-3);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-3);
        	}
        	if(ctripPoiInfo.endsWith("民俗村") && supplierPoiInfo.endsWith("民俗村")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-3);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-3);
        	}
        	if(ctripPoiInfo.endsWith("古镇") && supplierPoiInfo.endsWith("古镇")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-2);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-2);
        	}
        	//只要景点名称（无论哪一方）含以下词语，计算匹配程度时直接去掉：
        	//（2）固定词语，一般为后缀：景区、风景区、旅游区、旅游景区、旅游风景区、旅游度假区、文化旅游区、风景名胜、自然保护区
        	if(ctripPoiInfo.endsWith("景区")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-2);
        	}
        	if(ctripPoiInfo.endsWith("风景区")||ctripPoiInfo.endsWith("旅游区")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-3);
        	}
        	if(ctripPoiInfo.endsWith("旅游景区")||ctripPoiInfo.endsWith("风景名胜")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-4);
        	}
        	if(ctripPoiInfo.endsWith("旅游风景区")||ctripPoiInfo.endsWith("旅游度假区")||ctripPoiInfo.endsWith("自然保护区")||ctripPoiInfo.endsWith("文化旅游区")){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-5);
        	}
        	if(supplierPoiInfo.endsWith("景区")){
        		supplierPoiInfo = supplierPoiInfo.substring(0, ctripPoiInfo.length()-2);
        	}
        	if(supplierPoiInfo.endsWith("风景区")||supplierPoiInfo.endsWith("旅游区")){
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-3);
        	}
        	if(supplierPoiInfo.endsWith("旅游景区")||supplierPoiInfo.endsWith("风景名胜")){
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-4);
        	}
        	if(supplierPoiInfo.endsWith("旅游风景区")||supplierPoiInfo.endsWith("旅游度假区")||supplierPoiInfo.endsWith("自然保护区")||supplierPoiInfo.endsWith("文化旅游区")){
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-5);
        	}
        	/*
        	 *2.双方景点名称（攻略景点名称与供应商名称）均含以下词语组中的词语，计算匹配程度时同时去掉：
        	 *（1）度假村、度假小镇、度假邨
        	 *（2）博物馆、博览馆、展览馆、陈列馆
        	 *（3）王宫、皇宫
        	 *（4）遗址、旧址
        	 *（5）生态园
        	 *（6）公园
        	 *（7）古镇
        	 *（8）民俗村
        	*/
        	if((ctripPoiInfo.endsWith("博物馆")||ctripPoiInfo.endsWith("博览馆")||ctripPoiInfo.endsWith("展览馆")||ctripPoiInfo.endsWith("陈列馆")) 
        			&& (supplierPoiInfo.endsWith("博物馆")||supplierPoiInfo.endsWith("博览馆")||supplierPoiInfo.endsWith("展览馆")||supplierPoiInfo.endsWith("陈列馆"))){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-3);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-3);
        	}
        	if((ctripPoiInfo.endsWith("王宫")||ctripPoiInfo.endsWith("皇宫")) 
        			&& (supplierPoiInfo.endsWith("王宫")||supplierPoiInfo.endsWith("皇宫"))){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-2);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-2);
        	}
        	if((ctripPoiInfo.endsWith("遗址")||ctripPoiInfo.endsWith("旧址")) 
        			&& (supplierPoiInfo.endsWith("遗址")||supplierPoiInfo.endsWith("旧址"))){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-2);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-2);
        	}
        	if((ctripPoiInfo.endsWith("度假村")||ctripPoiInfo.endsWith("度假邨")||ctripPoiInfo.endsWith("度假小镇")) 
        			&& (supplierPoiInfo.endsWith("度假村")||supplierPoiInfo.endsWith("度假邨")||supplierPoiInfo.endsWith("度假小镇"))){
        		ctripPoiInfo = ctripPoiInfo.substring(0, ctripPoiInfo.length()-3);
        		supplierPoiInfo = supplierPoiInfo.substring(0, supplierPoiInfo.length()-3);
        	}
        }
        
        if("".equals(ctripPoiInfo) && "".equals(supplierPoiInfo)){
            return 95;
        }else if(!"".equals(ctripPoiInfo) && "".equals(supplierPoiInfo)){
        	return 5;
        }else if("".equals(ctripPoiInfo) && !"".equals(supplierPoiInfo)){
        	return 5;
        }
        similarityRatio = getSimilarityRatio(ctripPoiInfo.trim(),supplierPoiInfo.trim());
        System.out.println(ctripPoiInfo+" 与 "+supplierPoiInfo+"-similarityRatio="+similarityRatio);
        infoScore = similarityRatio >= 0.90 ? 95:similarityRatio >= 0.75 ? 80:similarityRatio >= 0.50 ? 70:0;
        return infoScore;
    }

    /**
     * 获取 景点经纬度 匹配分值
     * @param ctripPoiLT 携程 景点经纬度
     * @param supplierPoiLT 供应商 景点经纬度
     * @return
     */
    public static int getPoiLTMatchScore(String ctripPoiLT,String supplierPoiLT,int type){
        int ltScore = 0;
        if(ctripPoiLT==null || supplierPoiLT==null || !ctripPoiLT.contains(STRING_REGIX_COMMA) || !supplierPoiLT.contains(STRING_REGIX_COMMA)
        		|| "0,0".equals(supplierPoiLT)){
        	return type == 2 ? 20 : 30;
        }
        String ctripLongitude = ctripPoiLT.substring(ctripPoiLT.indexOf(STRING_REGIX_COMMA)+1);//携程景点经度
        String ctripLatitude = ctripPoiLT.substring(0,ctripPoiLT.indexOf(STRING_REGIX_COMMA));//携程景点纬度

        String supplierLongitude = supplierPoiLT.substring(supplierPoiLT.indexOf(STRING_REGIX_COMMA)+1);//供应商景点经度
        String supplierLatitude = supplierPoiLT.substring(0,supplierPoiLT.indexOf(STRING_REGIX_COMMA));//供应商景点纬度

        double abs_lon = Math.abs(Double.parseDouble(ctripLongitude)-Double.parseDouble(supplierLongitude));//经度距离相差绝对值
        double abs_lat = Math.abs(Double.parseDouble(ctripLatitude)-Double.parseDouble(supplierLatitude));//纬度距离相差绝对值
        if(type==2){
        	ltScore = getLtScore75(abs_lon , abs_lat);
        }else{
        	ltScore = getLtScore90(abs_lon , abs_lat);
        }
        return ltScore;
    }
    /**
     * 景点名称匹配度在75-90之间的情况下匹配经纬度
     * @param abs_lon
     * @param abs_lat
     * @return
     */
    public static int getLtScore90(double abs_lon , double abs_lat){
    	int ltScore = 0;
    	if(abs_lon>0.1 || abs_lat>0.09){
            //经度或者纬度距离相差10公里以上
            ltScore = 31;
        }else if(abs_lon<=0.03 && abs_lat<=0.027){
        	//经度和纬度距离相差3公里以内
        	ltScore = 34;
        }else if(abs_lon<=0.05 && abs_lat<=0.045){
            //经度和纬度距离相差5公里以内
            ltScore = 33;
        }else{
            ltScore = 32;
        }
        return ltScore;
    }
    /**
     * 景点名称匹配度在50-75之间的情况下匹配经纬度
     * @param abs_lon
     * @param abs_lat
     * @return
     */
    public static int getLtScore75(double abs_lon , double abs_lat){
    	int ltScore = 0;
    	if(abs_lon<=0.02 && abs_lat<=0.018){
            //经度和纬度距离相差2公里以内
            ltScore = 23;
        }else if(abs_lon<=0.05 && abs_lat<=0.045){
        	//经度和纬度距离相差5公里以内
            ltScore = 22;
        }else{
        	//经度和纬度距离相差5公里以外
            ltScore = 21;
        }
        return ltScore;
    }

    /**
     * 获取 景点联系电话 匹配分值
     * @param ctripPoiPhone 携程 景点联系电话
     * @param supplierPoiPhone 供应商 景点联系电话
     * @return
     */
    public static int getPoiPhoneMatchScore(String ctripPoiPhone,String supplierPoiPhone){
        int phoneScore = 0;
        if(ctripPoiPhone==null || "".equals(ctripPoiPhone) || supplierPoiPhone==null || "".equals(supplierPoiPhone) || "暂无".equals(supplierPoiPhone)){
            return 1;
        }
        //发现ctrip自己的景点联系电话如果有多个时，间隔包含","、";"、"；"  这里统一处理为英文分号
        if(ctripPoiPhone.contains(STRING_REGIX_COMMA) || ctripPoiPhone.contains("；")){
        	ctripPoiPhone = ctripPoiPhone.replaceAll(STRING_REGIX_COMMA, STRING_REGIX_SEMICOLON).replaceAll("；", STRING_REGIX_SEMICOLON);
        }
        //链景数据景点联系电话含多个时，间隔符包括","  "、"   统一处理为英文逗号
        if(supplierPoiPhone.contains("、")){
        	supplierPoiPhone = supplierPoiPhone.replaceAll("、", STRING_REGIX_COMMA);
        }
        String[] ctripPhones ;
        String[] supplierPhones ;
        int i,j;
        if(ctripPoiPhone.contains(STRING_REGIX_SEMICOLON) && supplierPoiPhone.contains(STRING_REGIX_COMMA)){
            ctripPhones = ctripPoiPhone.split(STRING_REGIX_SEMICOLON);
            supplierPhones = supplierPoiPhone.split(STRING_REGIX_COMMA);
            if(ctripPhones.length>supplierPhones.length){
                for(i=0;i<supplierPhones.length;i++){
                    for(j=0;j<ctripPhones.length;j++){
                    	supplierPhones[i] = (pattern.matcher(supplierPhones[i])).replaceAll("").trim();
                    	ctripPhones[j] = (pattern.matcher(ctripPhones[j])).replaceAll("").trim();
                        if(supplierPhones[i].equals(ctripPhones[j])){
                            phoneScore = 100;
                            break;
                        }
                    }
                }
            }else{
                for(i=0;i<ctripPhones.length;i++){
                    for(j=0;j<supplierPhones.length;j++){
                    	supplierPhones[i] = (pattern.matcher(supplierPhones[i])).replaceAll("").trim();
                    	ctripPhones[j] = (pattern.matcher(ctripPhones[j])).replaceAll("").trim();
                        if(supplierPhones[i].equals(ctripPhones[j])){
                            phoneScore = 100;
                            break;
                        }
                    }
                }
            }
        }
        if(ctripPoiPhone.contains(STRING_REGIX_SEMICOLON) && !supplierPoiPhone.contains(STRING_REGIX_COMMA)){
        	ctripPhones = ctripPoiPhone.split(STRING_REGIX_SEMICOLON);
        	for (String ctripPhone : ctripPhones) {
        		ctripPhone = (pattern.matcher(ctripPhone)).replaceAll("").trim();
        		supplierPoiPhone = (pattern.matcher(supplierPoiPhone)).replaceAll("").trim();
        		if(ctripPhone.equals(supplierPoiPhone)){
        			phoneScore = 100;
        			break;
        		}
			}
        }
        if(!ctripPoiPhone.contains(STRING_REGIX_SEMICOLON) && supplierPoiPhone.contains(STRING_REGIX_COMMA)){
        	supplierPhones = supplierPoiPhone.split(STRING_REGIX_COMMA);
        	for (String supplierPhone : supplierPhones) {
        		ctripPoiPhone = (pattern.matcher(ctripPoiPhone)).replaceAll("").trim();
        		supplierPhone = (pattern.matcher(supplierPhone)).replaceAll("").trim();
        		if(supplierPhone.equals(ctripPoiPhone)){
        			phoneScore = 100;
        			break;
        		}
			}
        }
        if(!ctripPoiPhone.contains(STRING_REGIX_SEMICOLON) && !supplierPoiPhone.contains(STRING_REGIX_COMMA)){
        	ctripPoiPhone = (pattern.matcher(ctripPoiPhone)).replaceAll("").trim();
        	supplierPoiPhone = (pattern.matcher(supplierPoiPhone)).replaceAll("").trim();
            if(supplierPoiPhone.equals(ctripPoiPhone)){phoneScore = 100;}
        }
        return phoneScore;
    }


    /**
     * 比较两个字符串的相似度方法
     * @param str
     * @param target
     * @return
     */
    private static double getSimilarityRatio(String str, String target){
        int d[][];              //矩阵
        int n = str.length();
        int m = target.length();
        int i;                  // 遍历str的
        int j;                  // 遍历target的
        char ch1;               // str的
        char ch2;               // target的
        int temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) { return m; }
        if (m == 0) { return n; }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++){                       // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++){                       // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++){                       // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++){
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2+32 || ch1+32 == ch2){
                    temp = 0;
                } else{
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return 1 - (double) d[n][m] / Math.max(str.length(), target.length());
    }

    /**
     * 获取三个int对象的最小值
     * @param one
     * @param two
     * @param three
     * @return
     */
    private static int min(int one, int two, int three){
        return (one = one < two ? one : two) < three ? one : three;
    }

    public static void main(String[] args) {
    	MatchRuleUtil matchRuleTest = new MatchRuleUtil();
        /*MCtripPoi ctripPOI = new MCtripPoi(000001L,"上海","中国","上海城隍庙","sh_chenghuang temple","城隍庙","上海","31.2378,121.474101","31.2378,121.474101",
                "上海市黄浦区方浜中路249号","021-63284494",true);

        MSupplierPoi supplierPOI = new MSupplierPoi("上海城隍庙","sh_chenghuang temple","城隍庙","上海","31.2378,121.574101","31.2378,121.574101","上海市黄浦区方浜中路249号","021-63284494","2017001");

        int socre = matchRuleTest.getMatchScore(ctripPOI, supplierPOI);
        System.out.println(socre);*/
        //System.out.println(matchRuleTest.getSimilarityRatio("Zhanyuan Palace", "Zhanyuan Palace"));
        
        System.out.println("试试"+("中!国·#@! chinese 山122df东济南").replaceAll(regExCh, ""));
    }
}
