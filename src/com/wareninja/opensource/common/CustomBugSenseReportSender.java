package com.wareninja.opensource.common;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.acra.CrashReportData;
import org.acra.ReportField;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.google.gson.JsonObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class CustomBugSenseReportSender implements ReportSender {

	static final String TAG = "CustomBugSenseReportSender";
	//String bugSenseEndPoint = "http://bugsense.appspot.com/api/errors";
	String bugSenseEndPoint = "http://www.bugsense.com/api/acra?api_key=";
	String bugSenseApiKey = "";
    public CustomBugSenseReportSender(HashMap<String, String> params){
        // initialize your sender with needed parameters
    	if (params.containsKey("api_key")) {
    		bugSenseApiKey = params.get("api_key");
    		bugSenseEndPoint+=bugSenseApiKey;
    	}
    }

    @Override
    public void send(CrashReportData report) throws ReportSenderException {
        // Iterate over the CrashReportData instance and do whatever
        // you need with each pair of ReportField key / String value
    	
    	JsonObject json = new JsonObject();
    	JsonObject application_json = new JsonObject();
    	JsonObject exception_json = new JsonObject();
    	JsonObject client_json = new JsonObject();
    	JsonObject request_json = new JsonObject();
    	String stacktrace = "";
    	
    	String tempVal = "";
		
		tempVal =report.getProperty(ReportField.PHONE_MODEL, "");
		tempVal +=  " "+report.getProperty(ReportField.BRAND, "");
		if (!TextUtils.isEmpty(tempVal)) application_json.addProperty("phone", tempVal);
		tempVal =report.getProperty(ReportField.APP_VERSION_NAME, "");
		if (!TextUtils.isEmpty(tempVal)) application_json.addProperty("appver", tempVal);
		tempVal =report.getProperty(ReportField.PACKAGE_NAME, "");
		if (!TextUtils.isEmpty(tempVal)) application_json.addProperty("appname", tempVal);
		tempVal =report.getProperty(ReportField.ANDROID_VERSION, "");
		if (!TextUtils.isEmpty(tempVal)) application_json.addProperty("osver", tempVal);
		tempVal =report.getProperty(ReportField.DISPLAY, "");
		if (!TextUtils.isEmpty(tempVal)) application_json.addProperty("display", tempVal);
		/*application_json.put("wifi_on", wifi_status);
		application_json.put("mobile_net_on", mob_net_status);
		application_json.put("gps_on", gps_status);
		application_json.put("screen:width", screenProperties[0]);
		application_json.put("screen:height", screenProperties[1]);
		application_json.put("screen:orientation", screenProperties[2]);
		application_json.put("screen_dpi(x:y)", screenProperties[3] + ":"+ screenProperties[4]);
		*/
		
		tempVal =report.getProperty(ReportField.USER_COMMENT, "");
		if (!TextUtils.isEmpty(tempVal)) exception_json.addProperty("user_comment", tempVal);
		tempVal =report.getProperty(ReportField.USER_CRASH_DATE, "");
		if (!TextUtils.isEmpty(tempVal)) exception_json.addProperty("user_crash_date", tempVal);
		tempVal =report.getProperty(ReportField.STACK_TRACE, "");
		if (!TextUtils.isEmpty(tempVal)) {
			exception_json.addProperty("stack_trace", tempVal);
			stacktrace = tempVal;
		}
		
    	/*for (ReportField key:report.keySet()) {
    		//Log.d(TAG, "key : "+key + " / value : " + report.getProperty(key));
    	}*/
		tempVal =report.getProperty(ReportField.USER_CRASH_DATE, "");
		if (!TextUtils.isEmpty(tempVal)) exception_json.addProperty("occured_at", tempVal);
		else exception_json.addProperty("occured_at", WareNinjaUtils.convertExpiresInMillis2String(System.currentTimeMillis()));
    	json.add("exception", exception_json);
    	json.add("application_environment", application_json);

    	request_json.addProperty("remote_ip", "");
    	json.add("request", request_json);
    	
    	client_json.addProperty("version", "bugsense-android-custom-WareNinja-0.1");
		client_json.addProperty("name", "bugsense-android-custom-WareNinja");
		json.add("client", client_json);
		
		Log.d(TAG, "json : " + json);
		
		WebServiceNew webService = new WebServiceNew(bugSenseEndPoint);
		webService.addRequestHeader("X-BugSense-Api-Key", bugSenseApiKey);
		
		Bundle params = new Bundle();
		
		try {
			
			params.putString("data", json+"");
			params.putString("hash", WareNinjaUtils.MD5(stacktrace));
			
			webService.webPost("", params);
			
			//-submitError(WareNinjaUtils.MD5(stacktrace), json+"");
		}
		catch (Exception ex) {
			Log.e(TAG, "Exception : " + ex.toString());
		}
    }
    
    private final static int TIMEOUT = 60000;
    private void submitError(String hash, String data) {
    	
    	DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();
		
		HttpProtocolParams.setUseExpectContinue(params, false);
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT);
	
		HttpPost httpPost = new HttpPost(bugSenseEndPoint);
		httpPost.addHeader("X-BugSense-Api-Key", bugSenseApiKey);
		
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("data", data));
		nvps.add(new BasicNameValuePair("hash", hash));
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			
			// we don't care about the actual response
			// only if we managed to reach the server
	
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			Log.d(TAG, "entity : " + entity);
		}
		catch (Exception ex) {
			Log.e(TAG, "Error sending exception stacktrace", ex);
		}
    }
}
