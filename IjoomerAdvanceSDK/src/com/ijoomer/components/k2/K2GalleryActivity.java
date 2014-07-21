package com.ijoomer.components.k2;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerViewPager;
import com.ijoomer.src.R;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This Class Contains All Method Related To K2GalleryActivity.
 *
 * @author tasol
 *
 */
@SuppressWarnings("deprecation")
public class K2GalleryActivity extends K2MasterActivity {

    private Gallery gallary;
    public static IjoomerViewPager viewPager;
    private ProgressBar pbrLoadImage;

    private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
    private JSONArray imagesArray;
    private AQuery androidQuery;
    private PageAdapter adapter;

    private String IN_PHOTOS_PATHS;

    /**
     * Overrides methods
     */

    @Override
    public int setLayoutId() {
        return R.layout.k2_gallery;
    }

    @Override
    public void initComponents() {
        viewPager = (IjoomerViewPager) findViewById(R.id.viewPager);
        pbrLoadImage = (ProgressBar) findViewById(R.id.pbrLoadImage);
        gallary = (Gallery) findViewById(R.id.gallary);
        gallary.setSpacing(5);

        androidQuery = new AQuery(this);
        getIntentData();
    }

    @Override
    public int setHeaderLayoutId() {
        return 0;
    }

    @Override
    public int setFooterLayoutId() {
        return 0;
    }

    @Override
    public void prepareViews() {
        adapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, true);

        prepareGallary();
        gallary.setAdapter(getGallaryAdapter());
    }

    @Override
    public void setActionListeners() {

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                gallary.setSelection(arg0, true);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        gallary.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                viewPager.setCurrentItem(arg2, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    /**
     * Class methods
     */

    /**
     * This method used to get intent data.
     */
    private void getIntentData() {
        IN_PHOTOS_PATHS = getIntent().getStringExtra("IN_PHOTOS_PATHS") != null ? getIntent().getStringExtra("IN_PHOTOS_PATHS") : "";
        try {
            imagesArray = new JSONArray(IN_PHOTOS_PATHS);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * This method used to prepare gallery.
     */
    private void prepareGallary() {

        for (int i = 0; i < imagesArray.length(); i++) {
            SmartListItem item = new SmartListItem();
            item.setItemLayout(R.layout.k2_gallary_item);
            ArrayList<Object> obj = new ArrayList<Object>();
            try {
                obj.add(imagesArray.get(i));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            item.setValues(obj);
            listData.add(item);
        }

    }

    /**
     * List adapter for gallery.
     *
     * @return represented {@link SmartListAdapterWithHolder}
     */
    private SmartListAdapterWithHolder getGallaryAdapter() {

        SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.k2_gallary_item, listData, new ItemView() {

            @Override
            public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
                holder.imgItem = (ImageView) v.findViewById(R.id.imgItem);
                final JSONObject image = (JSONObject) item.getValues().get(0);

                try {
                    if(pbrLoadImage.getVisibility() == View.GONE){
                        pbrLoadImage.setVisibility(View.VISIBLE);
                    }
                    androidQuery.id(holder.imgItem).image(image.getString(IMAGEGALLERY), true, true, getDeviceWidth(), 0, new BitmapAjaxCallback() {
                        @Override
                        protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                            super.callback(url, iv, bm, status);
                            holder.imgItem.setImageBitmap(bm);
                            pbrLoadImage.setVisibility(View.GONE);
                        }

                    });
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                v.setLayoutParams(new Gallery.LayoutParams(70, 70));
                return v;
            }

            @Override
            public View setItemView(int position, View v, SmartListItem item) {
                return null;
            }
        });
        return adapterWithHolder;

    }

    /**
     * Inner class
     *
     * @author tasol
     *
     */
    private class PageAdapter extends FragmentStatePagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int pos) {
            System.gc();
            try {
                K2GalleryFragment fragment = new K2GalleryFragment(imagesArray.getJSONObject(pos).getString(IMAGEGALLERY));
                return fragment;
            } catch (Throwable e) {
            }
            return null;
        }

        @Override
        public int getCount() {
            return imagesArray.length();
        }

    }

}
