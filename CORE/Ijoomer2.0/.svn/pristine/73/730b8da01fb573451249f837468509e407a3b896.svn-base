package com.ijoomer.customviews;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.custom.interfaces.PhotoTagListener;

public class PhotoTagView extends ImageView {

	private ArrayList<HashMap<String, String>> tagedUserList;
	private RectF[] tagedUserRect;
	private RectF[] facesRect;
	private RectF[] tagedUserLabelRect;
	private static final int MAX_FACES = 10;

	PhotoTagListener photoTagListener;
	float x, y;
	Bitmap tagLabelBitmap;
	int tagLabelResource = 0;

	Paint textPaint;
	Paint textBackGroundPaint;

	float width = 0.0f;
	float height = 0.0f;
	float defaultWidth = 0.0f;
	float defaultHeight = 0.0f;
	float[] pts;

	RectF tagRect;

	String tagImageUrl;

	private Bitmap mBitmap;
	private Canvas mCanvas;
	private int mBitmapWidth = 200;
	private int mBitmapHeight = 200;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int[] mPX = null;
	private int[] mPY = null;

	AQuery aQuery;
	boolean addTag = false;
	boolean showTag = true;
	boolean showTagOptions = false;
	boolean isImageScaled = false;

	boolean isTagingDone = false;

	int originalImageWidth;
	int originalImageHeight;

	float defaultTop = 79;
	float defaultLeft = 74;

	private GestureDetector gestureDetector;

	public ArrayList<HashMap<String, String>> getTagedUserList() {
		return tagedUserList;
	}

	public void setTagedUserList(ArrayList<HashMap<String, String>> tagedUserList) {
		this.tagedUserList = tagedUserList;
		if (tagedUserList != null && tagedUserList.size() > 0) {
			tagedUserRect = new RectF[tagedUserList.size()];
			tagedUserLabelRect = new RectF[tagedUserList.size()];
			if (showTagOptions && photoTagListener != null) {
				photoTagListener.showTagOptions();
			}
		} else {
			if (tagedUserRect != null && tagedUserRect.length > 0) {
				if (photoTagListener != null) {
					photoTagListener.showTagOptions();
				}
				tagedUserLabelRect = null;
				tagedUserRect = null;
			}
		}

		if (isShowTag() && showTagOptions) {
			invalidate();
		}
	}

	public void setTagLabelResource(int tagLabelResource) {
		this.tagLabelResource = tagLabelResource;
		tagLabelBitmap = BitmapFactory.decodeResource(getResources(), tagLabelResource);
	}

	public boolean isTagingDone() {
		return isTagingDone;
	}

	public void setTagingDone(boolean isTagingDone) {
		this.isTagingDone = isTagingDone;
	}

	public PhotoTagListener getPhotoTagListener() {
		return photoTagListener;
	}

	public void setPhotoTagListener(PhotoTagListener photoTagListener) {
		this.photoTagListener = photoTagListener;
	}

	public boolean isShowTag() {
		return showTag;
	}

	public boolean isAddTag() {
		return addTag;
	}

	public void setShowTag() {
		addTag = false;
		showTag = true;
		invalidate();
	}

	public void setAddTag() {
		addTag = true;
		showTag = false;
		invalidate();
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
		gestureDetector = new GestureDetector(new SingleTapConfirm());
		aQuery = new AQuery(this);
		mBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.RGB_565);
		mCanvas = new Canvas(mBitmap);

		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setColor(0x80ff0000);
		mPaint.setStrokeWidth(convertSizeToDeviceDependent(3));

		x = convertSizeToDeviceDependent(20);
		y = convertSizeToDeviceDependent(20);
		width = convertSizeToDeviceDependent(50);
		height = convertSizeToDeviceDependent(50);

		defaultHeight = convertSizeToDeviceDependent(50);
		defaultWidth = convertSizeToDeviceDependent(50);

		textBackGroundPaint = new Paint();
		textBackGroundPaint.setColor(Color.BLACK);
		textBackGroundPaint.setStyle(Paint.Style.FILL);

		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(convertSizeToDeviceDependent(12));

		tagRect = new RectF();
	}

	public int convertSizeToDeviceDependent(int value) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return ((dm.densityDpi * value) / 160);
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	public void addNewTag() {
		if (addTag) {
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
	}

	public void setTagImageUrl(String imageUrl) {
		this.tagImageUrl = imageUrl;
		aQuery.ajax(imageUrl, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
			@Override
			public void callback(String url, Bitmap object, AjaxStatus status) {
				super.callback(url, object, status);
				originalImageWidth = object.getWidth();
				originalImageHeight = object.getHeight();
				mBitmap = object.copy(Bitmap.Config.RGB_565, true);
				if (mBitmap != null) {
					object.recycle();
					setImageBitmap(mBitmap);
				} else {
					setImageBitmap(object);
				}
			}
		});

	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		if (bm != null) {
			if (originalImageHeight <= 0 || originalImageWidth <= 0) {
				originalImageWidth = bm.getWidth();
				originalImageHeight = bm.getHeight();
			}

			mBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.RGB_565);
			mCanvas = new Canvas();
			mCanvas.setBitmap(mBitmap);
			if (bm.getWidth() > mBitmapWidth || bm.getHeight() > mBitmapHeight) {

				if (bm.getWidth() > bm.getHeight()) {
					mCanvas.drawBitmap(scaleImage(bm, mBitmapWidth), 0, 0, null);
				} else if (bm.getHeight() > bm.getWidth()) {
					mCanvas.drawBitmap(scaleImage(bm, mBitmapHeight), 0, 0, null);
				} else {
					mCanvas.drawBitmap(scaleImage(bm, mBitmapWidth), 0, 0, null);
				}

			} else {
				mCanvas.drawBitmap(bm, 0, 0, null);
			}
			setFace();
		}
		super.setImageBitmap(bm);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (getDrawable() != null) {
			int orientation = getResources().getConfiguration().orientation;
			if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
				mBitmapHeight = MeasureSpec.getSize(heightMeasureSpec);

				if (getLayoutParams().width == LayoutParams.WRAP_CONTENT) {
					float ratio = (float) getDrawable().getIntrinsicWidth() / (float) getDrawable().getIntrinsicHeight();
					mBitmapWidth = Math.round((float) mBitmapHeight * ratio);
				} else {
					mBitmapWidth = MeasureSpec.getSize(widthMeasureSpec);
				}
			} else {
				mBitmapWidth = MeasureSpec.getSize(widthMeasureSpec);
				if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
					float ratio = (float) getDrawable().getIntrinsicHeight() / (float) getDrawable().getIntrinsicWidth();
					mBitmapHeight = Math.round((float) mBitmapWidth * ratio);
				} else {
					mBitmapWidth = MeasureSpec.getSize(heightMeasureSpec);
				}
			}
			setMeasuredDimension(Math.min(mBitmapWidth, getDrawable().getIntrinsicWidth()), Math.min(mBitmapHeight, getDrawable().getIntrinsicHeight()));
		} else {
			mBitmapHeight = MeasureSpec.getSize(heightMeasureSpec);
			mBitmapWidth = MeasureSpec.getSize(widthMeasureSpec);
			setMeasuredDimension(mBitmapWidth, mBitmapHeight);
		}

	}

	private void setDisplayPoints(int[] xx, int[] yy, int total) {
		mPX = null;
		mPY = null;

		if (xx != null && yy != null && total > 0) {
			mPX = new int[total];
			mPY = new int[total];

			for (int i = 0; i < total; i++) {
				mPX[i] = xx[i];
				mPY[i] = yy[i];
			}
		}
		if (showTagOptions && total > 0) {
			invalidate();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawCanvas(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isAddTag()) {
			if (event.getAction() == MotionEvent.ACTION_DOWN && tagRect.contains(event.getX(), event.getY())) {
				if (photoTagListener != null) {
					addNewTag();
					return false;
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

	private void drawCanvas(Canvas canvas) {

		try {
			if (mBitmap != null) {
				if (isAddTag()) {
					canvas.drawBitmap(mBitmap, 0, 0, null);

					x = (x - width);
					y = (y - height);

					x = x <= 0 ? 0.0f : x;
					x = x >= getWidth() ? getWidth() : x;

					y = y <= 0 ? 0.0f : y;
					y = y >= getHeight() ? getHeight() : y;

					width = width < defaultWidth ? defaultWidth : width;
					height = height < defaultHeight ? defaultHeight : height;

					if (x + width > getWidth()) {
						x = x - Math.abs((getWidth() - (x + width)));
					}

					if (y + height > getHeight()) {
						y = y - Math.abs((getHeight() - (y + height)));
					}

					canvas.drawRect(x, y, x + width, y + height, mPaint);

					tagRect.left = x;
					tagRect.top = y;
					tagRect.right = x + width;
					tagRect.bottom = y + height;

				} else {
					canvas.drawBitmap(mBitmap, 0, 0, null);
					if (getTagedUserList() != null) {
						int size = getTagedUserList().size();
						for (int i = 0; i < size; i++) {
							tagedUserRect[i] = getDeviceRectFromData(getTagedUserList().get(i).get("position"));

							Rect textBound = new Rect();
							textPaint.getTextBounds(getTagedUserList().get(i).get("user_name"), 0, getTagedUserList().get(i).get("user_name").length(), textBound);

							tagedUserLabelRect[i] = new RectF(new RectF(tagedUserRect[i].left, tagedUserRect[i].bottom - convertSizeToDeviceDependent(12), tagedUserRect[i].left
									+ textBound.width() + convertSizeToDeviceDependent(6), (tagedUserRect[i].bottom - convertSizeToDeviceDependent(17)) + textBound.height()
									+ convertSizeToDeviceDependent(12)));

							canvas.drawBitmap(tagLabelBitmap, null, tagedUserLabelRect[i], null);

							canvas.drawText(getTagedUserList().get(i).get("user_name"), tagedUserRect[i].left + convertSizeToDeviceDependent(3), tagedUserRect[i].bottom, textPaint);

						}
					}
					if (mPX != null && mPY != null) {
						for (int i = 0; i < mPX.length; i++) {
							facesRect[i] = new RectF(mPX[i] - 20, mPY[i] - 20, mPX[i] + 20, mPY[i] + 50);
						}
					}
					if (showTagOptions) {
						if (facesRect != null) {
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
										canvas.drawRect(facesRect[j], mPaint);
									}
								}
							} else {
								for (int j = 0; j < facesRect.length; j++) {
									canvas.drawRect(facesRect[j], mPaint);
								}
							}
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

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

	private void setFace() {
		new AsyncTask<Void, Void, Void>() {
			int[] fpx = null;
			int[] fpy = null;
			int count = 0;
			Bitmap faceBitmap;
			int fWidth;
			int fHeight;

			protected void onPreExecute() {
				faceBitmap = mBitmap.copy(Config.RGB_565, true);
				fWidth = new Integer(mBitmapWidth);
				fHeight = new Integer(mBitmapHeight);
			};

			@Override
			protected Void doInBackground(Void... params) {
				FaceDetector fd;
				FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACES];
				PointF midpoint = new PointF();

				try {
					fd = new FaceDetector(fWidth, fHeight, MAX_FACES);
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

	private Bitmap scaleImage(Bitmap bitmap, int newSize) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int bounding = newSize;
		float xScale = ((float) bounding) / width;
		float yScale = ((float) bounding) / height;
		float scale = (xScale <= yScale) ? xScale : yScale;

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

		isImageScaled = true;

		return scaledBitmap;

	}

	private int dpToPx(int dp) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return Math.round((float) dp * density);
	}

	private class SingleTapConfirm extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapConfirmed(MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (!showTagOptions) {
					if (photoTagListener != null) {
						photoTagListener.showTagOptions();
						showTagOptions = true;
						invalidate();
						return false;

					}
				}
			}
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
			if (facesRect != null) {
				int i = 0;
				int size = facesRect.length;
				for (i = 0; i < size; i++) {
					if (facesRect[i].contains((int) event.getX(), (int) event.getY())) {
						break;
					}
				}
				if (photoTagListener != null && (i != size)) {
					if (getTagedUserList() != null && getTagedUserList().size() > 0) {
						boolean isConflict = false;
						int tagedUserRectSize = tagedUserRect.length;
						for (int j = 0; j < tagedUserRectSize; j++) {
							if (tagedUserRect[j].intersect(facesRect[i]) || tagedUserRect[j].contains(facesRect[i])) {
								isConflict = true;
								break;
							}
						}
						if (!isConflict) {
							photoTagListener.onAddNewTag(getTaggedPositionData(facesRect[i]));
							return false;
						}
					} else {
						photoTagListener.onAddNewTag(getTaggedPositionData(facesRect[i]));
						return false;
					}
				}
			}

			return true;
		}
	}
}