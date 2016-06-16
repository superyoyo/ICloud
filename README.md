# ICloud
快速建立你的App

ICloud--build you app quickly and quickly

**ICLOUD**
---

**概述**

简而言之，就是终端直接操纵数据库，完成简单的数据操作，这样可以减轻后台的一些压力，对于一些只需简单查询即可完成的需求，不用后台再去写对于API。

---

**保存对象**：

    IObject iObject = new IObject("user", "registe");
    iObject.put("name", "JackLee");
    iObject.put("age", 23);
    iObject.saveInBackGround(saveCallBack);
    
    private SaveCallBack saveCallBack = new SaveCallBack(this) {
        @Override
        public void saveDone(String tag, Exception exception) {
            if(tag.equals("registe")){
                if(exception != null){
                    exception.printStackTrace();
                }else{
                    Log.i(tag, "MainActivity --> saveDone--> registe success!!!");
                }
            }
        }
    };

其中`new IObject("user", "registe"); 
    “user”表示数据库中的表名
    “registe”表示这次操作的唯一标示，因为共用一个saveCallBack，
    所以需要在回调中作区分
    
其中 

    iObject.saveInBackGround(saveCallBack);
    
    调用saveInBackGround异步将该对象数据保存到服务器数据库
    
 ---   
**删除对象**

    IObject iObject = new IObject(objectId, "delete", "user");
    iObject.deleteInBackGround(deleteCallBack);
    
    private DeleteCallBack deleteCallBack = new DeleteCallBack(this) {
        @Override
        public void deleteDone(String tag, Exception exception) {
            if(tag.equals("delete")){
                if (exception != null){
                    exception.printStackTrace();
                }else{
                    Log.i(tag,"MainActivity --> deleteDone--> delete success!!!");
                }
            }
        }
    };
objectId为该对象的服务器返回的Id，是该对象的唯一标示
delete是这次删除动作的唯一标示，因为共用同一个deleteCallback，
所以需要做区分
user是对象的表名

*注意：删除的时候，会检测objectId是否为空，如果为空，则不让其删除*

    调用deleteInBackGround会将服务器对应的对象给删除掉

---
**修改对象**

    IObject iObject = new IObject(objectId, "update", "user");
    iObject.put("name", "JackHaHa");
    iObject.put("age", 26);
    iObject.updateInBackGround(updateCallBack);
    
    private UpdateCallBack updateCallBack = new UpdateCallBack(this) {
        @Override
        public void updateDone(String tag, Exception exception) {
            if(tag.equals("update")){
                if (exception != null){
                    exception.printStackTrace();
                }else{
                    Log.i(tag,"MainActivity --> deleteDone--> update success!!!");
                }
            }
        }
    };
    
objectId为该对象的服务器返回的Id，是该对象的唯一标示
update是这修改动作的唯一标示，因为共用同一个updateCallback，
所以需要做区分
user是对象的表名

*注意：修改的时候，会检测objectId是否为空，如果为空，则不让其修改*

    调用updateInBackGround会将服务器对应的对象给更新掉

---

**获得对象**

    IObject iObject = new IObject(objectId, "get", "user");
    List<String> keys = new ArrayList<>();
    keys.add("name");
    keys.add("age");
    iObject.setKeys(keys);
    iObject.getInBackGround(getCallBack);
    
    private GetCallBack getCallBack = new GetCallBack(this) {
        @Override
        public void getDone(String tag, IObject obj, Exception exception) {
            if(tag.equals("get")){
                if(exception != null){
                    exception.printStackTrace();
                }else{
                    Log.i(tag,"MainActivity --> getDone--> IObject:" + obj.get("name"));
                }
            }
        }
    };
    
objectId为该对象的服务器返回的Id，是该对象的唯一标示
get，因为共用同一个getCallback，所以需要做区分
user是对象的表名

*keys标示要返回的字段，譬如，查询好友信息的时候，不能把好友的密码也查出来，不安全，所以需要设置返回字段，默认全返回*

*注意：获取对象的时候，会检测objectId是否为空，如果为空，则不让其获取*

    调用getInBackGround会将服务器对应的对象给获取下来

---

**查询对象**
1.构造查询
    
    IQuery query = new IQuery("user", "find");
    
2.匹配查询条件

    query.whereEqualTo(filed, XXX); //where filed = XXX
    
    query.whereNotEqualTo(filed, XXX); //where filed != XXX
    
    query.whereContains(filed, "value"); //where like filed '%value%'
    
    query.whereStartWith(filed, "value"); //where like filed 'value%'
    
    query.whereEndWith(filed, "value"); //where like filed '%value'
    
    query.whereGreaterThan(filed, 100); //where filed > 100
    query.whereLessThan(filed, 100); //where filed < 100
    
    query.whereGreaterThanEqualTo(filed, 100);//where filed >= 100
    
    query.whereLessThanEqualTo(filed, 100);//where filed <= 100
    
    query.whereNear(latName, lngName, IPoint);//以IPoint为基准查询附近的数据，并且按照由近到远的顺序排列，若要查看每个点与基准的的距离，可以再回调中，对获取到的IObject调用get("distance");
    
    query.whereNearWithInKilometers(latName, lngName, IPoint, kilometer);// whereNear的基础上，指定查询范围，譬如5公里内的点的排序
    
    query.setLimit(limit);//设置返回的结果条数
    
    query.orderByASC(filed);//根据filed进行从小到大排序
    
    query.orderByDESC(filed);//根据filed进行从大到小排序
    
    query.setKeys(List<String>);//设置要返回的字段
    
    query.findInBackGround(FindCallBack);//异步向服务器查询
    
3.进行异步查询

    query.findInBackGround(findCallBack);
    
    
    private FindCallBack findCallBack = new FindCallBack(this) {
        @Override
        public void findDone(String tag, List<IObject> list, Exception exception) {
            if(tag.equals("find")){
                if(exception != null){
                    exception.printStackTrace();
                }else{
                    for(IObject obj : list){
                        Log.i(tag,"MainActivity --> findDone--> className:" + obj.getClassName() + " distance:" + obj.get("distance")
                        + " name:" + obj.get("name") + " id:" + obj.getObjectId());
                    }
                }
            }
        }
    };

    


   
    
    
