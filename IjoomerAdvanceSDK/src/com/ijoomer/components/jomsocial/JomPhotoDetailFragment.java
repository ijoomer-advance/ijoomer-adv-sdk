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
import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
import com.ijoomer.customviews.IjoomerVoiceButton;
import com.ijoomer.src.R;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;

/**
 * This Fragment Contains All Method Related To JomPhotoDetailFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class JomPhotoDetailFragment extends SmartFragment {

	private ImageView imgPhotoDetail;
	private IjoomerVoiceButton btnPlayStopVoice;

	private GestureDetector gestureDetector;
	private CustomClickListner local;
	private AQuery androidQuery;

	private String photoUrl;
	private String photoVoice;

	private ProgressBar pbrLoading;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_photo_detail_image_item;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		gestureDetector = new GestureDetector(getActivity(), new SingleTapConfirm());
		imgPhotoDetail = (ImageView) currentView.findViewById(R.id.imgPhotoDetail);
		btnPlayStopVoice = (IjoomerVoiceButton) currentView.findViewById(R.id.btnPlayStopVoice);
		btnPlayStopVoice.setVisibility(View.GONE);
		pbrLoading = (ProgressBar) currentView.findViewById(R.id.pbrLoading);
	}

	@Override
	public void prepareViews(View currentView) {
		pbrLoading.setVisibility(View.VISIBLE);
		androidQuery.id(imgPhotoDetail).image(photoUrl, true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0, new BitmapAjaxCallback() {
			@Override
			protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
				super.callback(url, iv, bm, status);
				pbrLoading.setVisibility(View.GONE);
			}
		});
		// androidQuery.id(imgPhotoDetail).image(photoUrl, true, true,
		// ((SmartActivity) getActivity()).getDeviceWidth(), 0);
	}

	@Override
	public void setActionListeners(View currentView) {
		if (((JomMasterActivity) getActivity()).getAudio(photoVoice) != null) {

			btnPlayStopVoice.setAudioPath(((JomMasterActivity) getActivity()).getAudio(photoVoice), false);
			btnPlayStopVoice.setAudioListener(new AudioListener() {

				@Override
				public void onReportClicked() {
					((JomMasterActivity) getActivity()).reportVoice(((JomMasterActivity) getActivity()).getAudio(photoVoice));
				}

				@Override
				public void onPrepared() {
				}

				@Override
				public void onPlayClicked(boolean isplaying) {
				}

				@Override
				public void onComplete() {
				}
			});

			btnPlayStopVoice.setVisibility(View.VISIBLE);
			btnPlayStopVoice.setText(((JomMasterActivity) getActivity()).getAudioLength(photoVoice));

		}
		imgPhotoDetail.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gestureDetector.onTouchEvent(event);
				return true;
			}
		});
	}

	/**
	 * Class methods
	 */

	/**
	 * Constructor
	 * 
	 * @param url
	 *            represented photo path
	 * @param voice
	 *            represented photo voice data
	 * @param target
	 *            represented {@link CustomClickListner}
	 */
	public JomPhotoDetailFragment(String url, String voice, CustomClickListner target) {
		androidQuery = new AQuery(getActivity());
		photoUrl = url;
		photoVoice = voice;
		local = target;
	}

	/**
	 * This method used to set voice from photo voice data.
	 * 
	 * @param photoVoice
	 *            represented voice data
	 */
	public void setVoice(final String photoVoice) {

		btnPlayStopVoice.setAudioPath(((JomMasterActivity) getActivity()).getAudio(photoVoice), false);
		btnPlayStopVoice.setAudioListener(new AudioListener() {

			@Override
			public void onReportClicked() {
				((JomMasterActivity) getActivity()).reportVoice(((JomMasterActivity) getActivity()).getAudio(photoVoice));
			}

			@Override
			public void onPrepared() {
			}

			@Override
			public void onPlayClicked(boolean isplaying) {
			}

			@Override
			public void onComplete() {
			}
		});

		btnPlayStopVoice.setVisibility(View.VISIBLE);
		btnPlayStopVoice.setText(((JomMasterActivity) getActivity()).getAudioLength(photoVoice));

	}

	/**
	 * Inner class
	 * 
	 * @author tasol
	 * 
	 */
	private class SingleTapConfirm extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			local.onClick("");
			return true;
		}
	}

}
