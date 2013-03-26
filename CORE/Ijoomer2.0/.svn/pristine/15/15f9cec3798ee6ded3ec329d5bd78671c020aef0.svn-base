package com.ijoomer.common.classes;

import java.util.ArrayList;
import java.util.HashMap;

public class IjoomerMenus {

	private ArrayList<HashMap<String, String>> tabBarData;
	private ArrayList<HashMap<String, String>> sideMenuData;
	private String selectedScreenName;
	public static IjoomerMenus ijoomerMenus;

	public static IjoomerMenus getInstance() {
		if (ijoomerMenus == null) {
			ijoomerMenus = new IjoomerMenus();
		}
		return ijoomerMenus;
	}

	private IjoomerMenus() {
	}

	public ArrayList<HashMap<String, String>> getTabBarData() {
		if (tabBarData != null && tabBarData.size() <= 0) {
			return null;
		}
		return tabBarData;
	}

	public void setTabBarData(ArrayList<HashMap<String, String>> tabBarData) {
		this.tabBarData = tabBarData;
	}

	public String getSelectedScreenName() {
		return selectedScreenName;
	}

	public void setSelectedScreenName(String selectedScreenName) {
		this.selectedScreenName = selectedScreenName;
	}

	public ArrayList<HashMap<String, String>> getSideMenuData() {
		if (sideMenuData != null && sideMenuData.size() <= 0) {
			return null;
		}
		return sideMenuData;
	}

	public void setSideMenuData(ArrayList<HashMap<String, String>> sideMenuData) {
		this.sideMenuData = sideMenuData;
	}

}
