package com.ijoomer.components.jReview;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerFileChooserActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.IjoomerLoginActivity;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

public class JreviewArticleAttachmentsActivity extends JReviewMasterActivity{

	private String IN_ARTICLENAME;
	private String IN_ARTICLEID;
	private String IN_CATEGORYID;

	private ListView lstAttachments;
	private ImageView addAttachments;

	private ArrayList<SmartListItem> fileListData = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> IN_FILELIST;
	private ArrayList<HashMap<String, String>> IN_ARTICLE_DETAILS;
	private SmartListAdapterWithHolder fileListAdapter;
	private AQuery androidQuery;
	private JReviewDataProvider dataProvider;
	private IjoomerCaching iCaching;

	final private int DOWNLOAD_FILE_LOCATION = 5;
	final private int UPLOAD_FILE_LOCATION = 4;
	private int downlodIndex = 0;

	@Override
	public int setLayoutId() {
		return R.layout.jreview_article_attachments_list;
	}

	@Override
	public void initComponents() {
		getIntentData();

		androidQuery = new AQuery(this);
		dataProvider = new JReviewDataProvider(this);
		iCaching = new IjoomerCaching(this);

		addAttachments = ((ImageView) getHeaderView().findViewById(R.id.imgAddArticle));
		addAttachments.setVisibility(View.VISIBLE);
		lstAttachments = (ListView) findViewById(R.id.lstAttachments);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(IjoomerApplicationConfiguration.isReloadRequired()){
			getArticleAttachments();
			IjoomerApplicationConfiguration.setReloadRequired(false);
		}
	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(IN_ARTICLENAME);

		getArticleAttachments();
	}

	@Override
	public void setActionListeners() {
		addAttachments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0) {
					if(IjoomerGlobalConfiguration.isJreviewAttachmentUploadEnable().equalsIgnoreCase("1")){
						if(IN_ARTICLE_DETAILS.get(0).get(ATTACHMENT_COUNT)
								.equalsIgnoreCase(IjoomerGlobalConfiguration.getJreviewArticleAttachmentUploadLimit())){
							IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_attachments), getString(R.string.jreview_upload_limit_notice)
									, getString(R.string.ok), R.layout.ijoomer_ok_dialog,
									new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						}else{
							Intent intent = new Intent(JreviewArticleAttachmentsActivity.this, IjoomerFileChooserActivity.class);
							intent.putExtra("IN_ISOPENFILE", true);
							startActivityForResult(intent, UPLOAD_FILE_LOCATION);
						}
					}else{
						if(IN_ARTICLE_DETAILS.get(0).get(USERNAME).
								equalsIgnoreCase(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, ""))){
							if(IN_ARTICLE_DETAILS.get(0).get(ATTACHMENT_COUNT)
									.equalsIgnoreCase(IjoomerGlobalConfiguration.getJreviewArticleAttachmentUploadLimit())){
								IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_attachments), getString(R.string.jreview_upload_limit_notice)
										, getString(R.string.ok), R.layout.ijoomer_ok_dialog,
										new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {

									}
								});
							}else{
								Intent intent = new Intent(JreviewArticleAttachmentsActivity.this, IjoomerFileChooserActivity.class);
								intent.putExtra("IN_ISOPENFILE", true);
								startActivityForResult(intent, UPLOAD_FILE_LOCATION);
							}
						}else{
							IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_attachments), getString(R.string.jreview_upload_notice)
									, getString(R.string.ok), R.layout.ijoomer_ok_dialog,
									new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						}
					}
				} else {
					try {
						getSmartApplication().writeSharedPreferences(SP_DOLOGIN, true);
						loadNew(IjoomerLoginActivity.class, JreviewArticleAttachmentsActivity.this, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * Class methods
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == DOWNLOAD_FILE_LOCATION) {
				final String path = data.getStringExtra("IN_PATH");
				final String fileName = IN_FILELIST.get(downlodIndex).get(TITLE);
				androidQuery.download(IN_FILELIST.get(downlodIndex).get(ATTACHMENT), new File(path + fileName), new AjaxCallback<File>() {
					@Override
					public void callback(String url, File object, AjaxStatus status) {
						super.callback(url, object, status);
						if (status.getCode() == 200) {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.download), status.getMessage(), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.download), status.getMessage(), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						}
					}
				});
			} else if (requestCode == UPLOAD_FILE_LOCATION) {
				IjoomerUtilities.addToNotificationBar(getString(R.string.jreview_attachment_upload_starts), getString(R.string.jreview_upload_attachment),
						getString(R.string.jreview_attachment_upload_starts));
				final String path = data.getStringExtra("IN_PATH");
				dataProvider.uploadAttachmentFile(path, IN_ARTICLEID, IN_CATEGORYID, new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {

					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							IjoomerUtilities.addToNotificationBar(getString(R.string.jreview_attachment_uploaded), IjoomerUtilities.mSmartAndroidActivity.getString(R.string.upload_photo), getString(R.string.photo_uploaded));

							if(IjoomerUtilities.mSmartAndroidActivity instanceof JreviewArticleAttachmentsActivity){
								IjoomerApplicationConfiguration.setReloadRequired(true);
								((JReviewArticleGalleryActivity)IjoomerUtilities.mSmartAndroidActivity).onResume();
							}
							getSmartApplication().writeSharedPreferences(SP_RELOADARTICLEDETAILS, true);
							getSmartApplication().writeSharedPreferences(SP_RELOADARTICLES, true);
						} else {
							if (errorMessage != null && errorMessage.length() > 0) {
								IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.jreview_attachment_upload_failure),
										IjoomerUtilities.mSmartAndroidActivity.getString(R.string.jreview_upload_attachment), errorMessage);
							} else {
								IjoomerUtilities.addToNotificationBar(
										IjoomerUtilities.mSmartAndroidActivity.getString(R.string.jreview_attachment_upload_failure),
										IjoomerUtilities.mSmartAndroidActivity.getString(R.string.jreview_upload_attachment),
										IjoomerUtilities.mSmartAndroidActivity.getString(IjoomerUtilities.mSmartAndroidActivity.getResources().getIdentifier("code" + responseCode,
												"string", IjoomerUtilities.mSmartAndroidActivity.getPackageName())));
							}
						}
					}
				});
			}
		}
	}


	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		try{
			IN_CATEGORYID = getIntent().getStringExtra(CATEGORY_ID) == null ? "0" : getIntent().getStringExtra(CATEGORY_ID);
			IN_ARTICLENAME = getIntent().getStringExtra(ARTICLENAME) == null ? "" : getIntent().getStringExtra(ARTICLENAME) ;
			IN_ARTICLEID = getIntent().getStringExtra(ARTICLEID) == null ? "" : getIntent().getStringExtra(ARTICLEID);
		}catch(Exception e){
		}
	}

	/**
	 * This method is used to get article details.
	 */
	private void getArticleAttachments(){
		try{
			IN_ARTICLE_DETAILS = dataProvider.getArticleAttachemnts(IN_CATEGORYID, IN_ARTICLEID);
			IN_FILELIST = iCaching.parseData(new JSONArray(IN_ARTICLE_DETAILS.get(0).get(MEDIAATTACHMENT)));
		}catch(Exception e){
			IN_FILELIST = null;
			e.printStackTrace();
		}

		if (IN_FILELIST !=null && IN_FILELIST.size()>0) {
			//prepare attachment list
			prepareFileList(IN_FILELIST,false);
			fileListAdapter = getFileListAdapter();
			lstAttachments.setAdapter(fileListAdapter);
		}
	}

	/**
	 * This method used to prepare list for file.
	 * 
	 * @param append
	 *            represented data append
	 */
	private void prepareFileList(ArrayList<HashMap<String, String>> fileList,boolean append) {
		if (fileList != null) {
			if (!append) {
				fileListData.clear();
			}
			for (HashMap<String, String> hashMap : fileList) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jreview_article_attatchments_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					fileListAdapter.add(item);
				} else {
					fileListData.add(item);
				}
			}

		}
	}

	/**
	 * List adapter for file.
	 * 
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	private SmartListAdapterWithHolder getFileListAdapter() {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.jreview_article_attatchments_list_item, fileListData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.jreviewarticletxtFileTitle = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtFileTitle);
				holder.jreviewarticletxtFileHit = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtFileHit);
				holder.jreviewarticletxtFileSize = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtFileSize);
				holder.jreviewarticletxtFileDesc = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtFileDesc);
				holder.jreviewarticlebtnfiledownload = (IjoomerButton) v.findViewById(R.id.jreviewarticlebtnfiledownload);

				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				holder.jreviewarticletxtFileTitle.setText(row.get(TITLE));
				holder.jreviewarticletxtFileDesc.setText(String.format(getString(R.string.by), row.get(FILEDESCRIPTION)));
				holder.jreviewarticletxtFileSize.setText(getString(R.string.jreview_size) + ":" + IjoomerUtilities.readableFileSize(Long.parseLong(row.get(FILESIZE))));
				holder.jreviewarticletxtFileHit.setText(getString(R.string.jreview_type) + ":"+row.get(FILEEXTENSION));

				holder.jreviewarticletxtFileTitle.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						downlodIndex = position;
						Intent intent = new Intent(JreviewArticleAttachmentsActivity.this, IjoomerFileChooserActivity.class);
						startActivityForResult(intent, DOWNLOAD_FILE_LOCATION);
					}
				});

				holder.jreviewarticlebtnfiledownload.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						downlodIndex = position;
						Intent intent = new Intent(JreviewArticleAttachmentsActivity.this, IjoomerFileChooserActivity.class);
						startActivityForResult(intent, DOWNLOAD_FILE_LOCATION);
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
