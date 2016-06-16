package com.autonavi.icloud.bean.urlformat;

import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by lj88868 on 2016/6/8.
 */
public class SaveFormat {
    private HashMap<String, Object> params;

    public SaveFormat(HashMap<String, Object> datas, String className) {
        params = new HashMap<>();
        params.put("data", new Gson().toJson(datas));
        params.put("action", "save");
        params.put("className", className);
    }

    public HashMap<String, Object> getParams(){
        return params;
    }
}
