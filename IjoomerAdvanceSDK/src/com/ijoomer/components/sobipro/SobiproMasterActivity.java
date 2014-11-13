package com.ijoomer.components.sobipro;

import android.view.View;
import android.widget.RadioGroup;

import com.ijoomer.caching.IjoomerCachingConstants;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.src.R;

public abstract class SobiproMasterActivity extends IjoomerSuperMaster implements SobiproTagHolder {
	public static SobiproTheme[] themes;

	public static int IMAGE_MAX_SIZE = 12;

	public SobiproMasterActivity() {
		super();
		IjoomerCachingConstants.unNormalizeFields = SobiproCachingConstants.getUnnormlizeFields();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
	}

	@Override
	public void loadHeaderComponents() {
		super.loadHeaderComponents();

	}

	@Override
	public void initTheme() {
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public int setHeaderLayoutId() {
		return R.layout.sobipro_header;
	}

	@Override
	public int setFooterLayoutId() {
		return R.layout.sobipro_footer;
	}

	@Override
	public String[] setTabItemNames() {
		return null;
	}

	@Override
	public int setTabBarDividerResId() {
		return 0;
	}

	@Override
	public int setTabItemLayoutId() {
		return 0;
	}

	@Override
	public int[] setTabItemOnDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemOffDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemPressDrawables() {
		return null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		IjoomerCachingConstants.unNormalizeFields = SobiproCachingConstants.getUnnormlizeFields();
	}

	public double distanceFrom(float lat1, float lng1, float lat2, float lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;
		return IjoomerUtilities.convertDistance(dist + "", IjoomerUtilities.MILE, IjoomerUtilities.MILE);
	}

}
