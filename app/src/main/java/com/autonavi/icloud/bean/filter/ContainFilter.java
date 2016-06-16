package com.autonavi.icloud.bean.filter;


/**
 * Created by lj88868 on 2016/6/8.
 */
public class ContainFilter extends IFilter{
    private String key;
    private Object value;

    public ContainFilter(String key, Object value) {
        this.tag = "like";
        this.key = key;
        this.value = value;
    }

}
