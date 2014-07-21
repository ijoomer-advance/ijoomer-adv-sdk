package com.ijoomer.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.widget.SeekBar;

import com.ijoomer.src.R;

/**
 * This Class Contains All Method Related To IjoomerProgressView.
 * 
 * @author tasol
 * 
 */
public class IjoomerProgressView {

	IjoomerTextView txtMessage;
	IjoomerTextView txtProgrss;
	Dialog dialog;
	SeekBar skProgress;

	Activity activity;

	String title;
	String message;
	int progress;

	public IjoomerProgressView(Activity mActivity) {
		this.activity = mActivity;
		dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dialog.setContentView(R.layout.ijoomer_loading_dialog2);
	}

	/**
	 * This method used to show ijoomer loading dialog. 
	 */
	public void showIjLoding() {
		dialog.show();
	}

	/**
	 * This method used to hide ijoomer loading dialog.
	 */
	public void hideIjDilog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	/**
	 * This method used to get dialog title.
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This method used to set dialog title.
	 * @param title represented title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * This method used to get dialog message.
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * This method used to set dialog message.
	 * @param message represented message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	
	/**
	 * This method used to get dialog progress.
	 * @return represented {@link Integer}
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * This method used to set dialog progress.
	 * @param progress represented progress
	 */
	public void setProgress(int progress) {
		this.progress = progress;
	}
}
