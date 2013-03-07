/***
 * 	Copyright (c) 2013 WareNinja.com / BEERSTORM.net
 *  	
 *  @author yg@wareninja.com / twitter: @WareNinja
 *  @see http://www.WareNinja.com - https://github.com/WareNinja
 */

package com.wareninja.opensource.genericstore_example;

import com.wareninja.opensource.common.WareNinjaUtils;
import com.wareninja.opensource.droidfu.widgets.WebImageView;
import com.wareninja.opensource.genericstore.GenericStore;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GenericStoreExample extends Activity {

	final String TAG = GenericStoreExample.class.getSimpleName();
	
	Context mContext;
	Activity mActivity;
	WebImageView img_webview = null;
	Button button_loadobject;
	TextView txt_loadobject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = this;
		mActivity = this;
		
		setContentView(R.layout.activity_generic_store_example);
		
		/* TODO: implement example for usage of GenericStore, covering
         * - ObjectCache
         * - SharedPrefs
         * - ImageCache
        */
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		((MainApplication)mContext.getApplicationContext()).setAppState(mContext, true);
		
		// 
		if (img_webview==null) {
			img_webview = ((WebImageView) findViewById(R.id.img_webview));
			// ensure that webimageview is using the shared memdiskcache
			img_webview.setPreInitImageCache( ((MainApplication)mContext.getApplicationContext()).getImageCacheFu() );
			// optional; set default image 
			img_webview.setNoImageDrawable(R.drawable.placeholder_default);
        }
		
		// set the url of the image & this will load in async way, and also by caching the image data in memdiskcache!!!
		img_webview.setImageUrl("http://graph.facebook.com/100001789213579/picture?type=large");
		img_webview.loadImage();
    	
		if (txt_loadobject==null) txt_loadobject = (TextView)findViewById(R.id.txt_loadobject);
		if (button_loadobject==null) button_loadobject = (Button)findViewById(R.id.button_loadobject);
		button_loadobject.setOnClickListener(mGenericOnClickListener);
	}
	@Override
	protected void onPause() {
		super.onPause();
		((MainApplication)mContext.getApplicationContext()).setAppState(mContext, false);
	}

	GenericOnClickListener mGenericOnClickListener = new GenericOnClickListener() {
		@Override
		public void onClick(View v) { 
			//WareNinjaUtils.hideSoftKeyboard(mContext, v);
			onClick_buttonAction(v);
		}
	};

	protected void onClick_buttonAction(View v) {
		String buttonTag = ""+v.getTag();
		if (AppContext.isDebugMode()) Log.d(TAG, "buttonTag->"+buttonTag);
		
		if (buttonTag.equals("button_loadobject")) {
			// generate SampleObject with random content
			SampleObject sampleObject = new SampleObject();
			sampleObject.oid = ""+System.currentTimeMillis();
			sampleObject.setCreatedAt(System.currentTimeMillis());
			sampleObject.dummy_data.put("key1", "val1");
			sampleObject.dummy_data.put("key2", "val2");
			sampleObject.dummy_data.put("key3", "val3");
			
			/* 
			 * NOTE: you can use GenericStore.TYPE.MEMDISKCACHE or GenericStore.TYPE.SHAREDPREF as the cache base
			 * below we will be using MEMDISKCACHE as part of the example
			*/
			// store in MEMDISKCACHE
			GenericStore.saveObject(GenericStore.TYPE.MEMDISKCACHE
					, AppContext.KEYSTORE._WARENINJA_SAMPLE_OBJECT.name(), sampleObject
					, mContext);
			
			// load from MEMDISKCACHE
			SampleObject sampleObject_fromCache = (SampleObject)GenericStore.getObject(GenericStore.TYPE.MEMDISKCACHE
					, AppContext.KEYSTORE._WARENINJA_SAMPLE_OBJECT.name()
					, mContext);
			
			// display the object content loaded from MEMDISKCACHE
			String text2display = sampleObject_fromCache==null ? "NULL" : sampleObject_fromCache.toString();
			txt_loadobject.setText( Html.fromHtml("object content <br/>---<br/> "+text2display) );
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.generic_store_example, menu);
		return true;
	}

}
