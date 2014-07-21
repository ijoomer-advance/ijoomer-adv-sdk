package com.ijoomer.common.classes;

import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.IjoomerVoiceButton;

/**
 * This Class Contains All Method Related To ViewHolder.
 * 
 * @author tasol
 * 
 */
public class ViewHolder {

	public Integer efficientFlag = 0;

	/*
	 * Dynamic view
	 */

	public LinearLayout lnrGgroup;
	public LinearLayout lnrLabel;
	public LinearLayout lnrEdit;
	public LinearLayout lnrEditArea;
	public LinearLayout lnrEditClickable;
	public LinearLayout lnrSpin;
	public LinearLayout lnrIjoomerCheckBox;
	public LinearLayout lnrComplex;

	/*
	 * Voice Message And Text
	 */
	public IjoomerVoiceButton btnPlayStopVoice;
	/*
	 * Ijoomer Map Address
	 */
	public IjoomerTextView txtMapAddressData;

	/*
	 * Jom message details list item
	 */

	public LinearLayout lnrSent;
	public ImageView imgSentUser;
	public IjoomerTextView txtSentMessage;
	public IjoomerTextView txtSentMessageDate;
	public IjoomerButton btnSentMessageRemove;
	public IjoomerVoiceButton btnSentMessagePlayVoice;

	public LinearLayout lnrReceive;
	public ImageView imgReceiveUser;
	public IjoomerTextView txtReceiveMessage;
	public IjoomerTextView txtReceiveMessageDate;
	public IjoomerButton btnReceiveMessageRemove;
	public IjoomerVoiceButton btnReceiveMessagePlayVoice;

	/*
	 * Jom message list
	 */

	public LinearLayout lnrMessage;
	public ImageView imgMessageUser;
	public IjoomerTextView txtMessageUserName;
	public IjoomerTextView txtMessageSubject;
	public IjoomerTextView txtMessageDate;
	public IjoomerButton btnMessageRemove;
	public ImageView imgMessageOutgoing;
	public ImageView imgMessageIncoming;

	/*
	 * Group Member list
	 */

	public ImageView imgGroupMemberAvatar;
	public IjoomerTextView txtGroupMemberName;
	public IjoomerButton btnGroupMemberRemove;
	public ImageView imgGroupMemberOnlineStatus;
	public LinearLayout lnrGroupMemberRemove;
	public IjoomerTextView txtGroupMemberRemove;
	public IjoomerCheckBox chbGroupMemberBlock;
	public IjoomerButton btnGroupMemberYes;
	public IjoomerButton btnGroupMemberNo;
	public IjoomerTextView txtGroupMemberSetAsAdmin;
	public IjoomerTextView txtGroupMemberSetAsUser;
	public IjoomerTextView txtGroupMemberSetAsBan;
	public IjoomerTextView txtGroupMemberSetAsUnban;
	public IjoomerTextView txtGroupMemberApproval;

	/*
	 * Group File List
	 */
	public IjoomerTextView txtFileTitle;
	public IjoomerTextView txtFileHit;
	public IjoomerTextView txtFileSize;
	public IjoomerButton btnFileRemove;
	public IjoomerTextView txtFileDesc;
	/*
	 * Group Discussion Replies List
	 */
	public ImageView imgDiscussionRepliesAvatar;
	public IjoomerTextView txtDiscussionRepliesUser;
	public IjoomerTextView txtDiscussionRepliesTitle;
	public IjoomerTextView txtDiscussionRepliesDate;
	public IjoomerTextView txtDiscussionRepliesEdit;
	public ImageView imgDiscussionRepliesRemove;

	/*
	 * Group Discussion List
	 */

	public IjoomerTextView txtDiscussionTitle;
	public IjoomerTextView txtDiscussionStartedBy;
	public IjoomerTextView txtDiscussionReplies;

	/*
	 * Group Announcement List
	 */
	public IjoomerTextView txtAnnouncementTitle;
	public IjoomerTextView txtAnnouncementStartedBy;
	public IjoomerTextView txtAnnouncementStartedOn;

	/*
	 * Group List
	 */
	public LinearLayout lnrGroupItem;
	public ImageView imgGroupAvatar;
	public IjoomerTextView txtGroupTitle;
	public IjoomerTextView txtGroupDescription;
	public IjoomerTextView txtGroupMember;
	public IjoomerTextView txtGroupDiscussion;
	public IjoomerTextView txtGroupWallPost;
	public LinearLayout lnrGroupPending;
	public IjoomerTextView txtGroupPendingAccept;
	public IjoomerTextView txtGroupPendingReject;

	/*
	 * Event Wall List
	 */
	public ImageView imgEventWallUserAvatar;
	public IjoomerTextView txtEventWallUserName;
	public IjoomerTextView txtEventWallDate;
	public IjoomerButton btnEventWallRemove;
	public IjoomerTextView txtEventWallTitle;
	public IjoomerTextView txtEventWallLike;
	public IjoomerTextView txtEventWallComment;
	public IjoomerTextView txtEventWallLikeCount;
	public IjoomerTextView txtEventWallCommentCount;
	public LinearLayout lnrEventWallLikeCommnet;
	public LinearLayout lnrEventWallWriteComment;
	public IjoomerEditText edtEventWallWriteComment;
	public IjoomerButton btnEventWallWriteCommentSend;
	/*
	 * Event Guest List
	 */

	public ImageView imgEventGuestAvatar;
	public IjoomerTextView txtEventGuestName;
	public IjoomerButton btnEventGuestRemove;
	public ImageView imgEventGuestOnlineStatus;
	public LinearLayout lnrEventGuestRemove;
	public IjoomerTextView txtEventGuestRemove;
	public IjoomerCheckBox chbEventGuestBock;
	public IjoomerButton btnEventGuestYes;
	public IjoomerButton btnEventGuestNo;
	public IjoomerTextView txtEventGuestSetAsAdmin;
	public IjoomerTextView txtEventGuestSetAsMember;
	public IjoomerTextView txtEventGuestApproval;

	/*
	 * Filter list
	 */
	public IjoomerTextView txtFilterString;
	public IjoomerRadioButton rdbSelectItem;
	public IjoomerCheckBox chkSelectItem;

	/*
	 * Filtering Map Address Dialog List
	 */

	public IjoomerTextView txtMapAddress;
	/*
	 * Event List
	 */
	public LinearLayout lnrEventList;
	public ImageView imgEventAvatar;
	public IjoomerTextView txtEventDate;
	public IjoomerTextView txtEventTitle;
	public IjoomerTextView txtEventStatus;
	public IjoomerTextView txEventLocation;
	public IjoomerTextView txtEventStartEndDate;
	public IjoomerTextView txtEventGuestAttendingCount;
	public LinearLayout lnrEventPending;
	public IjoomerTextView txtEventPendingAccept;
	public IjoomerTextView txtEventPendingReject;

	/*
	 * Video List
	 */

	public ImageView imgVideoAvatar;
	public ImageView imgVideoArrow;
	public IjoomerTextView txtVideoTitle;
	public IjoomerTextView txtVideoBy;
	public IjoomerTextView txtVideoDateLocation;
	public IjoomerTextView txtVideoLikeCount;
	public IjoomerTextView txtVideoDislikeCount;
	public IjoomerTextView txtVideoCommentCount;
	public IjoomerTextView txtVideoShare;

	/*
	 * Jom Photo/Video Tag List
	 */
	public IjoomerTextView txtPhotoTagUser;
	public IjoomerButton btnPhotoTag;
	public IjoomerButton btnRemovePhotoTag;

	/*
	 * Ijoomer Contact Mail Dialog List
	 */
	public ImageView imgContactUser;
	public IjoomerTextView txtContactName;
	public IjoomerTextView txtContactEmail;
	public IjoomerCheckBox chbContact;
	/*
	 * Jom Albums List
	 */

	public ImageView imgAlbumAvatar;
	public IjoomerTextView txtAlbumTitle;
	public IjoomerTextView txtAlbumPhotoCount;
	public IjoomerTextView txtAlbumDateLocation;
	public IjoomerTextView txAlbumBy;
	public ImageView imgArrow;

	/*
	 * Album Photo List Item
	 */
	public ImageView imgAlbumphoto;

	/*
	 * Jom Comment List
	 */
	public ImageView imgCommentUserAvatar;
	public IjoomerTextView txtCommentUserName;
	public IjoomerTextView txtCommentDate;
	public IjoomerButton btnCommentRemove;
	public IjoomerTextView txtCommentTitle;

	/*
	 * Jom Wall/Activity List
	 */
	public ImageView imgWallOrActvityUserAvatar;
	public ImageView imgWallOrActvityUploadedPhotos;
	public ImageView imgWallOrActvityCoverPhoto;
	public ImageView imgWallOrActvityContentImage;
	public LinearLayout lnrContentImageScrollable;

	public ImageView imgWallOrActvityContentVideoImage;

	public IjoomerTextView txtWallOrActvityTitle;
	public IjoomerTextView txtWallOrActvityUserName;
	public IjoomerTextView txtWallOrActvityDate;
	public IjoomerTextView txtWallOrActivityLike;
	public IjoomerTextView txtWallOrActivityComment;
	public IjoomerTextView txtWallOrActivityLikeCount;
	public IjoomerTextView txtWallOrActivityCommentCount;
	public IjoomerTextView txtWallOrActvityContent;

	public RelativeLayout lnrWallOrActivityContentCoverPhoto;
	public LinearLayout lnrWallOrActivityContentImage;
	public LinearLayout lnrWallOrActivityContentVideo;
	public LinearLayout lnrWallOrActivityContentView;
	public LinearLayout lnrWallOrActivityWriteComment;
	public LinearLayout lnrWallOrActivityLikeCommnet;
	public IjoomerButton btnWallOrActivityRemove;

	/*
	 * Jom Friend/Member List
	 */
	public IjoomerTextView friendmembertxtName;
	public ImageView friendmemberimgOnlineStatus;
	public IjoomerTextView friendmembertxtHeading;
	public ImageView friendmemberImage;
	public LinearLayout friendmemberlnrName;
	public LinearLayout friendmemberlnrHeading;
	public IjoomerCheckBox chkSelectFriend;
	public IjoomerTextView txtInvited;

	/*
	 * Jom Notification Friend Request List
	 */
	public LinearLayout lnrFriendRequest;
	public ImageView imgFriendRequestUser;
	public IjoomerTextView txtFriendRequestUserName;
	public IjoomerTextView txtFriendRequestMessage;
	public IjoomerButton btnFriendRequestAccept;
	public IjoomerButton btnFriendRequestReject;

	/*
	 * Jom Notification Message List
	 */
	public LinearLayout lnrNotificationMessage;
	public ImageView imgNotificationMessageUser;
	public IjoomerTextView txtNotificationMessageUserName;
	public IjoomerTextView txtNotificationMessageMessage;
	public IjoomerTextView txtNotificationMessageDate;

	/*
	 * Jom Notification Group List
	 */
	public LinearLayout lnrGlobal;
	public ImageView imgGlobalNotificationMessageUser;
	public IjoomerTextView txtGlobalNotificationMessagetitle;
	public IjoomerTextView txtGlobalNotificationMessageDate;

	/*
	 * Icms Article and Category Blog List item
	 */

	public IjoomerTextView icmsTxtTitle;
	public IjoomerTextView icmsTxtIntro;
	public ImageView icmsImageThumb;
	public ImageView icmsCatImageThumb;
	public IjoomerTextView icmsTxtArticlesCount;
	public IjoomerTextView icmsCatTxtArticlesCount;
	public IjoomerTextView icmsCatTxtTitle;
	public LinearLayout icmsLnrCatListItem;
	public LinearLayout icmsLnrArtListItem;
	public LinearLayout icmsLnrBlogListItem;
	public ImageView icmsCatDivider;
	public ImageView icmsCatArticleSeparator;
	public IjoomerButton icmsArticleRemove;
	public LinearLayout icmsLnrArticle;

	/*
	 * Ijoomer Side Menu
	 */
	public ImageView imgMenuItemicon;
	public IjoomerTextView txtMenuItemCaption;

	/*
	 * Advance Search
	 */

	public IjoomerTextView txtCriteriaName;
	public IjoomerTextView tvCondition;
	public IjoomerTextView tvValue;
	public ImageView btnAddCriteria;
	public RelativeLayout rvTitleRow;


	/*
	 * Plugins Vimeo Videos
	 */

	public IjoomerTextView txtPlaylistVimeoTitle;
	public ImageView imgPlaylistVimeoThumb;

	/*
	 * Plugins Youtube Videos
	 */

	public ImageView imgVideoThumb;
	public IjoomerTextView txtVideoDuration;
	public IjoomerTextView txtVideoViews;
	public IjoomerTextView txtVideoDescription;
	public ImageView imgPlaylistThumb;
	public IjoomerTextView txtVideoCount;
	public IjoomerTextView txtVideoLabel;
	public IjoomerTextView txtPlaylistTitle;
	public IjoomerTextView txtPlaylistCreatedOn;

	/*
	 * Sobipro Section Categories
	 */
	public ImageView sobiproImgCategories;
	public IjoomerTextView sobiproTxtCategoriesCaption;
	public LinearLayout sobiproGridItemLayout;

	/*
	 * Sobipro Entries
	 */
	public ImageView imgEntryIcon;
	public LinearLayout lnrEntry;
	public IjoomerButton btnEntryRemove;
	public IjoomerTextView txtTitle;
	public IjoomerTextView txtReview;
	public IjoomerTextView txtAddress;
	public IjoomerTextView txtDistance;
	public IjoomerTextView txtPrice;
	public IjoomerRatingBar rtbRating;
	public IjoomerTextView txtCaption;
	public IjoomerTextView txtValue;
	public LinearLayout lnrAbout;
	public LinearLayout lnrRatingCriteria;
	public IjoomerTextView txtPositiveReview;
	public IjoomerTextView txtNegativeReview;
	public IjoomerTextView txtGoodFor;
	public ImageView imgSeparator;
	public LinearLayout lnrPros;
	public LinearLayout lnrCons;
	public IjoomerTextView txtProsTitle;
	public IjoomerTextView txtConsTitle;
	public LinearLayout lnrRatingLeft;
	public LinearLayout lnrRatingRight;
	public IjoomerTextView txtPlus;
	public IjoomerTextView txtProsIcon;
	public IjoomerTextView txtConsIcon;
	public LinearLayout lnrExpandedView;
	public IjoomerTextView txtCarDetail;
	public LinearLayout lnrGridBorder;
	public ImageView imgCall;
	public ImageView imgEmail;
	public IjoomerButton btnFavourite;
	public ImageView imgShare;
	public IjoomerTextView txtLess;
	public IjoomerTextView txtReviewOn;
	public IjoomerTextView txtDescription;
	public LinearLayout lnrReview;
	public IjoomerTextView txtAddReview;

	// facebook checkins

	public IjoomerTextView txtLocationValue;

	/*
	 * k2 main directories list items
	 */

	public IjoomerTextView txtk2DirectoriesCategoryName;
	public IjoomerTextView txtk2DirectoriesCategoryNoItem;
	public HorizontalScrollView hrzk2DirectoriesCategoryItem;
	public LinearLayout lnrk2DirectoriesScrollable;

	/*
	 * k2 catalog coverflow and list items
	 */

	public LinearLayout lnrK2ListItem;
	public ImageView imgk2CatalogCategory;
	public IjoomerTextView txtk2CatalogCategoryNoItem;
	public ImageView imgk2CatalogCategoryItem;
	public IjoomerTextView txtk2CatalogCategoryItem;
	public IjoomerButton btnk2CatalogCategoryItemPrice;

	/*
	 * k2 news list / main list item
	 */
	public LinearLayout lnrK2NewsListItem;
	public LinearLayout lnrCategoryHeader;
	public ImageView imgk2NewsCategoryItem;
	public IjoomerTextView txtk2NewsCategoryItem;
	public IjoomerTextView txtk2NewsCategoryDescription;
	public IjoomerTextView txtk2NewsCategoryName;

	/*
	 * k2 grid item
	 */
	public LinearLayout lnrK2Grid;
	public ImageView imgk2GridCategoryItem;
	public IjoomerTextView txtk2GridCategoryItemName;

	/*
	 * k2 Catalog Comment List
	 */
	public ImageView imgK2CommentUserAvatar;
	public IjoomerTextView txtK2CommentUserName;
	public IjoomerTextView txtK2CommentDate;
	public IjoomerTextView txtK2CommentTitle;
	public IjoomerTextView txtK2CommentUrl;

	/*
	 * k2 Gallery
	 */
	public ImageView imgItem;

	/**
	 * Ijoomer Emojis
	 */
	public ImageView imgEmojis;

	/**
	 * weather location list
	 */

	public IjoomerTextView txtLocation;
	public ImageView imgRemoveLocation;


	/**
	 * JBolo Chat
	 */
	/**
	 * Chat GroupChat List
	 */

	public LinearLayout lnrChatOthers;
	public ImageView imgChatOthersUser;
	public IjoomerTextView txtChatOthersUserName;
	public IjoomerTextView txtChatOthersMessage;
	public IjoomerTextView txtChatOthersDate;

	public LinearLayout lnrChat;
	public IjoomerTextView txtChatMessage;

	public LinearLayout lnrChatMe;
	public ImageView imgMeChatUser;
	public IjoomerTextView txtMeChatUserName;
	public IjoomerTextView txtMeChatMessage;
	public IjoomerTextView txtMeChatDate;
	public ProgressBar pbrMeChatLoading;

	/**
	 * Online User
	 */
	public IjoomerTextView jbolotxtUserStatusMessage;
	public ImageView jboloUserImg;
	public IjoomerTextView jbolotxtUserName;
	public ImageView jboloUserStatusImg;

	/**
	 * EasyBlog Entries list
	 */
	public ImageView easyblogEntriesImage;
	public IjoomerTextView easyblogEntriesTxtTitle;
	public IjoomerTextView easyblogEntriesTxtIntro;
	public IjoomerTextView easyblogEntriesTxtPostedBy;
	public IjoomerTextView easyblogEntriesTxtHits;
	public IjoomerTextView easyblogEntriesCommentCount;
	public IjoomerRatingBar easyblogEntriesRtbRating;
	public IjoomerButton easyblogEntriesEdit;

	/**
	 * EasyBlog detail Comment List
	 */
	public ImageView easyblogimgCommentUserAvatar;
	public IjoomerTextView easyblogtxtCommentUserName;
	public IjoomerTextView easyblogtxtCommentDate;
	public IjoomerButton easyblogbtnCommentRemove;
	public IjoomerButton easyblogbtnCommentEdit;
	public IjoomerTextView easyblogtxtCommentTitle;
	public IjoomerTextView easyblogReplay;
	public IjoomerTextView easyblogLike;
	public IjoomerTextView easyblogUnlike;
	public IjoomerTextView easyblogtxtCommentLikeCount;
	public IjoomerTextView easyblogtxtReplayCount;
	public LinearLayout lnrCommentEdit;
	public IjoomerEditText edtCommentEdit;
	public IjoomerButton btnCommentEditSave;
	public IjoomerButton btnCommentEditCancel;
	/**
	 * Easyblog category list item
	 */
	public ImageView easyblogCatImageThumb;
	public IjoomerTextView easyblogCatTxtTitle;

	/*
	 * JReview Directories list item
	 */
	public IjoomerTextView jreviewTxtDirectoriesCaption;
	public IjoomerTextView jreviewTxtCategoriesCaption;
	public IjoomerTextView jreviewTxtCategoriesCount;

	/*
	 * JReview Articles list item
	 */
	public LinearLayout jreviewArticleView;
	public IjoomerTextView jreviewTxtArticleTitletxt;
	public IjoomerTextView jreviewTxtArticleIntrotxt;
	public IjoomerTextView jreviewTxtArticleVideoCount;
	public IjoomerTextView jreviewTxtArticlePhotoCount;
	public IjoomerTextView jreviewTxtArticleMusicCount;
	public IjoomerTextView jreviewTxtArticleAttachmentCount;
	public IjoomerTextView jreviewTxtArticleFavouriteCount;
	public IjoomerTextView jreviewTxtArticleUserRatingCount;
	public IjoomerRatingBar jreviewArticleUserRatingbar;
	public IjoomerRatingBar jreviewArticleEditorRatingbar;
	public IjoomerTextView jreviewArticleUserRating;
	public IjoomerTextView jreviewArticleEditorRating;
	public IjoomerTextView jreviewTxtArticleEditorRatingCount;
	public ImageView jreviewArticleImg;

	/*
	 * JReview Article Reviews list item
	 */
	public IjoomerTextView jreviewTxtArticleReviewsUsernametxt;
	public IjoomerTextView jreviewTxtArticleReviewsTitletxt;
	public IjoomerTextView jreviewTxtArticleReviewsIntrotxt;
	public IjoomerTextView jreviewTxtArticleReviewsAverageCount;
	public IjoomerTextView jreviewTxtArticleReviewsDatetxt;
	public IjoomerTextView jreviewTxtArticleReviewsViewMoretxt;
	public IjoomerTextView jreviewTxtArticleReviewsCommentCount;
	public IjoomerTextView jreviewTxtArticleReviewsLikeCount;
	public IjoomerTextView jreviewTxtArticleReviewsUnlikeCount;
	public IjoomerRatingBar jreviewArticleReviewsRatingbar;
	public LinearLayout jreviewArticleReviewsViewMoreLayout;
	public LinearLayout jreviewArticleReviewsRatingLayout;
	public ImageView jreviewArticleReviewsImg;
	public ImageView jreviewArticleReviewsViewMoreToggleImg;

	/*
	 * JReview Article Video List item
	 */

	public ImageView jreviewarticleimgVideoAvatar;
	public ImageView jreviewarticleimgVideoPlay;
	public ImageView jreviewarticleimgVideoArrow;
	public ImageView jreviewarticleimgVideoShare;
	public LinearLayout jreviewarticlelnrVideo;
	public LinearLayout jreviewarticleVideoLike;
	public LinearLayout jreviewarticleVideoUnlike;
	public IjoomerTextView jreviewarticletxtVideoTitle;
	public IjoomerTextView jreviewarticletxtVideoBy;
	public IjoomerTextView jreviewarticletxtVideoDateLocation;
	public IjoomerTextView jreviewarticletxtVideoLikeCount;
	public IjoomerTextView jreviewarticletxtVideoDislikeCount;
	public IjoomerTextView jreviewarticletxtVideoShare;

	/*
	 * JReview Article Attachments List item
	 */
	public IjoomerTextView jreviewarticletxtFileTitle;
	public IjoomerTextView jreviewarticletxtFileHit;
	public IjoomerTextView jreviewarticletxtFileSize;
	public IjoomerButton jreviewarticlebtnfiledownload;
	public IjoomerTextView jreviewarticletxtFileDesc;

	/*
	 * JReview Article Audios List item
	 */
	public ImageView jreviewarticleAudioPlaybtn;
	public IjoomerTextView jreviewarticletxtAudioFileTitle;
	public IjoomerTextView jreviewarticletxtAudioDuration;
	public ProgressBar jreviewarticleAudioProgressbar;

	/*
	 * JReview Article Reviews Comments List item
	 */
	public ImageView jreviewreviewimgCommentUserAvatar;
	public IjoomerTextView jreviewreviewtxtCommentUserName;
	public IjoomerTextView jreviewreviewtxtCommentDate;
	public IjoomerButton jreviewreviewbtnCommentRemove;
	public IjoomerTextView jreviewreviewtxtCommentTitle;
}
