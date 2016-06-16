package com.autonavi.icloud.callback;

import android.app.Activity;

import java.util.HashMap;
import java.util.List;

import com.autonavi.icloud.bean.IObject;

public abstract class FindCallBack {
	private Activity activity;
	public FindCallBack(Activity activity) {
		this.activity = activity;
	}

	public Activity getActivity(){
		return activity;
	}
	/**
	 * 查询完毕后的回调
	 * @param tag 标示是哪个查询对象进行的查找
	 * @param list
	 * @param exception 操作返回的异常信息（包涵操作失败的信息说明）
	 */
	public abstract void findDone(String tag, List<IObject> list, Exception exception);
}	
