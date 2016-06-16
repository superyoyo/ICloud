package com.autonavi.icloud.bean.urlformat;

import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by lj88868 on 2016/6/8.
 */
public class UpdeteFormat {
    private HashMap<String, Object> params;

    public UpdeteFormat(String objectId, String className, HashMap<String, Object> data) {
        params = new HashMap<>();
        params.put("objectId", objectId);
        params.put("action", "update");
        params.put("className", className);
        params.put("data", new Gson().toJson(data));
    }

    public HashMap<String, Object> getParams(){
        return params;
    }
}
