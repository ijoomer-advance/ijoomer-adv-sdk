package com.ijoomer.components.jomsocial;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView.BufferType;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerMapAddress;
import com.ijoomer.common.classes.IjoomerPhotoGalaryActivity;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.SelectImageDialogListner;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerListView;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager.MessageHandler;
import com.ijoomer.customviews.IjoomerVoiceButton;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.page.indicator.CirclePageIndicator;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomAlbumsDetailsActivity.
 *
 * @author tasol
 *
 */
public class JomAlbumsDetailsActivity extends JomMasterActivity {

	private LinearLayout listFooter;
	private LinearLayout lnrPhotoList;
	private LinearLayout lnrPlayRecordComment;
	private IjoomerListView lstAlbumComment;
	private LinearLayout lnrAlbumWriteComment;
	private LinearLayout lnrAlbumDetail;
	private LinearLayout lnrCreateAlbum;
	private LinearLayout lnrHeader;
	private IjoomerTextView txtAlbumUploadPhoto;
	private IjoomerTextView txtAlbumTitle;
	private IjoomerTextView txAlbumBy;
	private IjoomerTextView txtAlbumDateLocation;
	private IjoomerTextView txtAlbumDescription;
	private IjoomerTextView txtAlbumLikeCount;
	private IjoomerTextView txtAlbumDislikeCount;
	private IjoomerTextView txtAlbumCommentCount;
	private IjoomerTextView txtAlbumShare;
	private IjoomerTextView txtAlbumPrivacy;
	private IjoomerTextView txtAlbumEdit;
	private IjoomerTextView txtTotalRecordComment;
	private IjoomerTextView txtRecordUser;
	private IjoomerTextView txtAlbumRemove;
	private IjoomerEditText edtAlbumName;
	private IjoomerEditText edtAlbumLocation;
	private IjoomerEditText edtAlbumDescription;
	private IjoomerButton btnSave;
	private IjoomerButton btnCancle;
	private IjoomerButton btnUploadPhoto;
	private IjoomerVoiceButton btnPlayAll;
	private IjoomerVoiceAndTextMessager voiceMessager;
	private Spinner spnWhoCanSee;
	private ImageView imgAlbumAvatar;
	private ImageView imgMap;
	private ViewPager viewPager;
	private CirclePageIndicator indicator;
	private ViewGroup albumDeatilHeaderLayout;

	private AQuery androidQuery;
	private HashMap<String, String> IN_ALBUM;
	private PageAdapter adapter;
	private SmartListAdapterWithHolder commentAdapter;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<JomPhotoFragment> photoFragmetStack = new ArrayList<JomPhotoFragment>();

	private JomGalleryDataProvider commentProvider;
	private JomGalleryDataProvider provider;

	final private int PICK_IMAGE = 0;
	final private int PICK_IMAGE_MULTIPLE = 1;
	final private int GET_ADDRESS_FROM_MAP = 4;
	final private int TAKE_IMAGE = 2;
	private int recordCommentCounter;
	private int recordCommentTotal;
	private int recordCommentLast;
	public static boolean isResume = false;
	public static int PHOTO_COUNT;
	private String IN_USERID;
	private String IN_GROUP_ID;
	private String IN_PROFILE_COVER;
	private String IN_GROUP_UPLOAD_PHOTO;
	private int pageLimit = 12;

	/**
	 * Overrides method
	 */
	@Override
	public int setLayoutId() {
		return R.layout.jom_album_details;
	}

	@Override
	public void initComponents() {

		albumDeatilHeaderLayout = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.jom_album_details_header, null);
		listFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);

		lstAlbumComment = (IjoomerListView) findViewById(R.id.lstAlbumComment);
		lnrHeader = (LinearLayout) findViewById(R.id.lnrHeader);
		txtAlbumEdit = (IjoomerTextView) findViewById(R.id.txtAlbumEdit);
		txtAlbumRemove = (IjoomerTextView) findViewById(R.id.txtAlbumRemove);
		txtAlbumUploadPhoto = (IjoomerTextView) findViewById(R.id.txtAlbumUploadPhoto);
		lstAlbumComment.addHeaderView(albumDeatilHeaderLayout, null, false);
		lstAlbumComment.addFooterView(listFooter, null, false);
		lstAlbumComment.setAdapter(null);
		lnrAlbumWriteComment = (LinearLayout) findViewById(R.id.lnrAlbumWriteComment);

		lnrCreateAlbum = (LinearLayout) albumDeatilHeaderLayout.findViewById(R.id.lnrCreateAlbum);
		lnrAlbumDetail = (LinearLayout) albumDeatilHeaderLayout.findViewById(R.id.lnrAlbumDetail);
		lnrPhotoList = (LinearLayout) albumDeatilHeaderLayout.findViewById(R.id.lnrPhotoList);
		lnrPlayRecordComment = (LinearLayout) albumDeatilHeaderLayout.findViewById(R.id.lnrPlayRecordComment);

		txtAlbumTitle = (IjoomerTextView) albumDeatilHeaderLayout.findViewById(R.id.txtAlbumTitle);
		txAlbumBy = (IjoomerTextView) albumDeatilHeaderLayout.findViewById(R.id.txAlbumBy);
		txtAlbumDateLocation = (IjoomerTextView) albumDeatilHeaderLayout.findViewById(R.id.txtAlbumDateLocation);
		txtAlbumDescription = (IjoomerTextView) albumDeatilHeaderLayout.findViewById(R.id.txtAlbumDescription);
		txtAlbumLikeCount = (IjoomerTextView) albumDeatilHeaderLayout.findViewById(R.id.txtAlbumLikeCount);
		txtAlbumDislikeCount = (IjoomerTextView) albumDeatilHeaderLayout.findViewById(R.id.txtAlbumDislikeCount);
		txtAlbumCommentCount = (IjoomerTextView) albumDeatilHeaderLayout.findViewById(R.id.txtAlbumCommentCount);
		txtAlbumShare = (IjoomerTextView) albumDeatilHeaderLayout.findViewById(R.id.txtAlbumShare);
		txtAlbumPrivacy = (IjoomerTextView) albumDeatilHeaderLayout.findViewById(R.id.txtAlbumPrivacy);
		txtTotalRecordComment = (IjoomerTextView) albumDeatilHeaderLayout.findViewById(R.id.txtTotalRecordComment);
		txtRecordUser = (IjoomerTextView) albumDeatilHeaderLayout.findViewById(R.id.txtRecordUser);
		imgAlbumAvatar = (ImageView) albumDeatilHeaderLayout.findViewById(R.id.imgAlbumAvatar);
		imgMap = (ImageView) albumDeatilHeaderLayout.findViewById(R.id.imgMap);
		indicator = (CirclePageIndicator) albumDeatilHeaderLayout.findViewById(R.id.indicator);
		edtAlbumName = (IjoomerEditText) albumDeatilHeaderLayout.findViewById(R.id.edtAlbumName);
		edtAlbumLocation = (IjoomerEditText) albumDeatilHeaderLayout.findViewById(R.id.edtAlbumLocation);
		edtAlbumDescription = (IjoomerEditText) albumDeatilHeaderLayout.findViewById(R.id.edtAlbumDescription);
		spnWhoCanSee = (Spinner) albumDeatilHeaderLayout.findViewById(R.id.spnWhoCanSee);
		btnSave = (IjoomerButton) albumDeatilHeaderLayout.findViewById(R.id.btnSave);
		btnCancle = (IjoomerButton) albumDeatilHeaderLayout.findViewById(R.id.btnCancle);
		btnUploadPhoto = (IjoomerButton) albumDeatilHeaderLayout.findViewById(R.id.btnUploadPhoto);
		btnPlayAll = (IjoomerVoiceButton) albumDeatilHeaderLayout.findViewById(R.id.btnPlayAll);
		btnPlayAll.setReportVoice(false);
		viewPager = (ViewPager) albumDeatilHeaderLayout.findViewById(R.id.viewPager);
		voiceMessager = (IjoomerVoiceAndTextMessager) findViewById(R.id.voiceMessager);

		androidQuery = new AQuery(this);
		commentProvider = new JomGalleryDataProvider(this);
		provider = new JomGalleryDataProvider(this);

		getIntentData();
	}

	@Override
	public void prepareViews() {

		if (!IN_PROFILE_COVER.equals("0")) {
			ting(getString(R.string.tap_photo_to_set_profile_cover));
			lnrHeader.setVisibility(View.GONE);
			lnrAlbumDetail.setVisibility(View.GONE);
			lnrAlbumWriteComment.setVisibility(View.GONE);
			btnUploadPhoto.setVisibility(View.VISIBLE);
			lnrPlayRecordComment.setVisibility(View.GONE);

		}
		spnWhoCanSee.setAdapter(new IjoomerUtilities.MyCustomAdapter(this, new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.wall_post_type)))));
		spnWhoCanSee.setSelection(getPrivacyIndex(IN_ALBUM.get(PERMISSION)));
		if (IN_ALBUM.get(COUNT).equals("0") && IN_PROFILE_COVER.equals("0")) {
			lnrPhotoList.setVisibility(View.GONE);
		} else {
			System.out.println("stack : " + getSupportFragmentManager().getBackStackEntryCount());
			adapter = new PageAdapter(getSupportFragmentManager());
			viewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, calculateheight()));
			viewPager.setAdapter(adapter);
			viewPager.setCurrentItem(0);
			indicator.setPageColor(Color.TRANSPARENT);
			indicator.setStrokeColor(Color.parseColor(getString(R.color.jom_blue)));
			indicator.setStrokeWidth(convertSizeToDeviceDependent(1));
			indicator.setRadius(convertSizeToDeviceDependent(3));

			indicator.setFillColor(Color.parseColor(getString(R.color.jom_blue)));
			indicator.setViewPager(viewPager, 0);
			indicator.setSnap(true);
			if(Integer.parseInt(IN_ALBUM.get(COUNT)) <= 0 ){
				indicator.setVisibility(View.GONE);
			}
			indicator.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					System.out.println("PAGE state " + arg0);
					((JomPhotoFragment) adapter.getItem(arg0)).notifyChanges();
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (IjoomerApplicationConfiguration.isReloadRequired()) {
			IjoomerApplicationConfiguration.setReloadRequired(false);
			if (!IN_ALBUM.get(COUNT).toString().equals(PHOTO_COUNT)) {
				if (lnrPhotoList.getVisibility() == View.GONE) {
					lnrPhotoList.setVisibility(View.VISIBLE);
				}
				photoFragmetStack.clear();
				IN_ALBUM.put(COUNT, String.valueOf(PHOTO_COUNT));
				IjoomerApplicationConfiguration.setReloadRequired(true);
				prepareViews();
			} else {
				photoFragmetStack.clear();
				prepareViews();
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void setActionListeners() {

		btnPlayAll.setAudioListener(new AudioListener() {

			@Override
			public void onReportClicked() {
			}

			@Override
			public void onPrepared() {
			}

			@Override
			public void onPlayClicked(boolean isplaying) {

				if (!isplaying) {
					recordCommentCounter = 0;
					recordCommentLast = 0;
					txtRecordUser.setVisibility(View.VISIBLE);
					recordCommentCounter += 1;
					txtTotalRecordComment.setText("(" + recordCommentCounter + "/" + recordCommentTotal + ")");
					for (int i = 0; i < listData.size(); i++) {
						if (getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(COMMENT)) != null) {
							btnPlayAll.setAudioPath(getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(COMMENT)), false);
							btnPlayAll.setText(getAudioLength(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(COMMENT)));
							txtRecordUser.setText(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(USER_NAME));
							recordCommentLast = i;
							break;
						}
					}
				} else {
					btnPlayAll.setCustomText(getString(R.string.play_all));
					txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
					txtRecordUser.setVisibility(View.GONE);
					recordCommentCounter = recordCommentTotal;
				}
			}

			@Override
			public void onComplete() {
				if (recordCommentCounter == recordCommentTotal) {
					btnPlayAll.setCustomText(getString(R.string.play_all));
					txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
					txtRecordUser.setVisibility(View.GONE);
				} else {
					recordCommentCounter += 1;
					txtTotalRecordComment.setText("(" + recordCommentCounter + "/" + recordCommentTotal + ")");
					for (int i = recordCommentLast + 1; i < listData.size(); i++) {
						if (getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(COMMENT)) != null) {
							btnPlayAll.setAudioPath(getAudio(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(COMMENT)), true);
							btnPlayAll.setText(getAudioLength(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(COMMENT)));
							txtRecordUser.setText(((HashMap<String, String>) listData.get(i).getValues().get(0)).get(USER_NAME));
							recordCommentLast = i;
							break;
						}
					}
				}
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				if (edtAlbumName.getText().toString().trim().length() > 0) {
					if (isAlbumDataChanged()) {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						Address address = IjoomerUtilities.getLatLongFromAddress(edtAlbumLocation.getText().toString().trim());
						provider.addAlbum(IN_ALBUM.get(ID), IN_GROUP_ID, edtAlbumName.getText().toString().trim(), edtAlbumDescription.getText().toString().trim(),
								address != null ? address.getLatitude() : 0, address != null ? address.getLongitude() : 0,
										getPrivacyCode(spnWhoCanSee.getSelectedItem().toString().trim()).toString(), new WebCallListener() {

									@Override
									public void onProgressUpdate(int progressCount) {
										proSeekBar.setProgress(progressCount);
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										if (responseCode == 200) {
											updateHeader(provider.getNotificationData());
											IjoomerApplicationConfiguration.setReloadRequired(true);
											saveAlbumDetails();
											lnrAlbumDetail.setVisibility(View.VISIBLE);
											lnrCreateAlbum.setVisibility(View.GONE);
											lnrPhotoList.setVisibility(View.VISIBLE);
											lnrAlbumWriteComment.setVisibility(View.VISIBLE);
										} else {
											IjoomerUtilities.getCustomOkDialog(getString(R.string.photo),
													getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
													R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

												@Override
												public void NeutralMethod() {
													lnrAlbumDetail.setVisibility(View.VISIBLE);
													lnrCreateAlbum.setVisibility(View.GONE);
													lnrPhotoList.setVisibility(View.VISIBLE);
													lnrAlbumWriteComment.setVisibility(View.VISIBLE);
												}
											});
										}
									}

								});
					} else {
						lnrAlbumDetail.setVisibility(View.VISIBLE);
						lnrCreateAlbum.setVisibility(View.GONE);
						lnrPhotoList.setVisibility(View.VISIBLE);
						lnrAlbumWriteComment.setVisibility(View.VISIBLE);
					}
				} else {
					edtAlbumName.setError(getString(R.string.validation_value_required));
				}

			}
		});

		btnUploadPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				IjoomerUtilities.selectImageDialog(new SelectImageDialogListner() {

					@Override
					public void onPhoneGallery() {
						if (IjoomerApplicationConfiguration.isUploadMultiplePhotos()) {
							Intent intent = new Intent(JomAlbumsDetailsActivity.this, IjoomerPhotoGalaryActivity.class);
							startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
						} else {
							Intent intent = new Intent();
							intent.setType("image/*");
							intent.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
						}
					}

					@Override
					public void onCapture() {
						final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
						startActivityForResult(intent, TAKE_IMAGE);
					}
				});

			}
		});

		imgMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(JomAlbumsDetailsActivity.this, IjoomerMapAddress.class);
				startActivityForResult(intent, GET_ADDRESS_FROM_MAP);
			}
		});

		voiceMessager.setMessageHandler(new MessageHandler() {

			@Override
			public void onVoiceMessageRecordingComplete(String message, String voiceMessagePath) {
				lstAlbumComment.setSelectionAfterHeaderView();
				provider.addAlbumComment(IN_ALBUM.get(ID), message, voiceMessagePath, new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(provider.getNotificationData());
							IjoomerApplicationConfiguration.setReloadRequired(true);
							IN_ALBUM.put(COMMENTCOUNT, String.valueOf(Integer.parseInt(IN_ALBUM.get(COMMENTCOUNT)) + 1));
							txtAlbumCommentCount.setText(IN_ALBUM.get(COMMENTCOUNT));
							getComment();
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.photo),
									getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
									new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {
									lnrAlbumDetail.setVisibility(View.VISIBLE);
									lnrCreateAlbum.setVisibility(View.GONE);
								}
							});
						}
					}
				});
			}

			@Override
			public void onButtonSend(String message) {
				lstAlbumComment.setSelectionAfterHeaderView();
				provider.addAlbumComment(IN_ALBUM.get(ID), message, null, new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(provider.getNotificationData());
							IjoomerApplicationConfiguration.setReloadRequired(true);
							IN_ALBUM.put(COMMENTCOUNT, String.valueOf(Integer.parseInt(IN_ALBUM.get(COMMENTCOUNT)) + 1));
							txtAlbumCommentCount.setText(IN_ALBUM.get(COMMENTCOUNT));
							getComment();
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.photo),
									getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
									new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {
									lnrAlbumDetail.setVisibility(View.VISIBLE);
									lnrCreateAlbum.setVisibility(View.GONE);
								}
							});
						}
					}
				});
			}

			@Override
			public void onToggle(int messager) {

			}
		});

		txtAlbumUploadPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IjoomerUtilities.selectImageDialog(new SelectImageDialogListner() {

					@Override
					public void onPhoneGallery() {
						if (IjoomerApplicationConfiguration.isUploadMultiplePhotos()) {
							Intent intent = new Intent(JomAlbumsDetailsActivity.this, IjoomerPhotoGalaryActivity.class);
							startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
						} else {
							Intent intent = new Intent();
							intent.setType("image/*");
							intent.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
						}
					}

					@Override
					public void onCapture() {
						final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
						startActivityForResult(intent, TAKE_IMAGE);
					}
				});
			}
		});

		txtAlbumEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (lnrCreateAlbum.getVisibility() == View.GONE) {
					lnrAlbumDetail.setVisibility(View.GONE);
					lnrPhotoList.setVisibility(View.GONE);
					lnrAlbumWriteComment.setVisibility(View.GONE);
					lnrCreateAlbum.setVisibility(View.VISIBLE);
				} else {
					lnrAlbumDetail.setVisibility(View.VISIBLE);
					lnrPhotoList.setVisibility(View.VISIBLE);
					lnrAlbumWriteComment.setVisibility(View.VISIBLE);
					lnrCreateAlbum.setVisibility(View.GONE);
				}
				lstAlbumComment.setSelection(0);

			}
		});

		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lnrAlbumDetail.setVisibility(View.VISIBLE);
				lnrCreateAlbum.setVisibility(View.GONE);
				lnrPhotoList.setVisibility(View.VISIBLE);
				lnrAlbumWriteComment.setVisibility(View.VISIBLE);
			}
		});

		txtAlbumShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					loadNew(IjoomerShareActivity.class, JomAlbumsDetailsActivity.this, false, "IN_SHARE_CAPTION", IN_ALBUM.get(NAME), "IN_SHARE_DESCRIPTION",
							IN_ALBUM.get(DESCRIPTION), "IN_SHARE_THUMB", IN_ALBUM.get(THUMB), "IN_SHARE_SHARELINK", IN_ALBUM.get(SHARELINK));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		txtAlbumLikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (IN_ALBUM.get(LIKED).equals("1")) {
					txtAlbumLikeCount.setClickable(false);
					provider.unlikeAlbum(IN_ALBUM.get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IN_ALBUM.put(LIKED, "0");
								IN_ALBUM.put(LIKES, String.valueOf(Integer.parseInt(IN_ALBUM.get(LIKES)) - 1));
								txtAlbumLikeCount.setText(IN_ALBUM.get(LIKES));
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtAlbumLikeCount.setClickable(true);
						}
					});
				} else {
					txtAlbumLikeCount.setClickable(false);
					provider.likeAlbum(IN_ALBUM.get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IN_ALBUM.put(LIKED, "1");
								IN_ALBUM.put(LIKES, String.valueOf(Integer.parseInt(IN_ALBUM.get(LIKES)) + 1));
								txtAlbumLikeCount.setText(IN_ALBUM.get(LIKES));
								if (IN_ALBUM.get(DISLIKED).equals("1")) {
									IN_ALBUM.put(DISLIKES, String.valueOf(Integer.parseInt(IN_ALBUM.get(DISLIKES)) - 1));
									IN_ALBUM.put(DISLIKED, "0");
									txtAlbumDislikeCount.setText(IN_ALBUM.get(DISLIKES));
								}
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtAlbumLikeCount.setClickable(true);
						}
					});
				}
			}
		});

		txtAlbumDislikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (IN_ALBUM.get(DISLIKED).equals("1")) {
					txtAlbumDislikeCount.setClickable(false);
					provider.unlikeAlbum(IN_ALBUM.get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IN_ALBUM.put(DISLIKED, "0");
								IN_ALBUM.put(DISLIKES, String.valueOf(Integer.parseInt(IN_ALBUM.get(DISLIKES)) - 1));
								txtAlbumDislikeCount.setText(IN_ALBUM.get(DISLIKES));
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtAlbumDislikeCount.setClickable(true);
						}
					});
				} else {
					txtAlbumDislikeCount.setClickable(false);
					provider.dislikeAlbum(IN_ALBUM.get(ID), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {

						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IN_ALBUM.put(DISLIKED, "1");
								IN_ALBUM.put(DISLIKES, String.valueOf(Integer.parseInt(IN_ALBUM.get(DISLIKES)) + 1));
								txtAlbumDislikeCount.setText(IN_ALBUM.get(DISLIKES));
								if (IN_ALBUM.get(LIKED).equals("1")) {
									IN_ALBUM.put(LIKES, String.valueOf(Integer.parseInt(IN_ALBUM.get(LIKES)) - 1));
									IN_ALBUM.put(LIKED, "0");
									txtAlbumLikeCount.setText(IN_ALBUM.get(LIKES));
								}
							} else {
								responseErrorMessageHandler(responseCode, false);
							}
							txtAlbumDislikeCount.setClickable(true);
						}
					});
				}
			}
		});

		txtAlbumRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IjoomerUtilities.getCustomConfirmDialog(getString(R.string.album_title_remove), getString(R.string.are_you_sure), getString(R.string.yes), getString(R.string.no),
						new CustomAlertMagnatic() {

					@Override
					public void PositiveMethod() {
						provider.removeAlbum(IN_ALBUM.get(ID), new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {

							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									updateHeader(provider.getNotificationData());
									IjoomerApplicationConfiguration.setReloadRequired(true);
									finish();
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					}

					@Override
					public void NegativeMethod() {

					}
				});

			}
		});

		lstAlbumComment.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 2) {
					if (!commentProvider.isCalling() && commentProvider.hasNextPage()) {
						listFooterVisible();
						commentProvider.getAlbumCommentList(IN_ALBUM.get(ID), new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {

							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								listFooterInvisible();
								if (responseCode == 200) {
									updateHeader(commentProvider.getNotificationData());
									prepareList(data1, true);
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
							}
						});
					}
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case PICK_IMAGE_MULTIPLE:

				if(IjoomerGlobalConfiguration.isEnableVoice()){
					IjoomerUtilities.getConfirmDialog(getString(R.string.upload_photo), getString(R.string.photo_uploaded_with_voice), getString(R.string.yes), getString(R.string.no),
							false, new AlertMagnatic() {

						@Override
						public void PositiveMethod(DialogInterface dialog, int id) {
							try {
								loadNew(JomUplodPhotosActivity.class, JomAlbumsDetailsActivity.this, false, "IN_PHOTOS_PATHS", data.getStringExtra("data").split("\\|"),
										"IN_ALBUM_ID", IN_ALBUM.get(ID));
							} catch (Exception e) {
							}
						}

						@Override
						public void NegativeMethod(DialogInterface dialog, int id) {
							startUploadPhoto( data.getStringExtra("data").split("\\|"), IN_ALBUM.get(ID));
						}
					});
				}else{
					startUploadPhoto( data.getStringExtra("data").split("\\|"), IN_ALBUM.get(ID));
				}

				break;
			case PICK_IMAGE:
				
				if(IjoomerGlobalConfiguration.isEnableVoice()){
					IjoomerUtilities.getConfirmDialog(getString(R.string.upload_photo), getString(R.string.photo_uploaded_with_voice), getString(R.string.yes), getString(R.string.no),
							false, new AlertMagnatic() {

						@Override
						public void PositiveMethod(DialogInterface dialog, int id) {
							try {
								loadNew(JomUplodPhotosActivity.class, JomAlbumsDetailsActivity.this, false, "IN_PHOTOS_PATHS",
										(getAbsolutePath(data.getData()) + "|").split("\\|"), "IN_ALBUM_ID", IN_ALBUM.get(ID));
							} catch (Exception e) {
							}
						}

						@Override
						public void NegativeMethod(DialogInterface dialog, int id) {
							startUploadPhoto((getAbsolutePath(data.getData()) + "|").split("\\|"), IN_ALBUM.get(ID));
						}
					});	
				}else{
					startUploadPhoto((getAbsolutePath(data.getData()) + "|").split("\\|"), IN_ALBUM.get(ID));
				}
				
				break;
			case TAKE_IMAGE:
				
				if(IjoomerGlobalConfiguration.isEnableVoice()){
					IjoomerUtilities.getConfirmDialog(getString(R.string.upload_photo), getString(R.string.photo_uploaded_with_voice), getString(R.string.yes), getString(R.string.no),
							false, new AlertMagnatic() {

						@Override
						public void PositiveMethod(DialogInterface dialog, int id) {
							try {
								loadNew(JomUplodPhotosActivity.class, JomAlbumsDetailsActivity.this, false, "IN_PHOTOS_PATHS", (getImagePath() + "|").split("\\|"),
										"IN_ALBUM_ID", IN_ALBUM.get(ID));
							} catch (Exception e) {
							}
						}

						@Override
						public void NegativeMethod(DialogInterface dialog, int id) {
							startUploadPhoto((getImagePath() + "|").split("\\|"), IN_ALBUM.get(ID));
						}
					});
				}else{
					startUploadPhoto((getImagePath() + "|").split("\\|"), IN_ALBUM.get(ID));
				}

				break;
			case GET_ADDRESS_FROM_MAP:
				edtAlbumLocation.setText(((HashMap<String, String>) data.getSerializableExtra("MAP_ADDRESSS_DATA")).get("address"));
				break;
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {

	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to get inent data.
	 */
	@SuppressWarnings("unchecked")
	private void getIntentData() {
		IN_ALBUM = ((HashMap<String, String>) getIntent().getSerializableExtra("IN_ALBUM")) == null ? new HashMap<String, String>() : ((HashMap<String, String>) getIntent()
				.getSerializableExtra("IN_ALBUM"));
		IN_USERID = getIntent().getStringExtra("IN_USERID") == null ? "0" : getIntent().getStringExtra("IN_USERID");
		IN_GROUP_ID = getIntent().getStringExtra("IN_GROUP_ID") == null ? "0" : getIntent().getStringExtra("IN_GROUP_ID");
		IN_PROFILE_COVER = getIntent().getStringExtra("IN_PROFILE_COVER") == null ? "0" : getIntent().getStringExtra("IN_PROFILE_COVER");
		PHOTO_COUNT = IN_ALBUM.get(COUNT) == null ? 0 : Integer.parseInt(IN_ALBUM.get(COUNT));
		IN_GROUP_UPLOAD_PHOTO = getIntent().getStringExtra("IN_GROUP_UPLOAD_PHOTO") == null ? "0" : getIntent().getStringExtra("IN_GROUP_UPLOAD_PHOTO");

		setIntentAlbumDetails();
		if(IN_PROFILE_COVER.equals("0")){
			getComment();
		}
	}

	/**
	 * This method used to set album details form intent data.
	 */
	private void setIntentAlbumDetails() {

		if (IN_ALBUM.get(DELETEALLOWED).equals("1")) {
			txtAlbumRemove.setVisibility(View.VISIBLE);
			txtAlbumEdit.setVisibility(View.VISIBLE);
			txtAlbumUploadPhoto.setVisibility(View.VISIBLE);
		}

		if (IN_ALBUM.containsKey(EDITALBUM) && IN_ALBUM.get(EDITALBUM).equals("1")) {
			txtAlbumEdit.setVisibility(View.VISIBLE);
		}
		if (IN_GROUP_UPLOAD_PHOTO.equals("1")) {
			txtAlbumUploadPhoto.setVisibility(View.VISIBLE);
		}

		if (txtAlbumRemove.getVisibility() == View.GONE && txtAlbumEdit.getVisibility() == View.GONE && txtAlbumUploadPhoto.getVisibility() == View.GONE) {
			lnrHeader.setVisibility(View.GONE);
		}

		androidQuery.id(imgAlbumAvatar).image(IN_ALBUM.get(THUMB), true, true, getDeviceWidth(), 0);
		txAlbumBy.setMovementMethod(LinkMovementMethod.getInstance());
		txAlbumBy.setText(addClickablePart(Html.fromHtml(String.format(getString(R.string.by), IN_ALBUM.get(USER_NAME))), IN_ALBUM), BufferType.SPANNABLE);
		setAlbumDetails();

		txtAlbumLikeCount.setText(IN_ALBUM.get(LIKES));
		txtAlbumDislikeCount.setText(IN_ALBUM.get(DISLIKES));
		txtAlbumCommentCount.setText(IN_ALBUM.get(COMMENTCOUNT));

		edtAlbumName.setText(IN_ALBUM.get(NAME));

	}

	/**
	 * This method used to get album comment.
	 */
	private void getComment() {
		commentProvider.restorePagingSettings();
		commentProvider.getAlbumCommentList(IN_ALBUM.get(ID), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					updateHeader(commentProvider.getNotificationData());
					prepareList(data1, false);
					commentAdapter = getListAdapter();
					if (!IN_PROFILE_COVER.equals("0")) {
						lstAlbumComment.setAdapter(null);
					} else {
						lstAlbumComment.setAdapter(commentAdapter);
					}

				} else {
					if (responseCode != 204) {
						responseErrorMessageHandler(responseCode, false);
					}
				}
			}
		});
	}

	/**
	 * This method used to set album details for editing.
	 */
	private void setAlbumDetails() {

		edtAlbumName.setText(IN_ALBUM.get(NAME));
		edtAlbumLocation.setText(IN_ALBUM.get(LOCATION));
		edtAlbumDescription.setText(IN_ALBUM.get(DESCRIPTION));
		spnWhoCanSee.setSelection(getPrivacyIndex(IN_ALBUM.get(PERMISSION)));

		txtAlbumTitle.setText(IN_ALBUM.get(NAME));
		if (IN_ALBUM.get(LOCATION).trim().length() <= 0) {
			txtAlbumDateLocation.setText(IN_ALBUM.get(DATE));
		} else {
			txtAlbumDateLocation.setText(IN_ALBUM.get(DATE) + " @ " + IN_ALBUM.get(LOCATION));
		}
		txtAlbumPrivacy.setText(getPrivacyString(IN_ALBUM.get(PERMISSION)));
		txtAlbumDescription.setText(IN_ALBUM.get(DESCRIPTION));

	}

	/**
	 * This method used to save edited album details.
	 */
	private void saveAlbumDetails() {

		IN_ALBUM.put(NAME, edtAlbumName.getText().toString().trim());
		IN_ALBUM.put(LOCATION, edtAlbumLocation.getText().toString().trim());
		IN_ALBUM.put(DESCRIPTION, edtAlbumDescription.getText().toString().trim());
		IN_ALBUM.put(PERMISSION, spnWhoCanSee.getSelectedItem().toString());
		setAlbumDetails();
	}

	/**
	 * This method used to calculate dynamic height for album photo grid view
	 * pager.
	 *
	 * @return
	 */
	public int calculateheight() {
		int calculateHeight;
		int totalCount = Integer.parseInt(IN_ALBUM.get(COUNT));
		if (totalCount < pageLimit) {
			calculateHeight = (((totalCount % 4 == 0 ? totalCount / 4 : totalCount / 4 + 1) * 270) / 3);

			return convertSizeToDeviceDependent(calculateHeight);

		}
		return convertSizeToDeviceDependent(270);
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
	 * This method used to check album details changed.
	 *
	 * @return represented {@link Boolean}
	 */
	private boolean isAlbumDataChanged() {
		boolean isChaged = false;
		if (!(edtAlbumName.getText().toString().equals(IN_ALBUM.get(NAME)) && edtAlbumDescription.getText().toString().equals(IN_ALBUM.get(DESCRIPTION))
				&& edtAlbumLocation.getText().toString().equals(IN_ALBUM.get(LOCATION)) && getPrivacyCode(spnWhoCanSee.getSelectedItem().toString()).equals(
						IN_ALBUM.get(PERMISSION)))) {
			isChaged = true;
		}
		return isChaged;
	}

	/**
	 * This method used to get photo from fragment stack.
	 *
	 * @return represented {@link JomPhotoFragment} list
	 */
	public ArrayList<JomPhotoFragment> getPhotoFragmetStack() {
		return photoFragmetStack;
	}

	/**
	 * This method used to prepare list album comment.
	 *
	 * @param data
	 *            represented album comment data
	 * @param append
	 *            represented append data
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listData.clear();
				recordCommentTotal = 0;
			}
			for (HashMap<String, String> hashMap : data) {
				if (getAudio(hashMap.get(COMMENT)) != null) {
					recordCommentTotal += 1;
				}
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_comment_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					commentAdapter.add(item);
				} else {
					listData.add(item);
				}
			}
			if (recordCommentTotal > 1 && IN_PROFILE_COVER.equals("0")) {
				lnrPlayRecordComment.setVisibility(View.VISIBLE);
				btnPlayAll.setCustomText(getString(R.string.play_all));
				txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
			}
		}
	}

	private void startUploadPhoto(final String[] paths, final String albumID) {

		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_uploading_photos));
		new JomGalleryDataProvider(this).uploadPhoto(null, null, paths, albumID, new WebCallListener() {

			@Override
			public void onProgressUpdate(final int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

				if (responseCode == 200) {
					if(IjoomerUtilities.mSmartAndroidActivity instanceof JomAlbumsActivity){
						IjoomerApplicationConfiguration.setReloadRequired(true);
						((JomAlbumsActivity)IjoomerUtilities.mSmartAndroidActivity).onResume();
					}else if(IjoomerUtilities.mSmartAndroidActivity instanceof JomAlbumsDetailsActivity){
						IjoomerApplicationConfiguration.setReloadRequired(true);
						PHOTO_COUNT += paths.length;
						((JomAlbumsDetailsActivity)IjoomerUtilities.mSmartAndroidActivity).onResume();
					}else if(IjoomerUtilities.mSmartAndroidActivity instanceof JomPhotoDetailsActivity || IjoomerUtilities.mSmartAndroidActivity instanceof JomPhotoTagActivity){
						IjoomerApplicationConfiguration.setReloadRequired(true);
						PHOTO_COUNT += paths.length;
					}
				} else {
					if (errorMessage != null && errorMessage.length() > 0) {
						IjoomerUtilities.getCustomOkDialog(getString(R.string.upload_photo), errorMessage, getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

							@Override
							public void NeutralMethod() {
							}
						});
					} else {
						IjoomerUtilities.getCustomOkDialog(getString(R.string.upload_photo), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

							@Override
							public void NeutralMethod() {
							}
						});
					}

				}
			}
		});

	}

	/**
	 * This method used to shown response message.
	 *
	 * @param responseCode
	 *            represented response code
	 * @param finishActivityOnConnectionProblem
	 *            represented finish activity on connection problem
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.photo), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {

			}
		});

	}

	/**
	 * List adapter for album comment.
	 */

	private SmartListAdapterWithHolder getListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(JomAlbumsDetailsActivity.this, R.layout.jom_comment_list_item, listData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
				holder.imgCommentUserAvatar = (ImageView) v.findViewById(R.id.imgCommentUserAvatar);
				holder.txtCommentUserName = (IjoomerTextView) v.findViewById(R.id.txtCommentUserName);
				holder.txtCommentDate = (IjoomerTextView) v.findViewById(R.id.txtCommentDate);
				holder.txtCommentTitle = (IjoomerTextView) v.findViewById(R.id.txtCommentTitle);
				holder.btnCommentRemove = (IjoomerButton) v.findViewById(R.id.btnCommentRemove);
				holder.btnPlayStopVoice = (IjoomerVoiceButton) v.findViewById(R.id.btnPlayStopVoice);
				holder.btnPlayStopVoice.setVisibility(View.GONE);
				holder.btnCommentRemove.setVisibility(View.GONE);
				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				androidQuery.id(holder.imgCommentUserAvatar).image(row.get(USER_AVATAR), true, true, getDeviceWidth(), 0);
				holder.txtCommentTitle.setText(getPlainText(row.get(COMMENT)));
				holder.txtCommentUserName.setText(row.get(USER_NAME));
				holder.txtCommentDate.setText(row.get(DATE));
				if (row.containsKey(DELETEALLOWED) && row.get(DELETEALLOWED).equals("1")) {
					holder.btnCommentRemove.setVisibility(View.VISIBLE);
				}

				if (getAudio(row.get(COMMENT)) != null) {

					holder.btnPlayStopVoice.setVisibility(View.VISIBLE);
					holder.btnPlayStopVoice.setText(getAudioLength(row.get(COMMENT)));
					holder.btnPlayStopVoice.setAudioPath(getAudio(row.get(COMMENT)), false);
					holder.btnPlayStopVoice.setAudioListener(new AudioListener() {

						@Override
						public void onReportClicked() {
							reportVoice(getAudio(row.get(COMMENT)));
						}

						@Override
						public void onPrepared() {
						}

						@Override
						public void onPlayClicked(boolean isplaying) {
							btnPlayAll.setCustomText(getString(R.string.play_all));
							txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
							txtRecordUser.setVisibility(View.GONE);
							recordCommentCounter = recordCommentTotal;
						}

						@Override
						public void onComplete() {
						}
					});

				}

				holder.btnCommentRemove.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						IjoomerUtilities.getCustomConfirmDialog(getString(R.string.album), getString(R.string.are_you_sure), getString(R.string.yes), getString(R.string.no),
								new CustomAlertMagnatic() {

							@Override
							public void PositiveMethod() {
								provider.removeAlbumComment(row.get(ID), new WebCallListener() {
									final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

									@Override
									public void onProgressUpdate(int progressCount) {
										proSeekBar.setProgress(progressCount);
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										if (responseCode == 200) {
											updateHeader(provider.getNotificationData());
											commentAdapter.remove(commentAdapter.getItem(position));
											if (getAudio(row.get(COMMENT)) != null) {
												recordCommentTotal -= 1;
												txtTotalRecordComment.setText("(" + recordCommentTotal + ")");
												if (recordCommentTotal == 1) {
													lnrPlayRecordComment.setVisibility(View.GONE);
												}
											}
											IN_ALBUM.put(COMMENTCOUNT, String.valueOf(Integer.parseInt(IN_ALBUM.get(COMMENTCOUNT)) - 1));
											txtAlbumCommentCount.setText(IN_ALBUM.get(COMMENTCOUNT));
										} else {
											responseErrorMessageHandler(responseCode, false);
										}

									}
								});
							}

							@Override
							public void NegativeMethod() {

							}
						});

					}
				});

				holder.imgCommentUserAvatar.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						if (row.get(USER_PROFILE).equals("1")) {
							gotoProfile(row.get(USER_ID));
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

	/**
	 * Inner class
	 */

	private class PageAdapter extends FragmentStatePagerAdapter {

		private boolean isInitial = true;

		public PageAdapter(FragmentManager fm) {
			super(fm);
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int pos) {

			try {
				return photoFragmetStack.get(pos);
			} catch (Exception e) {
				JomPhotoFragment fragment = new JomPhotoFragment(pos + 1, IN_ALBUM, IN_USERID, IN_PROFILE_COVER);
				if (isInitial) {
					fragment.setInitial(true);
					isInitial = false;
				}
				photoFragmetStack.add(fragment);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			return ((PHOTO_COUNT % pageLimit) == 0 ? PHOTO_COUNT / pageLimit : (PHOTO_COUNT / pageLimit) + 1);
		}

	}

}
