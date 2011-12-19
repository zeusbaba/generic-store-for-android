
package com.wareninja.opensource.common;

/**
 * Copyright (c) 2011 Aspiro TV / RDML
 * http://www.aspiro.com - https://github.com/AspiroTV
 * 
 * Author: yg@aspiro.com / 15-Nov-2011
 * 
 */

/**
 * Immutable request header.  Thread safe.
 * 
 */
public class PlainRequestHeader implements RequestHeader {

	private String key;
	private String value;
	
	/**
	 * Constructor, specify key and value.
	 */
	public PlainRequestHeader(final String key, final String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlainRequestHeader [key=");
		builder.append(key);
		builder.append(", value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlainRequestHeader other = (PlainRequestHeader) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

}
