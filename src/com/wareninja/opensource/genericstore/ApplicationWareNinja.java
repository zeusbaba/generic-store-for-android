/***
 * 	Copyright (c) 2010-2011 WareNinja.com
 * 	Author: yg@wareninja.com
 * 	
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
*/

package com.wareninja.opensource.genericstore;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.wareninja.opensource.common.LOGGING;
import com.wareninja.opensource.droidfu.cachefu.ImageCache;
import com.wareninja.opensource.droidfu.cachefu.ObjectCache;

public class ApplicationWareNinja extends android.app.Application {
	private static String TAG="ApplicationWareNinja";
	
	public ApplicationWareNinja() {
		super();
	}
	
	@Override 
	public void onCreate() {
		super.onCreate();
		
		// initialize CACHE once App is created, this does init/create only ONCE!
		objectCache = getObjectCache();
		imageCache = getImageCache();
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		
		Log.w(TAG, "onLowMemory() -> clearImageCacheFu!!!");
		// clearing image cache should be enough, feel free to also clear object cache
		clearImageCacheFu();
	}
	@Override
	public void onTerminate() {
		super.onTerminate();
		
		Log.w(TAG, "onTerminate() -> Clearing all cache-fu!!!");
		clearAllCacheFu();
	}

	
	public void clearImageCacheFu() {
		int itemCount = imageCache.removeAllObjects();
		if(LOGGING.DEBUG)Log.d(TAG, "cleared " 
				+ itemCount 
				+ " items from ImageCache"
				+ "|" + imageCache.getDiskCacheDirectory()
				);
	}
	public void clearObjectCacheFu() {
		int itemCount = objectCache.removeAllObjects();
		if(LOGGING.DEBUG)Log.d(TAG, "cleared " 
				+ itemCount
				+ " items from ObjectCache"
				+ "|" + objectCache.getDiskCacheDirectory()
				);
	}
	public void clearAllCacheFu() {
		
		clearObjectCacheFu();
		clearImageCacheFu();
	}
	
	
	
	// --- for Droid-Fu ---
	// the below funcs are copied from DroidFu
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
    
    
    
    // below funcs are used by WareNinja in different apps, works like a charm
	//NOTE: you can change these values as you wish!
    
    private String mainCacheDirName_AppCache = ".WareNinja_appCache";
    public void setMainCacheDirName_AppCache(String dirName) {
    	this.mainCacheDirName_AppCache = dirName;
    }
    
    // --- object cache ---
    private ObjectCache objectCache = null;
    private static final int INITIAL_CAPACITY_objectCache = 25;
    private static final int DEFAULT_POOL_SIZE_objectCache = 3;
    private static final int DEFAULT_TTL_MINUTES_objectCache = 1 * 60;//mins
    
    public ObjectCache getObjectCache() {
		
		if (objectCache==null) {
			if(LOGGING.DEBUG)Log.d(TAG, "objectCache: init NEW");
			objectCache = new ObjectCache(INITIAL_CAPACITY_objectCache, DEFAULT_TTL_MINUTES_objectCache, DEFAULT_POOL_SIZE_objectCache);
			objectCache.setCacheDirName(mainCacheDirName_AppCache);// main cache dir name
			objectCache.enableDiskCache(this, ObjectCache.DISK_CACHE_SDCARD);
            
            if ( !GenericStore.isCustomKeyExist(GenericStore.TYPE_MEMDISKCACHE, GenericStore.KEY_OBJECTCACHEFUPATH, this) )
	        	GenericStore.setCustomData(
	        			GenericStore.TYPE_SHAREDPREF
	        			, GenericStore.KEY_OBJECTCACHEFUPATH 
	        			,objectCache.getDiskCacheDirectory()
	        			, this);
		}
		
		return(objectCache);
	}
    
    // --- image cache ---
    private ImageCache imageCache = null;
    private static final int INITIAL_CAPACITY_ImageCache = 25;
    private static final int DEFAULT_POOL_SIZE_ImageCache = 3;
    private static final int DEFAULT_TTL_MINUTES_ImageCache = 1 * 60;// mins
    public ImageCache getImageCache() {
		
		if (imageCache==null) {
			if(LOGGING.DEBUG)Log.d(TAG, "imageCache: init NEW");
			imageCache = new ImageCache(INITIAL_CAPACITY_ImageCache, DEFAULT_TTL_MINUTES_ImageCache, DEFAULT_POOL_SIZE_ImageCache);
			imageCache.setCacheDirName(mainCacheDirName_AppCache);// main cache dir name
			imageCache.enableDiskCache(this, ObjectCache.DISK_CACHE_SDCARD);
			
			if ( !GenericStore.isCustomKeyExist(GenericStore.TYPE_MEMDISKCACHE, GenericStore.KEY_IMAGECACHEFUPATH, this) )
	        	GenericStore.setCustomData(
	        			GenericStore.TYPE_SHAREDPREF
	        			, GenericStore.KEY_IMAGECACHEFUPATH 
	        			,imageCache.getDiskCacheDirectory()
	        			, this);
		}
		
		return(imageCache);
	}
    
}