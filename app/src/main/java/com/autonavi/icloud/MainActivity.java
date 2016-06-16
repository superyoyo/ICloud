package com.autonavi.icloud;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.autonavi.icloud.bean.IObject;
import com.autonavi.icloud.bean.IPoint;
import com.autonavi.icloud.bean.IQuery;
import com.autonavi.icloud.callback.DeleteCallBack;
import com.autonavi.icloud.callback.FindCallBack;
import com.autonavi.icloud.callback.GetCallBack;
import com.autonavi.icloud.callback.SaveCallBack;
import com.autonavi.icloud.callback.UpdateCallBack;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private String objectId = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void save(View view){
        IObject iObject = new IObject("user", "registe");
        iObject.put("name", "JackLee");
        iObject.put("age", 23);
        iObject.saveInBackGround(saveCallBack);
    }

    public void delete(View view){
        IObject iObject = new IObject(objectId, "delete", "user");
        iObject.deleteInBackGround(deleteCallBack);
    }

    public void update(View view){
        IObject iObject = new IObject("1", "update", "user");
        iObject.put("name", "JackHaHa");
        iObject.put("age", 26);
        iObject.updateInBackGround(updateCallBack);
    }

    public void get(View view){
        IObject iObject = new IObject("1", "get", "user");
        List<String> keys = new ArrayList<>();
        keys.add("name");
        keys.add("age");
        iObject.setKeys(keys);
        iObject.getInBackGround(getCallBack);
    }

    public void find(View view){
        IQuery query = new IQuery("user", "find");
        //query.whereEqualTo("station_name", "毛纺路北口");
        /*query.whereEqualTo("city_code", "010");
        query.whereContains("station_name", "西");*/
        /*query.whereGreaterThan("age", 23);*/
        query.setLimit(10);
        //query.whereNearsheWithInKilometers("lat", "lng", new IPoint(39.992706, 116.396574), 3);
        query.whereNear("lat", "lng", new IPoint(39.992706, 116.396574));
        List<String> keys = new ArrayList<>();
        /*keys.add("station_name");
        keys.add("station_id");*/
        query.setKeys(keys);
        query.findInBackGround(findCallBack);
    }

    private SaveCallBack saveCallBack = new SaveCallBack(this) {
        @Override
        public void saveDone(String tag, Exception exception) {
            if(tag.equals("registe")){
                if(exception != null){
                    exception.printStackTrace();
                }else{
                    Log.i("liuji", "MainActivity --> saveDone--> registe success!!!");
                }
            }
        }
    };

    private DeleteCallBack deleteCallBack = new DeleteCallBack(this) {
        @Override
        public void deleteDone(String tag, Exception exception) {
            if(tag.equals("delete")){
                if (exception != null){
                    exception.printStackTrace();
                }else{
                    Log.i("liuji","MainActivity --> deleteDone--> delete success!!!");
                }
            }
        }
    };

    private UpdateCallBack updateCallBack = new UpdateCallBack(this) {
        @Override
        public void updateDone(String tag, Exception exception) {
            if(tag.equals("update")){
                if (exception != null){
                    exception.printStackTrace();
                }else{
                    Log.i("liuji","MainActivity --> deleteDone--> update success!!!");
                }
            }
        }
    };

    private GetCallBack getCallBack = new GetCallBack(this) {
        @Override
        public void getDone(String tag, IObject obj, Exception exception) {
            if(tag.equals("get")){
                if(exception != null){
                    exception.printStackTrace();
                }else{
                    Log.i("liuji","MainActivity --> getDone--> IObject:" + obj.get("name"));
                }
            }
        }
    };

    private FindCallBack findCallBack = new FindCallBack(this) {
        @Override
        public void findDone(String tag, List<IObject> list, Exception exception) {
            if(tag.equals("find")){
                if(exception != null){
                    exception.printStackTrace();
                }else{
                    Log.i("liuji","MainActivity --> getDone--> list:" + list);
                    for(IObject obj : list){
                        Log.i("liuji","MainActivity --> findDone--> className:" + obj.getClassName() + " distance:" + obj.get("distance")
                        + " name:" + obj.get("name") + " id:" + obj.getObjectId());
                    }
                }
            }
        }
    };
}