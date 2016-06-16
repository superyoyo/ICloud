package com.autonavi.icloud.bean.urlformat;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
/**
 * Created by lj88868 on 2016/6/8.
 */
public class GetFormat {
    private HashMap<String, Object> params;

    public GetFormat(String objectId, String className, List<String> keys) {
        params = new HashMap<>();
        params.put("objectId", objectId);
        params.put("action", "get");
        params.put("className", className);
        params.put("keys", new Gson().toJson(keys));
    }

    public GetFormat(String objectId, String className) {
        params = new HashMap<>();
        params.put("objectId", objectId);
        params.put("action", "get");
        params.put("className", className);
    }

    public HashMap<String, Object> getParams(){
        return params;
    }
}
