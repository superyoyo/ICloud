package com.autonavi.icloud.bean;

import android.text.TextUtils;
import android.util.Log;

import com.autonavi.icloud.bean.urlformat.DeleteFormat;
import com.autonavi.icloud.bean.urlformat.GetFormat;
import com.autonavi.icloud.bean.urlformat.SaveFormat;
import com.autonavi.icloud.bean.urlformat.UpdeteFormat;
import com.autonavi.icloud.callback.DeleteCallBack;
import com.autonavi.icloud.callback.GetCallBack;
import com.autonavi.icloud.callback.SaveCallBack;
import com.autonavi.icloud.callback.UpdateCallBack;
import com.autonavi.okhttp.callback.FindCallBack;
import com.autonavi.okhttp.util.XNetOKHttp;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lj88868 on 2016/6/8.
 */
public class IObject{
    private HashMap<String, Object> params;//该对象的所有参数
    private String className;//该对象的名字
    private String action;//用该对象去干啥的唯一标识，为了在回调中进行区别对待
    private String objectId;//该对象的服务器Id
    private List<String> keys;//要返回的值

    /**
     * 构造方法
     * @param className 该对象的数据库名字
     * @param action 该对象的意图
     */
    public IObject(String className, String action) {
        this.className = className;
        this.action = action;
        params = new HashMap<>();
    }

    /**
     * 构造方法
     * @param objectId 该对象的服务器Id，唯一标示
     * @param action 该对象的意图
     * @param className 该对象的数据库名字
     */
    public IObject(String objectId, String action, String className) {
        this.objectId = objectId;
        this.action = action;
        this.className = className;
        params = new HashMap<>();
    }

    /**
     * 获取该对象的className
     * @return 该对象的className
     */
    public String getClassName(){
        return className;
    }

    /**
     * 获取该对象的唯一Id
     * @return 服务器唯一Id
     */
    public String getObjectId(){
        return objectId;
    }

    /**
     * 为该对象设置数据库Id，方便查询
     * @param objectId
     */
    public void setObjectId(String objectId){
        this.objectId = objectId;
    }
    /**
     * 为该对象添加属性
     * @param key 该对象的属性
     * @param value 该对象对应属性的属性值
     */
    public void put(String key, Object value){
        params.put(key, value);
    }

    /**
     * 获取该对象的属性值
     * @param key 该对象的属性
     * @return key对应的属性值
     */
    public Object get(String key){
        if(params.containsKey(key)){
            return params.get(key);
        }
        return null;
    }

    public void setKeys(List<String> keys){
        this.keys = keys;
    }

    /**
     * 让服务器保存改对象
     * @param saveCallBack
     */
    public void saveInBackGround(final SaveCallBack saveCallBack){
        if(TextUtils.isEmpty(getClassName())){
            saveCallBack.saveDone(action, new Exception("className不能为空"), null);
        }else if(TextUtils.isEmpty(action)){
            //TODO action为空不知道怎么提示
        }else if(params == null && params.size() == 0){
            saveCallBack.saveDone(action, new Exception("params不能为空"), null);
        }else{
            SaveFormat sf = new SaveFormat(params, className);
            XNetOKHttp xnet = XNetOKHttp.getInstance(saveCallBack.getActivity());
            xnet.addPostTask(Urls.url, sf.getParams(), action, new FindCallBack() {
                @Override
                public void done(String tag, String result, Exception e) {
                    if(e == null){
                        try {
                            Integer.parseInt(result);
                            objectId = result;
                            saveCallBack.saveDone(tag, null, IObject.this);
                        }catch (Exception e1){
                            e1.printStackTrace();
                            saveCallBack.saveDone(tag, new Exception(result), null);
                        }
                    }else{
                        saveCallBack.saveDone(tag, e, null);
                    }
                }
            });
        }
    }

    /**
     * 同步保存
     * @throws Exception
     */
    public void save() throws Exception{
        if(TextUtils.isEmpty(getClassName())){
            throw new Exception("className不能为空");
        }else if(params == null && params.size() == 0){
            throw new Exception("params不能为空");
        }else{
            SaveFormat sf = new SaveFormat(params, className);
            XNetOKHttp xnet = XNetOKHttp.getInstance();
            XNetOKHttp.ResponseBean rb = xnet.addPostTask(Urls.url, sf.getParams());
            if(rb.getCode() == 0){
                try {
                    Integer.parseInt(rb.getResult());
                    objectId = rb.getResult();
                }catch (Exception e){
                    e.printStackTrace();
                    throw new Exception(rb.getResult());
                }
            }else{
                throw rb.getException();
            }
        }
    }

    /**
     * 让服务器删除该对象
     * @param deleteCallBack
     */
    public void deleteInBackGround(final DeleteCallBack deleteCallBack){
        if(TextUtils.isEmpty(getClassName())){
            deleteCallBack.deleteDone(action, new Exception("className不能为空"));
        }else if(TextUtils.isEmpty(action)){
            //TODO action为空不知道怎么提示
        }else if(TextUtils.isEmpty(objectId)){
            deleteCallBack.deleteDone(action, new Exception("objectId不能为空"));
        }else {
            DeleteFormat df = new DeleteFormat(objectId, className);
            XNetOKHttp xnet = XNetOKHttp.getInstance(deleteCallBack.getActivity());
            xnet.addPostTask(Urls.url, df.getParams(), action, new FindCallBack() {
                @Override
                public void done(String tag, String result, Exception e) {
                    if(e == null){
                        try{
                            int count = Integer.parseInt(result);
                            Log.i("liuji", "IObject --> done--> 删除受影响的条数是：" + count);
                            deleteCallBack.deleteDone(tag, null);
                        }catch (Exception e1){
                            deleteCallBack.deleteDone(tag, new Exception(result));
                        }
                    }else{
                        deleteCallBack.deleteDone(tag, e);
                    }
                }
            });
        }
    }

    /**
     * 同步删除
     * @throws Exception
     */
    public void delete() throws Exception{
        if(TextUtils.isEmpty(getClassName())){
            throw new Exception("className不能为空");
        }else if(TextUtils.isEmpty(objectId)){
            throw new Exception("objectId不能为空");
        }else {
            DeleteFormat df = new DeleteFormat(objectId, className);
            XNetOKHttp xnet = XNetOKHttp.getInstance();
            XNetOKHttp.ResponseBean rb = xnet.addPostTask(Urls.url, df.getParams());
            if(rb.getCode() == 0){
                try{
                    int count = Integer.parseInt(rb.getResult());
                    Log.i("liuji", "IObject --> done--> 删除受影响的条数是：" + count);
                }catch (Exception e1){
                    throw new Exception(rb.getResult());
                }
            }else{
                throw rb.getException();
            }
        }
    }
    /**
     * 更新一个对象的某些属性
     * @param updateCallBack
     */
    public void updateInBackGround(final UpdateCallBack updateCallBack){
        if(TextUtils.isEmpty(getClassName())){
            updateCallBack.updateDone(action, new Exception("className不能为空"));
        }else if(TextUtils.isEmpty(action)){
            //TODO action为空不知道怎么提示
        }else if(TextUtils.isEmpty(objectId)){
            updateCallBack.updateDone(action, new Exception("objectId不能为空"));
        }else {
            UpdeteFormat uf = new UpdeteFormat(objectId, className, params);
            XNetOKHttp xnet = XNetOKHttp.getInstance(updateCallBack.getActivity());
            xnet.addPostTask(Urls.url, uf.getParams(), action, new FindCallBack() {
                @Override
                public void done(String tag, String result, Exception e) {
                    if(e == null){
                        try{
                            int count = Integer.parseInt(result);
                            Log.i("liuji", "IObject --> done--> 更新受影响的条数是:" + count);
                            updateCallBack.updateDone(tag, null);
                        }catch (Exception e1){
                            updateCallBack.updateDone(tag, new Exception(result));
                        }
                    }else{
                        updateCallBack.updateDone(tag, e);
                    }
                }
            });
        }
    }

    /**
     * 同步进行更新
     * @throws Exception
     */
    public void update() throws Exception{
        if(TextUtils.isEmpty(getClassName())){
            throw new Exception("className不能为空");
        }else if(TextUtils.isEmpty(objectId)){
            throw new Exception("objectId不能为空");
        }else if(params == null || params.size() == 0){
            throw new Exception("params 不能为空");
        }else {
            UpdeteFormat uf = new UpdeteFormat(objectId, className, params);
            XNetOKHttp xnet = XNetOKHttp.getInstance();
            XNetOKHttp.ResponseBean rb = xnet.addPostTask(Urls.url, uf.getParams());
            if(rb.getCode() == 0){
                try{
                    int count = Integer.parseInt(rb.getResult());
                    Log.i("liuji", "IObject --> done--> 更新受影响的条数是:" + count);
                }catch (Exception e1){
                    throw new Exception(rb.getResult());
                }
            }else{
                throw rb.getException();
            }
        }
    }
    /**
     * 根据objectId获取该对象
     * @param getCallBack
     */
    public void getInBackGround(final GetCallBack getCallBack){
        if(TextUtils.isEmpty(getClassName())){
            getCallBack.getDone(action, null, new Exception("className不能为空"));
        }else if(TextUtils.isEmpty(action)){
            //TODO action为空不知道怎么提示
        }else if(TextUtils.isEmpty(objectId)){
            getCallBack.getDone(action, null, new Exception("objectId不能为空"));
        }else {
            GetFormat gf = null;
            if(keys == null){
                gf = new GetFormat(objectId, className);
            }else{
                gf = new GetFormat(objectId, className, keys);
            }
            XNetOKHttp xnet = XNetOKHttp.getInstance(getCallBack.getActivity());
            xnet.addPostTask(Urls.url, gf.getParams(), action, new FindCallBack() {
                @Override
                public void done(String tag, String result, Exception e) {
                    if(e == null){
                        IObject obj = null;
                        try {
                            obj = convertJsonToIObject(result);
                            getCallBack.getDone(tag, obj, null);
                        }catch (Exception e1){
                            getCallBack.getDone(tag, null, e1);
                        }
                    }else{
                        getCallBack.getDone(tag, null, e);
                    }
                }
            });
        }
    }

    /**
     * 同步获取
     * @return
     * @throws Exception
     */
    public IObject get() throws Exception{
        if(TextUtils.isEmpty(getClassName())){
            throw new Exception("className不能为空");
        }else if(TextUtils.isEmpty(objectId)){
            throw new Exception("objectId不能为空");
        }else {
            GetFormat gf = null;
            if(keys == null){
                gf = new GetFormat(objectId, className);
            }else{
                gf = new GetFormat(objectId, className, keys);
            }
            XNetOKHttp xnet = XNetOKHttp.getInstance();
            XNetOKHttp.ResponseBean rb = xnet.addPostTask(Urls.url, gf.getParams());
            if(rb.getCode() == 0){
                IObject obj = null;
                try {
                    obj = convertJsonToIObject(rb.getResult());
                    return obj;
                }catch (Exception e1){
                    throw e1;
                }
            }else{
                throw rb.getException();
            }
        }
    }

    /**
     * 将json串转化为IObject
     * @param iobj
     * @return
     * @throws Exception
     */
    public static IObject convertJsonToIObject(JSONObject iobj) throws Exception{
        IObject obj = null;
        if(iobj.has("className") && iobj.has("id")){
            obj = new IObject(iobj.get("id").toString(), "", iobj.getString("className"));
            Iterator<String> it = iobj.keys();
            while (it.hasNext()){
                String key = it.next();
                if(key.equals("id") || key.equals("className")){
                    continue;
                }
                Object value = iobj.get(key);
                if(key.equals("distance")){
                    try{
                        value = Math.sqrt((double)value);
                    }catch (Exception e){
                        //e.printStackTrace();
                    }
                }

                obj.put(key, value);
            }
        }
        return obj;
    }
    /**
     * 将json串转化为IObject
     * @param json
     * @return
     * @throws Exception
     */
    public static IObject convertJsonToIObject(String json) throws Exception{
        JSONObject iobj = new JSONObject(json);
        return convertJsonToIObject(iobj);
    }

}
