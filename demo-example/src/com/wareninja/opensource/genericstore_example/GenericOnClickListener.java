package com.wareninja.opensource.genericstore_example;


import android.os.Bundle;
import android.util.Log;
import android.view.View;


public abstract class GenericOnClickListener implements android.view.View.OnClickListener {

	private static final String TAG = GenericOnClickListener.class.getSimpleName();
	
	@Override
	public void onClick(View v) {
		if(AppContext.isDebugMode())Log.d(TAG, "onClick():" + v.getTag());
	}
	
	public void onComplete(String response) {
		if(AppContext.isDebugMode())Log.d(TAG, "onComplete():" + response);
	}
	public void onComplete_wBundle(Bundle params) {
		if(AppContext.isDebugMode())Log.d(TAG, "onComplete_wBundle():" + params);
	}
	
    public void onError(String e) {
    	if(AppContext.isDebugMode())Log.d(TAG, "onError():" + e);
    }
	
}
