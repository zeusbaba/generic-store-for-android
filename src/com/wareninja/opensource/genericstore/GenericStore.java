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

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.wareninja.opensource.common.LOGGING;
import com.wareninja.opensource.common.ObjectSerializer;
import com.wareninja.opensource.droidfu.cachefu.ObjectCache;

public class GenericStore {
    
	private static final String TAG = "GenericStore";
	
	public static final int TYPE_SHAREDPREF = 0;
	public static final int TYPE_MEMDISKCACHE = 1;
	
	public static final String KEY_IMAGECACHEFUPATH = "IMAGECACHEFUPATH";
    public static final String KEY_OBJECTCACHEFUPATH = "OBJECTCACHEFUPATH";
    
	//set SharedPreferences file name. default value, change it as you wish
	private static String PREF_FILE_NAME = "WareNinja_appPrefs";
	public static final String SETTINGS_ISCACHEHAVEDATA = "ISCACHEHAVEDATA";
    
	public static void clearAll(int type, Context context) {
		clearAll(type, context, "unknown");
	}
	public static void clearAll(int type, Context context, String caller) {
		if (type==TYPE_MEMDISKCACHE)
			clearCache(context, caller);
		else
			clearLocal(context, caller);
	}
	private static void clearCache(Context context, String caller) {
		((ApplicationWareNinja)context.getApplicationContext()).getObjectCache().removeAllObjects();
		((ApplicationWareNinja)context.getApplicationContext()).getImageCache().removeAllObjects();
		removeObject(SETTINGS_ISCACHEHAVEDATA, context);
	}
    private static void clearLocal(Context context, String caller) {
        Editor editor = 
            context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    // ---
    public static boolean saveObject(int type, String objKey, Serializable objData, Context context) {
    	
    	if (type==TYPE_MEMDISKCACHE)
    		return saveObjectInCache(objKey, objData, context);
    	else
    		return saveObject(objKey, objData, context);
    }
    private static boolean saveObjectInCache(String objKey, Serializable objData, Context context) {
    	if (!getCustomBoolean(SETTINGS_ISCACHEHAVEDATA, context))
			setCustomData(TYPE_SHAREDPREF, SETTINGS_ISCACHEHAVEDATA, true, context);
    	
    	ObjectCache objCache = ((ApplicationWareNinja)context.getApplicationContext()).getObjectCache();
        return objCache.saveObject(objKey, objData);
    }
    private static boolean saveObject(String objKey, Serializable dataObj, Context context) {
        Editor editor =
            context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(objKey, ObjectSerializer.serialize(dataObj) );
        
        return editor.commit(); 
    }
    
    public static Object getObject(int type, String objKey, Context context) {
    	if (type==TYPE_MEMDISKCACHE)
    		return getObjectFromCache(objKey, context);
    	else
    		return getObject(objKey, context);
    }
    private static Object getObjectFromCache(String objKey, Context context) {
    	ObjectCache objCache = ((ApplicationWareNinja)context.getApplicationContext()).getObjectCache();
    	return objCache.getObject(objKey);
    }
    private static Object getObject(String objKey, Context context) {
        SharedPreferences savedSession =
            context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        
        Object dataObj = ObjectSerializer.deserialize(savedSession.getString(objKey, null));
        
        return dataObj;
    }
    
    public static Object removeObject(int type, String objKey, Context context) {
    	if (type==TYPE_MEMDISKCACHE)
    		return removeObjectFromCache(objKey, context);
    	else
    		return removeObject(objKey, context);
    }
    private static boolean removeObjectFromCache(String objKey, Context context) {
    	ObjectCache objCache = ((ApplicationWareNinja)context.getApplicationContext()).getObjectCache();
    	return objCache.removeObject(objKey);
    }
    private static boolean removeObject(String objKey, Context context) {
        Editor editor =
            context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(objKey);
        
        //if(LOGGING.DEBUG)Log.d(TAG, "removedObject| objKey:"+objKey);
        
        return editor.commit(); 
    }
    
    public static boolean isCustomKeyExist(int type, String customKey, Context context) {
    	if (type==TYPE_MEMDISKCACHE)
        	return isCustomKeyExistInCache(customKey, context);
        else
        	return isCustomKeyExistInLocal(customKey, context);
    }
    private static boolean isCustomKeyExistInCache(String objKey, Context context) {
    	ObjectCache objCache = ((ApplicationWareNinja)context.getApplicationContext()).getObjectCache();
    	return objCache.containsKey(objKey);
    }
    private static boolean isCustomKeyExistInLocal(String customKey, Context context) {
        SharedPreferences savedSession =
            context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        
        return savedSession.contains(customKey);
    }
    // ---
    
    public static boolean setCustomData(int type, String key, Object value, Context context) {
    	if (type==TYPE_MEMDISKCACHE)
    		return saveObjectInCache(key, (Serializable)value, context);
    	else
    		return setCustomDataInLocal(key, value, context);
    }
    private static boolean setCustomDataInLocal(String key, Object value, Context context) {
    	
    	Editor editor =
            context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
    	
    	if (value instanceof Boolean)
        	editor.putBoolean(key, (Boolean)value);
        else if (value instanceof Integer)
        	editor.putInt(key, (Integer)value);
        else if (value instanceof String)
        	editor.putString(key, (String)value);
        else if (value instanceof Float)
        	editor.putFloat(key, (Float)value);
        else if (value instanceof Long)
        	editor.putLong(key, (Long)value);
    	
    	/*
    	final boolean booleanObj = false;
    	final int intObj = -1;
    	final String stringObj = "";
    	final float floatObj = 1F;
    	final long longObj = 1L;
    		
        if (value.getClass().isInstance(booleanObj))
        	editor.putBoolean(key, (Boolean)value);
        else if (value.getClass().isInstance(intObj))
        	editor.putInt(key, (Integer)value);
        else if (value.getClass().isInstance(stringObj))
        	editor.putString(key, (String)value);
        else if (value.getClass().isInstance(floatObj))
        	editor.putFloat(key, (Float)value);
        else if (value.getClass().isInstance(longObj))
        	editor.putLong(key, (Long)value);
        */
        
        return editor.commit(); 
    }
    
    public static boolean getCustomBoolean(String key, Context context) {
        SharedPreferences savedSession =
            context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        
        return (savedSession.getBoolean(key, false));
    }
    public static String getCustomString(String key, Context context) {
        SharedPreferences savedSession =
            context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        
        return (savedSession.getString(key, null));
    }
    public static int getCustomInt(String key, Context context) {
        SharedPreferences savedSession =
            context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        
        return (savedSession.getInt(key, -1));
    }
    

    // ------
    //-- <optional> you can use this to track&clear pre-defined set of data from cache
    public static final String CUSTOMPREFIXES_INCACHE = "CUSTOMPREFIXES_INCACHE";// object: UsersInCache
    public static void checkAddCustomPrefix(String prefix, Context context) {
    	
    	CustomPrefixesInCache prefixesInCache = new CustomPrefixesInCache();
    	if ( !isCustomKeyExist(GenericStore.TYPE_MEMDISKCACHE,CUSTOMPREFIXES_INCACHE, context)) {
    		
    		prefixesInCache.add(prefix);
    		if(LOGGING.DEBUG)Log.d(TAG, CUSTOMPREFIXES_INCACHE+" doesNOT exist, create NEW|"+prefix+"|"+prefixesInCache.toString());
    		
    		saveObject(GenericStore.TYPE_MEMDISKCACHE,CUSTOMPREFIXES_INCACHE, prefixesInCache, context);
    	}
    	else {
    		prefixesInCache = (CustomPrefixesInCache)getObject(GenericStore.TYPE_MEMDISKCACHE,CUSTOMPREFIXES_INCACHE, context);
    		if (!prefixesInCache.contains(prefix)) {
    			prefixesInCache.add(prefix);
    			
    			if(LOGGING.DEBUG)Log.d(TAG, CUSTOMPREFIXES_INCACHE+" do exist, add and save|"+prefix+"|"+prefixesInCache.toString());
    			
        		saveObject(GenericStore.TYPE_MEMDISKCACHE,CUSTOMPREFIXES_INCACHE, prefixesInCache, context);
    		}
    	}
    }
    public static void checkRemoveCustomPrefix(String prefix, Context context) {
    	CustomPrefixesInCache prefixesInCache = new CustomPrefixesInCache();
    	if ( isCustomKeyExist(GenericStore.TYPE_MEMDISKCACHE,CUSTOMPREFIXES_INCACHE, context)) {
    		
    		prefixesInCache = (CustomPrefixesInCache)getObject(GenericStore.TYPE_MEMDISKCACHE,CUSTOMPREFIXES_INCACHE, context);
    		if (prefixesInCache.contains(prefix)) {
    			prefixesInCache.remove(prefix);
    			
    			if(LOGGING.DEBUG)Log.d(TAG, CUSTOMPREFIXES_INCACHE+" do exist, add and save|"+prefix+"|"+prefixesInCache.toString());
    			
    			if (prefixesInCache.size()==0)
    				removeObject(GenericStore.TYPE_MEMDISKCACHE,CUSTOMPREFIXES_INCACHE, context);
    			else
    				saveObject(GenericStore.TYPE_MEMDISKCACHE,CUSTOMPREFIXES_INCACHE, prefixesInCache, context);
    		}
    	}
    }
    public static void removeAllCustomPrefixes(Context context) {
    	if ( isCustomKeyExist(GenericStore.TYPE_MEMDISKCACHE,CUSTOMPREFIXES_INCACHE, context)) {
    		CustomPrefixesInCache prefixesInCache = (CustomPrefixesInCache)getObject(GenericStore.TYPE_MEMDISKCACHE,CUSTOMPREFIXES_INCACHE, context);
    		List<String> customPrefixes = prefixesInCache.getCustomPrefixes();
    		
    		if(LOGGING.DEBUG)Log.d(TAG, CUSTOMPREFIXES_INCACHE+"| will clear all data for "+prefixesInCache.toString());
    		for (String customPrefix:customPrefixes)
    			removeObject(GenericStore.TYPE_MEMDISKCACHE,customPrefix, context);
    		removeObject(GenericStore.TYPE_MEMDISKCACHE,CUSTOMPREFIXES_INCACHE, context);
    	}
    	else {
    		if(LOGGING.DEBUG)Log.d(TAG, CUSTOMPREFIXES_INCACHE+" doesNOT exist, no action needed");
    	}
    }
    
}