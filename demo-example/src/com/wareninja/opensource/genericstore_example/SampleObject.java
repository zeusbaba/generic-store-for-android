package com.wareninja.opensource.genericstore_example;


import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wareninja.opensource.common.WareNinjaUtils;

public class SampleObject implements Serializable {
	private static final long serialVersionUID = 1L;
	final String TAG = SampleObject.class.getSimpleName();
	
	public String oid;
	public Map<String, String> dummy_data = new LinkedHashMap<String, String>();

	public Long createdAt;
	public String createdAtStr;
	public void setCreatedAt(Long createdAt) {
		try {
			this.createdAt = createdAt;
			if (createdAt!=null && createdAt!=0L) this.createdAtStr = WareNinjaUtils.getFormattedDate(createdAt);
		} catch (Exception ex) {}
	}
	public void setCreatedAt(Long createdAt, String timeZone) {
		try {
			this.createdAt = createdAt;
			if (createdAt!=null && createdAt!=0L) this.createdAtStr = WareNinjaUtils.getFormattedDate(createdAt, timeZone);
		} catch (Exception ex) {}
	}
	
	
	@Override
	public String toString() {
		return TAG+" [oid=" + oid + ", dummy_data=" + dummy_data
				+ ", createdAt=" + createdAt + ", createdAtStr=" + createdAtStr
				+ "]";
	}
}

