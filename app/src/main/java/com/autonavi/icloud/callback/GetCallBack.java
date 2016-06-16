package com.autonavi.icloud.callback;

import android.app.Activity;

import com.autonavi.icloud.bean.IObject;

import java.util.HashMap;
import java.util.List;

public abstract class GetCallBack {
	private Activity activity;
	public GetCallBack(Activity activity) {
		this.activity = activity;
	}

	public Activity getActivity(){
		return activity;
	}
	/**
	 * 查询完毕后的回调
	 * @param tag 标示是哪个查询对象进行的查找
	 * @param obj
	 * @param exception 操作返回的异常信息（包涵操作失败的信息说明）
	 */
	public abstract void getDone(String tag, IObject obj, Exception exception);
}	
