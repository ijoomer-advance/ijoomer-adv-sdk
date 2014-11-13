package com.ijoomer.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ijoomer.custom.interfaces.CustomSliderListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This Class Contains All Method Related To CustomSliderView.
 * 
 * @author tasol
 * 
 */
public class CustomSliderView extends FrameLayout implements OnTouchListener {
	// The touch position relative to the slider view (ie: left=0, right=width of the slider view)
	private float mTouchXPosition;
	private float mTouchXPositionAfterResized;
	private float mTouchXPositionForThumb;
	// Images used for thumb and the bar
    protected ImageView mThumbImageView, mSliderBarImageView;
    protected Bitmap mThumbBitmap;
    protected Bitmap mThumbBitmapResized;
    protected Bitmap mSliderBarBitmap;

    // These two variables are useful if you want to programatically reskin the slider
    protected int mThumbResourceId;
    protected int mSliderBarResourceId;
     GestureDetector gestureDetector1;
	View.OnTouchListener gestureListener;
    // Default ranges
    protected int mMinValue=0;
    protected int mMaxValue=100;
    protected int xDistance=0;
    protected int xResizedDistance=0;
//    protected int calculatedSizeOfThumb;
    protected int calculatedSizeOfThumbResized;
    protected boolean isTouchEvent = true;
    protected boolean isMultiTouch = false;
    // Used internally during touches event
    protected float mTargetValue=0;    
    protected int mSliderLeftPosition, mSliderRightPosition;
    protected ArrayList<Bitmap> allTracks;
    // Holds the object that is listening to this slider.
    protected OnTouchListener mDelegateOnTouchListener;
    protected OnLongClickListener mDelegateOnLongListener;
    protected int calculatedSizeOfThumb;
    CustomSliderListener customSliderListener;
	/**
	 * Default constructors.  
	 * Just tell Android that we're doing custom drawing and that we want to listen to touch events.
	 */
    public CustomSliderListener getCustomSliderListener() {
		return customSliderListener;
	}

	public void setCustomSliderListener(CustomSliderListener customSliderListener) {
		this.customSliderListener = customSliderListener;
	}
	public CustomSliderView(Context context) 
	{
		super(context);
		setWillNotDraw(false);
		setOnTouchListener(this);	
		 
	}

	public CustomSliderView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		calculatedSizeOfThumbResized = 0;
		setWillNotDraw(false);
		setOnTouchListener(this);	
	
	}

	public CustomSliderView(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
		setWillNotDraw(false);
		this.setOnTouchListener(this);	
		
	}

	/*
	 * This should be called by the object that wants to listen to the touch events
	 */
	public void setDelegateOnTouchListener(OnTouchListener onTouchListener)
	{
		eventDataMap = new HashMap<Integer, EventData>();
		mDelegateOnTouchListener=onTouchListener;
	}
	public void setDelegateOnLongListener(OnLongClickListener onLongListener)
	{
		mDelegateOnLongListener=onLongListener;
	}
	
	public boolean getEvent(){
		return isTouchEvent;
	}
	
	public void setDistance(int xDistance){
		this.xDistance = xDistance;
	}
	public void setResizedDistance(int xResizedDistance){
		this.xResizedDistance = xResizedDistance;
	}
	public void setResourceIds(int thumbResourceId, int sliderBarResourceId)
	{
		mThumbResourceId=thumbResourceId;
		mSliderBarResourceId=sliderBarResourceId;
		mThumbImageView=null;
		
		mSliderBarImageView=null;
	}
	String ItemId;
	
	private float MIN_DISTANCE = 20;
	private float downX;
	
	
    /* 
     * This sets the range of the slider values.  Eq: 0 to 100 or 20 to 70.
     */
    public void setRange(int min, int max)
    {
    	mMinValue=min;
    	mMaxValue=max;
    }

    /* 
     * This sets the value, between mMinValue and mMaxValue
     */
    public void setScaledValue(int value)
    {        
    	mTargetValue=value;//((value-mMinValue)/range)*fillWidth;
    	invalidate();
    }
    
    /**
     * @return The actual value of the thumb position, scaled to the min and max value
     */
    public int getScaledValue()
    {
    	return (int)mMinValue+(int)((mMaxValue-mMinValue)*getPercentValue());    	
    }
    
    /**
     * @return The percent value of the current thumb position.
     */
    public float getPercentValue()
    {
    	float fillWidth=mSliderBarImageView.getWidth();
    	float relativeTouchX=mTouchXPosition-mSliderBarImageView.getLeft();
    	float percentage=relativeTouchX/fillWidth;
    	return percentage;
    }
    
    /** 
     * 
     * @param percentValue	between 0 to 1.0f
     */
    public void setPercentValue(float percentValue)
    {
    	float position=mSliderLeftPosition+percentValue*(mSliderRightPosition-mSliderLeftPosition-mThumbBitmap.getWidth());
    	mTouchXPosition=position;
    	invalidate();
    }
    
    public void setTracks(ArrayList<Bitmap> arrayOfTracks){
    	allTracks = arrayOfTracks;
    }
    public void calculateLeftDistance(int distance){
    	if(xResizedDistance > 0 && xResizedDistance < xDistance){
    		if(calculatedSizeOfThumb== xDistance){
    			
    		}else{
    			distance = xResizedDistance;	
    		}
    		
    	}else{
    		distance = xDistance;
    	}
		if (mTouchXPosition  < mSliderLeftPosition + distance){
        	if(!isTouchEvent){
        		mTouchXPosition=distance ;
        	}else{
        		mTouchXPositionForThumb = mSliderLeftPosition+distance - mTouchXPosition;
            	mTouchXPosition=distance - mTouchXPositionForThumb;
            	if(distance - mTouchXPosition > mSliderLeftPosition){
            		mTouchXPosition = distance;
            	}
            }
        	
        }
	}
	@Override 
	protected void onDraw (Canvas canvas)
	{
		// Load the resources if not already loaded
		if (mThumbImageView==null)
		{
			mThumbImageView=(ImageView)this.getChildAt(1);
			this.removeView(mThumbImageView);
			
			if (mThumbResourceId>0)
			{
				mThumbBitmap=BitmapFactory.decodeResource(getContext().getResources(), mThumbResourceId);
				mThumbImageView.setImageBitmap(mThumbBitmap);
			}
			
			// USe the drawing cache so that we don't have to scale manually.
			mThumbImageView.setDrawingCacheEnabled(true);
			mThumbBitmap = mThumbImageView.getDrawingCache(true); 
			mThumbBitmapResized = mThumbImageView.getDrawingCache(true);
			
		}

		if (mSliderBarImageView==null)
		{
			try{
			mSliderBarImageView=(ImageView)this.getChildAt(0);
			this.removeView(mSliderBarImageView);

			// If user has specified a different skin, load it
			if (mSliderBarResourceId>0)
			{
				mSliderBarBitmap=BitmapFactory.decodeResource(getContext().getResources(), mSliderBarResourceId);
				mSliderBarImageView.setImageBitmap(mSliderBarBitmap);
			}

			// USe the drawing cache so that we don't have to scale manually.
			mSliderBarImageView.setDrawingCacheEnabled(true);
	        mSliderBarBitmap = mSliderBarImageView.getDrawingCache(true);
	        //mSliderBarImageView.setDrawingCacheEnabled(false);
	        
	        mSliderLeftPosition=mSliderBarImageView.getLeft();
	        mSliderRightPosition=mSliderBarImageView.getLeft()+mSliderBarBitmap.getWidth();	    
			}catch (Exception e) {
				e.printStackTrace();
			}
		}


		// Adjust thumb position (this handles the case where setScaledValue() was called)
		if (mTargetValue>0)
        {
	        float fillWidth=mSliderBarImageView.getMeasuredWidth();
	    	float range=(mMaxValue-mMinValue);
	    	mTouchXPosition=((mTargetValue-mMinValue)/range)*fillWidth;
	    	mTargetValue=0;
        }
        
        // Don't allow going out of bounds
		if(isMultiTouch){
			calculateLeftDistance(xResizedDistance);
		}else{
			calculateLeftDistance(xDistance);
		}
        
         if (mTouchXPosition>mSliderRightPosition)
        	mTouchXPosition=mSliderRightPosition;
       
        if (mSliderBarBitmap!=null)
        
        if (mThumbBitmap!=null)
        {
        	writeOnDrawable(canvas);
        	if(isMultiTouch){
        	int i = 0;
        	
        	float startingPointOfThumb = 0;
    		float endingPointOfThumb = 0;
        	for(EventData event : eventDataMap.values())
        	{
        		if(i==0){
        			startingPointOfThumb = event.x;
    	    	}
    	    	if(i==1){
    	    		endingPointOfThumb = event.x;
    	    	}
    	    	i++;
        	}
        	if(endingPointOfThumb >0){
        	if(endingPointOfThumb>startingPointOfThumb){
        		calculatedSizeOfThumb = (int) (endingPointOfThumb - startingPointOfThumb);	
        		if(calculatedSizeOfThumbResized>0){
        			mThumbBitmap = mThumbBitmapResized;
        		}else{
        			calculatedSizeOfThumbResized = calculatedSizeOfThumb;
        		}
        	}else{
        		calculatedSizeOfThumb = (int) (startingPointOfThumb - endingPointOfThumb);
        		if(calculatedSizeOfThumbResized>0){
        			mThumbBitmap = mThumbBitmapResized;
        		}else{
        			calculatedSizeOfThumbResized = calculatedSizeOfThumb;
        		}
        	}
        	}
        	if(calculatedSizeOfThumb>0 && calculatedSizeOfThumb < xDistance){
        	Bitmap n = Bitmap.createScaledBitmap(mThumbBitmap,calculatedSizeOfThumb,mThumbBitmap.getHeight() ,true);
        	mThumbBitmap = n;
        	
        	if(customSliderListener!=null){
        	customSliderListener.onThumbSizeChanged(calculatedSizeOfThumb);
        	if(mTouchXPosition > mSliderRightPosition - mThumbBitmap.getWidth()){
        		canvas.drawBitmap(n , (mTouchXPosition - mThumbBitmap.getWidth()),0, null);
        	}else{
        		canvas.drawBitmap(n , (mTouchXPosition),0, null);	
        	}
        	}
        	}else{
        		calculatedSizeOfThumb = xDistance;
        		canvas.drawBitmap(mThumbBitmap , mTouchXPosition-mThumbBitmap.getWidth(),0, null);
        	}
        	}else{
        		if(mTouchXPosition-mThumbBitmap.getWidth() == 0){
        		if(!isTouchEvent){
        		canvas.drawBitmap(mThumbBitmap , mTouchXPosition-mThumbBitmap.getWidth(),0, null);
        		}else{
        			if(mTouchXPositionAfterResized > mSliderRightPosition - xDistance){
        				canvas.drawBitmap(mThumbBitmap , mTouchXPositionAfterResized/2,0, null);
        			}else{
        				canvas.drawBitmap(mThumbBitmap , mTouchXPositionAfterResized,0, null);	
        			}
        		}
        		}else{
        			canvas.drawBitmap(mThumbBitmap , mTouchXPosition-mThumbBitmap.getWidth(),0, null);
        		}
        	}
        		
        }
        	
	}
	
	@SuppressWarnings("deprecation")
	public BitmapDrawable writeOnDrawable(Canvas canvas){
		Bitmap  newBitmap = null;
		try {
			  newBitmap = Bitmap.createBitmap(mThumbBitmap.getWidth(),mThumbBitmap.getHeight(), Bitmap.Config.ARGB_8888);


			Paint   p = new Paint();
			int x = 0;
			int y = 5;
			for(int i=0;i<allTracks.size();i++){
				Bitmap n = Bitmap.createScaledBitmap(allTracks.get(i),80,60,true);
				canvas.drawBitmap(n,x,y, p);
				x = x+40;
				}
  
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BitmapDrawable(newBitmap);
	}

	 		private float pressureAmplification = (float) DefaultValues.PressureAmplificaton;
	 	    private Map<Integer, EventData> eventDataMap; 
	 		
		    private class EventData{
		    	public float x;
		    }
	    
		    public class DefaultValues {
		    	public final static float TouchCircleRadius = (float) 5.0;
		    	public final static float PressureRingOffset = (float) 1.0;
		        public final static float PressureAmplificaton = (float) 3.0;
		        public final static int PauseUIThread = 1000;
		    }

			@Override
			public boolean onTouch(View view, MotionEvent event) {
		    	int action = event.getActionMasked();
		  		int pointerIndex = event.getActionIndex();
		    	int pointerId = event.getPointerId(pointerIndex);
		    	EventData eventData = null ;
		    	switch (action) {
		    	case MotionEvent.ACTION_DOWN:
		    	downX = event.getX();
	    	 	isTouchEvent = true;
	    	 	mTouchXPosition=event.getX();
	    	 	mTouchXPositionAfterResized = event.getX();
	    	 	eventData = new EventData();
	    		eventData.x = event.getX(pointerIndex);
	    		eventDataMap.put(new Integer(pointerId), eventData);
	    		
	    	 	return true;
		    	case MotionEvent.ACTION_POINTER_DOWN:
		    		downX = event.getX();
		    		mTouchXPosition=event.getX();
		    		mTouchXPositionAfterResized = event.getX();
		    	 	isTouchEvent = true;
		    	 	isMultiTouch = true;
		    	 	eventData = new EventData();
		    		eventData.x = event.getX(pointerIndex);
		    		eventDataMap.put(new Integer(pointerId), eventData);
		    		return true;
		    	case MotionEvent.ACTION_MOVE:
		    		isTouchEvent = false;
		    		for(int i = 0; i < event.getPointerCount(); i++)
		    		{
		    			int curPointerId = event.getPointerId(i);
			    		if(eventDataMap.containsKey(new Integer(curPointerId)))
			    		{
			        		EventData moveEventData = eventDataMap.get(new Integer(curPointerId));
			        		moveEventData.x = event.getX(i);
			        		try{
				        		float calculateDistance = downX < event.getX() ?  event.getX() - downX : downX - event.getX();
				        		if((int)(calculateDistance) > MIN_DISTANCE ){
				        	    mTouchXPosition=event.getX();
				        	    mTouchXPositionAfterResized = event.getX();
					        	isTouchEvent = false;
					        	invalidate();
					        	if (mDelegateOnTouchListener!=null){
					        		mDelegateOnTouchListener.onTouch(view, event);
					        	}
				        		}else{
				        		}
				        	}catch (Throwable e) {
								e.printStackTrace();
							}
						   }
						}
		    		eventData = new EventData();
		    		eventData.x = event.getX(pointerIndex);
		    	
		    		eventDataMap.put(new Integer(pointerId), eventData);
		    		 mTouchXPosition=event.getX();
		    		return false;
		    	case MotionEvent.ACTION_UP:
			    	 	isTouchEvent = true;
			    	 	isMultiTouch = false;
			    		eventDataMap.remove(new Integer(pointerId));
			    		mTouchXPosition=event.getX();
		        		if (mDelegateOnTouchListener!=null)
		        		mDelegateOnTouchListener.onTouch(view, event);
		        return true;
		    	case MotionEvent.ACTION_POINTER_UP:
		    		eventDataMap = new HashMap<Integer, EventData>();
		    		isMultiTouch = false;
		    		isTouchEvent = true;
		    		eventDataMap.remove(new Integer(pointerId));
		    		return true;
		    	case MotionEvent.ACTION_OUTSIDE:
		    		break;
		    	}
		    	
		    	return false;
		    }
		
}