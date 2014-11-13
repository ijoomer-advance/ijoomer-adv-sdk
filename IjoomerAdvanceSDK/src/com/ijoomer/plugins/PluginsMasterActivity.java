package com.ijoomer.plugins;

import android.view.View;
import android.widget.RadioGroup;

import com.ijoomer.caching.IjoomerCachingConstants;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.src.R;

/**
 * This Class Contains UnNormalizeFields PluginsMasterActivity.
 * 
 * @author tasol
 * 
 */
public abstract class PluginsMasterActivity extends IjoomerSuperMaster implements PluginsTagHolder {

	public PluginsMasterActivity() {
		super();
		IjoomerCachingConstants.unNormalizeFields = PluginsCachingConstants.getUnnormlizeFields();
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
		return R.layout.plugins_header;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public View setBottomAdvertisement() {
		// return getAdvertisement("0445b7141d9d4e1b");
		return null;
	}

	@Override
	public View setTopAdvertisement() {
		return null; // getAdvertisement("0445b7141d9d4e1b");
	}

	@Override
	protected void onResume() {
		super.onResume();
		IjoomerCachingConstants.unNormalizeFields = PluginsCachingConstants.getUnnormlizeFields();
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
