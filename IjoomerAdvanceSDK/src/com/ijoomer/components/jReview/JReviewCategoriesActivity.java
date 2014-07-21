package com.ijoomer.components.jReview;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.R;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

public class JReviewCategoriesActivity extends JReviewMasterActivity {

	private String PID = "0";
	private String CAPTION = "";

	private ListView categoty_list;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder adapterCategories;
	private JReviewDataProvider categoriesDataProvider;

	@Override
	public int setLayoutId() {
		return R.layout.jreview_allcategories_list;
	}

	@Override
	public void initComponents() {
		getIntentData();
		categoriesDataProvider = new JReviewDataProvider(this);
		categoty_list = (ListView) findViewById(R.id.list);
	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(CAPTION);

		prepareList(categoriesDataProvider.getCategories(PID), false);
		adapterCategories = getListAdapter();
		categoty_list.setAdapter(adapterCategories);
	}

	@Override
	public void setActionListeners() {

	}

	/*
	 * 
	 * class methods
	 */

	/**
	 * This method used to prepare list directories.
	 * @param data represented search member data
	 * @param append represented data append
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jreview_allcategories_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					adapterCategories.add(item);
				} else {
					listData.add(item);
				}
			}

		}
	}

	/**
	 * List adapter for directories.
	 */
	private SmartListAdapterWithHolder getListAdapter() {
		SmartListAdapterWithHolder adapter = new SmartListAdapterWithHolder(this, R.layout.jreview_allcategories_list_item, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
				holder.jreviewTxtDirectoriesCaption = (IjoomerTextView) v.findViewById(R.id.jreviewCatTxtTitle);
				holder.jreviewTxtCategoriesCount = (IjoomerTextView) v.findViewById(R.id.jreviewCatTxtArticlesCount);

				@SuppressWarnings("unchecked")
				final HashMap<String, String> category = (HashMap<String, String>) item.getValues().get(0);

				holder.jreviewTxtDirectoriesCaption.setText(category.get(CATEGORY_NAME));
				if(category.get(TOTALSUBCATEGORIES).equalsIgnoreCase("0")){
					holder.jreviewTxtCategoriesCount.setText("("+category.get(TOTALARTICLES)+")");
				}else{
					holder.jreviewTxtCategoriesCount.setText("("+category.get(TOTALSUBCATEGORIES)+")");
				}
				v.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						try {
							if(category.get(TOTALSUBCATEGORIES).equalsIgnoreCase("0")){
								//if(!category.get(TOTALARTICLES).equalsIgnoreCase("0")){
								loadNew(JReviewCategoryArticlesActivity.class, JReviewCategoriesActivity.this, false, 
										CATEGORY_NAME, category.get(CATEGORY_NAME), CATEGORY_ID, category.get(CATEGORY_ID),
										TOTALARTICLES, category.get(TOTALARTICLES));
								//}
							}else{
								loadNew(JReviewCategoriesActivity.class, JReviewCategoriesActivity.this, false, 
										ITEMCAPTION, category.get(CATEGORY_NAME), PARENTID, category.get(CATEGORY_ID));								
							}
						} catch (Throwable e) {
							e.printStackTrace();
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
		return adapter;
	}

	/**
	 * This method is used to get intent data.
	 */
	private void getIntentData() {
		try {
			PID = getIntent().getStringExtra(PARENTID);
			CAPTION = getIntent().getStringExtra(ITEMCAPTION);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
