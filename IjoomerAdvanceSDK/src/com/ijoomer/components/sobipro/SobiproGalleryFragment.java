package com.ijoomer.components.sobipro;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.src.R;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;

/**
 * This Fragment Contains All Method Related To SobiproGalleryFragment.
 * 
 * @author tasol
 * 
 */

public class SobiproGalleryFragment extends SmartFragment implements SobiproTagHolder {

	private ViewPager viewPager;
	private LinearLayout lnrImgs;
	private PhotoAdapter photoAdapter;
	String IN_IMAGES[];
	int IN_INDEX;
	AQuery aQuery;

	/**
	 * Overrides methods.
	 */

	@Override
	public int setLayoutId() {
		return R.layout.sobipro_gallery_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		viewPager = (ViewPager) currentView.findViewById(R.id.viewPager);
		aQuery = new AQuery(getActivity());
		lnrImgs = (LinearLayout) currentView.findViewById(R.id.lnrImgs);
		IN_INDEX = 0;
		photoAdapter = new PhotoAdapter(getActivity().getSupportFragmentManager());
	}

	@Override
	public void prepareViews(View currentView) {
		getIntentData();
		if (IN_IMAGES != null) {
			prepareBottomView();
			viewPager.setAdapter(photoAdapter);
			viewPager.setCurrentItem(IN_INDEX);
		}
	}

	@Override
	public void setActionListeners(View currentView) {
	}

	/**
	 * Class methods.
	 */

	/**
	 * This method is used to set Gallery bottom scrollable thumbnail view.
	 */
	@SuppressLint("NewApi")
	public void prepareBottomView() {
		for (int i = 0; i < IN_IMAGES.length; i++) {

			ImageView img = new ImageView(getActivity());
			img.setBackgroundColor(Color.WHITE);
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(((SmartActivity) getActivity()).convertSizeToDeviceDependent(50),
					((SmartActivity) getActivity()).convertSizeToDeviceDependent(50));
			param.setMargins(2, 2, 2, 2);
			img.setScaleType(ScaleType.FIT_XY);
			img.setId(i);
			img.setTag(i);
			img.setPadding(2, 2, 2, 2);

			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					viewPager.setCurrentItem((Integer) v.getTag());
				}
			});
			aQuery.id(img).image(IN_IMAGES[i]);
			lnrImgs.addView(img, param);

		}

	}

	/**
	 * Custom Photo Adapter for gallery view.
	 */

	private class PhotoAdapter extends FragmentStatePagerAdapter {

		public PhotoAdapter(FragmentManager fm) {
			super(fm);
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int pos) {
			SobiproImageFragment fragment = new SobiproImageFragment(IN_IMAGES[pos]);
			return fragment;
		}

		@Override
		public int getCount() {
			return IN_IMAGES.length;
		}

	}

	/**
	 * This method is used to get intent data.
	 */

	private void getIntentData() {
		try {
			IN_IMAGES = ((IjoomerSuperMaster) getActivity()).getStringArray(((SmartActivity) getActivity()).getIntent().getStringExtra("IN_IMAGES"));
			IN_INDEX = getActivity().getIntent().getIntExtra("IN_INDEX", 0);
		} catch (Exception e) {
			IN_IMAGES = null;
		}

	}
}
