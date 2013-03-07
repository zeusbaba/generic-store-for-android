/***
 * 	Copyright (c) 2013 WareNinja.com / BEERSTORM.net
 *  	
 *  @author yg@wareninja.com / twitter: @WareNinja
 *  @see http://www.WareNinja.com - https://github.com/WareNinja
 */

package com.wareninja.opensource.genericstore_example;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import com.wareninja.opensource.droidfu.cachefu.ImageCache;
import com.wareninja.opensource.droidfu.cachefu.ObjectCache;
import com.wareninja.opensource.genericstore.GenericStore;

public class MainApplication extends com.wareninja.opensource.genericstore.ApplicationWareNinja {
	
	private static String TAG = MainApplication.class.getSimpleName();
	
	private ImageCache imageCache = null;
	private ObjectCache objectCache = null;
	
	public MainApplication() {
		super(); 
		
		Thread.setDefaultUncaughtExceptionHandler(onBlooey);
	}
	
	@Override 
	public void onCreate() {
	
        // initialize CACHE once App is created, create ONCE only
        super.setMainCacheDirName_AppCache(AppContext.APP_CACHEDIR);
		objectCache = super.getObjectCache();
		imageCache = super.getImageCache();
		
		super.onCreate();
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Log.w(TAG, "onLowMemory() -> clearImageCacheFu!!!");
		clearImageCacheFu();
	}
	@Override
	public void onTerminate() {
		
		if (AppContext.isDebugMode()) {
			Log.d(TAG, "onTerminate() -> Clearing all cache-fu!!!");
			clearCacheFu(); 
		}
		setAppState(this, false);
				
    	super.onTerminate();
	}
	
	void goBlooey(Throwable t) {
		/*AlertDialog.Builder builder=new AlertDialog.Builder(this);
		
		builder
			.setTitle(R.string.exception) 
			.setMessage(t.toString())
			.setPositiveButton(R.string.ok, null)
			.show();
		*/
	}
	
	public ImageCache getImageCacheFu() {
		return imageCache;
	}
	public void clearImageCacheFu() {
		int itemCount = imageCache.removeAllObjects();
		if(AppContext.isDebugMode()) {
			Log.d(TAG, "cleared "
				+ itemCount 
				+ " items from ImageCache"
				+ "|" + imageCache.getDiskCacheDirectory()
				);
		}
	}
	public void clearObjectCacheFu() {
		int itemCount = objectCache.removeAllObjects();
		if(AppContext.isDebugMode()) {
			Log.d(TAG, "cleared " 
				+ itemCount
				+ " items from ObjectCache"
				+ "|" + objectCache.getDiskCacheDirectory()
				);
		}
	}
	public void clearCacheFu() {
		
		clearObjectCacheFu();
		clearImageCacheFu();
	}
	
	private Thread.UncaughtExceptionHandler onBlooey=
		new Thread.UncaughtExceptionHandler() {
		public void uncaughtException(Thread thread, Throwable ex) {
			Log.wtf(TAG, "Uncaught exception", ex);
			goBlooey(ex);
		}
	};
	
	
	// --- for Droid-Fu ---
	// the below funcs are copied from DroidFuApplication
	private HashMap<String, WeakReference<Context>> contextObjects = new HashMap<String, WeakReference<Context>>();
    public synchronized Context getActiveContext(String className) {
        WeakReference<Context> ref = contextObjects.get(className);
        if (ref == null) {
            return null;
        }

        final Context c = ref.get();
        if (c == null) // If the WeakReference is no longer valid, ensure it is removed.
            contextObjects.remove(className);

        return c;
    }
    public synchronized void setActiveContext(String className, Context context) {
        WeakReference<Context> ref = new WeakReference<Context>(context);
        this.contextObjects.put(className, ref);
    }
    public synchronized void resetActiveContext(String className) {
        contextObjects.remove(className);
    }
    /**
     * <p>
     * Invoked if the application is about to close. Application close is being defined as the
     * transition of the last running Activity of the current application to the Android home screen
     * using the BACK button. You can leverage this method to perform cleanup logic such as freeing
     * resources whenever your user "exits" your app using the back button.
     * </p>
     * <p>
     * Note that you must not rely on this callback as a general purpose "exit" handler, since
     * Android does not give any guarantees as to when exactly the process hosting an application is
     * being terminated. In other words, your application can be terminated at any point in time, in
     * which case this method will NOT be invoked.
     * </p>
     */
    public void onClose() {
        // NO-OP by default
    }
    //---
   
    /*
     * keep track of AppState
     */
    private boolean isAppActive = false;
    public void setAppState(Context context, boolean state) {
    	isAppActive = state;
    	GenericStore.setCustomData(GenericStore.TYPE.SHAREDPREF, AppContext.KEYSTORE._WARENINJA_SETTINGS_ISAPPACTIVE.name(), isAppActive, this);
	}
	public boolean getAppState(Context context) {
		isAppActive = GenericStore.getCustomBoolean(AppContext.KEYSTORE._WARENINJA_SETTINGS_ISAPPACTIVE.name(), this);
		return isAppActive;
	}
	
}