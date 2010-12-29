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

package com.wareninja.opensource.common;

// <optional> NOTE: I used this, for global constants of my Android app
public class CONSTANTS {

	public static final String GRAPH_BASE_URL = "http://graph.facebook.com/";
	public static final String GRAPH_BASE_URL_SSL = "https://graph.facebook.com/";
	
	public static final String DEFAULT_SAVEFOTOPATH = "/Pictures/";
    
	/**
	 * returns full url for any facebook identifier
	 * example: profile picture url for a user
	 */
	public static String getPictureUrl(String id) {
		return getPictureUrl(false, id);
    }
	public static String getPictureUrl(boolean isSsl, String id) {
		if (isSsl)
			return GRAPH_BASE_URL_SSL + id + "/picture";
		else
			return GRAPH_BASE_URL + id + "/picture";
    }
}
