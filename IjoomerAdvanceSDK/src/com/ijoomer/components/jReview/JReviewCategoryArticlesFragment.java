package com.ijoomer.components.jReview;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.graphics.Bitmap;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.IjoomerLoginActivity;
import com.ijoomer.src.R;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Fragment Contains All Method Related To IcmsCategoryBlogFragment.
 * 
 * @author tasol
 * 
 */
public class JReviewCategoryArticlesFragment extends SmartFragment implements JReviewTagHandler,
IjoomerSharedPreferences{

	private String IN_CATEGORYID = "0";
	private String IN_CAPTION = "";

	private ListView listArticles;
	private ImageView imgMap;
	private ImageView addArticle;
	private IjoomerEditText edtSearchArticles;
	private ArrayList<HashMap<String, String>> groups;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> mapData = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> ARTICLEIDS_ARRAY;
	private ArrayList<String> CATEGORYIDS_ARRAY;
	private SmartListAdapterWithHolder listAdapterWithHolder;

	private JReviewDataProvider dataProvider;
	private AQuery androidAQuery;
	private IjoomerCaching iCaching;

	private boolean hasNextpage = true;
	private int pageNo = 0;
	private int pageLimit = 20;
	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jreview_article_fragment;
	}

	@Override
	public void initComponents(View currentView) {
		getIntentData();

		dataProvider = new JReviewDataProvider(getActivity());
		androidAQuery = new AQuery(getActivity());
		iCaching = new IjoomerCaching(getActivity());

		imgMap = ((ImageView) ((IjoomerSuperMaster) getActivity()).getHeaderView().
				findViewById(R.id.imgMap));
		addArticle = ((ImageView) ((SmartActivity) getActivity()).getHeaderView().
				findViewById(R.id.imgAddArticle));
		listArticles = (ListView) currentView.findViewById(R.id.jreviewListArticle);
		edtSearchArticles = (IjoomerEditText) currentView.findViewById(R.id.jreviewarticleSearch);
		edtSearchArticles.setVisibility(View.VISIBLE);
		ARTICLEIDS_ARRAY = new ArrayList<String>();
		CATEGORYIDS_ARRAY = new ArrayList<String>();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(((SmartActivity) getActivity()).getSmartApplication()
				.readSharedPreferences().getBoolean(SP_RELOADARTICLES, false)){
			((SmartActivity) getActivity()).getSmartApplication()
			.writeSharedPreferences(SP_RELOADARTICLES, false);
			getArticles();
		}
	}

	@Override
	public void prepareViews(View currentView) {
		((TextView) ((IjoomerSuperMaster) getActivity()).getHeaderView().findViewById(R.id.txtHeader)).setText(IN_CAPTION);

		getArticles();
	}

	@Override
	public void setActionListeners(View currentView) {
		imgMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					((SmartActivity) getActivity()).loadNew(JReviewArticlesMapActivity.class, getActivity(), false, 
							"IN_PAGE", PAGEARTICLES, "IN_CAPTION", IN_CAPTION, "IN_MAPLIST", mapData, 
							"IN_ARTICLE_ID_ARRAY", ARTICLEIDS_ARRAY,
							"IN_CATEGORY_ID_ARRAY", CATEGORYIDS_ARRAY, "IN_SHOW_BUBBLE", true);
				} catch (Throwable e) {
					e.printStackTrace();
				}	
			}
		});

		addArticle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(((SmartActivity)getActivity()).getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0){
					try{
						((SmartActivity) getActivity()).loadNew(JReviewCreateListingActivity.class, getActivity(), false, CATEGORY_ID, IN_CATEGORYID,
								ARTICLEID, "0", GROUPS, groups, "IS_EDIT", false);
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					try{
						((SmartActivity)getActivity()).getSmartApplication().writeSharedPreferences(SP_DOLOGIN, true);
						((SmartActivity) getActivity()).loadNew(IjoomerLoginActivity.class, getActivity(), false);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});

		listArticles.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 0) {
					if(hasNextpage){
						if(edtSearchArticles.getText().toString().length() > 0){
							prepareList(dataProvider.getSearchArticles(IN_CATEGORYID, edtSearchArticles.getText().toString().toLowerCase().trim(), 
									String.valueOf(pageNo), String.valueOf(pageLimit)), false);
							listAdapterWithHolder.notifyDataSetChanged();
						}else{
							prepareList(dataProvider.getArticles(IN_CATEGORYID, String.valueOf(listArticles.getCount()), String.valueOf(pageLimit)),true);
							listAdapterWithHolder.notifyDataSetChanged();
						}
					}
				}

			}
		});

		edtSearchArticles.addTextChangedListener(new TextWatcher()
		{
			public void afterTextChanged(Editable s)
			{
				String searchString = edtSearchArticles.getText().toString().toLowerCase().trim();
				if(searchString.length()>0)
				{
					prepareList(dataProvider.getSearchArticles(IN_CATEGORYID, searchString, String.valueOf(pageNo), String.valueOf(pageLimit)), false);
					listAdapterWithHolder = getListAdapter(listData);
					listArticles.setAdapter(listAdapterWithHolder);
				}
				else
				{
					prepareList(dataProvider.getArticles(IN_CATEGORYID, String.valueOf(pageNo), String.valueOf(pageLimit)),false);
					listAdapterWithHolder = getListAdapter(listData);
					listArticles.setAdapter(listAdapterWithHolder);
				}
			}

			public void beforeTextChanged(CharSequence s, int start,int count, int after)
			{
			}

			public void onTextChanged(CharSequence s, int start,int before, int count)
			{
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
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		IN_CATEGORYID = getActivity().getIntent().getStringExtra(CATEGORY_ID);
		IN_CAPTION = getActivity().getIntent().getStringExtra(CATEGORY_NAME);
	}

	private void getArticles(){
		//get create article form data 
		groups = dataProvider.getListingForm(IN_CATEGORYID);

		//create article listing
		prepareList(dataProvider.getArticles(IN_CATEGORYID, String.valueOf(pageNo), String.valueOf(pageLimit)),false);
		listAdapterWithHolder = getListAdapter(listData);
		listArticles.setAdapter(listAdapterWithHolder);

		if(mapData!=null && mapData.size()>0){
			imgMap.setVisibility(View.VISIBLE);
		}

		if(((SmartActivity)getActivity()).getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0){
			if(IjoomerGlobalConfiguration.isJreviewAddListingAllow()){
				addArticle.setVisibility(View.VISIBLE);
			}
		}
	} 

	/**
	 * This method used to prepare list blog categories article.
	 * 
	 * @param catData
	 *            represented categories data
	 * @param articleData
	 *            represented article data
	 */
	public void prepareList(ArrayList<HashMap<String, String>> articleData,boolean append) {
		if (!append) {
			listData.clear();
			mapData.clear();
			CATEGORYIDS_ARRAY.clear();
			ARTICLEIDS_ARRAY.clear();
		}
		if (articleData != null && articleData.size() > 0) {
			for (int i = 0; i < articleData.size(); i++) {
				CATEGORYIDS_ARRAY.add(articleData.get(i).get(CATEGORY_ID));
				ARTICLEIDS_ARRAY.add(articleData.get(i).get(ARTICLEID));
				mapData.add(articleData.get(i));
				SmartListItem articleitem = new SmartListItem();
				articleitem.setItemLayout(R.layout.jreview_article_listitem);
				ArrayList<Object> articlevalues = new ArrayList<Object>();
				articlevalues.add(articleData.get(i));
				articleitem.setValues(articlevalues);
				if (append) {
					listAdapterWithHolder.add(articleitem);
				} else {
					listData.add(articleitem);
				}
			}
		}else{
			hasNextpage = false;
		}
	}

	/**
	 * List adapter for blog categories and article.
	 * 
	 * @param listData
	 *            represented categories data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.jreview_article_listitem, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}

			@SuppressWarnings("unchecked")
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.jreviewArticleView = (LinearLayout) v.findViewById(R.id.jreviewarticleview);
				holder.jreviewArticleImg = (ImageView) v.findViewById(R.id.jreviewarticleimg);
				holder.jreviewTxtArticleTitletxt = (IjoomerTextView) v.findViewById(R.id.jreviewarticletitle);
				holder.jreviewTxtArticleIntrotxt = (IjoomerTextView) v.findViewById(R.id.jreviewarticleintro);
				holder.jreviewArticleUserRatingbar = (IjoomerRatingBar) v.findViewById(R.id.jreviewarticleuserratingBar);
				holder.jreviewArticleUserRating = (IjoomerTextView) v.findViewById(R.id.jreviewarticleuserrating);
				holder.jreviewTxtArticleUserRatingCount = (IjoomerTextView) v.findViewById(R.id.jreviewarticleuserratingcount);
				holder.jreviewArticleEditorRatingbar = (IjoomerRatingBar) v.findViewById(R.id.jreviewarticleeditorratingBar);
				holder.jreviewArticleEditorRating = (IjoomerTextView) v.findViewById(R.id.jreviewarticleeditorrating);
				holder.jreviewTxtArticleEditorRatingCount = (IjoomerTextView) v.findViewById(R.id.jreviewarticleeditorratingcount);
				holder.jreviewTxtArticleVideoCount = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtVideoCount);
				holder.jreviewTxtArticlePhotoCount = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtPhotoCount);
				holder.jreviewTxtArticleMusicCount = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtMusicCount);
				holder.jreviewTxtArticleAttachmentCount = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtAttachmentCount);
				holder.jreviewTxtArticleFavouriteCount = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtFavouriteCount);

				try{
					final HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);
					//view article image
					if(value.get(IMAGES) != null && value.get(IMAGES).length() > 0){
						androidAQuery.id(holder.jreviewArticleImg).image( ((IjoomerSuperMaster) getActivity()).getStringArray(value.get(IMAGES))[0], true, true,
								200, 0, new BitmapAjaxCallback() {
							@Override
							protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
								super.callback(url, iv, bm, status);
								if (bm != null) {
									holder.jreviewArticleImg.setVisibility(View.VISIBLE);
									holder.jreviewArticleImg.setImageBitmap(bm);
								} else {
									holder.jreviewArticleImg.setVisibility(View.GONE);
								}
							}
						});
					} else if(value.get(MEDIAIMAGES) != null 
							&& value.get(MEDIAIMAGES).length() > 0){
						androidAQuery.id(holder.jreviewArticleImg).image(iCaching.parseData(new JSONArray(value.get(MEDIAIMAGES))).get(0).get(ORIGINAL), true, true,
								200, 0, new BitmapAjaxCallback() {
							@Override
							protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
								super.callback(url, iv, bm, status);
								if (bm != null) {
									holder.jreviewArticleImg.setVisibility(View.VISIBLE);
									holder.jreviewArticleImg.setImageBitmap(bm);
								} else {
									holder.jreviewArticleImg.setVisibility(View.GONE);
								}
							}
						});
					}else{
						holder.jreviewArticleImg.setVisibility(View.GONE);
					}

					//view article title and introtext
					holder.jreviewTxtArticleTitletxt.setText(value.get(ARTICLENAME).trim());
					holder.jreviewTxtArticleIntrotxt.setText(Html.fromHtml(value.get(INTROTEXT).trim()));

					//view article user and editor ratings
					holder.jreviewArticleUserRatingbar.setStarRating(IjoomerUtilities.parseFloat(value.get(OVERALLAVERAGERATING)));
					holder.jreviewArticleUserRating.setText("("+IjoomerUtilities.parseFloat(value.get(OVERALLAVERAGERATING))+")");
					holder.jreviewTxtArticleUserRatingCount.setText("("+value.get(USERRATINGCOUNT)+")");
					holder.jreviewArticleEditorRatingbar.setStarRating(IjoomerUtilities.parseFloat(value.get(EDITORRATING)));
					holder.jreviewArticleEditorRating.setText("("+IjoomerUtilities.parseFloat(value.get(EDITORRATING))+")");
					holder.jreviewTxtArticleEditorRatingCount.setText("("+value.get(EDITORRATINGCOUNT)+")");

					//view counts belongs to article(media & favourite)
					if(IjoomerGlobalConfiguration.isJreviewVideoEnable()){
						holder.jreviewTxtArticleVideoCount.setVisibility(View.VISIBLE);
						holder.jreviewTxtArticleVideoCount.setText("("+value.get(VIDEO_COUNT)+")");
					}else{
						holder.jreviewTxtArticleVideoCount.setVisibility(View.GONE);
					}

					if(IjoomerGlobalConfiguration.isJreviewPhotoEnable()){
						holder.jreviewTxtArticlePhotoCount.setVisibility(View.VISIBLE);
						holder.jreviewTxtArticlePhotoCount.setText("("+value.get(PHOTO_COUNT)+")");
					}else{
						holder.jreviewTxtArticlePhotoCount.setVisibility(View.GONE);
					}

					if(IjoomerGlobalConfiguration.isJreviewAudioEnable()){
						holder.jreviewTxtArticleMusicCount.setVisibility(View.VISIBLE);
						holder.jreviewTxtArticleMusicCount.setText("("+value.get(AUDIO_COUNT)+")");
					}else{
						holder.jreviewTxtArticleMusicCount.setVisibility(View.GONE);
					}

					if(IjoomerGlobalConfiguration.isJreviewAttachmentEnable()){
						holder.jreviewTxtArticleAttachmentCount.setVisibility(View.VISIBLE);
						holder.jreviewTxtArticleAttachmentCount.setText("("+value.get(ATTACHMENT_COUNT)+")");
					}else{
						holder.jreviewTxtArticleAttachmentCount.setVisibility(View.GONE);
					}

					if(IjoomerGlobalConfiguration.isJreviewFavouriteEnable()){
						holder.jreviewTxtArticleFavouriteCount.setVisibility(View.VISIBLE);
						if(value.get(ISFAVORITE).equalsIgnoreCase("1")){
							holder.jreviewTxtArticleFavouriteCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jreview_favorites_selected, 0, 0, 0);
						}else{
							holder.jreviewTxtArticleFavouriteCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.jreview_favorites, 0, 0, 0);
						}
						holder.jreviewTxtArticleFavouriteCount.setText("("+value.get(TOTALFAVORITE)+")");
					}else{
						holder.jreviewTxtArticleFavouriteCount.setVisibility(View.GONE);
					}

					//set click on article
					holder.jreviewArticleView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							try {
								((SmartActivity) getActivity()).loadNew(JReviewArticleDetailActivity.class, getActivity(), false, "IN_PAGE", PAGEARTICLES, "IN_CAPTION", IN_CAPTION, "IN_ARTICLE_INDEX", position
										+ "", "IN_ARTICLE_ID_ARRAY", ARTICLEIDS_ARRAY,"IN_CATEGORY_ID_ARRAY", CATEGORYIDS_ARRAY);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}catch(Exception e){
					e.printStackTrace();
				}
				return v;
			}
		});
		return adapterWithHolder;
	}

}