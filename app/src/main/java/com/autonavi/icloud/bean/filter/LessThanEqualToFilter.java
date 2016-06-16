package com.autonavi.icloud.bean.filter;


/**
 * Created by lj88868 on 2016/6/8.
 */
public class LessThanEqualToFilter extends IFilter{
    private String key;
    private Object value;

    public LessThanEqualToFilter(String key, Object value) {
        this.tag = "<=";
        this.key = key;
        this.value = value;
    }

}
