package com.ijoomer.components.jomsocial;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.ijoomer.src.R;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;

import java.util.HashMap;

/**
 * This Fragment Contains All Method Related To JomPhotoTagDetailFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class JomPhotoFullScreenFragment extends SmartFragment implements JomTagHolder {

	private AQuery androidQuery;
	private ImageView imgPhotoDetail;
	private HashMap<String, String> photoData;
	private ProgressBar pbrLoading;
	private GestureDetector gestureDetector;

	public JomPhotoFullScreenFragment(HashMap<String, String> photoData) {
		androidQuery = new AQuery(getActivity());
		this.photoData = photoData;
	}

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_photo_full_screen_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		gestureDetector = new GestureDetector(getActivity(), new SingleTapConfirm());
		imgPhotoDetail = (ImageView) currentView.findViewById(R.id.imgPhotoDetail);
		pbrLoading = (ProgressBar) currentView.findViewById(R.id.pbrLoading);
	}

	@Override
	public void prepareViews(View currentView) {
		pbrLoading.setVisibility(View.VISIBLE);
		androidQuery.id(imgPhotoDetail).image(photoData.get("url"), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0, new BitmapAjaxCallback() {
			@Override
			protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
				super.callback(url, iv, bm, status);
				pbrLoading.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void setActionListeners(View currentView) {
		imgPhotoDetail.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gestureDetector.onTouchEvent(event);
				return true;
			}
		});
	}

	/**
	 * Inner class
	 * 
	 * @author tasol
	 * 
	 */
	private class SingleTapConfirm extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent event) {
			JomPhotoFullScreenActivity.toggleOptions();
			return true;
		}
	}

}
