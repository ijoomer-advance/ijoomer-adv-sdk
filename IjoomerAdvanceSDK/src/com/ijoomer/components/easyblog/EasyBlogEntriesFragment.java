package com.ijoomer.components.easyblog;

import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
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
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.easyblog.EasyBlogEntryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Fragment Contains All Method Related To EasyBlogEntriesFragment.
 * 
 * @author tasol
 * 
 */
public class EasyBlogEntriesFragment extends SmartFragment implements EasyBlogTagHolder {

	private ListView lstEntries;
	private View listFooter;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<String> ID_ARRAY;
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;

	private EasyBlogEntryDataProvider dataProvider;
	private String IN_CATGEORY_IDS;

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.easyblog_entries_fragment;
	}

	@Override
	public void initComponents(View currentView) {
		dataProvider = new EasyBlogEntryDataProvider(getActivity());
		lstEntries = (ListView) currentView.findViewById(R.id.easyblogLstEntries);
		listFooter = LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
		lstEntries.addFooterView(listFooter, null, false);
		ID_ARRAY = new ArrayList<String>();
		androidAQuery = new AQuery(getActivity());
	}

	@Override
	public void prepareViews(View currentView) {
		((TextView) ((SmartActivity) getActivity()).getHeaderView().findViewById(R.id.txtHeader)).setText(getString(R.string.easy_blogs));
		getIntentData();
	}

	@Override
	public void setActionListeners(View currentView) {
		lstEntries.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 0) {

					if (!dataProvider.isCalling() && dataProvider.hasNextPage()) {
						listFooterVisible();
						dataProvider.getBlogEnteries(IN_CATGEORY_IDS, new WebCallListenerWithCacheInfo() {

							@Override
							public void onProgressUpdate(int progressCount) {
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit,
									boolean fromCache) {
								listFooterInvisible();
								if (responseCode == 200) {
									if (data1 != null && data1.size() > 0) {
										prepareList(data1, true, fromCache, pageNo, pageLimit);
									}
								}

							}
						});

					}
				}
			}
		});
		lstEntries.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

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

	/**
	 * This method used to update all blog enteries.
	 */
	public void update() {
		getAllEnteries();
	}

	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		try {
			String str = getActivity().getIntent().getStringExtra("IN_OBJ");
			JSONObject obj = new JSONObject(str).getJSONObject("itemdata");
			IN_CATGEORY_IDS = obj.getString("catID") == null ? "" : (new JSONObject(getActivity().getIntent().getStringExtra("IN_OBJ")).getJSONObject("itemdata"))
					.getString("catID");
			Log.e("IN_CATEGORY_IDS", IN_CATGEORY_IDS);
		} catch (Exception e) {

			IN_CATGEORY_IDS = (getActivity().getIntent().getStringExtra("IN_CATEGORY_IDS"));

		}
		if (IN_CATGEORY_IDS.trim().length() > 0)
			getAllEnteries();

	}

	/**
	 * This method used to visible list footer
	 */
	public void listFooterVisible() {
		listFooter.setVisibility(View.VISIBLE);
	}

	/**
	 * This method used to gone list footer
	 */
	public void listFooterInvisible() {
		listFooter.setVisibility(View.GONE);
	}

	/**
	 * This method used to get blog enteries list.
	 */
	private void getAllEnteries() {
		dataProvider.restorePagingSettings();
		;
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		dataProvider.getBlogEnteries(IN_CATGEORY_IDS, new WebCallListenerWithCacheInfo() {

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
						lstEntries.setAdapter(listAdapterWithHolder);
					} else {
						IjoomerUtilities.getCustomOkDialog(getString(R.string.easy_blog_list),
								getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok),
								R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {
										if (responseCode == 599) {
											getActivity().onBackPressed();
										}
									}
								});
					}
				} catch (Throwable e) {
				}

			}
		});

	}

	/**
	 * This method used to prepare blog enteries ids.
	 */
	@SuppressWarnings("unchecked")
	public void prepareIDS() {
		ID_ARRAY.clear();
		for (SmartListItem row : listData) {
			ID_ARRAY.add(((HashMap<String, String>) row.getValues().get(0)).get(ID));
		}
	}

	/**
	 * This method used to prepare list blog enteries.
	 * 
	 * @param data
	 *            represented article data
	 * @param append
	 *            represented data append
	 * @param fromCache
	 *            represented data from cache
	 * @param pageno
	 *            represented page no
	 * @param pagelimit
	 *            represented page limit
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
				item.setItemLayout(R.layout.easyblog_entry_listitem);
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
	 * List adapter for blog enteries
	 * 
	 * @param listData
	 *            represented blog enteries data
	 * @return represented {@link com.smart.framework.SmartListAdapterWithHolder}
	 */
	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.easyblog_entry_listitem, listData, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {

				holder.easyblogEntriesImage = (ImageView) v.findViewById(R.id.easyblogEntriesImage);
				holder.easyblogEntriesTxtTitle = (IjoomerTextView) v.findViewById(R.id.easyblogEntriesTxtTitle);
				holder.easyblogEntriesTxtIntro = (IjoomerTextView) v.findViewById(R.id.easyblogEntriesTxtIntro);
				holder.easyblogEntriesTxtHits = (IjoomerTextView) v.findViewById(R.id.easyblogEntriesTxtHits);
				holder.easyblogEntriesTxtPostedBy = (IjoomerTextView) v.findViewById(R.id.easyblogEntriesTxtPostedBy);
				holder.easyblogEntriesRtbRating = (IjoomerRatingBar) v.findViewById(R.id.easyblogEntriesRtbRating);
				holder.easyblogEntriesCommentCount = (IjoomerTextView) v.findViewById(R.id.easyblogEntriesCommentCount);
				holder.easyblogEntriesEdit = (IjoomerButton) v.findViewById(R.id.easyblogEntriesEdit);

				@SuppressWarnings("unchecked")
				final HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);

				androidAQuery.id(holder.easyblogEntriesImage).image(value.get(IMAGE), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.icms_default);
				holder.easyblogEntriesTxtTitle.setMovementMethod(new ScrollingMovementMethod());
				holder.easyblogEntriesTxtTitle.setText(value.get(TITLE).trim());
				holder.easyblogEntriesTxtIntro.setText(Html.fromHtml(value.get(INTRO)).toString().trim());
				holder.easyblogEntriesTxtHits.setText(value.get(HITS).trim());
				holder.easyblogEntriesTxtPostedBy.setText(value.get(CREATEDBYNAME).trim());
				holder.easyblogEntriesCommentCount.setText(value.get(COMMENTCOUNT).trim());
				holder.easyblogEntriesRtbRating.setStarRating(Float.parseFloat(value.get(RATINGVALUE)) / Float.parseFloat(value.get(RATINGTOTAL)));
				holder.easyblogEntriesRtbRating.setEditable(false);
				holder.easyblogEntriesTxtPostedBy.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						((IjoomerSuperMaster) getActivity()).gotoProfile(value.get(CREATEDBY));
					}
				});

				holder.easyblogEntriesEdit.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						try {
							prepareIDS();
							((SmartActivity) getActivity()).loadNew(EasyBlogAddBlogActivity.class, getActivity(), false, "IN_BLOGID", value.get(ID));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

				holder.easyblogEntriesTxtTitle.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						try {
							prepareIDS();
							((SmartActivity) getActivity()).loadNew(EasyBlogEntryDetailActivity.class, getActivity(), false, "IN_INDEX", String.valueOf(position), "IN_ID_ARRAY",
									ID_ARRAY);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				holder.easyblogEntriesTxtIntro.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						try {
							prepareIDS();
							((SmartActivity) getActivity()).loadNew(EasyBlogEntryDetailActivity.class, getActivity(), false, "IN_INDEX", String.valueOf(position), "IN_ID_ARRAY",
									ID_ARRAY);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				holder.easyblogEntriesImage.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						try {
							prepareIDS();
							((SmartActivity) getActivity()).loadNew(EasyBlogEntryDetailActivity.class, getActivity(), false, "IN_INDEX", String.valueOf(position), "IN_ID_ARRAY",
									ID_ARRAY);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				holder.easyblogEntriesRtbRating.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						try {
							prepareIDS();
							((SmartActivity) getActivity()).loadNew(EasyBlogEntryDetailActivity.class, getActivity(), false, "IN_INDEX", String.valueOf(position), "IN_ID_ARRAY",
									ID_ARRAY);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				holder.easyblogEntriesCommentCount.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						try {
							prepareIDS();
							((SmartActivity) getActivity()).loadNew(EasyBlogEntryDetailActivity.class, getActivity(), false, "IN_INDEX", String.valueOf(position), "IN_ID_ARRAY",
									ID_ARRAY);
						} catch (Exception e) {
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
		return adapterWithHolder;
	}

}