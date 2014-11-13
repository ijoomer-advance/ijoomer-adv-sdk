package com.ijoomer.customviews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.custom.interfaces.PhotoTagListener;
import com.ijoomer.src.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To PhotoTagView.
 * 
 * @author tasol
 * 
 */
@SuppressLint("DefaultLocale")
public class PhotoTagView extends ImageView {

	private ArrayList<HashMap<String, String>> tagedUserList;
	private GestureDetector gestureDetector;
	PhotoTagListener photoTagListener;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	Paint textPaint;
	Paint textBackGroundPaint;
	Bitmap tagLabelBitmap;
	Bitmap cancelTag;
	AQuery aQuery;

	private static final int MAX_FACES = 10;
	private RectF[] facesRect;
	private RectF cancelRect;
	private RectF[] tagedUserRect;
	private RectF[] tagedUserLabelRect;

	private int[] mPX = null;
	private int[] mPY = null;

	RectF tagRect;
	String tagImageUrl;
	int tagLabelResource = 0;
	int originalImageWidth;
	int originalImageHeight;
	float[] pts;
	float x, y;
	float width;
	float height;
	boolean isCancel = true;

	int ih;
	int iw;
	int iH;
	int iW;

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		originalImageWidth = bm.getWidth();
		originalImageHeight = bm.getHeight();
		ih = getMeasuredHeight();// height of imageView
		iw = getMeasuredWidth();// width of imageView
		iH = getDrawable().getIntrinsicHeight();// original height of underlying
		iW = getDrawable().getIntrinsicWidth();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawCanvas(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isCancel) {

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (cancelRect.contains(event.getX(), event.getY())) {
					isCancel = true;
					if (photoTagListener != null) {
						photoTagListener.onCancel();
					}
					invalidate();
					return true;
				}
			}
			x = event.getX();
			y = event.getY();
			invalidate();
			return true;
		} else {
			gestureDetector.onTouchEvent(event);
		}
		return true;
	}

	public PhotoTagView(Context c) {
		super(c);
		init();
	}

	public PhotoTagView(Context c, AttributeSet attrs) {
		super(c, attrs);
		setDrawingCacheEnabled(true);
		init();
	}

	private void init() {
		ViewTreeObserver vto = getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {

				if (ih <= 0 && iw <= 0) {
					try {
						ih = getMeasuredHeight();// height of imageView
						iw = getMeasuredWidth();// width of imageView
						iH = getDrawable().getIntrinsicHeight();// original
																// height
																// of underlying
																// image
						iW = getDrawable().getIntrinsicWidth();

						if (ih / iH <= iw / iW)
							iw = iW * ih / iH;// rescaled width of image within
						else
							ih = iH * iw / iW;

						if (iw > 0 && ih > 0) {
							setFace(iw, ih);
						}
					} catch (Exception e) {
					}
				}
				return true;
			}

		});
		cancelRect = new RectF();
		cancelTag = BitmapFactory.decodeResource(getResources(), R.drawable.com_facebook_close);
		gestureDetector = new GestureDetector(getContext(), new SingleTapConfirm());
		aQuery = new AQuery(this);

		width = convertSizeToDeviceDependent(50);
		height = convertSizeToDeviceDependent(50);

		textBackGroundPaint = new Paint();
		textBackGroundPaint.setColor(Color.BLACK);
		textBackGroundPaint.setStyle(Paint.Style.FILL);

		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(convertSizeToDeviceDependent(12));

		tagRect = new RectF();
	}

	/**
	 * This method used to get taged user list.
	 * 
	 * @return represented {@link HashMap} list
	 */
	public ArrayList<HashMap<String, String>> getTagedUserList() {
		return tagedUserList;
	}

	/**
	 * This method used to set taged user list.
	 * 
	 * @param tagedUserList
	 *            represented user list
	 */
	public void setTagedUserList(ArrayList<HashMap<String, String>> tagedUserList) {
		this.tagedUserList = tagedUserList;
		if (tagedUserList != null && tagedUserList.size() > 0) {
			tagedUserRect = new RectF[tagedUserList.size()];
			tagedUserLabelRect = new RectF[tagedUserList.size()];
			if (photoTagListener != null) {
				photoTagListener.showTagOptions(isCancel);
			}
		} else {
			if (tagedUserRect != null && tagedUserRect.length > 0) {
				if (photoTagListener != null) {
					photoTagListener.showTagOptions(isCancel);
				}
				tagedUserLabelRect = null;
				tagedUserRect = null;
			}
		}

		drawDetectedFace();
		photoTagListener.showTagOptions(isCancel);
	}

	/**
	 * This method used to set tag label resource id.
	 * 
	 * @param tagLabelResource
	 *            represented resource id
	 */
	public void setTagLabelResource(int tagLabelResource) {
		this.tagLabelResource = tagLabelResource;
		tagLabelBitmap = BitmapFactory.decodeResource(getResources(), tagLabelResource);
	}

	/**
	 * This method used to get tag listener.
	 * 
	 * @return represented {@link PhotoTagListener}
	 */
	public PhotoTagListener getPhotoTagListener() {
		return photoTagListener;
	}

	/**
	 * This method used to set tag listener.
	 * 
	 * @param photoTagListener
	 *            represented listener
	 */
	public void setPhotoTagListener(PhotoTagListener photoTagListener) {
		this.photoTagListener = photoTagListener;
	}

	/**
	 * This method used to set add tag.
	 */
	public void setAddTag() {
		isCancel = false;
		x = getWidth() / 2.0f;
		y = getHeight() / 2.0f;
		invalidate();
	}

	/**
	 * This method used to convert size to device dependent.
	 * 
	 * @param value
	 *            represented size
	 * @return represented {@link Integer}
	 */
	public int convertSizeToDeviceDependent(int value) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return ((dm.densityDpi * value) / 160);
	}

	/**
	 * This method used to add new tag.
	 */
	public void addNewTag() {
		if (photoTagListener != null) {
			if (getTagedUserList() != null) {
				boolean isValidTagPosition = true;
				int size = getTagedUserList().size();
				for (int i = 0; i < size; i++) {

					if (tagedUserRect[i].intersect(getDeviceRectFromData(getTaggedPositionData(tagRect)))) {
						isValidTagPosition = false;
						break;
					}
				}
				if (isValidTagPosition) {
					photoTagListener.onAddNewTag(getTaggedPositionData(tagRect));
				} else {
					photoTagListener.onTagAreaConflict();
				}

			} else {
				photoTagListener.onAddNewTag(getTaggedPositionData(tagRect));
			}
		}
	}

	/**
	 * This method used to set tag image url.
	 * 
	 * @param imageUrl
	 *            represented url
	 */
	public void setTagImageUrl(String imageUrl) {
		this.tagImageUrl = imageUrl;
		aQuery.ajax(imageUrl, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
			@Override
			public void callback(String url, Bitmap object, AjaxStatus status) {
				super.callback(url, object, status);
				originalImageWidth = object.getWidth();
				originalImageHeight = object.getHeight();
				setImageBitmap(object);
			}
		});

	}

	/**
	 * This method used to draw canvas
	 * 
	 * @param canvas
	 *            represented canvas
	 */
	private void drawCanvas(Canvas canvas) {

		try {

			if (getTagedUserList() != null) {
				int size = getTagedUserList().size();
				for (int i = 0; i < size; i++) {
					tagedUserRect[i] = getDeviceRectFromData(getTagedUserList().get(i).get("position"));

					Rect textBound = new Rect();
					textPaint.getTextBounds(getTagedUserList().get(i).get("user_name"), 0, getTagedUserList().get(i).get("user_name").length(), textBound);

					tagedUserLabelRect[i] = new RectF(new RectF(tagedUserRect[i].left, tagedUserRect[i].bottom - convertSizeToDeviceDependent(15), tagedUserRect[i].left
							+ textBound.width() + convertSizeToDeviceDependent(9), (tagedUserRect[i].bottom - convertSizeToDeviceDependent(20)) + textBound.height()
							+ convertSizeToDeviceDependent(15)));

					Rect src = new Rect();
					src.set(0, 0, tagLabelBitmap.getWidth(), tagLabelBitmap.getHeight());

					canvas.drawBitmap(tagLabelBitmap, src, tagedUserLabelRect[i], null);

					canvas.drawText(getTagedUserList().get(i).get("user_name"), tagedUserRect[i].left + convertSizeToDeviceDependent(3), tagedUserRect[i].bottom, textPaint);

				}
			}

			if (!isCancel) {
				x = (x - width);
				y = (y - height);

				x = x <= 0 ? 0.0f : x;
				x = x >= getWidth() ? getWidth() : x;

				y = y <= 0 ? 0.0f : y;
				y = y >= getHeight() ? getHeight() : y;

				if (x + width > getWidth()) {
					x = x - Math.abs((getWidth() - (x + width)));
				}

				if (y + height > getHeight()) {
					y = y - Math.abs((getHeight() - (y + height)));
				}

				mPaint.setStyle(Paint.Style.STROKE);
				mPaint.setStrokeCap(Paint.Cap.ROUND);
				mPaint.setColor(Color.RED);
				mPaint.setStrokeWidth(convertSizeToDeviceDependent(3));
				mPaint.setPathEffect(null);
				canvas.drawRect(x, y, x + width, y + height, mPaint);
				mPaint.setStyle(Paint.Style.STROKE);
				mPaint.setStrokeCap(Paint.Cap.ROUND);
				mPaint.setColor(Color.WHITE);
				mPaint.setStrokeWidth(convertSizeToDeviceDependent(3));
				mPaint.setPathEffect(new DashPathEffect(new float[] { 5, 5 }, 5));
				canvas.drawRect(x, y, x + width, y + height, mPaint);

				tagRect.left = x;
				tagRect.top = y;
				tagRect.right = x + width;
				tagRect.bottom = y + height;

				cancelRect.left = tagRect.left + cancelTag.getWidth() + convertSizeToDeviceDependent(5);
				cancelRect.top = tagRect.top - cancelTag.getHeight() + convertSizeToDeviceDependent(10);
				cancelRect.right = cancelRect.left + cancelTag.getWidth();
				cancelRect.bottom = cancelRect.top + cancelTag.getHeight();

				canvas.drawBitmap(cancelTag, cancelRect.left, cancelRect.top, null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method used to get device Rect from data.
	 * 
	 * @param pointData
	 *            represented data
	 * @return represented {@link RectF}
	 */
	private RectF getDeviceRectFromData(String pointData) {

		float width, height, left, top;
		width = height = left = top = 0;

		String strPos[] = pointData.split(",");

		width = Float.parseFloat(strPos[2]) * originalImageWidth;
		height = Float.parseFloat(strPos[3]) * originalImageHeight;
		top = (Float.parseFloat(strPos[0]) * originalImageHeight) - (height / 2);
		left = (Float.parseFloat(strPos[1]) * originalImageWidth) - (width / 2);

		width = (getWidth() * width) / originalImageWidth;
		height = (getHeight() * height) / originalImageHeight;
		left = (getWidth() * left) / originalImageWidth;
		top = (getHeight() * top) / originalImageHeight;

		RectF rectF = new RectF(left, top, (left + width), (top + height));

		return rectF;
	}

	/**
	 * This method used to get tagged position data.
	 * 
	 * @param tagRect
	 *            represented tagged RectF
	 * @return represented {@link String}
	 */
	public String getTaggedPositionData(RectF tagRect) {

		// 'top' : (photoTagSize.y1 + (photoTagSize.height / 2)) /
		// photo.height(),
		// 'left' : (photoTagSize.x1 + (photoTagSize.width / 2)) /
		// photo.width(),
		// 'width' : photoTagSize.width / photo.width(),
		// 'height' : photoTagSize.height / photo.height()

		float width, height, left, top;
		width = height = left = top = 0;

		width = (tagRect.width() / getWidth());
		height = (tagRect.height()) / getHeight();
		top = (tagRect.top + (tagRect.height() / 2)) / getHeight();
		left = (tagRect.left + (tagRect.width() / 2)) / getWidth();

		return String.format(String.format("%.2f,%.2f,%.2f,%.2f", top, left, width, height));
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

			try {
				if (getTagedUserList() != null) {
					int i;
					int size = getTagedUserList().size();
					for (i = 0; i < size; i++) {

						if (tagedUserLabelRect[i].contains((int) event.getX(), (int) event.getY())) {
							break;
						}
					}
					if (photoTagListener != null && (i != size)) {
						photoTagListener.onTagedItemClicked(i, getTagedUserList().get(i));
						return false;
					}
				}

			} catch (Exception e) {
			}
			return true;
		}
	}

	private void setFace(final int w, final int h) {
		new AsyncTask<Void, Void, Void>() {
			int[] fpx = null;
			int[] fpy = null;
			int count = 0;
			Bitmap faceBitmap = getDrawingCache();

			protected void onPreExecute() {
				faceBitmap = faceBitmap.copy(Config.RGB_565, true);
			};

			@Override
			protected Void doInBackground(Void... params) {
				FaceDetector fd;
				FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
				PointF midpoint = new PointF();

				try {
					fd = new FaceDetector(w, h, MAX_FACES);
					count = fd.findFaces(faceBitmap, faces);
					faceBitmap.recycle();
					faceBitmap = null;
				} catch (Exception e) {
					faceBitmap.recycle();
					faceBitmap = null;
					return null;
				}

				// check if we detect any faces
				if (count > 0) {
					facesRect = new RectF[count];

					fpx = new int[count];
					fpy = new int[count];

					for (int i = 0; i < count; i++) {
						try {
							faces[i].getMidPoint(midpoint);

							fpx[i] = (int) midpoint.x;
							fpy[i] = (int) midpoint.y;
						} catch (Exception e) {
						}
					}

				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				setDisplayPoints(fpx, fpy, count);
			}
		}.execute();

	}

	private void setDisplayPoints(int[] xx, int[] yy, int total) {
		mPX = null;
		mPY = null;

		if (xx != null && yy != null && total > 0) {
			mPX = new int[total];
			mPY = new int[total];

			for (int i = 0; i < total; i++) {
				mPX[i] = xx[i] + 20;
				mPY[i] = yy[i] + 40;
			}

			for (int i = 0; i < mPX.length; i++) {
				facesRect[i] = new RectF(mPX[i], mPY[i], mPX[i] + 50, mPY[i] + 50);
			}
			drawDetectedFace();
		}
	}

	private void drawDetectedFace() {
		try {
			isCancel = true;
			if (tagedUserRect != null && tagedUserRect.length > 0) {

				int size = facesRect.length;
				for (int j = 0; j < size; j++) {
					boolean tagExists = false;
					int tagedUserRectSize = tagedUserRect.length;
					for (int k = 0; k < tagedUserRectSize; k++) {
						if (tagedUserRect[k].intersect(facesRect[j]) || tagedUserRect[k].contains(facesRect[j])) {
							tagExists = true;
							break;
						}
					}
					if (!tagExists) {
						x = facesRect[j].left;
						y = facesRect[j].top;
						isCancel = false;
					}
				}
			} else {
				if (facesRect != null && facesRect.length > 0) {
					isCancel = false;
					x = facesRect[0].left;
					y = facesRect[0].top;
				}
			}
			invalidate();
			photoTagListener.showTagOptions(isCancel);
		} catch (Exception e) {
		}
		invalidate();
	}
}