package com.ctrip.tingting.bean;

/**
 * 调用链景接口
 * 4.3获取景区实体
 * 举例：https://op.lianjinglx.com/scenic.ashx?cmd=FindScenic&p=
 * {"Area":"北京","Name":"天坛"}&salt=1469415549&sign=130f4d3bdc2b991c011e0bb0dc313e2d&tag=10000&lang=1&version=1.0
 * 获取的对象
 * Created by wang.donga on 2017/8/14.
 */
public class LJScenicEntity {
    private String state;//0-成功，1-失败
    private String msg;//错误消息,成功该值为空
    private String value;//参数集合
    private String pagecount;//总页数
    private String extend;//总条数
	public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getPagecount() {
		return pagecount;
	}
	public void setPagecount(String pagecount) {
		this.pagecount = pagecount;
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
    @Override
	public String toString() {
		return "LJScenicEntity [state=" + state + ", msg=" + msg + ", value="
				+ value + ", pagecount=" + pagecount + ", extend=" + extend
				+ "]";
	}
}
