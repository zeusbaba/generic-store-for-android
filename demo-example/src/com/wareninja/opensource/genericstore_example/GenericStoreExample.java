package com.wareninja.opensource.genericstore_example;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GenericStoreExample extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generic_store_example);
		
		/* TODO: implement example for usage of GenericStore, covering
         * - ObjectCache
         * - SharedPrefs
         * - ImageCache
        */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.generic_store_example, menu);
		return true;
	}

}
