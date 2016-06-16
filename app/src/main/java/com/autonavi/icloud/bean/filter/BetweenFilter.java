package com.autonavi.icloud.bean.filter;


/**
 * Created by lj88868 on 2016/6/8.
 */
public class BetweenFilter extends IFilter{
    private String key;
    private Object value;

    public BetweenFilter(String key, Object value) {
        this.tag = "between";
        this.key = key;
        this.value = value;
    }

}
