package com.ijoomer.components.jomsocial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.VideoTrimmerUtilities;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.CustomSliderListener;
import com.ijoomer.customviews.CustomSliderView;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This Class Contains All Method Related To JomVideoTrimmerActivity.
 * 
 * @author tasol
 * 
 */
@SuppressLint("NewApi")
public class JomVideoTrimmerActivity extends JomMasterActivity implements OnTouchListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener ,CustomSliderListener{

	private IjoomerButton btnTrimVideo;
	private IjoomerButton btnPlayVideo;
	private IjoomerTextView txtVideoTitleValue;
	private IjoomerTextView txtVideoDescriptionValue;
	private IjoomerTextView txtVideoLocationValue;
	
	private IjoomerTextView txtVideoDescription;
	private IjoomerTextView txtVideoStartTimeValue;
	private IjoomerTextView txtVideoEndTimeValue;
	private ImageView thumbBar;
	private ProgressBar pbrLoadVideoFrames;
	private ProgressBar pbrUploadVideo;
	private VideoView vvSlider;
	private CustomSliderView customSlider;

	public static ArrayList<Bitmap> bArrayForFrames;
	public static ArrayList<Bitmap> allTrackArray;
	private JomGalleryDataProvider providerAllVideos;
	private Context context;

	public static String IN_VIDEO_PATH_FOR_TRIMMING;
	public String IN_VIDEO_FILE;
	public String IN_VIDEO_TITLE;
	public String IN_VIDEO_DESCRIPTION;
	public String IN_VIDEO_CAPTION;
	public String IN_CAT_ID;
	public String IN_GROUP_ID;
	public String IN_PRIVACY;
	public String IN_LOCATION;
	
	private double extractMin;
	private double extractMax;
	private double extractMaxForTrimming;
	private double extractMinForTrimming;
	public double IN_LATITUDE;
	public double IN_LONGITUDE;
	private int endingTimeForPlayingVideo;
	private int startingTimeForPlayingVideo;
	private double calculatedWidth;
	private Timer timer;
	private int durationOfPlayingVideo ;
	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.jom_video_trim;
	}

	@Override
	public void initComponents() {

		btnTrimVideo = (IjoomerButton) findViewById(R.id.btnTrimVideo);
		btnPlayVideo = (IjoomerButton) findViewById(R.id.btnPlayVideo);
		txtVideoTitleValue = (IjoomerTextView) findViewById(R.id.txtVideoTitleValue);
		txtVideoDescription = (IjoomerTextView) findViewById(R.id.txtVideoDescription);
		txtVideoStartTimeValue = (IjoomerTextView) findViewById(R.id.txtVideoStartTimeValue);
		txtVideoEndTimeValue = (IjoomerTextView) findViewById(R.id.txtVideoEndTimeValue);
		txtVideoDescriptionValue = (IjoomerTextView) findViewById(R.id.txtVideoDescriptionValue);
		txtVideoLocationValue = (IjoomerTextView) findViewById(R.id.txtVideoLocationValue);
		thumbBar = (ImageView) findViewById(R.id.thumbBar);
		thumbBar.getLayoutParams().width = width;
		pbrLoadVideoFrames = (ProgressBar) findViewById(R.id.pbrLoadVideoFrames);
		pbrUploadVideo = (ProgressBar) findViewById(R.id.pbrUploadVideo);
		customSlider = (CustomSliderView) this.findViewById(R.id.customSlider);
		vvSlider = (VideoView) findViewById(R.id.vvSlider);
		vvSlider.setOnErrorListener(this);
		pbrLoadVideoFrames.setVisibility(View.VISIBLE);
		pbrUploadVideo.setVisibility(View.GONE);
		
		providerAllVideos = new JomGalleryDataProvider(this);

	}

	@Override
	public void prepareViews() {
		context = this;
		extractMinForTrimming = 0;
		getIntentData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		vvSlider.setVideoURI(Uri.parse(IN_VIDEO_PATH_FOR_TRIMMING));
		vvSlider.requestFocus();
		vvSlider.setOnPreparedListener(this);
		vvSlider.setMediaController(null);
	}
	private void timerMethod()
    {
    this.runOnUiThread(generate);
    }
	  private Runnable generate= new Runnable() {	

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			if (vvSlider.isPlaying()) {
				/*System.out.println(":::vvSlider.getCurrentPosition() :::::"+vvSlider.getCurrentPosition());
				System.out.println("extractMaxForTrimming :::::::"+(int) (VideoTrimmerUtilities.progressToTimer(durationOfPlayingVideo * 1000, extractMax)));
				if(vvSlider.getCurrentPosition()>((int) (VideoTrimmerUtilities.progressToTimer(durationOfPlayingVideo * 1000, extractMax)))){*/
				vvSlider.pause();
				btnPlayVideo.setBackgroundDrawable(getResources().getDrawable(R.drawable.jom_video_play_btn));
			//	}
				timer.cancel();
			}else{
				
			}
			
		}
		  
	  };
	@Override
	public void setActionListeners() {

		btnPlayVideo.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if (vvSlider.isPlaying()) {
					vvSlider.pause();
					btnPlayVideo.setBackgroundDrawable(getResources().getDrawable(R.drawable.jom_video_play_btn));
				} else {
					timer = new Timer();
					vvSlider.seekTo((int) (VideoTrimmerUtilities.progressToTimer(startingTimeForPlayingVideo * 1000, extractMax)));
					vvSlider.start();
					btnPlayVideo.setBackgroundDrawable(getResources().getDrawable(R.drawable.jom_video_pause_btn));
					if(extractMaxForTrimming<extractMinForTrimming){
						durationOfPlayingVideo =(int) (extractMinForTrimming - extractMaxForTrimming);
					}else{
					durationOfPlayingVideo = (int) (extractMaxForTrimming - extractMinForTrimming);	
					}
					if(durationOfPlayingVideo <= 0){
						durationOfPlayingVideo = 10;
					}
					if(durationOfPlayingVideo * 1000 < 0){
						durationOfPlayingVideo = Math.abs(durationOfPlayingVideo);
					}
					timer.scheduleAtFixedRate(new TimerTask() {
				            
				            @Override
				            public void run() {
				                timerMethod();
				            }
				        },durationOfPlayingVideo * 1000,durationOfPlayingVideo);
				}
			}
		});

		btnTrimVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setProgressBarVisibility(true);
				btnTrimVideo.setClickable(false);
				makeVideo();

			}
		});

	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (vvSlider.isPlaying()) {
			vvSlider.pause();
			btnPlayVideo.setBackgroundDrawable(getResources().getDrawable(R.drawable.jom_video_play_btn));

		}

		CustomSliderView customSliderView = (CustomSliderView) view;
		
		if (!customSliderView.getEvent() && (int) (VideoTrimmerUtilities.progressToTimer(customSliderView.getScaledValue() * 1000, extractMax)) > 0
				&& (int) (VideoTrimmerUtilities.progressToTimer(customSliderView.getScaledValue() * 1000, extractMax)) <= extractMax) {
			vvSlider.seekTo((int) (VideoTrimmerUtilities.progressToTimer(customSliderView.getScaledValue() * 1000, extractMax)));
			
			double ms = calculateDurationFromWidth();
			endingTimeForPlayingVideo = customSliderView.getScaledValue();
			try {
				extractMaxForTrimming = VideoTrimmerUtilities.progressToTimer(customSliderView.getScaledValue(), extractMax);
				double startTime = Double.valueOf(VideoTrimmerUtilities.milliSecondsToTimer((long) (VideoTrimmerUtilities.progressToTimer(customSliderView.getScaledValue() * 1000,
						extractMax)))) < ms ? ms
						- Double.valueOf(VideoTrimmerUtilities.milliSecondsToTimer((long) (VideoTrimmerUtilities.progressToTimer(customSliderView.getScaledValue() * 1000,
								extractMax)))) : Double.valueOf(VideoTrimmerUtilities.milliSecondsToTimer((long) (VideoTrimmerUtilities.progressToTimer(
						customSliderView.getScaledValue() * 1000, extractMax))))
						- ms;
				double endTime = Double.valueOf(VideoTrimmerUtilities.milliSecondsToTimer((long) (VideoTrimmerUtilities.progressToTimer(customSliderView.getScaledValue() * 1000,
						extractMax))));
				double startTimeConversion;
				double endTimeConversion;
				startTimeConversion = startTime;
				endTimeConversion = endTime;
				if(startTime > endTime){
					endTime = startTimeConversion;
					startTime = endTimeConversion;
				}
				 DecimalFormat f = new DecimalFormat("##0.00");
			     txtVideoStartTimeValue.setText(String.valueOf(f.format(startTime)));
				txtVideoEndTimeValue.setText(String.valueOf(endTime));
				startingTimeForPlayingVideo = VideoTrimmerUtilities.getProgressPercentage((long) startTime, (long) endTime);
				extractMinForTrimming = VideoTrimmerUtilities.progressToTimer((int) startingTimeForPlayingVideo, extractMax);
				thumbBar.setPadding(width, 0, 0, 0);
				
			} catch (Throwable e) {
			}
			customSliderView.setCustomSliderListener(new CustomSliderListener() {
				
				@Override
				public int onThumbSizeChanged(int calculatedSizeOfThumb) {
					if(calculatedWidth<calculatedSizeOfThumb){
					}else{
						thumbBar.getLayoutParams().width = calculatedSizeOfThumb;
						customSlider.setResizedDistance((int) calculatedSizeOfThumb);
					}
					return 0;
				}
			});
		}
		return true;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.setLooping(true);
		extractMax = vvSlider.getDuration();
		if (!customSlider.isShown()) {
			customSlider.setVisibility(View.VISIBLE);
			try {
				allTrackArray = new ArrayList<Bitmap>();
				allTrackArray = getAllFrames();
				customSlider.setTracks(allTrackArray);
				customSlider.setResourceIds(R.drawable.jom_custom_slider_thumb, R.drawable.jom_custom_slider_bar);
				customSlider.setDelegateOnTouchListener(this);
				try {
					long fileLength = new File(IN_VIDEO_PATH_FOR_TRIMMING).length();
					 calculatedWidth = ((IjoomerGlobalConfiguration.getVideoUploadSize() * (width)) / (fileLength / (1024 * 1024)));
					thumbBar.getLayoutParams().width = (int) calculatedWidth;
					customSlider.setDistance((int) calculatedWidth);
					double minTime = calculateDurationFromWidth();
					double maxTime = Double.valueOf(VideoTrimmerUtilities.milliSecondsToTimer((long) extractMax));
					System.out.println("::: MIN TIME ::::::"+minTime +":::maxTime ::::"+maxTime);
					 DecimalFormat f = new DecimalFormat("##0.00");
					txtVideoEndTimeValue.setText(String.valueOf(f.format(minTime)));
					txtVideoStartTimeValue.setText(String.valueOf(extractMinForTrimming));
					endingTimeForPlayingVideo = VideoTrimmerUtilities.getProgressPercentage((long) minTime, (long) maxTime);
					extractMaxForTrimming = VideoTrimmerUtilities.progressToTimer((int) endingTimeForPlayingVideo, extractMax);
					customSlider.setRange(1, 100);
					customSlider.setScaledValue(1);
				} catch (Throwable e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		ting(context.getString(R.string.error_loading_video));
		return false;
	}
	
	
	/**
	 * Class methods
	 */
	
	/**
	 * This method used to get intent data.
	 */
	public void getIntentData() {
		IN_VIDEO_PATH_FOR_TRIMMING = getIntent().getExtras().getString("IN_VIDEO_PATH_FOR_TRIMMING");
		IN_GROUP_ID = getIntent().getStringExtra("IN_GROUP_ID") == null ? "0" : getIntent().getStringExtra("IN_GROUP_ID");
		IN_VIDEO_FILE = getIntent().getStringExtra("IN_VIDEO_FILE") == null ? "0" : getIntent().getStringExtra("IN_VIDEO_FILE");
		IN_VIDEO_TITLE = getIntent().getStringExtra("IN_VIDEO_TITLE") == null ? "0" : getIntent().getStringExtra("IN_VIDEO_TITLE");
		IN_VIDEO_DESCRIPTION = getIntent().getStringExtra("IN_VIDEO_DESCRIPTION") == null ? "0" : getIntent().getStringExtra("IN_VIDEO_DESCRIPTION");
		IN_VIDEO_CAPTION = getIntent().getStringExtra("IN_VIDEO_CAPTION") == null ? "0" : getIntent().getStringExtra("IN_VIDEO_CAPTION");
		IN_LATITUDE = Double.valueOf(getIntent().getStringExtra("IN_LATITUDE") == null ? "0" : getIntent().getStringExtra("IN_LATITUDE"));
		IN_LONGITUDE = Double.valueOf(getIntent().getStringExtra("IN_LONGITUDE") == null ? "0" : getIntent().getStringExtra("IN_LONGITUDE"));
		IN_CAT_ID = getIntent().getStringExtra("IN_CAT_ID") == null ? "0" : getIntent().getStringExtra("IN_CAT_ID");
		IN_PRIVACY = getIntent().getStringExtra("IN_PRIVACY") == null ? "0" : getIntent().getStringExtra("IN_PRIVACY");
		IN_LOCATION = getIntent().getStringExtra("IN_LOCATION") == null ? "0" : getIntent().getStringExtra("IN_LOCATION");
		if(IN_VIDEO_DESCRIPTION!=null){
			txtVideoDescription.setVisibility(View.VISIBLE);
		}
		txtVideoDescriptionValue.setText(": " +IN_VIDEO_DESCRIPTION);
		txtVideoTitleValue.setText(": " +IN_VIDEO_TITLE);
		txtVideoLocationValue.setText(": " +IN_LOCATION);
	}

	
	/**
	 * This method used to make custom video.
	 */
	@SuppressWarnings("resource")
	public void makeVideo() {
		Movie movie;
		try {
			movie = MovieCreator.build(new FileInputStream(IN_VIDEO_PATH_FOR_TRIMMING).getChannel());
			List<Track> tracks = movie.getTracks();
			movie.setTracks(new LinkedList<Track>());
			double startTime = extractMinForTrimming;
			double endTime = extractMaxForTrimming;
			
			if(extractMinForTrimming > extractMaxForTrimming){
				startTime = extractMaxForTrimming;
				endTime = extractMinForTrimming;
			}
			
			boolean timeCorrected = false;

			for (Track track : tracks) {
				if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
					if (timeCorrected) {
						throw new RuntimeException(context.getString(R.string.error_same_track));
					}
					startTime = correctTimeToSyncSample(track, startTime, false);
					endTime = correctTimeToSyncSample(track, endTime, true);
					timeCorrected = true;
				}
			}

			for (Track track : tracks) {
				long currentSample = 0;
				double currentTime = 0;
				long startSample = -1;
				long endSample = -1;

				for (int i = 0; i < track.getDecodingTimeEntries().size(); i++) {
					TimeToSampleBox.Entry entry = track.getDecodingTimeEntries().get(i);
					for (int j = 0; j < entry.getCount(); j++) {
						if (currentTime <= startTime) {
							startSample = currentSample;
						}
						if (currentTime <= endTime) {
							endSample = currentSample;
						} else {
							break;
						}
						currentTime += (double) entry.getDelta() / (double) track.getTrackMetaData().getTimescale();
						currentSample++;
					}

				}

				movie.addTrack(new CroppedTrack(track, startSample, endSample));

			}

			IsoFile out = new DefaultMp4Builder().build(movie);
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(String.format(context.getString(R.string.path_of_trimmed_video), startTime, endTime));
				FileChannel fc = fos.getChannel();
				out.getBox(fc);
				fos.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				IN_VIDEO_FILE = context.getString(R.string.path_of_sd_card) + context.getString(R.string.path_of_trimmed_video);
				double fileLength = new File(context.getString(R.string.path_of_trimmed_video)).length();
				double bytesCalculation = fileLength / (1024 * 1024);
				if (bytesCalculation > IjoomerGlobalConfiguration.getVideoUploadSize()) {
					extractMinForTrimming = extractMinForTrimming + 50;
					makeVideo();
				} else {
					btnTrimVideo.setClickable(true);
					startVideoUpload(IN_GROUP_ID, IN_VIDEO_FILE, IN_VIDEO_TITLE, IN_VIDEO_DESCRIPTION, IN_VIDEO_CAPTION, IN_LATITUDE, IN_LONGITUDE, IN_CAT_ID, IN_PRIVACY);
				}

			} catch (Throwable e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	
	/**
	 * This method used to get video duration.
	 * @param track represented track
	 * @return represented {@link Long}
	 */
	protected static long getDuration(Track track) {
		long duration = 0;
		for (TimeToSampleBox.Entry entry : track.getDecodingTimeEntries()) {
			duration += entry.getCount() * entry.getDelta();
		}
		return duration;
	}

	/**
	 * This method used to correct time to sync sample.
	 * @param track represented track
	 * @param cutHere represented cut
	 * @param next represented is next
	 * @return represented {@link Double}
	 */
	private static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
		double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
		long currentSample = 0;
		double currentTime = 0;
		for (int i = 0; i < track.getDecodingTimeEntries().size(); i++) {
			TimeToSampleBox.Entry entry = track.getDecodingTimeEntries().get(i);
			for (int j = 0; j < entry.getCount(); j++) {
				if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
					timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
				}
				currentTime += (double) entry.getDelta() / (double) track.getTrackMetaData().getTimescale();
				currentSample++;
			}
		}
		double previous = 0;
		for (double timeOfSyncSample : timeOfSyncSamples) {
			if (timeOfSyncSample > cutHere) {
				if (next) {
					return timeOfSyncSample;
				} else {
					return previous;
				}
			}
			previous = timeOfSyncSample;
		}
		return timeOfSyncSamples[timeOfSyncSamples.length - 1];
	}

	
	/**
	 * This method used to get all frames from video.
	 * @return represented {@link Bitmap} list
	 */
	@SuppressLint({ "NewApi", "NewApi" })
	public ArrayList<Bitmap> getAllFrames() {
		bArrayForFrames = new ArrayList<Bitmap>();
		bArrayForFrames.clear();
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(IN_VIDEO_PATH_FOR_TRIMMING);
			for (int i = (int) extractMin; i < (int) extractMax; i++) {
				if (bArrayForFrames != null && bArrayForFrames.size() > 20 && bArrayForFrames.size() != i) {
					break;
				} else {
					if (bArrayForFrames.size() == 19) {
						bArrayForFrames.add(retriever.getFrameAtTime((long) extractMax * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC));

					} else {
						if (i % 5000 == 0) {
							bArrayForFrames.add(retriever.getFrameAtTime((long) extractMin * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC));

						}
					}
				}
				extractMin = i + 4999;
			}

		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		} catch (RuntimeException ex) {
			ex.printStackTrace();
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException ex) {
			}
		}
		pbrLoadVideoFrames.setVisibility(View.GONE);
		btnPlayVideo.setVisibility(View.VISIBLE);
		return bArrayForFrames;
	}

	
	/**
	 * This method used to calculate duration from width.
	 * @return
	 */
	public double calculateDurationFromWidth() {
		double ms = 0;
		ms = (thumbBar.getLayoutParams().width / extractMax);
		return ms * 10000;
	}

	
	
	/**
	 * This method used to start upload video
	 * @param groupID represented group id
	 * @param videoFilePath represented video file path
	 * @param videoTitle represented video title
	 * @param description represented video description
	 * @param videoCaption represented video caption
	 * @param lat represented latitude
	 * @param lng represented longitude
	 * @param categoryID represented category id
	 * @param privacy represented privacy
	 */
	private void startVideoUpload(final String groupID, final String videoFilePath, final String videoTitle, final String description, final String videoCaption, final double lat,
			final double lng, final String categoryID, final String privacy) {
		this.finish();
		IjoomerUtilities.addToNotificationBar(getString(R.string.video_upload_starts), getString(R.string.uplod_video), getString(R.string.video_upload_starts));

		providerAllVideos.uploadVideo(groupID, videoFilePath, videoTitle, description, videoCaption, lat, lng, categoryID, privacy, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

				if (responseCode == 200) {
					IjoomerUtilities.addToNotificationBar(getString(R.string.video_upload_successfully), getString(R.string.uplod_video),
							getString(R.string.video_upload_successfully));
				} else {
					if (errorMessage != null && errorMessage.length() > 0) {
						IjoomerUtilities.addToNotificationBar(getString(R.string.video_upload_failure), getString(R.string.uplod_video), errorMessage);
					} else {
						IjoomerUtilities.addToNotificationBar(getString(R.string.video_upload_failure), getString(R.string.uplod_video),
								getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())));
					}
				}
			}

		});

	}

	@Override
	public int onThumbSizeChanged(int calculatedSizeOfThumb) {
		System.out.println(":::calculatedSizeOfThumb ::::::"+calculatedSizeOfThumb);
		return 0;
	}

	
	
	
}
