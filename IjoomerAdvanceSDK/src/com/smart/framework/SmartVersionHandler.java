package com.smart.framework;

public interface SmartVersionHandler {

	/**
	 * This method will be called whenever the application is being installed for the first time. This will be called after the database queries has been executed.
	 * NOTE :- If this method has crash, application will not be loaded as these are the first statements to be executed in the flow.
	 */
	public abstract void onInstalling(SmartApplication smartApplication);
	
	/**
	 * This method will be called whenever the application is being replaced after words. This will be called after the database queries has been executed.
	 *  NOTE :- If this method has crash, application will not be loaded as these are the first statements to be executed in the flow.
	 * @param oldVersion = Existing database version number of application
	 * @param newVersion = New updating database version number being installed
	 */
	public abstract void onUpgrading(int oldVersion, int newVersion, SmartApplication smartApplication);
	
}
