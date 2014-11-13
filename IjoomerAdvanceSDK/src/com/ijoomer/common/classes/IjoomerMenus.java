package com.ijoomer.common.classes;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To IjoomerMenus.
 * 
 * @author tasol
 * 
 */
public class IjoomerMenus {

	public static IjoomerMenus ijoomerMenus;

	private ArrayList<HashMap<String, String>> tabBarData;
	private ArrayList<HashMap<String, String>> sideMenuData;
	
	private String selectedScreenName;

	private IjoomerMenus() {
	}

	/**
	 * This method used to get IjoomerMenus instance.
	 * @return represented {@link IjoomerMenus}
	 */
	public static IjoomerMenus getInstance() {
		if (ijoomerMenus == null) {
			ijoomerMenus = new IjoomerMenus();
		}
		return ijoomerMenus;
	}


	/**
	 * This method used to get tab bar data list.
	 * @return represented {@link ArrayList<HashMap<String, String>>}
	 */
	public ArrayList<HashMap<String, String>> getTabBarData() {
		if (tabBarData != null && tabBarData.size() <= 0) {
			return null;
		}
		return tabBarData;
	}

	/**
	 * This method used to set tab bar data list
	 * @param tabBarData represented tab bar data list
	 */
	public void setTabBarData(ArrayList<HashMap<String, String>> tabBarData) {
		this.tabBarData = tabBarData;
	}

	
	/**
	 * This method used to get selected screen name.
	 * @return represented {@link String}
	 */
	public String getSelectedScreenName() {
		return selectedScreenName;
	}

	/**
	 * This method used to set selected screen name
	 * @param selectedScreenName represented selected screen name
	 */
	public void setSelectedScreenName(String selectedScreenName) {
		this.selectedScreenName = selectedScreenName;
	}

	
	/**
	 * This method used to get side menu data list.
	 * @return represented {@link ArrayList<HashMap<String, String>>}
	 */
	public ArrayList<HashMap<String, String>> getSideMenuData() {
		if (sideMenuData != null && sideMenuData.size() <= 0) {
			return null;
		}
		return sideMenuData;
	}

	/**
	 * This method used to set side menu data list
	 * @param sideMenuData represented tab bar data list
	 */
	public void setSideMenuData(ArrayList<HashMap<String, String>> sideMenuData) {
		this.sideMenuData = sideMenuData;
	}

}
