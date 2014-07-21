package com.ijoomer.customviews;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

/**
 * This Class Contains Method IjoomerAudioPlayer.
 * 
 * @author tasol
 * 
 */
public class IjoomerAudioPlayer {

	private MediaPlayer audioPlayer;
	private AudioListener audioListener;
	static IjoomerAudioPlayer ijoomerAudioPlayer;



	/**
	 * This method used to set audio action listener.
	 * 
	 * @param audioListener
	 *            represented audio listener
	 */
	public void setAudioListener(AudioListener audioListener) {
		this.audioListener = audioListener;
	}

	/**
	 * This method used to play audio.
	 * 
	 * @param url
	 *            represented audio path
	 */
	public void playAudio(String url) {

		if (ijoomerAudioPlayer != null) {
			ijoomerAudioPlayer.stopAudio();
			ijoomerAudioPlayer = null;
		}
		ijoomerAudioPlayer = this;
		audioPlayer = new MediaPlayer();
		audioPlayer.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				if (audioListener != null) {

					audioListener.onComplete();
				}
				return false;
			}
		});
		audioPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {

				if (audioListener != null) {

					audioListener.onComplete();
				}

			}
		});

		audioPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				audioPlayer.start();
				if (audioListener != null) {
					audioListener.onPrepared();
				}

			}
		});

		try {
			audioPlayer.setDataSource(url);
			audioPlayer.prepareAsync();

		} catch (IOException e) {
			if (audioListener != null) {

				audioListener.onComplete();
			}
			e.printStackTrace();
		}

	}

	/**
	 * This method used to stop audio.
	 */
	public void stopAudio() {
		if (audioPlayer != null) {

			if (audioPlayer.isPlaying()) {
				audioPlayer.reset();
				audioPlayer.release();
				audioPlayer = null;
				if (audioListener != null) {
					audioListener.onComplete();
				}
			} else {
				audioPlayer.reset();
				audioPlayer.release();
				audioPlayer = null;
			}
		}
	}

	/**
	 * Inner Interface
	 * 
	 * @author tasol
	 * 
	 */
	public interface AudioListener {
		void onComplete();

		void onPrepared();

		void onPlayClicked(boolean isplaying);

		void onReportClicked();
	}

	/**
	 * This method used to check is audio playing.
	 * 
	 * @return represented {@link Boolean}
	 */
	public boolean isPlaying() {
		if (audioPlayer != null) {
			if (audioPlayer.isPlaying()) {
				return true;
			}
		}
		return false;
	}

	public static void stopAll() {
		try {
			if (ijoomerAudioPlayer != null) {
				ijoomerAudioPlayer.stopAudio();
				ijoomerAudioPlayer = null;
			}
		} catch (Exception e) {
		}
	}

}
