package com.ijoomer.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.widget.SeekBar;

import com.ijoomer.src.R;

public class IjoomerProgressView {

	String title;
	String message;
	int progress;
	Dialog dialog;
	Activity activity;
	SeekBar skProgress;
	IjoomerTextView txtMessage;
	IjoomerTextView txtProgrss;

	public IjoomerProgressView(Activity mActivity) {

		this.activity = mActivity;

		dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		dialog.setContentView(R.layout.ijoomer_loading_dialog2);
		// final IjoomerTextView txtMessage = (IjoomerTextView)
		// dialog.findViewById(R.id.txtMessage);
		// final IjoomerTextView txtProgrss = (IjoomerTextView)
		// dialog.findViewById(R.id.txtProgrss);
		// skProgress = (SeekBar) dialog.findViewById(R.id.skProgress);
		//
		// txtMessage.setText(message);
		// txtProgrss.setText("0 %");
		// skProgress.setMax(100);
		// skProgress.setProgress(0);
		// skProgress.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View arg0, MotionEvent arg1) {
		// return true;
		// }
		// });

	}

	public void showIjLoding() {
		dialog.show();
	}

	public void hideIjDilog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		// txtProgrss.setText(title);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		// txtMessage.setText(message);
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		// skProgress.setProgress(progress);
		// if (progress == 100) {
		// hideIjDilog();
		// }
	}

}
