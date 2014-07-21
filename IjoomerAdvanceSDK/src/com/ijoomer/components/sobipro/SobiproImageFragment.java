package com.ijoomer.components.sobipro;

import android.view.View;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.ijoomer.src.R;
import com.smart.framework.SmartFragment;

/**
 *  This Fragment Contains All Method Related To SobiproImageFragment.
 * 
 * @author tasol
 * 
 */

public class SobiproImageFragment extends SmartFragment {
	private ImageView img;
	private AQuery aQuery;
	private String imgUrl;
	
	/**
	 * Constructor
	 * @param imgUrl represented image url which will need to display.
	 */

	public SobiproImageFragment(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	/**
	 * Overrides methods.
	 */

	@Override
	public int setLayoutId() {
		return R.layout.sobipro_image_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		aQuery = new AQuery(getActivity());
		img = (ImageView) currentView.findViewById(R.id.sobiproImg);

	}

	@Override
	public void prepareViews(View currentView) {
		aQuery.id(img).image(imgUrl);
	}

	@Override
	public void setActionListeners(View currentView) {
	}

}
