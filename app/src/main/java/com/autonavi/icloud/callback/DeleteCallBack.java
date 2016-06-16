package com.autonavi.icloud.callback;

import android.app.Activity;


public abstract class DeleteCallBack {
	private Activity activity;
	public DeleteCallBack(Activity activity) {
		this.activity = activity;
	}

	public Activity getActivity(){
		return activity;
	}
	/**
	 * 删除完毕后的回调
	 * @param tag 标示是哪个对象进行的删除
	 * @param exception 操作返回的异常信息（包涵操作失败的信息说明）
	 */
	public abstract void deleteDone(String tag,Exception exception);
}	
