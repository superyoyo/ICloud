package com.autonavi.icloud.bean;

import android.text.TextUtils;
import android.util.Log;

import com.autonavi.icloud.bean.filter.BetweenFilter;
import com.autonavi.icloud.bean.filter.ContainFilter;
import com.autonavi.icloud.bean.filter.EndWithFilter;
import com.autonavi.icloud.bean.filter.EqualToFilter;
import com.autonavi.icloud.bean.filter.GreaterThanEqualToFilter;
import com.autonavi.icloud.bean.filter.GreaterThanFilter;
import com.autonavi.icloud.bean.filter.IFilter;
import com.autonavi.icloud.bean.filter.LessThanEqualToFilter;
import com.autonavi.icloud.bean.filter.LessThanFilter;
import com.autonavi.icloud.bean.filter.NotContainFilter;
import com.autonavi.icloud.bean.filter.NotEqualToFilter;
import com.autonavi.icloud.bean.filter.StartWithFilter;
import com.autonavi.icloud.bean.urlformat.FindFormat;
import com.autonavi.icloud.callback.FindCallBack;
import com.autonavi.okhttp.util.XNetOKHttp;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lj88868 on 2016/6/8.
 */
public class IQuery {
    private List<IFilter> filter;
    private String className;//要查询的对象的名字
    private String action;//用该查询去干啥的唯一标识，为了在回调中进行区别对待
    private int limit = -1;//要查询多少条 若为-1 则不对条数做限制
    private String orderBy = "";//排序语句
    private List<String> keys;//要返回的字段
    public IQuery(String className, String action) {
        this.className = className;
        this.action = action;
        filter = new ArrayList<>();
        keys = new ArrayList<>();
    }

    public IQuery(String className) {
        this.className = className;
        filter = new ArrayList<>();
    }

    public void setAction(String action){
        this.action = action;
    }

    /**
     * 添加查询条件 key = value
     * @param key 属性
     * @param value 属性值
     */
    public void whereEqualTo(String key, Object value){
        EqualToFilter etf = new EqualToFilter(key, value);
        filter.add(etf);
    }

    /**
     * 添加查询条件 key != value
     * @param key 属性
     * @param value 属性值
     */
    public void whereNotEqualTo(String key, Object value){
        NotEqualToFilter netf = new NotEqualToFilter(key, value);
        filter.add(netf);
    }

    /**
     * 添加查询条件 key > value
     * @param key 属性
     * @param value 属性值
     */
    public void whereGreaterThan(String key, double value){
        GreaterThanFilter gtf = new GreaterThanFilter(key, value);
        filter.add(gtf);
    }

    /**
     * 添加查询条件 key < value
     * @param key 属性
     * @param value 属性值
     */
    public void whereLessThan(String key, double value){
        LessThanFilter ltf = new LessThanFilter(key, value);
        filter.add(ltf);
    }

    /**
     * 添加查询条件 key >= value
     * @param key 属性
     * @param value 属性值
     */
    public void whereGreaterThanEqualTo(String key, double value){
        GreaterThanEqualToFilter gtetf = new GreaterThanEqualToFilter(key, value);
        filter.add(gtetf);
    }

    /**
     * 添加查询条件 key <= value
     * @param key 属性
     * @param value 属性值
     */
    public void whereLessThanEqualTo(String key, double value){
        LessThanEqualToFilter ltetf = new LessThanEqualToFilter(key, value);
        filter.add(ltetf);
    }

    /**
     * 添加查询条件 where key like '%value%'
     * @param key 属性
     * @param value 属性值
     */
    public void whereContains(String key, String value){
        ContainFilter cf = new ContainFilter(key, "%" +value + "%");
        filter.add(cf);
    }

    /**
     * 添加查询条件 where objectId not in (select * from className where key like '%value%')
     * @param key 属性
     * @param value 属性值
     */
    public void whereNotContains(String key, String value){
        NotContainFilter ncf = new NotContainFilter(key, value);
        filter.add(ncf);
    }
    /**
     * 以指定字符创开头 添加查询条件 where key like 'value%'
     * @param key 属性
     * @param value 属性值
     */
    public void whereStartWith(String key, String value){
        StartWithFilter swf = new StartWithFilter(key, value + "%");
        filter.add(swf);
    }

    /**
     * 以指定字符串结尾 添加查询条件 where key like '%value'
     * @param key 属性
     * @param value 属性值
     */
    public void whereEndWith(String key, String value){
        EndWithFilter ewf = new EndWithFilter(key, "%" + value);
        filter.add(ewf);
    }

    /**
     * 根据指定位置查询附近的对象，由近到远
     * @param latName
     * @param lngName
     * @param point
     */
    public void whereNear(String latName, String lngName, IPoint point){
        String key = "(abs(" +latName+ " - " +point.getLat()+ ") + abs(" +lngName+ " - " +point.getLng()+ ")) as distance";
        keys.add(0, key.toString());
        orderByASC("distance");
    }

    /**
     * 查询指定范围内的附近的点，排序：由近到远
     * @param latName
     * @param lngName
     * @param point
     * @param kilometers
     */
    public void whereNearWithInKilometers(String latName, String lngName, IPoint point, int kilometers){
        double lat=point.getLat();
        double lng=point.getLng();
        double range = 180 / Math.PI * kilometers/ 6372.797;      //里面的 kilometers就代表搜索 kilometers km 之内，单位km
        double lngR = range / Math.cos(lat * Math.PI / 180.0);
        double maxLat = lat + range;
        double minLat = lat - range;
        double maxLng = lng + lngR;
        double minLng = lng - lngR;
        BetweenFilter bf1at = new BetweenFilter(latName, minLat+" and "+maxLat);
        Log.i("liuji", "IQuery --> whereNearWithInKilometers--> " + new Gson().toJson(bf1at));
        filter.add(bf1at);
        BetweenFilter bflng = new BetweenFilter(lngName, minLng+" and "+maxLng);
        filter.add(bflng);
        whereNear(latName, lngName, point);
    }
    /**
     * 设置查询返回条数
     * @param limit 要返回多少条数据
     */
    public void setLimit(int limit){
        this.limit = limit;
    }

    public void orderByASC(String key){
        orderBy = "order by " + key + " asc";
    }

    public void orderByDESC(String key){
        orderBy = "order by " + key + " desc";
    }

    /**
     * 设置查询返回的字段
     * @param keys
     */
    public void setKeys(List<String> keys){
        this.keys.addAll(keys);
    }
    /**
     * 查询方法
     * @param findCallBack
     */
    public void findInBackGround(final FindCallBack findCallBack){
        if(TextUtils.isEmpty(className)){
            findCallBack.findDone(action, null, new Exception("className不能为空"));
        }else if(TextUtils.isEmpty(action)){
            //TODO action为空不知道怎么提示
        }else{
            FindFormat ff = null;
            if(keys == null){
                ff = new FindFormat(className, filter, limit, orderBy);
            }else{
                ff = new FindFormat(className, filter, keys, limit, orderBy);
            }
            XNetOKHttp xnet = XNetOKHttp.getInstance(findCallBack.getActivity());
            xnet.addPostTask(Urls.url, ff.getParams(), action, new com.autonavi.okhttp.callback.FindCallBack() {
                @Override
                public void done(String tag, String result, Exception e) {
                    Log.i("liuji","IQuery --> done--> result:" + result);
                    if(e == null){
                        try {
                            List<IObject> list = new ArrayList<IObject>();
                            JSONArray arr = new JSONArray(result);
                            Log.i("liuji","IQuery --> done--> arr:" + arr.length());
                            for(int i = 0, n = arr.length(); i < n; i++){
                                IObject obj = IObject.convertJsonToIObject(arr.getJSONObject(i));
                                if(obj != null){
                                    list.add(obj);
                                }
                            }
                            findCallBack.findDone(tag, list, null);
                        }catch (Exception e1){
                            e1.printStackTrace();
                            findCallBack.findDone(tag, null, e1);
                        }
                    }else{
                        findCallBack.findDone(tag, null, e);
                    }
                }
            });
        }
    }
}
