package com.ijoomer.components.jReview;

import android.view.View;
import android.widget.RadioGroup;

import com.ijoomer.caching.IjoomerCachingConstants;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.src.R;

public abstract class JReviewMasterActivity extends IjoomerSuperMaster implements JReviewTagHandler {
	public JReviewMasterActivity() {
		super();
		IjoomerCachingConstants.unNormalizeFields = JReviewCachingConstants.getUnnormlizeFields();
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
		return R.layout.jreview_header;
	}

	@Override
	public int setFooterLayoutId() {
		return 0;
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
		IjoomerApplicationConfiguration.setEnableSmiley(false);
		IjoomerCachingConstants.unNormalizeFields = JReviewCachingConstants.getUnnormlizeFields();
	}
}

