package com.ijoomer.components.jomsocial;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.IjoomerButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.IjoomerTextView;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.custom.interfaces.PhotoTagListener;
import com.ijoomer.customviews.PhotoTagView;
import com.ijoomer.library.jomsocial.JomFriendsDataProvider;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

public class JomPhotoTagActivity extends JomMasterActivity {

	private PhotoTagView tagView;
	private IjoomerTextView txtRemovePhotoTag;
	private IjoomerTextView txtAddPhotoTag;
	private IjoomerTextView txtShowPhotoTag;
	private JomGalleryDataProvider provider;

	// private JomFriendsDataProvider friendProvider;
	private JomGalleryDataProvider tagDataProvider;
	private ArrayList<HashMap<String, String>> tagList;
	private ArrayList<HashMap<String, String>> friendList;
	private ArrayList<SmartListItem> tagListData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder tagAdapter;
	private PopupWindow dialog;
	private ImageView imgTagClose;
	private ListView lstTagUser;
	private ProgressBar pbrTag;

	private HashMap<String, String> IN_PHOTO;

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
	}

	@Override
	public int setLayoutId() {
		return R.layout.jom_photo_tag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initComponents() {
		setApplicationOrientation(SCREEN_ORIENTATION_UNSPECIFIED);
		tagDataProvider = new JomGalleryDataProvider(this);
		provider = new JomGalleryDataProvider(this);
		IN_PHOTO = (HashMap<String, String>) getIntent().getSerializableExtra("IN_PHOTO");
		tagView = (PhotoTagView) findViewById(R.id.tagView);
		txtRemovePhotoTag = (IjoomerTextView) findViewById(R.id.txtRemovePhotoTag);
		txtAddPhotoTag = (IjoomerTextView) findViewById(R.id.txtAddPhotoTag);
		txtShowPhotoTag = (IjoomerTextView) findViewById(R.id.txtShowPhotoTag);
	}

	@Override
	public void prepareViews() {
		provider.getPhotoTages(IN_PHOTO.get(ID), new WebCallListener() {
			final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				tagList = data1;
				tagView.setTagImageUrl(IN_PHOTO.get(URL));
				tagView.setShowTag();
				tagView.setCloseIjoomerButtonResource(R.drawable.com_facebook_close);
				tagView.setTagedUserList(tagList);
				tagView.setTagLabelResource(R.drawable.tag_label);
				tagView.setPhotoTagListener(new PhotoTagListener() {

					@Override
					public void onTagedItemClicked(int position, Object data) {
						gotoProfile(tagList.get(position).get(USER_ID));
					}

				});

			}
		});

	}

	@Override
	public void setActionListeners() {

		txtAddPhotoTag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (txtAddPhotoTag.getText().equals(getString(R.string.select_tag_user))) {
					showPhotoTagOrRemoveDialog(false);
					txtAddPhotoTag.setText(getString(R.string.add_tag));
				} else {
					tagView.setAddTag();
					txtAddPhotoTag.setTextColor(getResources().getColor(R.color.blue));
					txtAddPhotoTag.setText(getString(R.string.select_tag_user));
					txtRemovePhotoTag.setTextColor(getResources().getColor(R.color.txt_color));
					txtShowPhotoTag.setTextColor(getResources().getColor(R.color.txt_color));
				}
			}
		});

		txtRemovePhotoTag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPhotoTagOrRemoveDialog(true);
				txtAddPhotoTag.setTextColor(getResources().getColor(R.color.txt_color));
				txtRemovePhotoTag.setTextColor(getResources().getColor(R.color.blue));
				txtShowPhotoTag.setTextColor(getResources().getColor(R.color.txt_color));
				txtAddPhotoTag.setText(getString(R.string.add_tag));
			}
		});
		txtShowPhotoTag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tagView.setShowTag();
				txtAddPhotoTag.setTextColor(getResources().getColor(R.color.txt_color));
				txtRemovePhotoTag.setTextColor(getResources().getColor(R.color.txt_color));
				txtShowPhotoTag.setTextColor(getResources().getColor(R.color.blue));
				txtAddPhotoTag.setText(getString(R.string.add_tag));
			}
		});
	}

	private void showPhotoTagOrRemoveDialog(final boolean isRemoveTag) {
		try {

			int popupWidth = getWindowManager().getDefaultDisplay().getWidth() - convertSizeToDeviceDependent(50);
			int popupHeight = getWindowManager().getDefaultDisplay().getHeight() - convertSizeToDeviceDependent(200);

			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = layoutInflater.inflate(R.layout.jom_photo_video_tag_dialog, null);

			dialog = new PopupWindow(this);
			dialog.setContentView(layout);
			dialog.setWidth(popupWidth);
			dialog.setHeight(popupHeight);
			dialog.setFocusable(true);
			dialog.setBackgroundDrawable(new BitmapDrawable());
			dialog.showAtLocation(layout, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);

			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					txtAddPhotoTag.setText(getString(R.string.add_tag));
				}
			});
			imgTagClose = (ImageView) layout.findViewById(R.id.imgTagClose);
			lstTagUser = (ListView) layout.findViewById(R.id.lstTagUser);
			pbrTag = (ProgressBar) layout.findViewById(R.id.pbrTag);
			lstTagUser.setAdapter(null);
			imgTagClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			if (!isRemoveTag) {
				tagDataProvider.restorePagingSettings();
				tagDataProvider.getFriendsForTagPhoto(IN_PHOTO.get(ID), new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {

					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						friendList = data1;
						if (responseCode == 200) {
							updateHeader(tagDataProvider.getNotificationData());
							IjoomerApplicationConfiguration.setReloadRequired(true);
							prepareTagList(isRemoveTag);
							tagAdapter = getTagListAdapter();
							lstTagUser.setAdapter(tagAdapter);
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_title_tag_user),
									getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
									new CustomAlertNeutral() {

										@Override
										public void NeutralMathod() {
											dialog.dismiss();
										}
									});
						}
						pbrTag.setVisibility(View.GONE);
					}
				});
			} else {
				pbrTag.setVisibility(View.GONE);
				prepareTagList(isRemoveTag);
				tagAdapter = getTagListAdapter();
				lstTagUser.setAdapter(tagAdapter);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void prepareTagList(boolean isRemoveTag) {
		tagListData.clear();

		if (isRemoveTag) {
			if (tagList != null && tagList.size() > 0) {
				for (int i = 0; i < tagList.size(); i++) {
					SmartListItem item = new SmartListItem();
					item.setItemLayout(R.layout.jom_photo_video_tag_dialog_item);
					ArrayList<Object> obj = new ArrayList<Object>();
					tagList.get(i).put(TAGED, "true");
					obj.add(tagList.get(i));
					item.setValues(obj);
					tagListData.add(item);
				}
			}
		}

		if (friendList != null && friendList.size() > 0) {
			for (int i = 0; i < friendList.size(); i++) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_photo_video_tag_dialog_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(friendList.get(i));
				item.setValues(obj);
				tagListData.add(item);
			}
		}

		if (tagListData.size() <= 0) {
			prepareTagList(true);

		}
	}

	private SmartListAdapterWithHolder getTagListAdapter() {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.jom_photo_video_tag_dialog_item, tagListData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {
				holder.txtPhotoTagUser = (IjoomerTextView) v.findViewById(R.id.txtPhotoTagUser);
				holder.btnPhotoTag = (IjoomerButton) v.findViewById(R.id.btnPhotoTag);
				holder.btnRemovePhotoTag = (IjoomerButton) v.findViewById(R.id.btnRemovePhotoTag);

				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				if (row.containsKey(TAGED) && row.get(DELETEALLOWED).equals("1")) {
					holder.btnPhotoTag.setVisibility(View.GONE);
					holder.btnRemovePhotoTag.setVisibility(View.VISIBLE);
				} else if (!row.containsKey(TAGED)) {
					holder.btnPhotoTag.setVisibility(View.VISIBLE);
					holder.btnRemovePhotoTag.setVisibility(View.GONE);
				}

				holder.txtPhotoTagUser.setText(row.get(USER_NAME));

				holder.txtPhotoTagUser.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (row.get(USER_PROFILE).equals("1")) {
							gotoProfile(row.get(USER_ID));
						}
					}
				});

				holder.btnPhotoTag.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						provider.addPhotoTag(IN_PHOTO.get(ID), row.get(USER_ID), tagView.getTaggedPositionData(), new WebCallListener() {
							final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

							@Override
							public void onProgressUpdate(int progressCount) {

							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									provider.getPhotoTages(IN_PHOTO.get(ID), new WebCallListener() {

										@Override
										public void onProgressUpdate(int progressCount) {
											proSeekBar.setProgress(progressCount);
										}

										@Override
										public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

											if (responseCode == 200) {
												tagList = data1;
											} else {
												IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_title_tag_user),
														getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
														R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

															@Override
															public void NeutralMathod() {

															}
														});
											}
											tagView.setTagedUserList(tagList);
											tagView.setShowTag();
											txtAddPhotoTag.setTextColor(getResources().getColor(R.color.txt_color));
											txtRemovePhotoTag.setTextColor(getResources().getColor(R.color.txt_color));
											txtShowPhotoTag.setTextColor(getResources().getColor(R.color.blue));

										}
									});
								} else {
									tagView.setShowTag();
									txtAddPhotoTag.setTextColor(getResources().getColor(R.color.txt_color));
									txtRemovePhotoTag.setTextColor(getResources().getColor(R.color.txt_color));
									txtShowPhotoTag.setTextColor(getResources().getColor(R.color.blue));

									proSeekBar.setProgress(100);
									IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_title_tag_user),
											getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
											R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

												@Override
												public void NeutralMathod() {
													if (dialog != null && dialog.isShowing()) {
														dialog.dismiss();
													}

												}
											});
								}
							}
						});
					}
				});
				holder.btnRemovePhotoTag.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						provider.removePhotoTag(row.get(ID), new WebCallListener() {
							final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									tagList.remove(position);
								} else {
									IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_title_tag_user),
											getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
											R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

												@Override
												public void NeutralMathod() {

												}
											});
								}
								tagView.setShowTag();
								txtAddPhotoTag.setTextColor(getResources().getColor(R.color.txt_color));
								txtRemovePhotoTag.setTextColor(getResources().getColor(R.color.txt_color));
								txtShowPhotoTag.setTextColor(getResources().getColor(R.color.blue));
							}
						});
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
