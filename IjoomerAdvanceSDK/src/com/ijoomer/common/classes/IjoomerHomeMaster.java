package com.ijoomer.common.classes;

import android.view.View;
import android.webkit.WebView;
import android.widget.RadioGroup;

import com.ijoomer.src.R;


/**
 * This Class Contains All Method Related To IjoomerHomeMaster.
 * 
 * @author tasol
 * 
 */
public abstract class IjoomerHomeMaster extends IjoomerSuperMaster {

	public IjoomerHomeMaster() {
		super();
	}

	/**
	 * Override method
	 */
	
	@Override
	public int setHeaderLayoutId() {
		return R.layout.ijoomer_home_header;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public View setTopAdvertisement() {
		WebView txt = new WebView(this);
		String str = "<marquee behavior='alternate'>Top Advertisement</marquee>";
		txt.loadData(str, "text/html", "utf-8");

		return null;
	}

	@Override
	public View setBottomAdvertisement() {
		WebView txt = new WebView(this);
		String str = "<marquee direction='right' behavior='alternate'>Bottom Advertisement</marquee>";
		txt.loadData(str, "text/html", "utf-8");
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

}
