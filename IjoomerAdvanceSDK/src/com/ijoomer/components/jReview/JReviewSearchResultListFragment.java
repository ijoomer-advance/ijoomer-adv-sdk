package com.ijoomer.components.jReview;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.graphics.Bitmap;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * 
 * @author tasol
 * 
 */
public class JReviewSearchResultListFragment extends SmartFragment implements JReviewTagHandler {
	private String IN_CAPTION = "";

	private ListView list;
	private ImageView imgMap;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	public  ArrayList<HashMap<String, String>> mapData = new ArrayList<HashMap<String, String>>();
	private ArrayList<String> ARTICLEIDS_ARRAY;
	private ArrayList<String> CATEGORYIDS_ARRAY;
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidAQuery;
	private SeekBar proSeekBar;
	private JReviewDataProvider dataProvider;
	private IjoomerCaching iCaching;
	private String optionCriteria, searchKey, searchPhrase, category_ids, author;
	private JSONArray searchFields;

	/**
	 * Overrides methods.
	 */
	@Override
	public int setLayoutId() {
		return R.layout.jreview_article_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		getIntentData();
		
		dataProvider = new JReviewDataProvider(getActivity());
		androidAQuery = new AQuery(getActivity());
		iCaching = new IjoomerCaching(getActivity());

		list = (ListView) currentView.findViewById(R.id.jreviewListArticle);
		ARTICLEIDS_ARRAY = new ArrayList<String>();
		CATEGORYIDS_ARRAY = new ArrayList<String>();
	}

	@Override
	public void prepareViews(View currentView) {
		((TextView) ((IjoomerSuperMaster) getActivity()).getHeaderView()
				.findViewById(R.id.txtHeader)).setText(IN_CAPTION);
		imgMap = ((ImageView) ((IjoomerSuperMaster) getActivity()).getHeaderView()
				.findViewById(R.id.imgMap));
		getResults();
	}

	@Override
	public void setActionListeners(View currentView) {
		imgMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					((SmartActivity) getActivity()).loadNew(JReviewArticlesMapActivity.class, getActivity()
							, false, "IN_PAGE", PAGEARTICLES, "IN_CAPTION", IN_CAPTION, "IN_MAPLIST", mapData, 
							"IN_ARTICLE_ID_ARRAY", ARTICLEIDS_ARRAY, "IN_CATEGORY_ID_ARRAY", CATEGORYIDS_ARRAY, 
							"IN_SHOW_BUBBLE", true);
				} catch (Throwable e) {
					e.printStackTrace();
				}	
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
		try {
			IN_CAPTION = getActivity().getIntent().getStringExtra("IN_CAPTION");
			optionCriteria = getActivity().getIntent().getStringExtra("IN_OPTIONCRITERIA");
			searchKey = getActivity().getIntent().getStringExtra("IN_SEARCH_KEY");
			searchPhrase = getActivity().getIntent().getStringExtra("IN_SEARCH_PHRASE");
			category_ids = getActivity().getIntent().getStringExtra("IN_CATEGORY_IDS");
			author = getActivity().getIntent().getStringExtra("IN_AUTHOR");
			searchFields = new JSONArray(getActivity().getIntent().getStringExtra("IN_SEARCHFIELD"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method used to get articles list.
	 */
	private void getResults() {
		proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

		dataProvider.getSearchResult(optionCriteria, searchKey, searchPhrase, category_ids, author, searchFields, new WebCallListener() {

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if(responseCode == 200){
					try {
						ArrayList<HashMap<String, String>> searcharticles = new ArrayList<HashMap<String,String>>();
						for(int i = 0; i < data1.size(); i++){
							ArrayList<HashMap<String, String>> articles = dataProvider.getSingleArticle(data1.get(i).get(CATEGORY_ID), data1.get(i).get(LISTINGID));
							if(articles != null && articles.size() > 0){
								searcharticles.add(articles.get(0));
							}
						}
						//create article listing
						prepareList(searcharticles,false);
						listAdapterWithHolder = getListAdapter(listData);
						list.setAdapter(listAdapterWithHolder);

						if(mapData!=null && mapData.size()>0){
							imgMap.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if(responseCode == 204){
					IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_search), getString(R.string.jreview_nocontentfound), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
							new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {
						}
					});
				} else{
					IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_search), getString(getResources()
							.getIdentifier("code" + responseCode, "string", ((SmartActivity) getActivity()).getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
							new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {

						}
					});
				}
			}

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

		});

	}

	/**
	 * This method used to prepare list blog categories article.
	 * 
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

				try {
					@SuppressWarnings("unchecked")
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
