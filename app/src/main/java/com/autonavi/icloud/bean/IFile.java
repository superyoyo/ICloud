package com.autonavi.icloud.bean;

import android.util.Log;

import com.autonavi.okhttp.callback.SaveCallBack;
import com.autonavi.okhttp.helper.ProgressHelper;
import com.autonavi.okhttp.listener.impl.UIProgressListener;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by liuji on 16/6/30.
 */
public class IFile {
    private String path;//本地路径
    private String name;//存放在服务器的名字
    private String bucketName;//属于那个bucket
    private String url;//文件的url路径
    private static final OkHttpClient client = new OkHttpClient.Builder()
            //设置超时，不设置可能会报异常
            .connectTimeout(1000, TimeUnit.MINUTES)
            .readTimeout(1000, TimeUnit.MINUTES)
            .writeTimeout(1000, TimeUnit.MINUTES)
            .build();

    private IFile(String name, String path, String bucketName) {
        this.name = name;
        this.path = path;
        this.bucketName = bucketName;
    }

    /**
     * 创建IFile对象
     * @param bucketName 服务器的bucketName
     * @param name 服务器的名字
     * @param path 本地的路径
     */
    public static IFile withAbsoluteLocalPath(String bucketName, String name, String path){
        return new IFile(name, path, bucketName);
    }

    public void saveInBackground(final SaveCallBack saveCallBack, UIProgressListener uiProgressRequestListener){
        File file = new File(path);
        //构造上传请求，类似web表单
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("hello", "android")
                .addFormDataPart("photo", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"another\";filename=\"another.dex\""), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        //进行包装，使其支持进度回调
        final Request request = new Request.Builder().url(Urls.url).post(ProgressHelper.addProgressRequestListener(requestBody, uiProgressRequestListener)).build();
        //开始请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                saveCallBack.done(e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("TAG", response.body().string());
                try{
                    JSONObject ob = new JSONObject(response.body().string());
                }catch (Exception e){
                    e.printStackTrace();
                    saveCallBack.done(e);
                }
            }
        });

    }
}
