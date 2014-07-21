package com.ijoomer.weservice;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Interface For web Service Call Listener.
 * @author tasol
 *
 */
public interface WebCallListenerWithCacheInfo {

	/**
	 * This method calls by library when web service call and data parsing / caching done.
	 * @param responseCode represent call status
	 * @param errorMessage represent error message 
	 * @param data1 by default response data represented in this variable
	 * @param data2 optional, used in case of response contains more than one type of data
	 */
	public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2,int pageNo,int pageLimit,boolean fromCache);
	
	/**
	 * This method continuously call by library during the web service call.
	 * @param progressCount represent call progress (1-100)
	 */
	public void onProgressUpdate(int progressCount);

}
