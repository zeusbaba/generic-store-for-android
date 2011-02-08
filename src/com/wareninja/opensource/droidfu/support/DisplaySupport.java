/* Copyright (c) 2009 Matthias Kaeppler
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

package com.wareninja.opensource.droidfu.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class DisplaySupport {

    public static final int SCREEN_DENSITY_LOW = 120;
    public static final int SCREEN_DENSITY_MEDIUM = 160;
    public static final int SCREEN_DENSITY_HIGH = 240;

    private static int screenDensity = -1;

    public static int dipToPx(Context context, int dip) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (dip * displayMetrics.density + 0.5f);
    }

    public static Drawable scaleDrawable(Context context, int drawableResourceId, int width,
            int height) {
        Bitmap sourceBitmap = BitmapFactory.decodeResource(context.getResources(),
            drawableResourceId);
        return new BitmapDrawable(Bitmap.createScaledBitmap(sourceBitmap, width, height, true));
    }

    public static int getScreenDensity(Context context) {
        if (screenDensity == -1) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            try {
                screenDensity = DisplayMetrics.class.getField("densityDpi").getInt(displayMetrics);
            } catch (Exception e) {
                screenDensity = SCREEN_DENSITY_MEDIUM;
            }
        }
        return screenDensity;
    }
    
    
    // --- added by YG ---
    /*
QVGA (240x320)			
WQVGA400 (240x400)
WQVGA432 (240x432)
HVGA (320x480)
WVGA800 (480x800)
WVGA854 (480x854)
     */
    public static final String SCREEN_SIZE_QVGA = "240x320";
    public static final String SCREEN_SIZE_WQVGA400 = "240x400";
    public static final String SCREEN_SIZE_WQVGA432 = "240x432";
    public static final String SCREEN_SIZE_HVGA = "320x480";
    public static final String SCREEN_SIZE_WVGA800 = "480x800";
    public static final String SCREEN_SIZE_WVGA854 = "480x854";
    private static String screenSizeType = "";
    private static int screenSizeW = -1;
    private static int screenSizeH = -1;
    public static String getScreenSizeType(Context context) {// returns type
        
    	if ( TextUtils.isEmpty(screenSizeType) ) {
	    	DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
	    	if (screenSizeW == -1) {
	            try {
	            	screenSizeW = DisplayMetrics.class.getField("widthPixels").getInt(displayMetrics);
	            } catch (Exception e) {
	            	screenSizeW = 320;
	            }
	        }
	    	if (screenSizeH == -1) {
	            try {
	            	screenSizeH = DisplayMetrics.class.getField("heightPixels").getInt(displayMetrics);
	            } catch (Exception e) {
	            	screenSizeH = 480;
	            }
	        }
    	}
    	
    	String scrSize = screenSizeW + "x" + screenSizeH;
    	if (scrSize.equals(SCREEN_SIZE_QVGA))
    		screenSizeType = SCREEN_SIZE_QVGA;
    	else if (scrSize.equals(SCREEN_SIZE_WQVGA400))
    		screenSizeType = SCREEN_SIZE_WQVGA400;
    	else if (scrSize.equals(SCREEN_SIZE_WQVGA432))
    		screenSizeType = SCREEN_SIZE_WQVGA432;
    	else if (scrSize.equals(SCREEN_SIZE_WVGA800))
    		screenSizeType = SCREEN_SIZE_WVGA800;
    	else if (scrSize.equals(SCREEN_SIZE_WVGA854))
    		screenSizeType = SCREEN_SIZE_WVGA854;
    	else 
    		screenSizeType = SCREEN_SIZE_HVGA;
    	
        return screenSizeType;
    }
    public static int getScreenSizeW(Context context) {
        if (screenSizeW == -1) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            try {
            	screenSizeW = DisplayMetrics.class.getField("widthPixels").getInt(displayMetrics);
            } catch (Exception e) {
            	screenSizeW = 320;
            }
        }
        return screenSizeW;
    }
    public static int getScreenSizeH(Context context) {
        if (screenSizeH == -1) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            try {
            	screenSizeH = DisplayMetrics.class.getField("heightPixels").getInt(displayMetrics);
            } catch (Exception e) {
            	screenSizeH = 480;
            }
        }
        return screenSizeH;
    }
}