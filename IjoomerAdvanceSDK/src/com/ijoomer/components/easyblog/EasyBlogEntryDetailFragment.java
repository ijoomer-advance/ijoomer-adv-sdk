package com.ijoomer.components.easyblog;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerWebviewClient;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerListView;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.easyblog.EasyBlogEntryDetailDataProvider;
import com.ijoomer.media.player.IjoomerMediaPlayer;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Fragment Contains All Method Related To EasyBlogEntryDetailFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("SimpleDateFormat")
public class EasyBlogEntryDetailFragment extends SmartFragment implements EasyBlogTagHolder, IjoomerSharedPreferences {

	private IjoomerListView lstEntryDetail;
	private LinearLayout lnrAddRating;
	private LinearLayout lnrAddComment;
	private IjoomerEditText edtComment;
	private IjoomerTextView txtPostedBy;
	private IjoomerTextView txtPostedOn;
	private IjoomerTextView txtTitle;
	private IjoomerTextView txtPageIndicator;
	private ImageView blogImg;
	private ImageView imgUserThumb;
	private IjoomerButton btnAddComment;
	private IjoomerButton btnSaveRating;
	private IjoomerRatingBar rtbBlog;
	private ProgressBar pbr;
	private WebView webViewBlog;
	private View headerView;
	private View listFooter;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> blogDetail;
	private EasyBlogEntryDetailDataProvider dataProvider;
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private AQuery androidQuery;
	private Context mContext;

	private String blogId;
	private String currentId;

	private int position, totalPages;

	/**
	 * Constructor
	 * 
	 * @param mContext
	 *            represented {@link android.content.Context}
	 * @param blogId
	 *            represented blog id
	 * @param position
	 *            represented blog position
	 * @param totalPages
	 *            represented blog total pages
	 */
	public EasyBlogEntryDetailFragment(Context mContext, String blogId, int position, int totalPages) {
		this.blogId = blogId;
		this.mContext = mContext;
		this.position = position;
		this.totalPages = totalPages;
	}

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.easyblog_entry_detail_listview;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void initComponents(View currentView) {

		lstEntryDetail = (IjoomerListView) currentView.findViewById(R.id.easyblogLstEntryDetail);
		pbr = (ProgressBar) currentView.findViewById(R.id.easyblogPbr);
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		listFooter = LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
		lstEntryDetail.addFooterView(listFooter, null, false);
		headerView = inflater.inflate(R.layout.easyblog_entry_detail_header, null, false);
		dataProvider = new EasyBlogEntryDetailDataProvider(mContext);
		lnrAddComment = (LinearLayout) currentView.findViewById(R.id.lnrAddComment);
		lnrAddRating = (LinearLayout) headerView.findViewById(R.id.lnrAddRating);
		txtPageIndicator = (IjoomerTextView) headerView.findViewById(R.id.easyblogTxtIndicator);
		imgUserThumb = (ImageView) headerView.findViewById(R.id.easyblogImgUserThumb);
		txtPostedOn = (IjoomerTextView) headerView.findViewById(R.id.easyblogTxtPostedOn);
		txtPostedBy = (IjoomerTextView) headerView.findViewById(R.id.easyblogTxtPostedBy);
		txtTitle = (IjoomerTextView) headerView.findViewById(R.id.easyblogTxtTitle);
		blogImg = (ImageView) headerView.findViewById(R.id.easyblogImg);
		btnAddComment = (IjoomerButton) currentView.findViewById(R.id.btnAddComment);
		btnSaveRating = (IjoomerButton) headerView.findViewById(R.id.btnSaveRating);
		edtComment = (IjoomerEditText) currentView.findViewById(R.id.edtComment);
		rtbBlog = (IjoomerRatingBar) headerView.findViewById(R.id.rtbBlog);
		androidQuery = new AQuery(mContext);

		webViewBlog = (WebView) headerView.findViewById(R.id.easyblogWebViewBlog);
		webViewBlog.setBackgroundColor(0);
		webViewBlog.getSettings().setJavaScriptEnabled(true);
		webViewBlog.getSettings().setPluginState(PluginState.ON);
		webViewBlog.setInitialScale(99);

	}

	@Override
	public void prepareViews(View currentView) {

		pbr.setVisibility(View.VISIBLE);
		txtPageIndicator.setText(position + " " + getString(R.string.of) + " " + totalPages);
		getBlogDetail(blogId);
		lstEntryDetail.addHeaderView(headerView);
		lstEntryDetail.setAdapter(null);
		lstEntryDetail.setSelectionAfterHeaderView();
		txtPageIndicator.setFocusable(true);
		getBlogComment();

	}

	@Override
	public void setActionListeners(View currentView) {
		btnAddComment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (edtComment.getText().toString().trim().length() > 0) {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
					dataProvider.addComment(currentId, edtComment.getText().toString().trim(), new WebCallListener() {
						@Override
						public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								IjoomerApplicationConfiguration.setReloadRequired(true);
								getBlogComment();
								edtComment.setText("");
							} else {
								IjoomerUtilities.getCustomOkDialog(getString(R.string.easy_blog_detail),
										getString(getResources().getIdentifier("code" + responseCode, "string", mContext.getPackageName())), getString(R.string.ok),
										R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

											@Override
											public void NeutralMethod() {
												if (responseCode == 599) {
													getActivity().onBackPressed();
												}
											}
										});
							}
						}

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);
						}
					});
				} else {
					edtComment.setText(getString(R.string.validation_value_required));
				}
			}
		});

		btnSaveRating.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
				dataProvider.addRating(currentId, String.valueOf(rtbBlog.getStarRating()), new WebCallListener() {
					@Override
					public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							IjoomerApplicationConfiguration.setReloadRequired(true);
							getBlogDetail(currentId);
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.easy_blog_detail),
									getString(getResources().getIdentifier("code" + responseCode, "string", mContext.getPackageName())), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											if (responseCode == 599) {
												getActivity().onBackPressed();
											}
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
		});
		lstEntryDetail.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 2) {
					if (!dataProvider.isCalling() && dataProvider.hasNextPage()) {
						listFooterVisible();
						dataProvider.getBlogComment(currentId, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
							}

							@Override
							public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								try {
									if (responseCode == 200) {
										prepareList(data1, true);
									} else {
										if (responseCode != 204) {
											IjoomerUtilities.getCustomOkDialog(getString(R.string.easy_blog_detail),
													getString(getResources().getIdentifier("code" + responseCode, "string", mContext.getPackageName())), getString(R.string.ok),
													R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

														@Override
														public void NeutralMethod() {
															if (responseCode == 599) {
																getActivity().onBackPressed();
															}
														}
													});
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						});
					}
				}
			}
		});
	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to get blog details.
	 * 
	 * @param id
	 *            represented blog id
	 */
	public void getBlogDetail(String id) {
		currentId = id;
		dataProvider.getBlogDetail(id, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						if (data1.get(0).get("id").equalsIgnoreCase(currentId)) {
							blogDetail = data1;
							prepareBlogDetail(data1);
							pbr.setVisibility(View.GONE);
						}
					} else {
						pbr.setVisibility(View.GONE);
						if (responseCode != 204) {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.easy_blog_detail),
									getString(getResources().getIdentifier("code" + responseCode, "string", mContext.getPackageName())), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											if (responseCode == 599) {
												getActivity().onBackPressed();
											}
										}
									});
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
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
	 * This method used to get blog comment.
	 */
	public void getBlogComment() {
		dataProvider.restorePagingSettings();
		dataProvider.getBlogComment(currentId, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						prepareList(data1, false);
						listAdapterWithHolder = getListAdapter();
						lstEntryDetail.setAdapter(listAdapterWithHolder);
					} else {
						if (responseCode != 204) {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.articles),
									getString(getResources().getIdentifier("code" + responseCode, "string", mContext.getPackageName())), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											if (responseCode == 599) {
												getActivity().onBackPressed();
											}
										}
									});
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * This method used to prepare blog details.
	 * 
	 * @param data
	 *            represented blog data
	 */
	public void prepareBlogDetail(ArrayList<HashMap<String, String>> data) {

		if (data != null) {

			try {

				txtTitle.setText(data.get(0).get(TITLE));

				try {
					if (data.get(0).get(IMAGE).toString().trim().length() > 0) {
						androidQuery.id(blogImg).image(data.get(0).get(IMAGE), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0, new BitmapAjaxCallback() {

							@Override
							protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
								super.callback(url, iv, bm, status);
								if (bm != null) {
									blogImg.setVisibility(View.VISIBLE);
									blogImg.setImageBitmap(bm);
								} else {
									blogImg.setVisibility(View.GONE);
								}
							}

						});
						blogImg.setVisibility(View.VISIBLE);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				androidQuery.id(imgUserThumb).image(data.get(0).get(CREATEDBYIMAGE), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.icms_article_default);

				if (data.get(0).get(CREATED) != null && data.get(0).get(CREATED).trim().length() > 0) {
					String dateStr = data.get(0).get(CREATED);

					SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date dateObj = curFormater.parse(dateStr);
					SimpleDateFormat postFormater = new SimpleDateFormat("dd-MMM-yyyy");
					txtPostedOn.setText(postFormater.format(dateObj));

				}
				rtbBlog.setStarRating(Float.parseFloat(data.get(0).get(RATINGVALUE)) / Float.parseFloat(data.get(0).get(RATINGTOTAL)));
				if (data.get(0).get(CREATEDBYNAME) != null && data.get(0).get(CREATEDBYNAME).trim().length() > 0) {
					txtPostedBy.setText(data.get(0).get(CREATEDBYNAME));

				}

				if (((IjoomerSuperMaster) getActivity()).getSmartApplication().readSharedPreferences().getString(SP_LOGIN_REQ_OBJECT, "").length() > 0) {
					if (data.get(0).get(ALLOWCOMMENT).equals("1")) {
						lnrAddComment.setVisibility(View.VISIBLE);
					} else {
						lnrAddComment.setVisibility(View.GONE);
					}

					if (data.get(0).get(ALLOWRATINGS).equals("1")) {
						lnrAddRating.setVisibility(View.VISIBLE);
						rtbBlog.setEditable(true);
						btnSaveRating.setVisibility(View.VISIBLE);
					} else {
						rtbBlog.setEditable(false);
						btnSaveRating.setVisibility(View.GONE);
					}
				} else {
					lnrAddComment.setVisibility(View.GONE);
					rtbBlog.setEditable(false);
					btnSaveRating.setVisibility(View.GONE);
					lnrAddRating.setVisibility(View.VISIBLE);
				}

				if (data.get(0).get(CONTENT).trim().length() > 0 || data.get(0).get(INTRO).trim().length() > 0) {
					webViewBlog.setWebViewClient(new WebViewClient() {

						@Override
						public void onPageFinished(WebView view, String url) {
							super.onPageFinished(view, url);
							lstEntryDetail.setSelectionAfterHeaderView();
						}

						@Override
						public boolean shouldOverrideUrlLoading(WebView view, String url) {
							Intent intent = new Intent(mContext, IjoomerWebviewClient.class);
							intent.putExtra("url", url);
							startActivity(intent);
							return true;
						}

						@Override
						public void onLoadResource(WebView view, String url) {
							try {

								if (url.contains("&video_id=")) {

									url = url.substring(url.indexOf("video_id="));
									url = url.substring(0, url.indexOf("&"));

									String video_id = url.split("=")[1];
									Intent lVideoIntent = new Intent(null, Uri.parse("ytv://" + video_id + ""), mContext, IjoomerMediaPlayer.class);
									startActivity(lVideoIntent);

									StringBuilder sb = new StringBuilder(); // StringBuilder();
									sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"weblayout.css\" /></HEAD><body>");
									String str = blogDetail.get(0).get(CONTENT).toString().trim() + blogDetail.get(0).get(INTRO).toString().trim();
									str = str.replaceAll("<iframe width=\"[0-9]*", "<iframe width=\"100\\%");
									str = str.replaceAll("<img[\\w]*", "<img height=\"auto\" style=\"max-width:100\\%\";");
									sb.append(str);
									sb.append("</body></HTML>");
									webViewBlog.loadDataWithBaseURL("file:///android_asset/css/", sb.toString(), "text/html", "utf-8", null);

								}
							} catch (Exception e) {

							}

							super.onLoadResource(view, url);
						}

					});

					StringBuilder sb = new StringBuilder(); // StringBuilder();
					sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"weblayout.css\" /></HEAD><body>");
					String str = blogDetail.get(0).get(CONTENT).toString().trim() + blogDetail.get(0).get(INTRO).toString().trim();
					str = str.replaceAll("<iframe width=\"[0-9]*", "<iframe width=\"100\\%");
					str = str.replaceAll("<img[\\w]*", "<img height=\"auto\" style=\"max-width:100\\%\";");
					sb.append(str);
					sb.append("</body></HTML>");
					webViewBlog.loadDataWithBaseURL("file:///android_asset/css/", sb.toString(), "text/html", "utf-8", null);
				} else {
					webViewBlog.setVisibility(View.GONE);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * This method used to prepare blog comment list.
	 * 
	 * @param data
	 *            represented comment data
	 * @param append
	 *            represented is comment data append.
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.easyblog_comment_list_item);
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
	 * List adapter for blog comment.
	 * 
	 * @return represented {@link com.smart.framework.SmartListAdapterWithHolder}
	 */
	private SmartListAdapterWithHolder getListAdapter() {
		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.easyblog_comment_list_item, listData, new ItemView() {
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.easyblogimgCommentUserAvatar = (ImageView) v.findViewById(R.id.easyblogimgCommentUserAvatar);
				holder.easyblogtxtCommentUserName = (IjoomerTextView) v.findViewById(R.id.easyblogtxtCommentUserName);
				holder.easyblogtxtCommentDate = (IjoomerTextView) v.findViewById(R.id.easyblogtxtCommentDate);
				holder.easyblogtxtCommentTitle = (IjoomerTextView) v.findViewById(R.id.easyblogtxtCommentTitle);
				holder.easyblogbtnCommentRemove = (IjoomerButton) v.findViewById(R.id.easyblogbtnCommentRemove);
				holder.easyblogbtnCommentEdit = (IjoomerButton) v.findViewById(R.id.easyblogbtnCommentEdit);
				holder.easyblogReplay = (IjoomerTextView) v.findViewById(R.id.easyblogReplay);
				holder.easyblogLike = (IjoomerTextView) v.findViewById(R.id.easyblogLike);
				holder.easyblogUnlike = (IjoomerTextView) v.findViewById(R.id.easyblogUnlike);
				holder.easyblogtxtCommentLikeCount = (IjoomerTextView) v.findViewById(R.id.easyblogtxtCommentLikeCount);
				holder.easyblogtxtReplayCount = (IjoomerTextView) v.findViewById(R.id.easyblogtxtReplayCount);
                holder.lnrCommentEdit = (LinearLayout) v.findViewById(R.id.lnrCommentEdit);
                holder.edtCommentEdit = (IjoomerEditText) v.findViewById(R.id.edtCommentEdit);
                holder.btnCommentEditSave = (IjoomerButton) v.findViewById(R.id.btnCommentEditSave);
                holder.btnCommentEditCancel = (IjoomerButton) v.findViewById(R.id.btnCommentEditCancel);

				@SuppressWarnings("unchecked")
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				androidQuery.id(holder.easyblogimgCommentUserAvatar).image(row.get(CREATEDBYIMAGE), true, true);
				holder.easyblogtxtCommentUserName.setText(row.get(CREATEDBYNAME));
				holder.easyblogtxtCommentDate.setText(row.get(CREATED));
				holder.easyblogtxtCommentTitle.setText(row.get(COMMENT));

//                if(row.get(ISEDITED).equals("1")){
//                    holder.easyblogbtnCommentEdit.setVisibility(View.VISIBLE);
//                }else{
//                    holder.easyblogbtnCommentEdit.setVisibility(View.GONE);
//                }
//
//                if(row.get(ALLOWDELETE).equals("1")){
//                    holder.easyblogbtnCommentRemove.setVisibility(View.VISIBLE);
//                }else{
//                    holder.easyblogbtnCommentRemove.setVisibility(View.GONE);
//                }
//
//                if(row.get(LIKESTATUS).equals("1")){
//                    if(row.get(LIKE).equals("1")){
//                        holder.easyblogUnlike.setVisibility(View.VISIBLE);
//                    }else{
//                        holder.easyblogLike.setVisibility(View.VISIBLE);
//                    }
//                }else{
//                    holder.easyblogLike.setVisibility(View.GONE);
//                    holder.easyblogUnlike.setVisibility(View.GONE);
//                }
//
//                holder.easyblogbtnCommentEdit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(holder.lnrCommentEdit.getVisibility() == View.VISIBLE){
//                            holder.easyblogtxtCommentTitle.setVisibility(View.VISIBLE);
//                            holder.lnrCommentEdit.setVisibility(View.GONE);
//                        }else{
//                            holder.edtCommentEdit.setText(holder.easyblogtxtCommentTitle.getText());
//                            holder.easyblogtxtCommentTitle.setVisibility(View.GONE);
//                            holder.lnrCommentEdit.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//
//                holder.btnCommentEditCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        holder.easyblogtxtCommentTitle.setVisibility(View.VISIBLE);
//                        holder.lnrCommentEdit.setVisibility(View.GONE);
//                    }
//                });
//
//                holder.btnCommentEditSave.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//                holder.easyblogbtnCommentRemove.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        IjoomerUtilities.getCustomConfirmDialog(getString(R.string.easy_blog_comment_remove), getString(R.string.are_you_sure), getString(R.string.yes),
//                                getString(R.string.no), new CustomAlertMagnatic() {
//
//                            @Override
//                            public void PositiveMethod() {
//                            }
//
//                            @Override
//                            public void NegativeMethod() {
//
//                            }
//                        });
//
//                    }
//                });
//
//                holder.easyblogReplay.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//                holder.easyblogLike.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//                holder.easyblogUnlike.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//                holder.easyblogtxtCommentLikeCount.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//                holder.easyblogtxtReplayCount.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });

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
