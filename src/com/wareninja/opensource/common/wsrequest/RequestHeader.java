
package com.wareninja.opensource.common.wsrequest;

/**
 * Copyright (c) 2011 Aspiro TV / RDML
 * http://www.aspiro.com - https://github.com/AspiroTV
 * 
 * Author: yg@aspiro.com / 15-Nov-2011
 * 
 */

/**
 * An interface used to describe an HTTP Request Header.  Implementations may
 * derive/build headers in any way they see fit (static, dynamic, contact another host, etc).
 * 
 */
public interface RequestHeader {
	
	public String getKey();
	
	public String getValue();
}
