package com.ctrip.tingting.bean;

/**
 * 调用链景供应商接口时的入参对象
 * Created by wang.donga on 2017/8/14.
 */
public class LJRequestParam {
    private String Area;
    private String Name;
    private String PageIndex;
    private String PageSize;
    private String Code;
    private String Unix;
    public String getArea() {
        return Area;
    }
    public void setArea(String area) {
        Area = area;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getPageIndex() {
        return PageIndex;
    }
    public void setPageIndex(String pageIndex) {
        PageIndex = pageIndex;
    }
    public String getPageSize() {
        return PageSize;
    }
    public void setPageSize(String pageSize) {
        PageSize = pageSize;
    }
    public String getCode() {
        return Code;
    }
    public void setCode(String code) {
        Code = code;
    }
    public String getUnix() {
        return Unix;
    }
    public void setUnix(String unix) {
        Unix = unix;
    }
}
