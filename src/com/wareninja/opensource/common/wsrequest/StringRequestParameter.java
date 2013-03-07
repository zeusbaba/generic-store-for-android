
package com.wareninja.opensource.common.wsrequest;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Copyright (c) 2011 Aspiro TV / RDML
 * http://www.aspiro.com - https://github.com/AspiroTV
 * 
 * Author: yg@aspiro.com / 15-Nov-2011
 * 
 */

/**
 * source code adapted from com.ginsberg.gowalla
 */
public class StringRequestParameter implements Serializable, RequestParameter {

	private static final long serialVersionUID = 7708741382098198002L;
	private String key;
	private Object value;
	
	public StringRequestParameter(final String key, final Object value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String format() {
		try {
			return String.format("%s=%s", key, URLEncoder.encode(String.valueOf(value), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return String.format("%s=%s", key, value);
		}
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("StringRequestParameter [key=%s, value=%s]", key,
				value);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringRequestParameter other = (StringRequestParameter) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
