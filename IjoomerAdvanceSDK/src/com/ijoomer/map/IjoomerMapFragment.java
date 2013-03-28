package com.ijoomer.map;

import android.app.Activity;

/**
 * This Interface Contains All Method Related To Map Fragment activity.
 * 
 * @author tasol
 * 
 */
public class IjoomerMapFragment extends IjoomerActivityHostFragment {

	@Override
	protected Class<? extends Activity> getActivityClass() {
		return IjoomerMapActivity.class;
	}

}