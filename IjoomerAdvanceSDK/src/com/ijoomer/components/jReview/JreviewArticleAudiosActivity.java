package com.ijoomer.components.jReview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.R;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

public class JreviewArticleAudiosActivity extends JReviewMasterActivity{

	private String IN_ARTICLENAME;
	private String IN_ARTICLEID;
	private String IN_CATEGORYID;

	private ListView lstAudios;
	private boolean flag_play;
	private static MediaPlayer mPlayer = null;

	private ArrayList<SmartListItem> fileListData = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> IN_AUDIOLIST;
	private ArrayList<HashMap<String, String>> IN_ARTICLE_DETAILS;
	private SmartListAdapterWithHolder fileListAdapter;
	private JReviewDataProvider dataProvider;
	private IjoomerCaching iCaching;

	Handler seekHandler = new Handler();

	@Override
	public int setLayoutId() {
		return R.layout.jreview_article_audios_list;
	}

	@Override
	public void initComponents() {
		getIntentData();

		dataProvider = new JReviewDataProvider(this);
		iCaching = new IjoomerCaching(this);

		lstAudios = (ListView) findViewById(R.id.lstAudios);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(IjoomerApplicationConfiguration.isReloadRequired()){
			getArticleAudios();
			IjoomerApplicationConfiguration.setReloadRequired(false);
		}
	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(IN_ARTICLENAME);

		getArticleAudios();
	}

	@Override
	public void setActionListeners() {

	}

	/**
	 * Class methods
	 */


	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		try{
			IN_CATEGORYID = getIntent().getStringExtra(CATEGORY_ID) == null ? "0" : getIntent().getStringExtra(CATEGORY_ID);
			IN_ARTICLENAME = getIntent().getStringExtra(ARTICLENAME) == null ? "" : getIntent().getStringExtra(ARTICLENAME) ;
			IN_ARTICLEID = getIntent().getStringExtra(ARTICLEID) == null ? "" : getIntent().getStringExtra(ARTICLEID);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to get article details.
	 */
	private void getArticleAudios(){
		try{
			IN_ARTICLE_DETAILS = dataProvider.getArticleAudios(IN_CATEGORYID, IN_ARTICLEID);
			IN_AUDIOLIST = iCaching.parseData(new JSONArray(IN_ARTICLE_DETAILS.get(0).get(MEDIAAUDIO)));
		}catch(Exception e){
			IN_AUDIOLIST = null;
			e.printStackTrace();
		}

		if (IN_AUDIOLIST !=null && IN_AUDIOLIST.size()>0) {
			//prepare attachment list
			prepareFileList(IN_AUDIOLIST,false);
			fileListAdapter = getFileListAdapter();
			lstAudios.setAdapter(fileListAdapter);
		}
	}

	/**
	 * This method used to prepare list for file.
	 * 
	 * @param append
	 *            represented data append
	 */
	private void prepareFileList(ArrayList<HashMap<String, String>> fileList,boolean append) {
		if (fileList != null) {
			if (!append) {
				fileListData.clear();
			}
			for (HashMap<String, String> hashMap : fileList) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jreview_article_audios_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					fileListAdapter.add(item);
				} else {
					fileListData.add(item);
				}
			}

		}
	}

	/**
	 * List adapter for file.
	 * 
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	private SmartListAdapterWithHolder getFileListAdapter() {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.jreview_article_audios_list_item, fileListData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.jreviewarticletxtAudioFileTitle = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtAudioTitle);
				holder.jreviewarticleAudioPlaybtn = (ImageView) v.findViewById(R.id.audioplay);
				holder.jreviewarticletxtAudioDuration = (IjoomerTextView) v.findViewById(R.id.txtDuration);
				holder.jreviewarticleAudioProgressbar = (ProgressBar) v.findViewById(R.id.skProgress);

				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				holder.jreviewarticletxtAudioFileTitle.setText(row.get(TITLE)+"."+row.get(FILEEXTENSION));
				holder.jreviewarticleAudioPlaybtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						for(int i = 0; i < lstAudios.getCount(); i++){
							View listItem = (View) lstAudios.getAdapter().getView(i, lstAudios.getChildAt(i), lstAudios);
							ProgressBar progressBar = (ProgressBar) listItem.findViewById(R.id.skProgress);
							IjoomerTextView Duration = (IjoomerTextView) listItem.findViewById(R.id.txtDuration);
							progressBar.setProgress(0);
							Duration.setText("00:00/00:00");
						}

						if (!flag_play) {
							startPlaying("http://sbmobile.vir2o.com/media/musicroom/disha/newww/tumbin03www.songs.pk.mp3");
							flag_play = true;
						} else {
							mPlayer.stop();
							holder.jreviewarticleAudioProgressbar.setProgress(0);
							flag_play = false;
						}
					}

					private void startPlaying(String file) {
						try {
							mPlayer = new MediaPlayer();

							mPlayer.reset();
							mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
							mPlayer.setDataSource(file);
							mPlayer.prepareAsync();
							mPlayer.setOnErrorListener(new OnErrorListener() {
								@Override
								public boolean onError(final MediaPlayer mPlayer, final int what,
										final int extra) {
									return false;
								}
							}); 

							mPlayer.setOnPreparedListener(new OnPreparedListener() {

								@Override
								public void onPrepared(MediaPlayer mPlayer) {
									mPlayer.start();
								}
							});

							mPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

								@Override
								public void onBufferingUpdate(MediaPlayer mPlayer, int percent) {
									// TODO Auto-generated method stub
									long totalDuration = mPlayer.getDuration();
									long currentDuration = mPlayer.getCurrentPosition();
									holder.jreviewarticletxtAudioDuration.setText(makeTimeString(currentDuration)
											+"/"+makeTimeString(totalDuration));
									// Updating progress bar
									int progress = (int) (getProgressPercentage(
											currentDuration, totalDuration));
									holder.jreviewarticleAudioProgressbar.setProgress(progress);
								}
							});

						} catch (IOException e) {
							Log.e("preparefailed", "prepare() failed");
						}
					}
				});

				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapterWithHolder;
	}

	private int getProgressPercentage(long currentDuration,
			long totalDuration) {
		// TODO Auto-generated method stub
		Double percentage = (double) 0;

		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);

		// calculating percentage
		percentage = (((double) currentSeconds) / totalSeconds) * 100;

		// return percentage
		return percentage.intValue();
	}

	private static String makeTimeString(long milliseconds) {
		String finalTimerString = "";
		String secondsString = "";

		// Convert total duration into time
		int hours = (int)( milliseconds / (1000*60*60));
		int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
		int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
		// Add hours if there
		if(hours > 0){
			finalTimerString = hours + ":";
		}

		// Prepending 0 to seconds if it is one digit
		if(seconds < 10){ 
			secondsString = "0" + seconds;
		}else{
			secondsString = "" + seconds;}

		finalTimerString = finalTimerString + minutes + ":" + secondsString;

		// return timer string
		return finalTimerString;
	}
}
