/***
 * 	Copyright (c) 2011 WareNinja.com
 * 	Author: yg@wareninja.com
 *  http://www.WareNinja.net - https://github.com/wareninja	
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
 *  
 *  source adapted from: 
 *  https://github.com/wareninja
 *  http://www.josecgomez.com/2010/04/30/android-accessing-restfull-web-services-using-json/
 */

package com.wareninja.opensource.common;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class WebServiceNew {

	private static final String TAG = "WebServiceNew";
	DefaultHttpClient httpClient;
	HttpContext localContext;
	private String ret;
	private final static int TIMEOUT = 60000;

	private Collection<RequestHeader> headers = new LinkedList<RequestHeader>();

	HttpResponse response = null;
	HttpPut httpPut = null;// PUT
	HttpPost httpPost = null; // POST
	HttpGet httpGet = null; // GET
	String webServiceUrl;

	int mHttpResponseCode = 404;

	// The serviceName should be the name of the Service you are going to be
	// using.
	public WebServiceNew(String serviceName) {
		HttpParams myParams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(myParams, TIMEOUT);
		HttpConnectionParams.setSoTimeout(myParams, TIMEOUT);
		httpClient = new DefaultHttpClient(myParams);
		localContext = new BasicHttpContext();

		webServiceUrl = serviceName;
	}

	public WebServiceNew() {
		HttpParams myParams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(myParams, TIMEOUT);
		HttpConnectionParams.setSoTimeout(myParams, TIMEOUT);
		httpClient = new DefaultHttpClient(myParams);
		localContext = new BasicHttpContext();
	}

	public void setWebServiceUrl(String serviceName) {
		webServiceUrl = serviceName;
	}

	public String getWebServiceUrl() {
		return webServiceUrl;
	}

	// added by YG
	public int getHttpResponseCode() {
		return mHttpResponseCode;
	}

	public void addRequestHeader(final String key, final String value) {

		if (LOGGING.DEBUG)
			Log.d(TAG, "addRequestHeader->" + "key:" + key + "|value:" + value);
		if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value))
			return;

		this.headers.add(new PlainRequestHeader(key, value));
	}

	public void setRequestHeaders(final Collection<RequestHeader> headers) {
		this.headers = new LinkedList<RequestHeader>(headers);
	}

	private void appendRequestHeaders(final HttpURLConnection conn,
			final Collection<RequestHeader> headers) {
		for (RequestHeader header : headers) {
			if (header != null) {
				conn.addRequestProperty(header.getKey(), header.getValue());
			}
		}
	}

	// added by YG
	public String getJsonFromParams(Bundle params) {// this has some fup on
													// TagLoco action, couldnt
													// find what!!!

		Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();

		String valStr;
		Object valObj;
		for (String key : params.keySet()) {

			valObj = params.get(key);
			if (valObj instanceof String) {

				// ugly check if the string value is JSONObj or JSONArr
				valStr = "" + valObj;
				valObj = null;
				try {
					valObj = new JSONObject(valStr);
				} catch (JSONException ex) {
					valObj = null;
				}

				if (valObj == null) {
					try {
						valObj = new JSONArray(valStr);
					} catch (JSONException ex) {
						valObj = null;
					}
				}
				if (valObj == null)
					valObj = valStr;
			}

			if (valObj != null)
				paramsMap.put(key, valObj);
		}

		return getJsonFromParams(paramsMap);
	}

	public String getJsonFromParams2(Bundle params) {

		Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();

		String valStr;
		Object valObj;
		for (String key : params.keySet()) {

			valObj = params.get(key);
			if (valObj instanceof String) {

				// ugly check if the string value is JSONObj or JSONArr
				valStr = "" + valObj;
				// if (LOGGING.DEBUG) Log.d(TAG, "valStr->"+valStr);
				valObj = null;
				try {
					valObj = new JSONObject(valStr);

					// note: very ugly hack just for tag loco part!
					if (valStr.contains("{") && valStr.contains("lat")
							&& valStr.contains("lng")) {

						LinkedHashMap<String, Object> tempMap = new LinkedHashMap<String, Object>();
						tempMap.put("lng", ((JSONObject) valObj).get("lng"));
						tempMap.put("lat", ((JSONObject) valObj).get("lat"));
						JSONObject tempJson = new JSONObject(tempMap);

						if (LOGGING.DEBUG)
							Log.d(TAG, "tempJson->" + tempJson + "| len:"
									+ tempJson.length());
						if (LOGGING.DEBUG)
							Log.d(TAG, "valObj(before)->" + valObj);

						if (tempJson.length() > 0) {
							valObj = tempJson;
						}

						if (LOGGING.DEBUG)
							Log.d(TAG, "valObj(after)->" + valObj);
					}
				} catch (JSONException ex) {
					valObj = null;
				}

				if (valObj == null) {
					try {
						valObj = new JSONArray(valStr);
					} catch (JSONException ex) {
						valObj = null;
					}
				}
				if (valObj == null)
					valObj = valStr;
			}

			if (valObj != null)
				paramsMap.put(key, valObj);
		}

		return getJsonFromParams(paramsMap);
	}

	public String getJsonFromParams(Map<String, Object> params) {

		JSONObject jsonObject = new JSONObject();

		for (Map.Entry<String, Object> param : params.entrySet()) {
			try {
				jsonObject.put(param.getKey(), param.getValue());
			} catch (JSONException e) {
				Log.e(TAG, "JSONException : " + e);
			}
		}
		return jsonObject.toString();
	}

	public String webInvokeWithJson(String methodName, String jsonStr) {
		return webInvokePOST(methodName, jsonStr, "application/json");
	}

	// Use this method to do a HttpPost\WebInvoke on a Web Service
	public String webInvoke(String methodName, Map<String, Object> params) {

		JSONObject jsonObject = new JSONObject();

		for (Map.Entry<String, Object> param : params.entrySet()) {
			try {
				jsonObject.put(param.getKey(), param.getValue());
			} catch (JSONException e) {
				Log.e(TAG, "JSONException : " + e);
			}
		}
		return webInvokePOST(methodName, jsonObject.toString(),
				"application/json");
	}

	private String webInvokePOST(String methodName, String data,
			String contentType) {
		ret = null;

		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.RFC_2109);

		httpPost = new HttpPost(webServiceUrl + methodName);
		response = null;

		StringEntity tmp = null;

		// httpPost.setHeader("User-Agent", "SET YOUR USER AGENT STRING HERE");
		httpPost.setHeader(
				"Accept",
				"text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");

		for (RequestHeader header : headers) {
			if (header != null) {
				httpPost.setHeader(header.getKey(), header.getValue());
			}
		}

		if (contentType != null) {
			httpPost.setHeader("Content-Type", contentType);
		} else {
			httpPost.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
		}

		try {
			tmp = new StringEntity(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "HttpUtils : UnsupportedEncodingException : " + e);
		}

		httpPost.setEntity(tmp);

		if (LOGGING.DEBUG)
			Log.d(TAG, webServiceUrl + "?" + data);

		try {
			response = httpClient.execute(httpPost, localContext);
			
			if (response != null) {
				ret = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			Log.e(TAG, "HttpUtils: " + e);
		}

		return ret;
	}

	public String webInvokeWithJsonPUT(String methodName, String jsonStr) {
		return webInvokePUT(methodName, jsonStr, "application/json");
	}

	private String webInvokePUT(String methodName, String data,
			String contentType) {
		ret = null;

		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
				CookiePolicy.RFC_2109);

		httpPut = new HttpPut(webServiceUrl + methodName);
		response = null;

		StringEntity tmp = null;

		// httpPost.setHeader("User-Agent", "SET YOUR USER AGENT STRING HERE");
		httpPut.setHeader(
				"Accept",
				"text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");

		for (RequestHeader header : headers) {
			if (header != null) {
				httpPut.setHeader(header.getKey(), header.getValue());
			}
		}

		if (contentType != null) {
			httpPut.setHeader("Content-Type", contentType);
		} else {
			httpPut.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
		}

		try {
			tmp = new StringEntity(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "HttpUtils : UnsupportedEncodingException : " + e);
		}

		httpPut.setEntity(tmp);

		if (LOGGING.DEBUG)
			Log.d(TAG, webServiceUrl + "?" + data);

		try {
			response = httpClient.execute(httpPut, localContext);

			if (response != null) {
				ret = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			Log.e(TAG, "HttpUtils: " + e);
		}

		return ret;
	}

	// Use this method to do a HttpGet/WebGet on the web service
	public String webGet(String methodName, Bundle params) {
		Map<String, String> reqParams = new HashMap<String, String>();

		// TODO: do checks to avoid fups!
		for (String key : params.keySet()) {
			// reqParams.put(key, params.getString(key));
			reqParams.put(key, params.get(key) + "");
		}

		return webGet(methodName, reqParams);
	}

	public String webGet(String methodName, Map<String, String> params) {

		String getUrl;
		if (methodName.startsWith(webServiceUrl)) {
			getUrl = methodName;
		} else {
			getUrl = webServiceUrl + methodName;
		}

		int i = 0;
		for (Map.Entry<String, String> param : params.entrySet()) {
			if (i == 0) {
				getUrl += "?";
			} else {
				getUrl += "&";
			}

			try {
				getUrl += param.getKey() + "="
						+ URLEncoder.encode(param.getValue(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			i++;
		}

		httpGet = new HttpGet(getUrl);
		if (LOGGING.DEBUG)
			Log.d(TAG, "WebGetURL: " + getUrl);

		for (RequestHeader header : headers) {
			if (header != null) {
				httpGet.setHeader(header.getKey(), header.getValue());

				if (LOGGING.DEBUG) {
					Log.d(TAG,
							String.format("httpGet.setHeader(%s, %s)",
									header.getKey(), header.getValue()));
				}
			}
		}

		try {
			response = httpClient.execute(httpGet);

			mHttpResponseCode = response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

		// we assume that the response body contains the error message
		try {

			ret = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}

		return ret;
	}

	// use this method for plain POST!
	public String webPost(String methodName, Bundle params)
			throws MalformedURLException, IOException {

		// random string as boundary for multi-part http post
		String strBoundary = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";
		String endLine = "\r\n";
		OutputStream os;

		ret = null;

		String postUrl = webServiceUrl + methodName;

		if (LOGGING.DEBUG) {
			Log.d(TAG, "POST URL: " + postUrl);
		}
		HttpURLConnection conn = (HttpURLConnection) new URL(postUrl)
				.openConnection();
		conn.setRequestProperty("User-Agent", System.getProperties()
				.getProperty("http.agent") + " WareNinjaAndroidSDK");
		HttpParams httpParams = httpClient.getParams();
		HttpProtocolParams.setUseExpectContinue(httpParams, false);

		Bundle dataparams = new Bundle();
		for (String key : params.keySet()) {

			byte[] byteArr = null;
			try {
				byteArr = (byte[]) params.get(key);
			} catch (Exception ex1) {
			}
			if (byteArr != null)
				dataparams.putByteArray(key, byteArr);
		}

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="
				+ strBoundary);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestProperty("Connection", "Keep-Alive");
		appendRequestHeaders(conn, headers);
		conn.connect();
		os = new BufferedOutputStream(conn.getOutputStream());

		os.write(("--" + strBoundary + endLine).getBytes());
		os.write((WareNinjaUtils.encodePostBody(params, strBoundary)).getBytes());
		os.write((endLine + "--" + strBoundary + endLine).getBytes());

		if (!dataparams.isEmpty()) {

			for (String key : dataparams.keySet()) {
				os.write(("Content-Disposition: form-data; filename=\"" + key
						+ "\"" + endLine).getBytes());
				os.write(("Content-Type: content/unknown" + endLine + endLine)
						.getBytes());
				os.write(dataparams.getByteArray(key));
				os.write((endLine + "--" + strBoundary + endLine).getBytes());

			}
		}
		os.flush();

		String response = "";
		try {
			response = WareNinjaUtils.read(conn.getInputStream());
		} catch (FileNotFoundException e) {
			// Error Stream contains JSON that we can parse to a FB error
			response = WareNinjaUtils.read(conn.getErrorStream());
		}
		if (LOGGING.DEBUG)
			Log.d(TAG, "POST response: " + response);

		return response;
	}

	public InputStream getHttpStream(String urlString) throws IOException {
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			mHttpResponseCode = response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		} catch (Exception e) {
			throw new IOException("Error connecting");
		} // end try-catch

		return in;
	}

	public void clearCookies() {
		httpClient.getCookieStore().clear();
	}

	public void abort() {
		try {
			if (httpClient != null) {
				System.out.println("Abort.");
				httpPost.abort();
			}
		} catch (Exception e) {
			// System.out.println("Your App Name Here" + e);
		}
	}
}
