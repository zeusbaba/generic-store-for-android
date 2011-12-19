
package com.wareninja.opensource.common;

/**
 * Copyright (c) 2011 Aspiro TV / RDML
 * http://www.aspiro.com - https://github.com/AspiroTV
 * 
 * Author: yg@aspiro.com / 15-Nov-2011
 * 
 */

public interface RequestParameter {

	/**
	 * @return the key
	 */
	public abstract String getKey();

	/**
	 * @param key the key to set
	 */
	public abstract void setKey(String key);

	/**
	 * @return the value
	 */
	public abstract Object getValue();

	/**
	 * @param value the value to set
	 */
	public abstract void setValue(Object value);

	/**
	 * Return the formatted pair.
	 */
	public abstract String format();

}