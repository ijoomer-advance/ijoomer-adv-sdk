package com.ijoomer.components.icms;

import android.view.View;
import android.widget.RadioGroup;

import com.ijoomer.caching.IjoomerCachingConstants;
import com.ijoomer.common.classes.IjoomerAdManager;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.src.R;

/**
 * This Class Contains All Method Related To IcmsMasterActivity.
 * 
 * @author tasol
 * 
 */
public abstract class IcmsMasterActivity extends IjoomerSuperMaster implements IcmsTagHolder {

	public IcmsMasterActivity() {
		super();
		IjoomerCachingConstants.unNormalizeFields = IcmsCachingConstants.getUnnormlizeFields();
	}

	/**
	 * Overrides methods
	 */

	@Override
	public int setFooterLayoutId() {
		return R.layout.ijoomer_footer;
	}

	@Override
	public int setHeaderLayoutId() {
		return R.layout.icms_header;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public View setBottomAdvertisement() {

		return IjoomerAdManager.getInstance().getBottomAdvertisement(this);
		// return null;
	}

	@Override
	public View setTopAdvertisement() {

		return null;
		// getAdvertisement("0445b7141d9d4e1b");
	}


	@Override
	protected void onResume() {
		super.onResume();
		IjoomerCachingConstants.unNormalizeFields = IcmsCachingConstants.getUnnormlizeFields();
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
	public String[] setTabItemNames() {
		return null;
	}

	@Override
	public int[] setTabItemOffDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemOnDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemPressDrawables() {
		return null;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

	}
}
