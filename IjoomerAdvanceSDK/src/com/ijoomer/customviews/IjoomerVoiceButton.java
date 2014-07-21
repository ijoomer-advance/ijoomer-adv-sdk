package com.ijoomer.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
import com.ijoomer.src.R;

/**
 * This Class Contains Method IjoomerVoiceButton.
 * 
 * @author tasol
 * 
 */
public class IjoomerVoiceButton extends LinearLayout {

	private LinearLayout lnrPlayVoice;
	private LinearLayout lnrReportVoice;
	private IjoomerTextView txtButtonCaption;
	private IjoomerTextView txtReportCaption;
	private ImageView imgPlay;
	private ImageView imgReport;
	private IjoomerGifView gifVoiceLoader;

	private String text;
	private String strReportCaption;
	private int playImageResId;
	private int voiceLoderGif;
	private int reportCaption;
	private int reportImage;
	private boolean isPlaying;
	private boolean reportVoice;

	private AudioListener audioListener;
	private IjoomerAudioPlayer ijoomerAudioPlayer;
	private String audioPath;
	private ProgressBar pbrLoading;

	// private boolean notifyOnComplete = true;
	//
	// public boolean isNotifyOnComplete() {
	// return notifyOnComplete;
	// }
	//
	// public void setNotifyOnComplete(boolean notifyOnComplete) {
	// this.notifyOnComplete = notifyOnComplete;
	// }

	public String getAudioPath() {
		return audioPath;
	}

	public void setAudioPath(String audioPath, boolean startPlaying) {
		this.audioPath = audioPath;

		if (startPlaying) {
			playVoice();
		}
	}

	public AudioListener getAudioListener() {
		return audioListener;
	}

	public void setAudioListener(AudioListener audioListener) {
		this.audioListener = audioListener;
	}

	@SuppressLint("NewApi")
	public IjoomerVoiceButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	public IjoomerVoiceButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public IjoomerVoiceButton(Context context) {
		super(context);
		init(null);
	}

	private void init(AttributeSet attrs) {
		setGravity(Gravity.CENTER_VERTICAL);

		if (attrs == null) {
			voiceLoderGif = R.drawable.ijoomer_voice_loder_gif;
			playImageResId = R.drawable.ijoomer_audio_play;
			text = "0";
			isPlaying = false;
			reportVoice = true;
			reportImage = R.drawable.ijoomer_report_icon;
			reportCaption = 0;

		} else {
			String namespace = "http://schemas.android.com/apk/res/" + getContext().getPackageName();
			voiceLoderGif = attrs.getAttributeResourceValue(namespace, "voice_loder_gif", R.drawable.ijoomer_voice_loder_gif);
			playImageResId = attrs.getAttributeResourceValue(namespace, "play_image", R.drawable.ijoomer_audio_play);
			reportImage = attrs.getAttributeResourceValue(namespace, "report_icon", R.drawable.ijoomer_report_icon);
			reportCaption = attrs.getAttributeResourceValue(namespace, "report_caption", 0);
			reportVoice = attrs.getAttributeBooleanValue(namespace, "report_voice", true);
			text = "0";
		}

		createView();
	}

	/**
	 * This method used to create view.
	 */
	private void createView() {
		if (txtButtonCaption == null) {
			View v = LayoutInflater.from(getContext()).inflate(R.layout.ijoomer_voice_button, null);

			lnrPlayVoice = (LinearLayout) v.findViewById(R.id.lnrPlayVoice);
			lnrReportVoice = (LinearLayout) v.findViewById(R.id.lnrReportVoice);

			txtButtonCaption = (IjoomerTextView) v.findViewById(R.id.txtButtonCaption);
			imgPlay = (ImageView) v.findViewById(R.id.imgPlay);
			gifVoiceLoader = (IjoomerGifView) v.findViewById(R.id.gifVoiceLoader);

			imgReport = (ImageView) v.findViewById(R.id.imgReport);
			txtReportCaption = (IjoomerTextView) v.findViewById(R.id.txtReportCaption);

			pbrLoading = (ProgressBar) v.findViewById(R.id.pbrLoading);

			addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			updateView();
			setActionListener();
		}
	}

	/**
	 * This method used to update view.
	 */
	private void updateView() {
		pbrLoading.setVisibility(View.GONE);
		if (isPlaying()) {
			gifVoiceLoader.setVisibility(View.VISIBLE);
			imgPlay.setVisibility(View.GONE);
		} else {
			gifVoiceLoader.setVisibility(View.GONE);
			imgPlay.setVisibility(View.VISIBLE);
		}
		imgPlay.setImageResource(getPlayImageResId());
		gifVoiceLoader.setGifImageResourceID(getVoiceLoderGif());
		txtButtonCaption.setText(getText() + " s");

	}

	/**
	 * This method used to update custom view.
	 */
	private void customUpdateView() {
		pbrLoading.setVisibility(View.GONE);
		if (isPlaying()) {
			gifVoiceLoader.setVisibility(View.VISIBLE);
			imgPlay.setVisibility(View.GONE);
		} else {
			gifVoiceLoader.setVisibility(View.GONE);
			imgPlay.setVisibility(View.VISIBLE);
		}
		imgPlay.setImageResource(getPlayImageResId());
		gifVoiceLoader.setGifImageResourceID(getVoiceLoderGif());
		txtButtonCaption.setText(getText());

	}

	/**
	 * This method used to set button action listener.
	 */
	private void setActionListener() {

		lnrPlayVoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (audioListener != null) {
					audioListener.onPlayClicked(isPlaying);
				}
				lnrReportVoice.setVisibility(View.GONE);
				playVoice();
			}
		});

		lnrReportVoice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (audioListener != null) {
					audioListener.onReportClicked();
				}
			}
		});
	}

	private void startVoiceLoader() {
		isPlaying = true;
		pbrLoading.setVisibility(View.VISIBLE);
		gifVoiceLoader.setVisibility(View.GONE);
		imgPlay.setVisibility(View.GONE);
	}

	private void playVoice() {
		if (isPlaying()) {
			stopPlaying();
			ijoomerAudioPlayer.stopAudio();
		} else {
			ijoomerAudioPlayer = new IjoomerAudioPlayer();
			ijoomerAudioPlayer.setAudioListener(new AudioListener() {

				@Override
				public void onReportClicked() {
				}

				@Override
				public void onPrepared() {
					startPlaying();
					audioListener.onPrepared();
				}

				@Override
				public void onPlayClicked(boolean isplaying) {
				}

				@Override
				public void onComplete() {
					stopPlaying();
					audioListener.onComplete();
				}
			});
			startVoiceLoader();
			ijoomerAudioPlayer.playAudio(getAudioPath());
		}
	}

	/**
	 * This method used to set is voice report.
	 * 
	 * @param reportVoice
	 *            represented is voice report.
	 */
	public void setReportVoice(boolean reportVoice) {
		this.reportVoice = reportVoice;
	}

	/**
	 * This method used to set voice report caption.
	 * 
	 * @param strReportCaption
	 *            represented voice report caption
	 */
	public void setStrReportCaption(String strReportCaption) {
		this.strReportCaption = strReportCaption;
	}

	/**
	 * This method used to check is playing.
	 * 
	 * @return represented {@link Boolean}
	 */
	public boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * This method used to start playing.
	 */
	public void startPlaying() {
		pbrLoading.setVisibility(View.GONE);
		isPlaying = true;
		if (IjoomerApplicationConfiguration.isEnableVoiceReport && reportVoice) {
			lnrReportVoice.setVisibility(View.VISIBLE);
			imgReport.setImageResource(reportImage);

			if (reportCaption != 0) {
				txtReportCaption.setText(reportCaption);
			} else if (strReportCaption != null && strReportCaption.length() > 0) {
				txtReportCaption.setText(strReportCaption);
			}
		} else {
			lnrReportVoice.setVisibility(View.GONE);
		}
		updateView();
	}

	/**
	 * This method used to stop playing.
	 */
	public void stopPlaying() {
		pbrLoading.setVisibility(View.GONE);
		isPlaying = false;
		updateView();
	}

	/**
	 * This method used to get voice loader gif id.
	 * 
	 * @return represented {@link Integer}
	 */
	private int getVoiceLoderGif() {
		return voiceLoderGif;
	}

	/**
	 * This method used to set voice loader gif id.
	 * 
	 * @param voiceLoderGif
	 *            represented gif id.
	 */
	public void setVoiceLoderGif(int voiceLoderGif) {
		this.voiceLoderGif = voiceLoderGif;
	}

	/**
	 * This method used to get play button text.
	 * 
	 * @return represented {@link String}
	 */
	private String getText() {
		return text;
	}

	/**
	 * This method used to set play button text.
	 * 
	 * @param text
	 *            represented button text
	 */
	public void setText(String text) {
		this.text = text;
		updateView();
	}

	/**
	 * This method used to set custom play button text.
	 * 
	 * @param text
	 *            represented button text
	 */
	public void setCustomText(String text) {
		this.text = text;
		customUpdateView();
	}

	/**
	 * This method used to get play image resource id.
	 * 
	 * @return represented {@link Integer}
	 */
	private int getPlayImageResId() {
		return playImageResId;
	}

	/**
	 * This method used to set play image resource id.
	 * 
	 * @param playImageResId
	 *            represented play image id
	 */
	public void setPlayImageResId(int playImageResId) {
		this.playImageResId = playImageResId;
	}

	public void destroy() {
		try {
			ijoomerAudioPlayer.stopAudio();
		} catch (Exception e) {
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		try {
			ijoomerAudioPlayer.stopAudio();
		} catch (Exception e) {
		}
	}

}
