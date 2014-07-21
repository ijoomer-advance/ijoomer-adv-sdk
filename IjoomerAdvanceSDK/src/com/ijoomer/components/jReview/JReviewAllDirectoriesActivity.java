package com.ijoomer.components.jReview;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

public class JReviewAllDirectoriesActivity extends JReviewMasterActivity{

	private ListView directory_list;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder adapterDirectories;
	private IjoomerCaching iCaching;
	private final String TABLE_JREVIEW_DIRECTORIES = "jreview_directory";

	@Override
	public int setLayoutId() {
		return R.layout.jreview_allcategories_list;
	}

	@Override
	public void initComponents() {
		iCaching = new IjoomerCaching(this);
		directory_list = (ListView) findViewById(R.id.list);
	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getString(R.string.jreview_directorylist));

		try{
			prepareList(iCaching.getDataFromCache(TABLE_JREVIEW_DIRECTORIES, 
					"SELECT * FROM " + TABLE_JREVIEW_DIRECTORIES), false);
			adapterDirectories = getListAdapter();
			directory_list.setAdapter(adapterDirectories);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void setActionListeners() {

	}

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
					adapterDirectories.add(item);
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
				final HashMap<String, String> directory = (HashMap<String, String>) item.getValues().get(0);

				holder.jreviewTxtDirectoriesCaption.setText(directory.get(DIRECTORY_NAME));
				holder.jreviewTxtCategoriesCount.setText("("+directory.get(TOTALCATEGORIES)+")");

				v.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						try {
							loadNew(JReviewCategoriesActivity.class, JReviewAllDirectoriesActivity.this, false, ITEMCAPTION, directory.get(DIRECTORY_NAME), PARENTID, directory.get(DIRECTORY_ID));
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
}
