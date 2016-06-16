package com.autonavi.icloud.bean.urlformat;

import java.util.HashMap;

/**
 * Created by lj88868 on 2016/6/8.
 */
public class DeleteFormat {
    private HashMap<String, Object> params;

    public DeleteFormat(String objectId, String className) {
        params = new HashMap<>();
        params.put("objectId", objectId);
        params.put("action", "delete");
        params.put("className", className);
    }

    public HashMap<String, Object> getParams(){
        return params;
    }
}
