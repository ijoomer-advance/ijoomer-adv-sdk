package com.application.configuration;

import android.widget.Toast;

import com.ijoomer.src.R;
import com.smart.framework.SmartApplication;
import com.smart.framework.SmartApplicationConfiguration;
import com.smart.framework.SmartVersionHandler;

public class ApplicationConfiguration implements SmartApplicationConfiguration, SmartVersionHandler {

	private String appName = "";

	@Override
	public String AppName(SmartApplication smartApplication) {
		appName = smartApplication.getResources().getString(R.string.app_name);
		return appName;
	}

	@Override
	public String CrashHandlerFileName() {
		return appName + "log.file";
	}

	@Override
	public boolean IsCrashHandlerEnabled() {
		return true;
	}

	@Override
	public boolean IsDBEnabled() {
		return false;
	}

	@Override
	public boolean IsSharedPreferenceEnabled() {
		return true;
	}

	@Override
	public String SecurityKey() {
		// return "1001e01d524213588a7b4d0579cdf";
		return "ac2e2b5a414f6f9a059706e13ddc41b";

	}

	@Override
	public String DatabaseName() {
		return "Ijoomer";
	}

	@Override
	public int DatabaseVersion() {
		return 1;
	}

	@Override
	public String DatabaseSQL() {
		return "Ijoomer" + ".sql";
	}

	@Override
	public void onInstalling(SmartApplication smartApplication) {
		Toast.makeText(smartApplication, "Success", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrading(int oldVersion, int newVersion, SmartApplication smartApplication) {
		Toast.makeText(smartApplication, "Old Version = " + oldVersion + ", New version = " + newVersion, Toast.LENGTH_SHORT).show();
	}

}
