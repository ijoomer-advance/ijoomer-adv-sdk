package com.ijoomer.components.jReview;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.ijoomer.src.R;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;

/**
 *  This Fragment Contains All Method Related To SobiproImageFragment.
 * 
 * @author tasol
 * 
 */

public class JReviewArticleImageFragment extends SmartFragment {
	private ImageView img;
	private AQuery aQuery;
	private String imgUrl;
	private ProgressBar imgloader;

	/**
	 * Constructor
	 * @param imgUrl represented image url which will need to display.
	 */

	public JReviewArticleImageFragment(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	/**
	 * Overrides methods.
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jreview_image_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		aQuery = new AQuery(getActivity());
		img = (ImageView) currentView.findViewById(R.id.sobiproImg);
		imgloader = (ProgressBar) currentView.findViewById(R.id.loader);
	}

	@Override
	public void prepareViews(View currentView) {
		aQuery.id(img).image(imgUrl, true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0, new BitmapAjaxCallback() {
			@Override
			protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
				super.callback(url, iv, bm, status);
				if (bm != null) {
					imgloader.setVisibility(View.GONE);
					img.setImageBitmap(bm);
				} else {
					imgloader.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	public void setActionListeners(View currentView) {
	}

}
