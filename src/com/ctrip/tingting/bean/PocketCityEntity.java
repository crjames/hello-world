package com.ctrip.tingting.bean;

import java.util.List;

/**
 * 调用口袋http://ctrip.api.kddaoyou.com/api/city/[城市key] 接口获取的返回对象
 * Created by wang.donga on 2017/8/14.
 */
public class PocketCityEntity {
    private String country;//国家名称
    private String city;//城市名称
    private double city_price;//景点对应城市的解锁价格，单位：元， 如果等于0的话表示该城市可以免费解锁
    private List<PocketCityItemEntity> site_list;//城市下包含的景点列表
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public double getCity_price() {
        return city_price;
    }
    public void setCity_price(double city_price) {
        this.city_price = city_price;
    }
    public List<PocketCityItemEntity> getSite_list() {
        return site_list;
    }
    public void setSite_list(List<PocketCityItemEntity> site_list) {
        this.site_list = site_list;
    }
    @Override
    public String toString() {
        return "PocketCityEntity [country=" + country + ", city=" + city
                + ", city_price=" + city_price + ", site_list=" + site_list
                + "]";
    }

}
