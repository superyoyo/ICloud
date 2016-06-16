package com.autonavi.icloud.bean.urlformat;

import android.text.TextUtils;

import com.autonavi.icloud.bean.filter.IFilter;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lj88868 on 2016/6/8.
 */
public class FindFormat {
    private HashMap<String, Object> params;

    public FindFormat(String className, List<IFilter> filters, List<String> keys, int limit, String orderBy) {
        params = new HashMap<>();
        params.put("action", "find");
        params.put("className", className);
        params.put("filters", new Gson().toJson(filters));
        if(keys != null && keys.size() > 0){
            if(keys.get(0).contains("as distance")){
                if(keys.size() == 1){
                    keys.add(0, "*");
                }
            }
            params.put("keys", keys);
        }
        if(limit != -1){
            params.put("limit", limit);
        }
        if(!TextUtils.isEmpty(orderBy)){
            params.put("orderBy", orderBy);
        }
    }

    public FindFormat(String className, List<IFilter> filters, int limit, String orderBy) {
        params = new HashMap<>();
        params.put("action", "find");
        params.put("className", className);
        params.put("filters", new Gson().toJson(filters));
        if(limit != -1){
            params.put("limit", limit);
        }
        if(!TextUtils.isEmpty(orderBy)){
            params.put("orderBy", orderBy);
        }
    }

    public HashMap<String, Object> getParams(){
        return params;
    }
}
