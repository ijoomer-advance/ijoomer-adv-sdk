package com.ijoomer.components.easyblog;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.easyblog.EasyBlogCategoryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Fragment Contains All Method Related To IcmsAllCategoryFragment.
 * 
 * @author tasol
 * 
 */
public class EasyBlogAllCategoryFragment extends SmartFragment implements EasyBlogTagHolder {

	private ListView list;
	private JSONObject IN_OBJ;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private HashMap<String, String> IN_PARENTCATEGORY;

	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;

	private EasyBlogCategoryDataProvider categoryDataProvider;

	/**
	 * Overrides method
	 */
	@Override
	public int setLayoutId() {
		return R.layout.easyblog_category_fragment;
	}

	@Override
	public void initComponents(View currentView) {
		categoryDataProvider = new EasyBlogCategoryDataProvider(getActivity());
		list = (ListView) currentView.findViewById(R.id.list);

		androidAQuery = new AQuery(getActivity());

		getIntentData();

	}

	@Override
	public void prepareViews(View currentView) {
		((TextView) ((SmartActivity) getActivity()).getHeaderView().findViewById(R.id.txtHeader)).setText(((IjoomerSuperMaster) getActivity()).getScreenCaption());
		getCategories();

	}

	@Override
	public void setActionListeners(View currentView) {

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) listAdapterWithHolder.getItem(arg2).getValues().get(0);
				((IjoomerSuperMaster) getActivity()).setScreenCaption(value.get(TITLE));

				try {

					((SmartActivity) getActivity()).loadNew(EasyBlogEntriesActivity.class, getActivity(), false, "IN_CATEGORY_IDS", value.get(ID));

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	@Override
	public View setLayoutView() {
		return null;
	}

	/**
	 * Class methods
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append, boolean fromCache, int pageno, int pagelimit) {
		if (data != null) {
			if (!append) {
				listData.clear();
			} else {
				if (!fromCache) {
					int startIndex = ((pageno - 1) * pagelimit);
					int endIndex = listAdapterWithHolder.getCount();
					if (startIndex > 0) {
						for (int i = endIndex; i >= startIndex; i--) {
							try {
								listAdapterWithHolder.remove(listAdapterWithHolder.getItem(i));
								listData.remove(i);
							} catch (Exception e) {
							}
						}
					}
				}
			}

			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.easyblog_category_listitem);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					listAdapterWithHolder.add(item);

				} else {
					listData.add(item);
				}
			}

		}
	}

	/**
	 * This method used to get intent data.
	 */
	@SuppressWarnings("unchecked")
	private void getIntentData() {
		try {
			IN_OBJ = new JSONObject(getActivity().getIntent().getStringExtra("IN_OBJ"));

			IN_PARENTCATEGORY = new HashMap<String, String>();
			try {
				IN_PARENTCATEGORY.put("title", getString(R.string.categorylist));
				IN_PARENTCATEGORY.put("categoryid", new JSONObject(IN_OBJ.getString(ITEMDATA)).getString("id"));
			} catch (JSONException e) {
				IN_PARENTCATEGORY.put("categoryid", "0");
				e.printStackTrace();
			}

		} catch (Exception e) {
			IN_PARENTCATEGORY = (HashMap<String, String>) getActivity().getIntent().getSerializableExtra("IN_PARENTCATEGORY");
		}
	}

	/**
	 * This method used to get article categories list.
	 */
	private void getCategories() {
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.category_progress_title));
		categoryDataProvider.getCategories(new WebCallListenerWithCacheInfo() {

			@Override
			public void onProgressUpdate(int progressCount) {

				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit,
					boolean fromCache) {
				try {
					if (responseCode == 200) {

						prepareList(data1, false, fromCache, pageNo, pageLimit);
						listAdapterWithHolder = getListAdapter(listData);
						list.setAdapter(listAdapterWithHolder);
					} else {
						IjoomerUtilities.getCustomOkDialog(getString(R.string.articles),
								getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok),
								R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {

									}
								});
					}
				} catch (Throwable e) {
				}

			}
		});
	}

	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.easyblog_category_listitem, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				holder.easyblogCatTxtTitle = (IjoomerTextView) v.findViewById(R.id.easyblogCatTxtTitle);

				holder.easyblogCatImageThumb = (ImageView) v.findViewById(R.id.easyblogCatImageThumb);

				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);

				androidAQuery.id(holder.easyblogCatImageThumb).image(value.get(THUMB), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.icms_default);

				holder.easyblogCatTxtTitle.setText(value.get(TITLE).trim());
				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapterWithHolder;
	}

}