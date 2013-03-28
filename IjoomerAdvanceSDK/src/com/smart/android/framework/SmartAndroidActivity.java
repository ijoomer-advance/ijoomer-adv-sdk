package com.smart.android.framework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.smart.framework.SmartActivity;

public abstract class SmartAndroidActivity extends SmartActivity implements OnCheckedChangeListener {

	// private LinearLayout footerView;
	private String[] tabItemNames;
	private RadioGroup tabBar;
	private int tabBarDividerResId, tabItemLayoutId;
	private KillReceiver clearActivityStack;

	private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };
	private static final int[] CHECKED_PRESSED_STATE_SET = { android.R.attr.state_pressed };

	private int[] tabItemOnDrawables;
	private int[] tabItemOffDrawables;
	private int[] tabItemPressDrawables;

	public abstract String[] setTabItemNames();

	public abstract int setTabBarDividerResId();

	public abstract int setTabItemLayoutId();

	public abstract int[] setTabItemOnDrawables();

	public abstract int[] setTabItemOffDrawables();

	public abstract int[] setTabItemPressDrawables();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		clearActivityStack = new KillReceiver();
		registerReceiver(clearActivityStack, IntentFilter.create("clearStackActivity", "text/plain"));
	}

	@Override
	protected void postOnCreate() {
		super.postOnCreate();
	}

	private final class KillReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(clearActivityStack);
	}


	public RadioGroup getTabBar() {
		return tabBar;
	}

	public String[] getTabItemNames() {
		return tabItemNames;
	}

	public int getTabBarDividerResId() {
		return tabBarDividerResId;
	}

	public int getTabItemLayoutId() {
		return tabItemLayoutId;
	}

}
