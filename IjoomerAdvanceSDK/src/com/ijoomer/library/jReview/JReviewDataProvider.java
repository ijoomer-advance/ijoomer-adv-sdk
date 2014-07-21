package com.ijoomer.library.jReview;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

/**
 * This Class Contains All Method Related To JReviewCategoriesDataProvider.
 * 
 * @author tasol
 * 
 */

public class JReviewDataProvider extends IjoomerPagingProvider {

	private Context mContext;
	private boolean isCalling = false;
	private final String CATEGORYID = "categoryID";
	private final String CATEGORY = "category";
	private final String ARTICLE = "article";

	// upload media
	private final String ADDARTICLEMEDIA = "addArticleMedia";
	private final String FILES = "attachment";
	private final String URL = "url";

	// listing search
	private final String SEARCHRESULTS = "searchResults";
	private final String LISTINGTITLE = "listingTitle";
	private final String LISTINGSUMMARY = "listingSummary";
	private final String LISTINGDISC = "listingDescription";
	private final String SEARCHPHRASE = "searchphrase";
	private final String FIELDS = "fields";
	private final String CATIDS = "catids";
	private final String KEYWORD = "keyword";
	private final String SEARCHBY = "searchBy";
	private final String REVIEWCOMMENTS = "reviewComments";
	private final String LISTINGAUTHOR = "listingAuthor";

	// add listing
	private final String ADDNEWLISTING = "addNewListing";
	private final String ID = "id";
	private final String TITLE = "title";
	private final String DESCRIPTION = "description";
	private final String ALIAS = "alias";
	private final String INTROTEXT = "introtext";
	private final String FULLTEXT = "fulltext";
	private final String CATID = "catid";
	private final String METAKEY = "metakey";
	private final String METADESC = "metadesc";
	private final String PLANID = "plan_id";
	private final String ISSUBSCRIPTION = "isSubscription";

	// favourite
	private final String ISUSERFAVORITE = "isuserfavorite";
	private final String GETFAVOURITE = "getfavorite";
	private final String ADDTOFAVOURITE = "addfavorite";
	private final String REMOVEFAVOURITE = "removefavorite";
	private final String ARTICLEID = "articleID";

	// reviews
	private final String ADDREVIEW = "addReview";
	private final String ADDREVIEWCOMMENT = "AddReviewComment";
	private final String REMOVEREVIEWCOMMENT = "RemoveReviewComment";
	private final String VOTEREVIEW = "voteReview";
	private final String COMMENT_ID = "commentID";
	private final String REVIEW_ID = "reviewID";
	private final String ARTICLE_ID = "articleID";
	private final String VOTE = "vote";
	private final String TEXT = "text";
	private final String COMMENTS = "comments";
	private final String RATINGS = "ratings";

	//nearby
	private final String NEARBYSERACH = "nearBySearch";
	private final String LATITUDE = "latitude";
	private final String LONGITUDE = "longitude";
	private final String DISTANCE = "distance";

	// tables
	private final String TABLE_FAVOURITE = "jreview_favourite_articles";
	private final String TABLE_JREVIEW_CATEGORIES = "jreview_categories";
	private final String TABLE_JREVIEW_ARTICLES = "jreview_articles";
	private final String TABLE_JREVIEW_SEARCH = "jreview_search";

	/**
	 * Constructor
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 */

	public JReviewDataProvider(Context mContext) {
		super(mContext);
		this.mContext = mContext;
	}

	/**
	 * This method used to check provider execute any request call.
	 * 
	 * @return {@link boolean}
	 */
	public boolean isCalling() {
		return isCalling;
	}

	/**
	 * This Method is used to get whole jreview data.
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getfavourites(final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, GETFAVOURITE);

				JSONObject taskData = new JSONObject();
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				IjoomerCaching ic = new IjoomerCaching(mContext);
				if (validateResponse(iw.getJsonObject())) {
					try {
						return ic.parseData(iw.getJsonObject().getJSONArray("favorite"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}

		}.execute();
	}

	/**
	 * This method used to get nearby aticles.
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getnearbyResult(final String latitude,final String longitude,final String distance, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, CATEGORY);
				iw.addWSParam(EXTTASK, NEARBYSERACH);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(LATITUDE, latitude);
					taskData.put(LONGITUDE , longitude);
					taskData.put(DISTANCE , distance);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});


				IjoomerCaching ic = new IjoomerCaching(mContext);
				try{
					if (validateResponse(iw.getJsonObject())) {
						return ic.parseData(iw.getJsonObject().getJSONArray("data"));
					}
				}catch(Exception e){

				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	/**
	 * This method used to submit new listing.
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void submitNewListing(final String categoryId, final String articleId, final String planId, final String isSubscription, final String title, final String alias, final String introtext,
			final String fulltext, final String metakeys, final String metadesc, final JSONArray Fields, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, ADDNEWLISTING);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(ID, articleId);
					taskData.put(CATID, categoryId);
					taskData.put(PLANID, planId);
					taskData.put(ISSUBSCRIPTION, isSubscription);
					taskData.put(TITLE, title);
					taskData.put(ALIAS, alias);
					taskData.put(INTROTEXT, introtext);
					taskData.put(FULLTEXT, fulltext);
					taskData.put(METAKEY, metakeys);
					taskData.put(METADESC, metadesc);
					taskData.put(FIELDS, Fields);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);

				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				IjoomerCaching ic = new IjoomerCaching(mContext);
				try {
					if (validateResponse(iw.getJsonObject())) {
						ic.cacheData(iw.getJsonObject().getJSONArray("article"), false, TABLE_JREVIEW_ARTICLES);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	/**
	 * This method used to add aticle to favourite.
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getSearchResult(final String searchby, final String keyword, final String searchphrase, final String catids, final String author, final JSONArray fields, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, SEARCHRESULTS);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(SEARCHBY, searchby);
					taskData.put(KEYWORD, keyword);
					taskData.put(SEARCHPHRASE, searchphrase);
					taskData.put(CATIDS, catids);
					taskData.put(LISTINGTITLE, "1");
					taskData.put(LISTINGSUMMARY, "0");
					taskData.put(LISTINGDISC, "0");
					taskData.put(REVIEWCOMMENTS, "0");
					taskData.put(LISTINGAUTHOR, author);
					taskData.put(FIELDS, fields);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				IjoomerCaching ic = new IjoomerCaching(mContext);
				try {
					if (validateResponse(iw.getJsonObject())) {
						return ic.parseData(iw.getJsonObject().getJSONArray("search"));
					}
				} catch (Exception e) {

				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	public void isfavourite(final String articleID, final WebCallListener target) {
		new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, ISUSERFAVORITE);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(ARTICLEID, articleID);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				if (validateResponse(iw.getJsonObject())) {
					return iw.getJsonObject();
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
			}
		}.execute();
	}

	public void addtofavourite(final String articleID, final WebCallListener target) {
		new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, ADDTOFAVOURITE);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(ARTICLEID, articleID);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				if (validateResponse(iw.getJsonObject())) {
					return iw.getJsonObject();
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
			}
		}.execute();
	}

	public void removefavourite(final String articleID, final WebCallListener target) {
		new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, REMOVEFAVOURITE);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(ARTICLEID, articleID);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				if (validateResponse(iw.getJsonObject())) {
					return iw.getJsonObject();
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
			}
		}.execute();
	}

	public void addreview(final String reviewID, final String articleID, final String reviewTitle, final String reviewComments, final String ratings, final WebCallListener target) {
		new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, ADDREVIEW);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(REVIEW_ID, reviewID);
					taskData.put(ARTICLE_ID, articleID);
					taskData.put(TITLE, reviewTitle);
					taskData.put(COMMENTS, reviewComments);
					taskData.put(RATINGS, ratings);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				if (validateResponse(iw.getJsonObject())) {
					return iw.getJsonObject();
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
			}
		}.execute();
	}

	public void addreviewComment(final String reviewID, final String articleID, final String text, final WebCallListener target) {
		new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, ADDREVIEWCOMMENT);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(REVIEW_ID, reviewID);
					taskData.put(ARTICLE_ID, articleID);
					taskData.put(TEXT, text);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				if (validateResponse(iw.getJsonObject())) {
					return iw.getJsonObject();
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
			}
		}.execute();
	}

	public void removereviewComment(final String commentID, final String reviewID, final String articleID, final WebCallListener target) {
		new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, REMOVEREVIEWCOMMENT);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(COMMENT_ID, commentID);
					taskData.put(REVIEW_ID, reviewID);
					taskData.put(ARTICLE_ID, articleID);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				if (validateResponse(iw.getJsonObject())) {
					return iw.getJsonObject();
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
			}
		}.execute();
	}

	public void sendReviewVote(final String vote,final String reviewID, final String articleID, final WebCallListener target) {
		new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, VOTEREVIEW);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(VOTE, vote);
					taskData.put(REVIEW_ID, reviewID);
					taskData.put(ARTICLE_ID, articleID);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				IjoomerCaching ic = new IjoomerCaching(mContext);
				try {
					if (validateResponse(iw.getJsonObject())) {
						ic.updateTable("UPDATE jreview_articles SET reviewVotes = '" + iw.getJsonObject().getString("reviewVotes") + "' WHERE articleID=" + articleID);
						return iw.getJsonObject();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
			}
		}.execute();
	}


	/**
	 * This method used to upload photo.
	 * 
	 * @param filePath
	 *            represented photo file path
	 * @param albumId
	 *            represented album id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void uploadPhoto(final String articleId, final String categoryId, final String filePath, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, ADDARTICLEMEDIA);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(CATEGORYID, categoryId);
					taskData.put(ARTICLE_ID, articleId);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);

				iw.WSCall(filePath, new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				IjoomerCaching ic = new IjoomerCaching(mContext);
				try {
					if (validateResponse(iw.getJsonObject())) {
						ic.updateTable("UPDATE jreview_articles SET jreviewMediaImage = '" + iw.getJsonObject().getString("jreviewMediaImage") + "' WHERE articleID=" + articleId);
						ic.updateTable("UPDATE jreview_articles SET photoCount = '" + iw.getJsonObject().getString("totalImageCount") + "' WHERE articleID=" + articleId);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}

			protected void onPostExecute(Void result) {
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			};
		}.execute();

	}

	/**
	 * This method used to upload video.
	 * 
	 * @param groupID
	 *            represented groupID(0- for video,groupID- for group video)
	 * @param videoFilePath
	 *            represented video file path
	 * @param videoTitle
	 *            represented video title
	 * @param description
	 *            represented video description
	 * @param lat
	 *            represented video latitude
	 * @param lng
	 *            represented video longitude
	 * @param categoryID
	 *            represented video category id
	 * @param privacy
	 *            represented video privacy
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void uploadVideo(final String articleId, final String categoryId, final String videoFilePath, final String videoTitle, final String description, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, ADDARTICLEMEDIA);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(CATEGORYID, categoryId);
					taskData.put(ARTICLE_ID, articleId);
					taskData.put(TITLE, videoTitle);
					taskData.put(DESCRIPTION, description);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);

				if (videoFilePath != null) {
					iw.WSCall(videoFilePath, new ProgressListener() {

						@Override
						public void transferred(long num) {
							if (num >= 100) {
								target.onProgressUpdate(95);
							} else {
								target.onProgressUpdate((int) num);
							}
						}
					});
				}

				if (validateResponse(iw.getJsonObject())) {
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, iw.getJsonObject());
			}
		}.execute();
	}

	/**
	 * This method used to upload video link.
	 * 
	 * @param articleId
	 *            represented article id
	 * @param categoryId
	 *            represented category id
	 * @param videoUrl
	 *            represented video link
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void linkVideo(final String articleId, final String categoryId, final String videoUrl, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, ADDARTICLEMEDIA);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(CATEGORYID, categoryId);
					taskData.put(ARTICLE_ID, articleId);
					taskData.put(URL, videoUrl);
					taskData.put(TITLE, "");
					taskData.put(DESCRIPTION, "");
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);

				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});
				if (validateResponse(iw.getJsonObject())) {
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, iw.getJsonObject());
			}
		}.execute();
	}

	/**
	 * This method is used to upload Announcement File
	 * 
	 * @param filePath
	 *            represented file path
	 * @param announcementID
	 *            represented announcement id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void uploadAttachmentFile(final String filePath, final String articleId, final String categoryId, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JREVIEW);
				iw.addWSParam(EXTVIEW, ARTICLE);
				iw.addWSParam(EXTTASK, ADDARTICLEMEDIA);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(CATEGORYID, categoryId);
					taskData.put(ARTICLE_ID, articleId);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				if (filePath != null) {
					iw.WSCall(FILES, filePath, new ProgressListener() {

						@Override
						public void transferred(long num) {
							if (num >= 100) {
								target.onProgressUpdate(95);
							} else {
								target.onProgressUpdate((int) num);
							}
						}
					});
				} else {
					iw.WSCall(new ProgressListener() {

						@Override
						public void transferred(long num) {
							if (num >= 100) {
								target.onProgressUpdate(95);
							} else {
								target.onProgressUpdate((int) num);
							}
						}
					});

				}

				IjoomerCaching ic = new IjoomerCaching(mContext);
				try{
					if (validateResponse(iw.getJsonObject())) {
						ic.updateTable("UPDATE jreview_articles SET jreviewMediaAttachments = '" + iw.getJsonObject().getString("jreviewMediaAttachments") + "' WHERE articleID=" + articleId);
						ic.updateTable("UPDATE jreview_articles SET attachmentCount = '" + iw.getJsonObject().getString("totalAttachmentCount") + "' WHERE articleID=" + articleId);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	/**
	 * This method is used to get categories from category table
	 * 
	 * @param pid
	 *            represented parent id
	 */
	public ArrayList<HashMap<String, String>> getCategories(String pid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_CATEGORIES, "SELECT * FROM " + TABLE_JREVIEW_CATEGORIES + " WHERE parent=" + pid);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get single category from category table
	 * 
	 * @param cid
	 *            represented category id
	 */
	public ArrayList<HashMap<String, String>> getSingleCategory(String cid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_CATEGORIES, "SELECT * FROM " + TABLE_JREVIEW_CATEGORIES + " WHERE categoryID=" + cid);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get categories by search criteria from category
	 * table
	 * 
	 * @param crid
	 *            represented criteria id
	 */
	public ArrayList<HashMap<String, String>> getCategoriesbyCriteria(String crid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_CATEGORIES, "SELECT * FROM " + TABLE_JREVIEW_CATEGORIES + " WHERE criteriaID=" + crid);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get articles by search criteria from articles
	 * table
	 * 
	 * @param crid
	 *            represented criteria id
	 */
	public HashMap<String, String> getArticlesbyCriteria(String crid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_ARTICLES, "SELECT jreview_group FROM " + TABLE_JREVIEW_ARTICLES + " WHERE criteriaID=" + crid + " LIMIT 1").get(0);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get articles from articles table
	 * 
	 * @param cid
	 *            represented category id
	 */
	public ArrayList<HashMap<String, String>> getArticles(String cid, String pageNO, String pageLimit) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_ARTICLES, "SELECT categoryID,criteriaName,shareLink,jreviewImages,articleName,userCommentCount,introtext,categoryName,isFavorite"
					+ ",userName,longitude,editorRatingCount,fulltext,editorRating,audioCount,videoCount,userRatingCount,photoCount,publish_up,attachmentCount,criteriaID,articleID"
					+ ",userCriteriaRatingCount,jreviewThumbImages,userID,hits,mediaCount,jreviewMediaImage,editorCriteriaRatingCount,overallAverageRating,totalFavorite,latitude" + " FROM "
					+ TABLE_JREVIEW_ARTICLES + " WHERE categoryID=" + cid + " LIMIT " + pageNO + "," + pageLimit);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<HashMap<String, String>> getSearchArticles(String cid, String articleName,String pageNO, String pageLimit) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_ARTICLES, "SELECT categoryID,criteriaName,shareLink,jreviewImages,articleName,userCommentCount,introtext,categoryName,isFavorite"
					+ ",userName,longitude,editorRatingCount,fulltext,editorRating,audioCount,videoCount,userRatingCount,photoCount,publish_up,attachmentCount,criteriaID,articleID"
					+ ",userCriteriaRatingCount,jreviewThumbImages,userID,hits,mediaCount,jreviewMediaImage,editorCriteriaRatingCount,overallAverageRating,totalFavorite,latitude" + " FROM "
					+ TABLE_JREVIEW_ARTICLES + " WHERE categoryID ='" + cid + "' AND articleName LIKE '%"+ articleName +"%' LIMIT " + pageNO + "," + pageLimit);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get search fields from search table
	 */
	public ArrayList<HashMap<String, String>> getSearchFields() {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_SEARCH, "SELECT * FROM " + TABLE_JREVIEW_SEARCH);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get add listing field from articles table
	 * 
	 * @param cid
	 *            represented category id
	 */
	public ArrayList<HashMap<String, String>> getListingForm(String cid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			HashMap<String, String> value = ic.getDataFromCache(TABLE_JREVIEW_ARTICLES, "SELECT jreview_group FROM " + TABLE_JREVIEW_ARTICLES + " WHERE categoryID=" + cid + " LIMIT 1").get(0);
			return ic.parseData(new JSONArray(value.get("jreview_group")));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get add listing field from articles table
	 * 
	 * @param cid
	 *            represented category id
	 * @param aid
	 *            represented article id
	 */
	public ArrayList<HashMap<String, String>> getListingForm(String cid, String aid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			ArrayList<HashMap<String, String>> value = ic.getDataFromCache(TABLE_JREVIEW_ARTICLES, "SELECT jreview_group FROM " + TABLE_JREVIEW_ARTICLES + " WHERE categoryID=" + cid
					+ " AND articleID=" + aid);
			return ic.parseData(new JSONArray(value.get(0).get("jreview_group")));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get single entry from articles table
	 * 
	 * @param cid
	 *            represented category id
	 * @param aid
	 *            represented article id
	 */
	public ArrayList<HashMap<String, String>> getSingleArticle(String cid, String aid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_ARTICLES, "SELECT * FROM " + TABLE_JREVIEW_ARTICLES + " WHERE categoryID=" + cid + " AND articleID=" + aid);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get media images from articles table
	 * 
	 * @param cid
	 *            represented category id
	 * @param aid
	 *            represented article id
	 */
	public ArrayList<HashMap<String, String>> getArticlePhotos(String cid, String aid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_ARTICLES, "SELECT categoryID,articleID,userName,photoCount,jreviewMediaImage,shareLink FROM " + TABLE_JREVIEW_ARTICLES + " WHERE categoryID=" + cid + " AND articleID="
					+ aid);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get media images from articles table
	 * 
	 * @param cid
	 *            represented category id
	 * @param aid
	 *            represented article id
	 */
	public ArrayList<HashMap<String, String>> getArticleAttachemnts(String cid, String aid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_ARTICLES, "SELECT categoryID,articleID,userName,attachmentCount,jreviewMediaAttachments,shareLink FROM " + TABLE_JREVIEW_ARTICLES + " WHERE categoryID=" + cid + " AND articleID="
					+ aid);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get media images from articles table
	 * 
	 * @param cid
	 *            represented category id
	 * @param aid
	 *            represented article id
	 */
	public ArrayList<HashMap<String, String>> getArticleAudios(String cid, String aid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_ARTICLES, "SELECT categoryID,articleID,userName,jreviewMediaAudio,shareLink FROM " + TABLE_JREVIEW_ARTICLES + " WHERE categoryID=" + cid + " AND articleID="
					+ aid);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get media images from articles table
	 * 
	 * @param cid
	 *            represented category id
	 * @param aid
	 *            represented article id
	 */
	public ArrayList<HashMap<String, String>> getArticleVideos(String cid, String aid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_ARTICLES, "SELECT categoryID,articleID,userName,videoCount,jreviewMediaVideo FROM " + TABLE_JREVIEW_ARTICLES + " WHERE categoryID=" + cid + " AND articleID="
					+ aid);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get single articel detail from articles table
	 * 
	 * @param cid
	 *            represented category id
	 * @param aid
	 *            represented article id
	 */
	public ArrayList<HashMap<String, String>> getArticleDetails(String cid, String aid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_ARTICLES, "SELECT categoryID,criteriaName,shareLink,jreviewImages,articleName,userCommentCount,introtext,categoryName"
					+ ",isFavorite,userName,longitude,editorRatingCount,fulltext,editorRating,jreviewMediaVideo,audioCount,videoCount,userRatingCount,photoCount,publish_up"
					+ ",attachmentCount,criteriaID,articleID,userCriteriaRatingCount,jreviewThumbImages,userID,hits,mediaCount,jreviewMediaImage,jreviewMediaAttachments"
					+ ",editorCriteriaRatingCount,overallAverageRating,totalFavorite,latitude,averageReviewCriteria,reviews FROM " + TABLE_JREVIEW_ARTICLES + " WHERE categoryID=" + cid
					+ " AND articleID=" + aid);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get reviews entry from articles table
	 * 
	 * @param cid
	 *            represented category id
	 * @param aid
	 *            represented article id
	 */
	public ArrayList<HashMap<String, String>> getArticleReviews(String cid, String aid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_ARTICLES, "SELECT overallAverageRating,averageReviewCriteria,reviews FROM " + TABLE_JREVIEW_ARTICLES + " WHERE categoryID=" + cid
					+ " AND articleID=" + aid);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to get reviews entry from articles table
	 * 
	 * @param cid
	 *            represented category id
	 * @param aid
	 *            represented article id
	 */
	public ArrayList<HashMap<String, String>> getArticleReviewsComments(String cid, String aid) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_JREVIEW_ARTICLES, "SELECT reviewComment FROM " + TABLE_JREVIEW_ARTICLES + " WHERE categoryID=" + cid + " AND articleID=" + aid);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method is used to add entry in favourite table
	 * 
	 * @param entry
	 *            represented entry data
	 */
	public void addToFavourite(ArrayList<HashMap<String, String>> entry) {
		new IjoomerCaching(mContext).createTable(entry, TABLE_FAVOURITE);
	}

	/**
	 * This method is used to remove entry in favourite table
	 * 
	 * @param cid
	 *            represented category id
	 * @param aid
	 *            represented article id
	 */
	public void removefromFavourite(String cid, String aid) {
		new IjoomerCaching(mContext).deleteDataFromCache("DELETE FROM " + TABLE_FAVOURITE + " WHERE categoryID=" + cid + " AND articleID=" + aid);
	}

	/**
	 * This method is used to get favourite articles in favourite table
	 */
	public ArrayList<HashMap<String, String>> getFavouriteArticles(String pageNO, String pageLimit) {
		try {
			IjoomerCaching ic = new IjoomerCaching(mContext);
			return ic.getDataFromCache(TABLE_FAVOURITE, "SELECT * FROM " + TABLE_FAVOURITE + " LIMIT " + pageNO + "," + pageLimit);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}
