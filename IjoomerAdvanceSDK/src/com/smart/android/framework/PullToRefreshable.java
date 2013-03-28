package com.smart.android.framework;

public interface PullToRefreshable {
	
	public int setLayoutId();
	public int setRefreshTextID();
	public int setRefreshImageID();
	public int setRefreshProgressID();
	public int setRefreshUpdatedID();
	
	public int setRefreshImage();
	
	public String setPullLabel();
	public String setReleaseLabel();
	public String setRefreshingLabel();
	public String setTapLabel();
}
