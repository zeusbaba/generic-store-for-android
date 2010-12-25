/* Copyright (c) 2010-2011 WareNinja.com
 * Author: yg@wareninja.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wareninja.opensource.droidfu.cachefu;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import android.util.Log;

/**
 * Implements a cache capable of caching Serializable objects. 
 * It exposes helper methods to immediately access objects
 * 
 * @author yg@wareninja.com
 *  
 */
public class ObjectCache extends AbstractCache<String, byte[]> {

    public ObjectCache(int initialCapacity, long expirationInMinutes, int maxConcurrentThreads) {
        super("ObjectCache", initialCapacity, expirationInMinutes, maxConcurrentThreads);
    }

    @Override
    public String getFileNameForKey(String imageUrl) {
        return CacheHelper.getFileNameFromUrl(imageUrl);
    }

    @Override
    protected byte[] readValueFromDisk(File file) throws IOException {
        BufferedInputStream istream = new BufferedInputStream(new FileInputStream(file), 10240);//YG: 10k=10240
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            throw new IOException("Cannot read files larger than " + Integer.MAX_VALUE + " bytes");
        }

        int objectDataLength = (int) fileSize;

        byte[] objectData = new byte[objectDataLength];
        istream.read(objectData, 0, objectDataLength);
        istream.close();

        return objectData;
    }

    public synchronized Object getObject(Object elementKey) {
        byte[] objectData = super.get(elementKey);
        if (objectData == null) {
            return null;
        }
        return ObjectSerializer.deserialize( new String(objectData) );
    }
    public synchronized boolean saveObject(String key, Serializable obj) {
    	super.put(key, ObjectSerializer.serialize(obj).getBytes());
    	return true;
    }
    public synchronized boolean removeObject(String key) {
    	super.remove(key);
    	return true;
    }
    public synchronized int removeAllObjects() {
    	//Log.d("ObjectCache", "clearing " + super.size() + " from ObjectCache");
    	int size = super.size();
    	super.clear();
    	
    	return size;
    }
    
    @Override
    protected void writeValueToDisk(BufferedOutputStream ostream, byte[] imageData)
            throws IOException {
        ostream.write(imageData);
    }
}
