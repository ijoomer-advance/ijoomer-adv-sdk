package com.ijoomer.plugins;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ijoomer.src.R;

@SuppressLint({ "ParserError", "SetJavaScriptEnabled" })
public class PluginsVimeoWebViewActivity
extends FragmentActivity implements OnGestureListener {

		  WebView browser;
		  ViewPager pager;
		  Bundle savedInstance;
		  FragmentStatePagerAdapter adapter;
		  
		  boolean isFromBigScreen = false;
		  boolean isFromRestore = false;
		  private GestureDetector gDetector;
		  private static final int SWIPE_MIN_DISTANCE = 120;
		  private static final int SWIPE_THRESHOLD_VELOCITY = 200;
		  private static int index;
		  ProgressBar progressBar;
		  
		  String IN_URL;
		  int IN_POSITION;
		  String [] IN_LINKS ;
	      int IN_WIDTH ;
	      int IN_HEIGHT ; 
		  
	      
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
	}
	
	public void getIntentData(){
		IN_URL = getIntent().getStringExtra("IN_URL");
    	IN_WIDTH = this.getIntent().getExtras().getInt("IN_WIDTH");
    	IN_HEIGHT = this.getIntent().getExtras().getInt("IN_HEIGHT");
    	IN_LINKS = getIntent().getStringArrayExtra("IN_LINKS");
    	IN_HEIGHT = IN_HEIGHT / 2;
    	IN_POSITION = this.getIntent().getExtras().getInt("IN_POSITION");
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		progressBar.setVisibility(View.VISIBLE);
		if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			getNextVideo();
			
		} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			getPreviousVideo();
		}
		
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gDetector.onTouchEvent(event);
	}
	public void getNextVideo(){
		try{
		if((IN_LINKS.length > index )){
		index = index+1;
		loadVideo(index);
		}
		}catch (Exception e) {
			index = index-1;
			e.printStackTrace();
		}
	}
	public void getPreviousVideo(){
		try{
		if(index>0){
		index = index-1;
		loadVideo(index);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
}
	
	public void loadVideo(int index){
		browser.stopLoading();
		browser.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				progressBar.setVisibility(View.VISIBLE);
			}
        	
        });
		String IN_URL = IN_LINKS[index]; 
		String[] url = IN_URL.split("/");
		int length = url.length;
		String link = url[length-1];
		String videoLink = "<!DOCTYPE HTML> <html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:og=\"http://opengraphprotocol.org/schema/\" xmlns:fb=\"http://www.facebook.com/2008/fbml\"> <head></head> <body style=\"margin:0 0 0 0; padding:0 0 0 0;\"><iframe src=\"http://player.vimeo.com/video/"+link+"?color=\"#000000\" width="+IN_WIDTH+" height="+IN_HEIGHT+"  webkitAllowFullScreen mozallowfullscreen allowFullScreen autopaly=\"1\"></iframe></body> </html> ";
	    browser.loadDataWithBaseURL("http://player.vimeo.com",videoLink,"text/html", "utf-8", null);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		if(isFromRestore){
			getIntentData();
		}else{
		getIntentData();
		index = IN_POSITION;
		}
		try
		{
			browser.destroy();
		}catch (Exception e) {
			e.printStackTrace();
		}
		setContentView(R.layout.plugins_vimeo_fragment_browser);
		browser = (WebView) findViewById(R.id.my_browser);
	  	progressBar = (ProgressBar) findViewById(R.id.progressBar1);
	  	gDetector = new GestureDetector(getBaseContext(),this);
        browser.setBackgroundColor(Color.BLACK);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setSupportMultipleWindows(true);
        browser.setInitialScale(97);
    	browser.setScrollContainer(false);
    	browser.getLayoutParams().width = IN_WIDTH;
    	browser.getSettings().setPluginState(PluginState.ON);
    	loadVideo(index);
    	
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		try{
		if(browser!=null){
			browser.reload();
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		isFromBigScreen = true;
		outState.putInt("index",index);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		savedInstance = savedInstanceState;
		isFromBigScreen = true;
		isFromRestore = true;
		index = savedInstance.getInt("index");
	}
	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		try{
			browser.stopLoading();
			browser.destroy();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

	
