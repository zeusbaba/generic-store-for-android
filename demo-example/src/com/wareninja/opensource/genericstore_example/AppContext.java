/***
 * 	Copyright (c) 2013 WareNinja.com / BEERSTORM.net
 *  	
 *  @author yg@wareninja.com / twitter: @WareNinja
 *  @see http://www.WareNinja.com - https://github.com/WareNinja
 */

package com.wareninja.opensource.genericstore_example;

public class AppContext {
	
	//public static final String PACKAGE_APPSTORE = "ANDROID";
	public static final String APP_CACHEDIR = ".WareNinja_appCache"; 
	
	private static boolean DEBUG_MODE = false;
	public static boolean isDebugMode() {
        return DEBUG_MODE;
    }
    public static void setDebugMode(boolean debugMode) {
    	AppContext.DEBUG_MODE = debugMode;
    }
	
	public static final String APPWEBSITE_URL = "http://www.loco8.com";
	public static final String WEB_APP_BASE_URL = "http://www.loco8.com";
	public static final String WEB_APP_FAQ = "/faq";
	public static final String WEB_APP_ABOUT = "/about";
	public static final String WEB_APP_TC = "/about";//"/terms";
	public static final String WEB_APP_APPVERSION = "app_version";
	
	
	public static final String WARENINJAAPPS_MARKET_URL = "market://search?q=wareninja";
	public static final String WARENINJAAPPS_MARKET_URL_WEB = "https://play.google.com/store/search?q=wareninja";
	
	public static final String LOCO_PACKAGE_NAME = "com.wareninja.android.loco";
	public static final String LOCO_APP_MARKET_URL = "market://details?id=" + LOCO_PACKAGE_NAME;
	public static final String LOCO_APP_MARKET_URL_WEB = "https://play.google.com/store/apps/details?id=" + LOCO_PACKAGE_NAME;
	
	// persistence keys used across app!
    public static enum KEYSTORE {
    	_WARENINJA_SETTINGS_ISAPPACTIVE
    	, _WARENINJA_SAMPLE_OBJECT
    }
    
    /*
    public static enum POPOVER_ACTION {
		APPLOGIN
		, APPMAIN_CONNECT_FACEBOOK, APPMAIN_CONNECT_FOURSQUARE
		, APPMAIN_DROPDOWN, APPMAIN_DROPDOWN_SUBACTIONS
	}
    */
	
	public static final String API_FACEBOOK_GRAPH_BASE_URL = "https://graph.facebook.com";
	public static final String API_FACEBOOK_GRAPH_BASE_URL_SSL = "https://graph.facebook.com";
	public static final String API_FACEBOOK_WEB_BASE_URL = "http://www.facebook.com";
	public static final String API_FACEBOOK_MOBILEWEB_BASE_URL = "http://m.facebook.com";
	
	public static final String API_TWITTER_PROFILEIMG_BASE_URL = "https://api.twitter.com/1/users/profile_image/";
	
    // --- Menu items ---
    public static final int MENU_ABOUT = 10;
	public static final int MENU_RATEAPP = 12;
	public static final int MENU_TC = 13;
	public static final int MENU_FAQ = 14;
	public static final int MENU_RELOAD = 15;
    //---
	
	
	// --- GOOGLE Analytics stuff ---
    public static final String GOOGLE_TRACKER_PAGE_SETTINGS_WARENINJAAPPS = "P_SETTINGS_WARENINJAAPPS";
	
	public static final String GOOGLE_TRACKER_PAGE_APPMAIN = "P_APPMAIN";
	public static final String GOOGLE_TRACKER_ACTION_NEWVERSION = "NEW_VERSION";
	
}
