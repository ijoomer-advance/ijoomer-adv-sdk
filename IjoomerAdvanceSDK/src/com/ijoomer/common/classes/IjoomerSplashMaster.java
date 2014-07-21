package com.ijoomer.common.classes;

import android.view.View;
import android.widget.RadioGroup;

/**
 * This Class Contains All Method Related To IjoomerSplashMaster.
 * 
 * @author tasol
 * 
 */
public abstract class IjoomerSplashMaster extends IjoomerSuperMaster {

	public IjoomerSplashMaster() {
		super();
	}

	
	/**
	 * Overrides methods
	 */
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

	@Override
	public int setFooterLayoutId() {
		return 0;
	}

	@Override
	public int setHeaderLayoutId() {
		return 0;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

}
