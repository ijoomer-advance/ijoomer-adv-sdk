package com.smart.framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class CrashReportActivity extends Activity {

	public static final String EXTRA_STACKTRACE = "hbk.crash.stackTrace";

	private String stackTrace;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		stackTrace = getIntent().getStringExtra(EXTRA_STACKTRACE);

		getMessage("Application Crashed!!\nPlease send " + SmartApplication.REF_SMART_APPLICATION.LOGFILENAME + " from SDCARD to administrator.");

	}

	private void appendLog(String text) {
		File logFile = new File("sdcard/" + SmartApplication.REF_SMART_APPLICATION.LOGFILENAME);
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
			buf.append(text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getMessage(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		int imageResource = android.R.drawable.ic_dialog_alert;
		Drawable image = getResources().getDrawable(imageResource);

		builder.setTitle(msg).setMessage(getIntent().getStringExtra("data")).setIcon(image).setCancelable(false).setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				appendLog(stackTrace);
				dialog.dismiss();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

}
