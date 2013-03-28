package com.ijoomer.common.classes;

import android.view.View;
import android.webkit.WebView;
import android.widget.RadioGroup;

import com.ijoomer.src.R;

public abstract class IjoomerHomeMaster extends IjoomerSuperMaster {

	public IjoomerHomeMaster() {
		super();
	}

	@Override
	public int setTabBarDividerResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setTabItemLayoutId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] setTabItemNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] setTabItemOffDrawables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] setTabItemOnDrawables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] setTabItemPressDrawables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

	}

	@Override
	public int setFooterLayoutId() {
		return 0;
	}

	@Override
	public int setHeaderLayoutId() {
		return R.layout.ijoomer_header;
	}

	@Override
	public View setLayoutView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View setTopAdvertisement() {
		// TODO Auto-generated method stub
		WebView txt = new WebView(this);
		String str = "<marquee behavior='alternate'>Top Advertisement</marquee>";
		txt.loadData(str, "text/html", "utf-8");

		return null;
	}

	@Override
	public View setBottomAdvertisement() {
		// TODO Auto-generated method stub
		WebView txt = new WebView(this);
		String str = "<marquee direction='right' behavior='alternate'>Bottom Advertisement</marquee>";
		txt.loadData(str, "text/html", "utf-8");
		return null;
	}

}
