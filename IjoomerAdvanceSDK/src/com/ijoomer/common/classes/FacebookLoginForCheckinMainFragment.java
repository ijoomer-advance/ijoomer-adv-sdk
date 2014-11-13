package com.ijoomer.common.classes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.widget.LoginButton;
import com.ijoomer.src.R;

import java.util.Arrays;


/**
 * This Class Contains All Method Related FacebookLoginMainFragment.
 * 
 * @author tasol
 * 
 */
public class FacebookLoginForCheckinMainFragment extends Fragment {

	
	/**
	 * Override method
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.facebook_main, container, false);

		LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
		authButton.setFragment(this);
		authButton.setPublishPermissions(Arrays.asList("publish_checkins",
				"publish_stream", "publish_actions"));
		authButton.setApplicationId(getString(R.string.facebook_app_id));

		return view;
	}
}
