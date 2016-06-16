package com.autonavi.icloud.callback;

import android.app.Activity;

public abstract class UpdateCallBack {
	private Activity activity;
	public UpdateCallBack(Activity activity) {
		this.activity = activity;
	}

	public Activity getActivity(){
		return activity;
	}

	public abstract void updateDone(String tag, Exception exception);
}	
