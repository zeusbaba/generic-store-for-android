/***
	Copyright (c) 2010-2011 WareNinja.com
	Author: yg@wareninja.com
*/

package com.wareninja.opensource.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.cookie.DateUtils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;


public class WareNinjaUtils {

	private static final String TAG = "WareNinjaUtils";

	public static boolean checkInternetConnection(Context context) {

    	ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

    	// ARE WE CONNECTED TO THE NET?
    	if (conMgr.getActiveNetworkInfo() != null
    			&& conMgr.getActiveNetworkInfo().isAvailable()
    			&& conMgr.getActiveNetworkInfo().isConnected()) {
    		return true;
    	} else {
    		Log.w(TAG, "Internet Connection NOT Present");
    		return false;
    	}
    }
	public static boolean isConnAvailAndNotRoaming(Context context) {

    	ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

    	if (conMgr.getActiveNetworkInfo() != null
    			&& conMgr.getActiveNetworkInfo().isAvailable()
    			&& conMgr.getActiveNetworkInfo().isConnected()) {
    		
    		if(!conMgr.getActiveNetworkInfo().isRoaming())
    			return true;
    		else
    			return false;
    	} else {
    		Log.w(TAG, "Internet Connection NOT Present");
    		return false;
    	}
    }
	public static boolean isRoaming(Context context) {

    	ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

    	return (conMgr.getActiveNetworkInfo()!=null && conMgr.getActiveNetworkInfo().isRoaming());
    }
	
	//--- trusting any ssl url! ---
	public static void trustEveryone() {
		
		if(LOGGING.DEBUG)Log.d(TAG, "trustEveryone()");
		
        try {
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                        public boolean verify(String hostname, SSLSession session) {
                                return true;
                        }});
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, new X509TrustManager[]{new X509TrustManager(){
                        public void checkClientTrusted(X509Certificate[] chain,
                                        String authType) throws CertificateException {}
                        public void checkServerTrusted(X509Certificate[] chain,
                                        String authType) throws CertificateException {}
                        public X509Certificate[] getAcceptedIssuers() {
                                return new X509Certificate[0];
                        }}}, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(
                                context.getSocketFactory());
        } catch (Exception e) { // should never happen
                e.printStackTrace();
        }
	}
	//---
	
	
	
    public static String convertExpiresInMillis2String(long millis) {
    	
    	if (millis<=0)
    		return ""+millis;
    	else {
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
	    	
	    	return sdf.format(new Date(millis));
    	}
    }  
    
    public static String getDaysBetween(String date1, String date2) {
    	// input is expected to be exactly like; 2011-01-05
    	// date2 must be before date1
    	String result = "";
    	
    	try {
    		
			Date dateOne = DateUtils.parseDate(date1, new String[] {"yyyy-MM-dd"} );
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(dateOne);
			
			Date dateTwo = DateUtils.parseDate(date2, new String[] {"yyyy-MM-dd"} );
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(dateTwo);
			
			//Log.d(TAG, "cal1<>cal2 : " + cal1.compareTo(cal2) );
			/*
			 * 0 if the times of the two Calendars are equal, 
			 * -1 if the time of this Calendar is before the other one, 
			 * 1 if the time of this Calendar is after the other one.
			 */
			
			// day difference between two dates!
			long diff = dateOne.getTime() - dateTwo.getTime();
			// long days = diff / (24 * 60 * 60 * 1000);
			
			Log.d(TAG, 
					"days in between:" 
					+ ( TimeUnit.MILLISECONDS.toSeconds(diff)/60/60/24  ) 
					);
			// days in between:4
		}
		catch (Exception ex) {
			Log.w(TAG, ex.toString());
		}
    	
    	return result;
    }

    public static boolean isDaysBefore(String string1, String string2) {
		// check if date1 is before date2
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		try{
			return ( sdf.parse(string1) ).before( sdf.parse(string2) );
		}
		catch (Exception ex) {
			return false;
		}
	}
   
	
	public static Drawable Drawable4LoadImageFromWeb(String url) {
        
        try {
        	
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        }catch (Exception e) {

        	Log.w(TAG, "Exc="+e);
            return null;
        }
    }
	
	public static byte[] loadImageDataFromWeb(String url) {
        
		byte[] imageData = null;
        try {
        	
        	URLConnection connection=new URL(url).openConnection();
			InputStream stream=connection.getInputStream();
			//BufferedInputStream in=new BufferedInputStream(stream);//default 8k buffer
			BufferedInputStream in=new BufferedInputStream(stream, 10240);//YG: 10k=10240, 2x8k=16384
			ByteArrayOutputStream out=new ByteArrayOutputStream(10240);
			int read;
			byte[] b=new byte[4096];
			
			while ((read = in.read(b)) != -1) {
					out.write(b, 0, read);
			}
			
			out.flush();
			out.close();
			
			imageData = out.toByteArray();
            
			
        }catch (Exception e) {

        	Log.w(TAG, "Exc="+e);
            return null;
        }
        
        return imageData;
    }
	
	
	public static Bitmap getBitmapWithReflection(Bitmap originalImage) {
	    
		//The gap we want between the reflection and the original image
        final int reflectionGap = 4;
		
		int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        
        //This will not scale but will flip on the Y axis
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        
        //Create a Bitmap with the flip matrix applied to it.
        //We only want the bottom half of the image
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);
            
        //Create a new bitmap with same width but taller to fit reflection
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width 
          , (height + height/2), Config.ARGB_8888);
      
       //Create a new Canvas with the bitmap that's big enough for
       //the image plus gap plus reflection
       Canvas canvas = new Canvas(bitmapWithReflection);
       //Draw in the original image
       canvas.drawBitmap(originalImage, 0, 0, null);
       //Draw in the gap
       Paint deafaultPaint = new Paint();
       canvas.drawRect(0, height, width, height + reflectionGap, deafaultPaint);
       //Draw in the reflection
       canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);
       
       //Create a shader that is a linear gradient that covers the reflection
       Paint paint = new Paint(); 
       LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, 
         bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, 
         TileMode.CLAMP); 
       //Set the paint to use this shader (linear gradient)
       paint.setShader(shader); 
       //Set the Transfer mode to be porter duff and destination in
       paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN)); 
       //Draw a rectangle using the paint with our linear gradient
       canvas.drawRect(0, height, width, 
    		   bitmapWithReflection.getHeight() + reflectionGap, paint); 
	 
	    return bitmapWithReflection;
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	        bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	 
	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);
	    final float roundPx = 12;
	 
	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	 
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	 
	    return output;
	}
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static String encodeUrl(Bundle parameters) {
        if (parameters == null) {
        	return "";
        }
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : parameters.keySet()) {
            if (first) first = false; else sb.append("&");
            sb.append(URLEncoder.encode(key) + "=" +
                      URLEncoder.encode(parameters.getString(key)));
        }
        return sb.toString();
    }

    public static String openGetUrl_respStr(String url, Bundle params) 
          throws MalformedURLException, IOException {
   
        url = url + "?" + encodeUrl(params);
        if (LOGGING.DEBUG)Log.d(TAG, "URL: " + url);
        
        HttpURLConnection conn = 
            (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", System.getProperties().
                getProperty("http.agent") + " FacebookAndroidSDK");
        
        String response = "";
        try {
        	response = readStringFromStream(conn.getInputStream());
        } catch (FileNotFoundException e) {
            // Error Stream contains JSON that we can parse to a FB error
            response = readStringFromStream(conn.getErrorStream());
        }
        //Log.d("Facebook-Util", method + " response: " + response);
        
        return response;
    }
	private static String readStringFromStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }
	public static byte[] openGetUrl_respByte(String url, Bundle params)
		throws MalformedURLException, IOException {
	
		byte[] response = null;
	  url = url + "?" + encodeUrl(params);
	  if (LOGGING.DEBUG)Log.d(TAG, "URL: " + url);
	  
	  HttpURLConnection conn = 
	      (HttpURLConnection) new URL(url).openConnection();
	  conn.setRequestProperty("User-Agent", System.getProperties().
	          getProperty("http.agent") + " FacebookAndroidSDK");
	  
	  try {
	  	response = readByteFromStream(conn.getInputStream());
	  } catch (FileNotFoundException e) {
	      // Error Stream contains JSON that we can parse to a FB error
	      Log.e(TAG, e.toString());
	  }
	  //Log.d("Facebook-Util", method + " response: " + response);
	  
	  return response;
	}
	private static byte[] readByteFromStream(InputStream in) throws IOException {
		
	    byte[] buffer = new byte[8192];
	    int bytesRead;
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    while ((bytesRead = in.read(buffer)) != -1) {
	        output.write(buffer, 0, bytesRead);
	    }
	    
	    in.close();
	    
	    return output.toByteArray();
	}
	
	public static byte[] retrieveImageData_fromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", System.getProperties().
  	          getProperty("http.agent") + " FacebookAndroidSDK");

        // determine the image size and allocate a buffer
        int fileSize = connection.getContentLength();
        byte[] imageData = new byte[fileSize];

        // download the file
        Log.d(TAG, "fetching image " + imageUrl + " (" + fileSize + ")");

        if (fileSize>0) {
        
	        BufferedInputStream istream = new BufferedInputStream(connection.getInputStream());
	        int bytesRead = 0;
	        int offset = 0;
	        while (bytesRead != -1 && offset < fileSize) {
	            bytesRead = istream.read(imageData, offset, fileSize - offset);
	            offset += bytesRead;
	        }
	        
	        istream.close();
        }
        else
        	Log.d(TAG, "fileSize is 0! skipping");

        // clean up
        connection.disconnect();

        return imageData;
    }
	
	//--- Android App Utils ---
	/**
	 * Get current version number.
	 * 
	 * @return
	 */
	public static String getVersionNumber(Context context) {
		String version = "?";
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "Package name not found", e);
		};
		return version;
	}
	
	/**
	 * Get application name.
	 * 
	 * @return
	 */
	public static String getApplicationName(Context context) {
		String name = "?";
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			name = context.getString(pi.applicationInfo.labelRes);
		} catch (PackageManager.NameNotFoundException e) {
			Log.e(TAG, "Package name not found", e);
		};
		return name;
	}
	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to the specified intent. If no suitable package is
	 * found, this method returns false.
	 *
	 * @param context The application's environment.
	 * @param intent The Intent to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(final Context context, final Intent intent) {
	    final PackageManager packageManager = context.getPackageManager();
	    
	    /*
	    Log.d("IntentUtils",
	    		context.getPackageName()
	    		+ "|" + context.getApplicationInfo().toString()
	    		+ "|" + intent.getAction()+ "|" + intent.toString()
	    		+ "|" + packageManager.toString()
	    	);
	    */
	    List<ResolveInfo> list =
	            packageManager.queryIntentActivities(intent,
	                    PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}
	
	
	// --- GENERAL Util func's ---
	/*
	 * Nice utils from
	 * https://github.com/apache/pig/tree/89c2e8e76c68d0d0abe6a36b4e08ddc56979796f/src/org/apache/pig/impl/util
	 */
	
    /**
	* This method is a helper for classes to implement {@link java.lang.Object#equals(java.lang.Object)}
	* checks if two objects are equals - two levels of checks are
	* made - first if both are null or not null. If either is null,
	* check is made whether both are null.
	* If both are non null, equality also is checked if so indicated
	* @param obj1 first object to be compared
	* @param obj2 second object to be compared
	* @param checkEquality flag to indicate whether object equality should
	* be checked if obj1 and obj2 are non-null
	* @return true if the two objects are equal
	* false otherwise
	* 
	* source from: https://github.com/apache/pig/blob/89c2e8e76c68d0d0abe6a36b4e08ddc56979796f/src/org/apache/pig/impl/util/Utils.java
	*/
    public static boolean checkNullEquals(Object obj1, Object obj2, boolean checkEquality) {
        if(obj1 == null || obj2 == null) {
            return obj1 == obj2;
        }
        if(checkEquality) {
            if(!obj1.equals(obj2)) {
                return false;
            }
        }
        return true;
    }   
    /**
	* This method is a helper for classes to implement {@link java.lang.Object#equals(java.lang.Object)}
	* The method checks whether the two arguments are both null or both not null and
	* whether they are of the same class
	* @param obj1 first object to compare
	* @param obj2 second object to compare
	* @return true if both objects are null or both are not null
	* and if both are of the same class if not null
	* false otherwise
	* 
	* source from: https://github.com/apache/pig/blob/89c2e8e76c68d0d0abe6a36b4e08ddc56979796f/src/org/apache/pig/impl/util/Utils.java
	*/
    public static boolean checkNullAndClass(Object obj1, Object obj2) {
        if(checkNullEquals(obj1, obj2, false)) {
            if(obj1 != null) {
                return obj1.getClass() == obj2.getClass();
            } else {
                return true; // both obj1 and obj2 should be null
            }
        } else {
            return false;
        }
    }

    // source from CastUtils: https://github.com/apache/pig/blob/89c2e8e76c68d0d0abe6a36b4e08ddc56979796f/src/org/apache/pig/impl/util/CastUtils.java
    private static Integer mMaxInt = Integer.valueOf(Integer.MAX_VALUE);
    private static Long mMaxLong = Long.valueOf(Long.MAX_VALUE);

    public static Double stringToDouble(String str) {
	    if (str == null) {
	    	return null;
	    } else {
		    try {
		    return Double.parseDouble(str);
		    } catch (NumberFormatException e) {
		    	Log.w(TAG, "Unable to interpret value "
		    		    + str
		    		    + " in field being "
		    		    + "converted to double, caught NumberFormatException <"
		    		    + e.getMessage() + "> field discarded");
		    	return null;
		    }
	    }
    }
    public static Float stringToFloat(String str) {
	    if (str == null) {
	    	return null;
	    } else {
		    try {
		    	return Float.parseFloat(str);
		    } catch (NumberFormatException e) {
		    	Log.w(TAG, "Unable to interpret value "
		    		    + str
		    		    + " in field being "
		    		    + "converted to float, caught NumberFormatException <"
		    		    + e.getMessage() + "> field discarded");
		    	return null;
		    }
	    }
    }
    public static Integer stringToInteger(String str) {
	    if (str == null) {
	    	return null;
	    } else {
		    try {
		    	return Integer.parseInt(str);
		    } catch (NumberFormatException e) {
			    // It's possible that this field can be interpreted as a double.
			    // Unfortunately Java doesn't handle this in Integer.valueOf. So
			    // we need to try to convert it to a double and if that works
			    // then
			    // go to an int.
			    try {
				    Double d = Double.valueOf(str);
				    // Need to check for an overflow error
				    if (d.doubleValue() > mMaxInt.doubleValue() + 1.0) {
				    	Log.w(TAG, "Value " + d
							    + " too large for integer");
				    	return null;
				    }
				    return Integer.valueOf(d.intValue());
			    } catch (NumberFormatException nfe2) {
			    	Log.w(TAG, "Unable to interpret value "
						    + str
						    + " in field being "
						    + "converted to int, caught NumberFormatException <"
						    + e.getMessage()
						    + "> field discarded");
			    	return null;
			    }
		    }
	    }
    }
    public static Long stringToLong(String str) {
	    if (str == null) {
	    	return null;
	    } else {
		    try {
		    	return Long.parseLong(str);
		    } catch (NumberFormatException e) {
			    // It's possible that this field can be interpreted as a double.
			    // Unfortunately Java doesn't handle this in Long.valueOf. So
			    // we need to try to convert it to a double and if that works
			    // then
			    // go to an long.
			    try {
				    Double d = Double.valueOf(str);
				    // Need to check for an overflow error
				    if (d.doubleValue() > mMaxLong.doubleValue() + 1.0) {
				    	Log.w(TAG, "Value " + d
							    + " too large for long");
				    	return null;
				    }
				    return Long.valueOf(d.longValue());
			    } catch (NumberFormatException nfe2) {
			    	Log.w(TAG, "Unable to interpret value "
						    + str
						    + " in field being "
						    + "converted to long, caught NumberFormatException <"
						    + nfe2.getMessage()
						    + "> field discarded");
			    	return null;
			    }
		    }
	    }
    }


}
