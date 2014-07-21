package com.smart.framework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public abstract class SmartFragment extends Fragment implements SmartFragmentHandler {

	private View fragmentView;

	public View getFragmentView() {
		return fragmentView;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (setLayoutView() != null) {
			fragmentView = setLayoutView();
		} else if (setLayoutId() != 0) {
			fragmentView = inflater.inflate(setLayoutId(), container, false);
		} else {
			fragmentView = new LinearLayout(getActivity());
		}

		initComponents(fragmentView);
		prepareViews(fragmentView);
		setActionListeners(fragmentView);

		return fragmentView;
	}

	/**
	 * This method used to add fragment to given layout id.
	 * 
	 * @param layoutId
	 *            represented layout id
	 * @param fragment
	 *            represented fragment
	 */
	public void addFragment(int layoutId, Fragment fragment) {
		FragmentManager fm = getChildFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(layoutId, fragment);
		ft.commit();
	}

}
