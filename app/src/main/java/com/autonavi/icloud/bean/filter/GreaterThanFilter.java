package com.autonavi.icloud.bean.filter;

import com.google.gson.Gson;

/**
 * Created by lj88868 on 2016/6/8.
 */
public class GreaterThanFilter extends IFilter{
    private String key;
    private Object value;

    public GreaterThanFilter(String key, Object value) {
        this.tag = ">";
        this.key = key;
        this.value = value;
    }

}
