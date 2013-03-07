/***
 * 	Copyright (c) 2010-2013 WareNinja.com / BEERSTORM.net
 * 	@author yg@wareninja.com
 *  @see http://github.com/WareNinja - http://about.me/WareNinja
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
import java.util.LinkedList;
import java.util.List;


public class CustomPrefixesInCache implements Serializable {
	private static final long serialVersionUID = 1L;
    
	private static final String TAG = CustomPrefixesInCache.class.getSimpleName();
	
	private List<String> customPrefixes;
    
    public CustomPrefixesInCache() {
    	customPrefixes = new LinkedList<String>(); 
    }
    
	public List<String> getCustomPrefixes() {
		return customPrefixes;
	}
	public void setCustomPrefixes(List<String> customPrefixes) {
		this.customPrefixes = customPrefixes;
	}

	public boolean contains(String prefix) {
		return customPrefixes.contains(prefix);
	}
	public void add(String prefix) {
		customPrefixes.add(prefix);
	}
	public void remove(String prefix) {
		customPrefixes.remove(prefix);
	}
	public int size() {
		return customPrefixes.size();
	}

	@Override
	public String toString() {
		return TAG+" [customPrefixes=" + customPrefixes + "]";
	}
}