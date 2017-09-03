package com.ctrip.tingting.bean;

/**
 * 用于数据匹配的供应商poi对象
 * Created by wang.donga on 2017/8/14.
 */
public class MSupplierPoi {
    public MSupplierPoi(){}
    private String chName;//中文景点名称
    private String enName;//英文景点名称
    private String localName;//当地语言景点名称
    private String destinationName;//所属目的地名称（城市级别-中文）
    private String gdLT;//高德经纬度
    private String bdLT;//百度经纬度
    private String chAddress;//中文景点地址
    private String phone;//联系电话
    private String supplierType;//供应商类型2017001-口袋；2017002-链景；2017003-美景
    public String getChName() {
        return chName;
    }
    public void setChName(String chName) {
        this.chName = chName;
    }
    public String getEnName() {
        return enName;
    }
    public void setEnName(String enName) {
        this.enName = enName;
    }
    public String getLocalName() {
        return localName;
    }
    public void setLocalName(String localName) {
        this.localName = localName;
    }
    public String getDestinationName() {
        return destinationName;
    }
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
    public String getGdLT() {
        return gdLT;
    }
    public void setGdLT(String gdLT) {
        this.gdLT = gdLT;
    }
    public String getBdLT() {
        return bdLT;
    }
    public void setBdLT(String bdLT) {
        this.bdLT = bdLT;
    }
    public String getChAddress() {
        return chAddress;
    }
    public void setChAddress(String chAddress) {
        this.chAddress = chAddress;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getSupplierType() {
        return supplierType;
    }
    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }
    @Override
    public String toString() {
        return "SupplierPOI [chName=" + chName + ", enName=" + enName
                + ", localName=" + localName + ", destinationName="
                + destinationName + ", gdLT=" + gdLT + ", bdLT=" + bdLT
                + ", chAddress=" + chAddress + ", phone=" + phone + "]";
    }
    public MSupplierPoi(String chName, String enName, String localName,
                       String destinationName, String gdLT, String bdLT, String chAddress,
                       String phone, String supplierType) {
        super();
        this.chName = chName;
        this.enName = enName;
        this.localName = localName;
        this.destinationName = destinationName;
        this.gdLT = gdLT;
        this.bdLT = bdLT;
        this.chAddress = chAddress;
        this.phone = phone;
        this.supplierType = supplierType;
    }

}
