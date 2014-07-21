package com.ijoomer.caching;

import android.content.ContentValues;

/**
 * This interfcae Contains Method IjoomerCachingInsertListener.
 * 
 * @author tasol
 * 
 */
public interface IjoomerCachingInsertListener {
	void onBeforeInsert(ContentValues dataToInsert);
}
