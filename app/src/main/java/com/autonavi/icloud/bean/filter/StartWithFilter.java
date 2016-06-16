package com.autonavi.icloud.bean.filter;


/**
 * Created by lj88868 on 2016/6/8.
 */
public class StartWithFilter extends IFilter{
    private String key;
    private Object value;

    public StartWithFilter(String key, Object value) {
        this.tag = "startwith";
        this.key = key;
        this.value = value;
    }
}
