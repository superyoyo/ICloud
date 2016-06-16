package com.autonavi.icloud.callback;

import android.app.Activity;

public abstract class SaveCallBack {
	private Activity activity;
	public SaveCallBack(Activity activity) {
		this.activity = activity;
	}

	public Activity getActivity(){
		return activity;
	}

	public abstract void saveDone(String tag,Exception exception);
}	
