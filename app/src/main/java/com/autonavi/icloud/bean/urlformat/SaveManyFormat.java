package com.autonavi.icloud.bean.urlformat;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lj88868 on 2016/6/8.
 */
public class SaveManyFormat {
    private HashMap<String, Object> params;

    public SaveManyFormat(List<HashMap<String, Object>> datas) {
        params = new HashMap<>();
        params.put("data", new Gson().toJson(datas));
        params.put("action", "updateMany");
    }

    public HashMap<String, Object> getParams(){
        return params;
    }
}
