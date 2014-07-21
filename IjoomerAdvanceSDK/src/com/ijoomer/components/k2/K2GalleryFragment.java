package com.ijoomer.components.k2;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.ijoomer.customviews.GestureImageView;
import com.ijoomer.customviews.GestureImageViewListener;
import com.ijoomer.src.R;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;

/**
 * This Fragment Contains All Method Related To K2GalleryFragment.
 *
 * @author tasol
 *
 */
@SuppressLint("ValidFragment")
public class K2GalleryFragment extends SmartFragment implements K2TagHolder {

    private GestureImageView imgPhotoDetail;

    private AQuery androidQuery;

    private String imagePath;

    private float scales ;

    /**
     * Constructor
     * @param path represented image path
     */
    public K2GalleryFragment(String path) {
        androidQuery = new AQuery(getActivity());
        this.imagePath = path;
    }

    /**
     * Overrides methods
     */

    @Override
    public int setLayoutId() {
        return R.layout.k2_gallary_fragment;
    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public void initComponents(View currentView) {
        imgPhotoDetail = (GestureImageView) currentView.findViewById(R.id.imgPhotoDetail);
    }

    @Override
    public void prepareViews(View currentView) {
        androidQuery.id(imgPhotoDetail).image(imagePath, true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0, new BitmapAjaxCallback() {
            @Override
            protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                super.callback(url, iv, bm, status);
                imgPhotoDetail.setAdjustViewBounds(true);
                imgPhotoDetail.setImageBitmap(bm);
            }
        });
    }

    @Override
    public void setActionListeners(View currentView) {


        imgPhotoDetail.setGestureImageViewListener(new GestureImageViewListener() {

            @Override
            public void onTouch(float x, float y) {

            }

            @Override
            public void onScale(float scale) {
                if (scale >scales) {
                    K2GalleryActivity.viewPager.setScrollable(false);
                } else {
                    K2GalleryActivity.viewPager.setScrollable(true);
                }
                scales=scale;
            }

            @Override
            public void onPosition(float x, float y) {

            }
        });
    }

}
