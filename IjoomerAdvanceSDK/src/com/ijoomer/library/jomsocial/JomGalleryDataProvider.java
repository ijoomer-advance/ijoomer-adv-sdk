package com.ijoomer.library.jomsocial;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomGalleryDataProvider.
 * 
 * @author tasol
 * 
 */
public class JomGalleryDataProvider extends IjoomerPagingProvider {
	private Context mContext;

	private final String NAME = "name";
	private final String SETPHOTOCOVER = "setPhotoCover";
	private final String MEDIA = "media";
	private final String TAGFRIENDS = "tagFriends";
	private final String ADDALBUM = "addAlbum";
	private final String REMOVEALBUM = "removeAlbum";
	private final String REMOVEPHOTO = "removePhoto";
	private final String REMOVEVIDEO = "removeVideo";
	private final String ADDCOMMENT = "addComment";
	private final String REMOVECOMMENT = "removeComment";
	private final String SETPHOTOCAPTION = "setPhotoCaption";
	private final String DESC = "desc";
	private final String LAT = "lat";
	private final String LONG = "long";
	private final String URL = "url";
	private final String PRIVACY = "privacy";
	private final String PROFILE = "profile";
	private final String CAPTION = "caption";
	private final String SORT = "sort";
	private final String WITHLIMIT = "withLimit";
	private final String MYALBUMS = "myAlbums";
	private final String PHOTOS = "photos";
	private final String USERID = "userID";
	private final String ALLALBUMS = "allAlbums";
	private final String ALLVIDEOS = "allVideos";
	private final String MYVIDEOS = "myVideos";
	private final String TAGS = "tags";
	private final String ALBUMID = "albumID";
	private final String GROUPID = "groupID";
	private final String GROUP = "group";
	private final String EVENT = "event";
	private final String PHOTOID = "photoID";
	private final String VIDEOID = "videoID";
	private final String MESSAGE = "message";
	private final String LIMIT = "limit";
	private final String COMMENTS = "comments";
	private final String UNIQUEID = "uniqueID";
	private final String TYPE = "type";
	private final String LIKE = "like";
	private final String DISLIKE = "dislike";
	private final String UNLIKE = "unlike";
	private final String ALBUM = "album";
	private final String ALBUMS = "albums";
	private final String PHOTO = "photo";
	private final String VIDEOS = "videos";
	private final String LINKVIDEO = "linkVideo";
	private final String UPLOADPHOTO = "uploadphoto";
	private final String VIDEOCATEGORIES = "videoCategories";
	private final String ISDEFAULT = "isDefault";
	private final String IMAGECOUNT = "imageCount";
	private final String UPLOADVIDEO = "uploadVideo";
	private final String CATEGORYID = "categoryID";
	private final String TITLE = "title";
	private final String DESCRIPTION = "description";
	private final String REPORT = "report";
	private final String ADDTAG = "addTag";
	private final String POSITION = "position";
	private final String REMOVETAG = "removeTag";
	private final String SETCOVER = "setCover";
	private final String SETUSERCOVER = "setUserCover";
	private final String SETAVATAR = "setAvatar";
	private final String SETPROFILEVIDEO = "setProfileVideo";
	private final String SEARCHVIDEO = "searchVideo";
	private final String QUERY = "query";
	private final String KEYWORD = "keyword";

	private boolean isCalling = false;

	/**
	 * This method used to check provider execute any request call.
	 * 
	 * @return {@link boolean}
	 */
	public boolean isCalling() {
		return isCalling;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            represented {@link Context}
	 */
	public JomGalleryDataProvider(Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * This method used to get my album list.
	 * 
	 * @param userId
	 *            represented user id (0 - for login user)
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getMyAlbumList(final String userId, final WebCallListener target) {

		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();
					iw.addWSParam(EXTNAME, JOMSOCIAL);
					iw.addWSParam(EXTVIEW, MEDIA);
					iw.addWSParam(EXTTASK, MYALBUMS);

					JSONObject taskData = new JSONObject();
					try {
						taskData.put(PAGENO, "" + getPageNo());
						if (!userId.equals("0")) {
							taskData.put(USERID, userId);
						}
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
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(PAGELIMIT);
							iw.getJsonObject().remove(TOTAL);
							return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
						} catch (Throwable ignored) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					isCalling = false;
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				}
			}.execute();
		} else {
			target.onProgressUpdate(100);
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
		}

	}

	/**
	 * This method used to get all album list.
	 * 
	 * @param groupID
	 *            represented group id(groupID -Group album,0 -for all album)
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getAllAlbumList(final String groupID, final WebCallListener target) {

		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();
					iw.addWSParam(EXTNAME, JOMSOCIAL);
					iw.addWSParam(EXTVIEW, MEDIA);
					iw.addWSParam(EXTTASK, ALLALBUMS);

					JSONObject taskData = new JSONObject();
					try {
						taskData.put(PAGENO, "" + getPageNo());
						if (!groupID.equals("0")) {
							taskData.put(GROUPID, groupID);
						}
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
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(PAGELIMIT);
							iw.getJsonObject().remove(TOTAL);
							return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
						} catch (Throwable e) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					isCalling = false;
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				}
			}.execute();
		} else {
			target.onProgressUpdate(100);
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
		}

	}

	/**
	 * This method used to add album.
	 * 
	 * @param albumID
	 *            represented album id(0- for new album,albumID- for edit album)
	 * @param albumName
	 *            represented album name
	 * @param albumDesc
	 *            represented album description
	 * @param lat
	 *            represented album latitude
	 * @param lng
	 *            represented album longitude
	 * @param privacy
	 *            represented album privacy
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void addAlbum(final String albumID, final String groupID, final String albumName, final String albumDesc, final double lat, final double lng, final String privacy,
			final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, ADDALBUM);

				JSONObject taskData = new JSONObject();
				try {
					if (!albumID.equals("0")) {
						taskData.put(ALBUMID, albumID);
					}
					if (!groupID.equals("0")) {
						taskData.put(GROUPID, groupID);
					}
					taskData.put(NAME, albumName);
					taskData.put(DESC, albumDesc);
					taskData.put(PRIVACY, privacy);
					taskData.put(LAT, "" + lat);
					taskData.put(LONG, "" + lng);
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
	 * This method used to remove album.
	 * 
	 * @param albumID
	 *            represented album id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void removeAlbum(final String albumID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, REMOVEALBUM);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(ALBUMID, albumID);
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
	 * This method is used to set photo caption
	 * 
	 * @param photoID
	 *            represented photo id
	 * @param photoCaption
	 *            represented photo caption
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void setPhotoCaption(final String photoID, final String photoCaption, final String voiceFilePath, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, SETPHOTOCAPTION);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, photoID);
					taskData.put(CAPTION, photoCaption);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				if (voiceFilePath != null) {
					iw.WSCall(voiceFilePath, new ProgressListener() {

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
	 * This method used to remove photo.
	 * 
	 * @param photoID
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void removePhoto(final String photoID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, REMOVEPHOTO);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(PHOTOID, photoID);
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
	 * This method used to remove video.
	 * 
	 * @param videoID
	 *            represented video id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void removeVideo(final String videoID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, REMOVEVIDEO);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(VIDEOID, videoID);
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
	 * This method used to add album comment.
	 * 
	 * @param albumID
	 *            represented album id
	 * @param message
	 *            represented album message
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void addAlbumComment(final String albumID, final String message, final String voiceFilePath, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, ADDCOMMENT);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, albumID);
					taskData.put(MESSAGE, message);
					taskData.put(TYPE, ALBUMS);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				if (voiceFilePath != null) {
					iw.WSCall(voiceFilePath, new ProgressListener() {

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
	 * This method used to remove album comment.
	 * 
	 * @param albumID
	 *            represented album id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void removeAlbumComment(final String albumID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, REMOVECOMMENT);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, albumID);
					taskData.put(TYPE, ALBUMS);
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
	 * This method used to remove photo comment.
	 * 
	 * @param photoID
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void removePhotoComment(final String photoID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, REMOVECOMMENT);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, photoID);
					taskData.put(TYPE, PHOTOS);
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
	 * This method used to remove video comment.
	 * 
	 * @param videoID
	 *            represented video id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void removeVideoComment(final String videoID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, REMOVECOMMENT);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, videoID);
					taskData.put(TYPE, VIDEOS);
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
	 * This method used add photo comment.
	 * 
	 * @param photoID
	 *            represented photo id
	 * @param message
	 *            represented photo message
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void addPhotoComment(final String photoID, final String message, final String voiceFilePath, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, ADDCOMMENT);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, photoID);
					taskData.put(MESSAGE, message);
					taskData.put(TYPE, PHOTOS);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				if (voiceFilePath != null) {
					iw.WSCall(voiceFilePath, new ProgressListener() {

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
	 * This method used to add video comment.
	 * 
	 * @param videoID
	 *            represented video id
	 * @param message
	 *            represented video message
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void addVideoComment(final String videoID, final String message, final String voiceFilePath, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, ADDCOMMENT);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, videoID);
					taskData.put(MESSAGE, message);
					taskData.put(TYPE, VIDEOS);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				if (voiceFilePath != null) {
					iw.WSCall(voiceFilePath, new ProgressListener() {

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
	public void uploadVideo(final String groupID, final String videoFilePath, final String videoTitle, final String description, final String videoCaption, final double lat,
			final double lng, final String categoryID, final String privacy, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, UPLOADVIDEO);

				JSONObject taskData = new JSONObject();
				try {
					if (!groupID.equals("0")) {
						taskData.put(GROUPID, groupID);
					}
					taskData.put(TITLE, videoTitle);
					taskData.put(CATEGORYID, categoryID);
					taskData.put(DESCRIPTION, description);
					taskData.put(CAPTION, videoCaption);
					if (privacy != null) {
						taskData.put(PRIVACY, privacy);
					}
					taskData.put(LAT, "" + lat);
					taskData.put(LONG, "" + lng);
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
	 * This method used to edit video.
	 * 
	 * @param videoID
	 *            represented video id
	 * @param groupID
	 *            represented groupID(0- for video,groupID- for group video)
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
	public void editVideo(final String videoID, final String groupID, final String videoTitle, final String description, final double lat, final double lng,
			final String categoryID, final String privacy, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, UPLOADVIDEO);

				JSONObject taskData = new JSONObject();
				try {
					if (!groupID.equals("0")) {
						taskData.put(GROUPID, groupID);
					}
					taskData.put(VIDEOID, videoID);
					taskData.put(TITLE, videoTitle);
					taskData.put(CATEGORYID, categoryID);
					taskData.put(DESCRIPTION, description);
					taskData.put(PRIVACY, privacy);
					taskData.put(LAT, "" + lat);
					taskData.put(LONG, "" + lng);
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
	 * This method used to upload video link.
	 * 
	 * @param groupID
	 *            represented groupID(0- for video,groupID- for group video)
	 * @param videoUrl
	 *            represented video link
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
	public void linkVideo(final String groupID, final String videoUrl, final String videoCaption, final double lat, final double lng, final String categoryID,
			final String privacy, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, LINKVIDEO);

				JSONObject taskData = new JSONObject();
				try {
					if (!groupID.equals("0")) {
						taskData.put(GROUPID, groupID);
					}
					taskData.put(URL, videoUrl);
					taskData.put(CATEGORYID, categoryID);
					taskData.put(PRIVACY, privacy);
					taskData.put(LAT, "" + lat);
					taskData.put(LONG, "" + lng);
					taskData.put(CAPTION, videoCaption);

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
	 * This method used to get album photo list.
	 * 
	 * @param albumId
	 *            represented album id
	 * @param userId
	 *            represented user id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getPhotoList(final String albumId, final String userId, final WebCallListener target) {

		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();
					iw.addWSParam(EXTNAME, JOMSOCIAL);
					iw.addWSParam(EXTVIEW, MEDIA);
					iw.addWSParam(EXTTASK, PHOTOS);

					JSONObject taskData = new JSONObject();
					try {
						taskData.put(PAGENO, "" + getPageNo());
						taskData.put(ALBUMID, "" + albumId);
						taskData.put(LIMIT, "" + getPageLimit());
						if (!userId.equals("0")) {
							taskData.put(USERID, userId);
						}
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
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(PAGELIMIT);
							iw.getJsonObject().remove(TOTAL);
							return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
						} catch (Throwable e) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					isCalling = false;
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				}
			}.execute();
		} else {
			target.onProgressUpdate(100);
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
		}

	}

	/**
	 * This method used to get album comment list
	 * 
	 * @param albumId
	 *            represented album id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getAlbumCommentList(final String albumId, final WebCallListener target) {

		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();
					iw.addWSParam(EXTNAME, JOMSOCIAL);
					iw.addWSParam(EXTVIEW, MEDIA);
					iw.addWSParam(EXTTASK, COMMENTS);

					JSONObject taskData = new JSONObject();
					try {
						taskData.put(PAGENO, "" + getPageNo());
						taskData.put(UNIQUEID, "" + albumId);
						taskData.put(TYPE, ALBUMS);
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
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(PAGELIMIT);
							iw.getJsonObject().remove(TOTAL);
							return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
						} catch (Throwable e) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					isCalling = false;
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				}
			}.execute();
		} else {
			target.onProgressUpdate(100);
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
		}

	}

	/**
	 * This method used to get photo comment list.
	 * 
	 * @param photoId
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getPhotoCommentList(final String photoId, final WebCallListener target) {

		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();
					iw.addWSParam(EXTNAME, JOMSOCIAL);
					iw.addWSParam(EXTVIEW, MEDIA);
					iw.addWSParam(EXTTASK, COMMENTS);

					JSONObject taskData = new JSONObject();
					try {
						taskData.put(PAGENO, "" + getPageNo());
						taskData.put(UNIQUEID, "" + photoId);
						taskData.put(TYPE, PHOTOS);
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
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(PAGELIMIT);
							iw.getJsonObject().remove(TOTAL);
							return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
						} catch (Throwable e) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					isCalling = false;
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				}
			}.execute();
		} else {
			target.onProgressUpdate(100);
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
		}

	}

	/**
	 * This method used to get video comment list.
	 * 
	 * @param videoId
	 *            represented video id.
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getVideoCommentList(final String videoId, final WebCallListener target) {

		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();
					iw.addWSParam(EXTNAME, JOMSOCIAL);
					iw.addWSParam(EXTVIEW, MEDIA);
					iw.addWSParam(EXTTASK, COMMENTS);

					JSONObject taskData = new JSONObject();
					try {
						taskData.put(PAGENO, "" + getPageNo());
						taskData.put(UNIQUEID, "" + videoId);
						taskData.put(TYPE, VIDEOS);
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
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(PAGELIMIT);
							iw.getJsonObject().remove(TOTAL);
							return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
						} catch (Throwable e) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					isCalling = false;
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				}
			}.execute();
		} else {
			target.onProgressUpdate(100);
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
		}

	}

	/**
	 * This method used to like album.
	 * 
	 * @param uniqueId
	 *            represented album id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void likeAlbum(final String uniqueId, final WebCallListener target) {
		final IjoomerWebService iw = new IjoomerWebService();
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, LIKE);
				JSONObject taskData = new JSONObject();

				try {
					taskData.put(UNIQUEID, uniqueId);
					taskData.put(TYPE, ALBUM);
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
	 * This method used to unlike album.
	 * 
	 * @param uniqueId
	 *            represented album id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void unlikeAlbum(final String uniqueId, final WebCallListener target) {
		final IjoomerWebService iw = new IjoomerWebService();
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, UNLIKE);
				JSONObject taskData = new JSONObject();

				try {
					taskData.put(UNIQUEID, uniqueId);
					taskData.put(TYPE, ALBUM);
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
	 * This method used to dislike album.
	 * 
	 * @param uniqueId
	 *            represented album id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void dislikeAlbum(final String uniqueId, final WebCallListener target) {
		final IjoomerWebService iw = new IjoomerWebService();
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, DISLIKE);
				JSONObject taskData = new JSONObject();

				try {
					taskData.put(UNIQUEID, uniqueId);
					taskData.put(TYPE, ALBUM);
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
	 * This method used to like photo.
	 * 
	 * @param uniqueId
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void likePhoto(final String uniqueId, final WebCallListener target) {
		final IjoomerWebService iw = new IjoomerWebService();
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, LIKE);
				JSONObject taskData = new JSONObject();

				try {
					taskData.put(UNIQUEID, uniqueId);
					taskData.put(TYPE, PHOTO);
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
	 * This method used to unlike photo.
	 * 
	 * @param uniqueId
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void unlikePhoto(final String uniqueId, final WebCallListener target) {
		final IjoomerWebService iw = new IjoomerWebService();
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, UNLIKE);
				JSONObject taskData = new JSONObject();

				try {
					taskData.put(UNIQUEID, uniqueId);
					taskData.put(TYPE, PHOTO);
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
	 * This method used to dislike photo.
	 * 
	 * @param uniqueId
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void dislikePhoto(final String uniqueId, final WebCallListener target) {
		final IjoomerWebService iw = new IjoomerWebService();
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, DISLIKE);
				JSONObject taskData = new JSONObject();

				try {
					taskData.put(UNIQUEID, uniqueId);
					taskData.put(TYPE, PHOTO);
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
	 * This method used to like video.
	 * 
	 * @param uniqueId
	 *            represented video id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void likeVideo(final String uniqueId, final WebCallListener target) {
		final IjoomerWebService iw = new IjoomerWebService();
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, LIKE);
				JSONObject taskData = new JSONObject();

				try {
					taskData.put(UNIQUEID, uniqueId);
					taskData.put(TYPE, VIDEOS);
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
	 * This method used to unlike video.
	 * 
	 * @param uniqueId
	 *            represented video id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void unlikeVideo(final String uniqueId, final WebCallListener target) {
		final IjoomerWebService iw = new IjoomerWebService();
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, UNLIKE);
				JSONObject taskData = new JSONObject();

				try {
					taskData.put(UNIQUEID, uniqueId);
					taskData.put(TYPE, VIDEOS);
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
	 * This method used to dislike video.
	 * 
	 * @param uniqueId
	 *            represented video id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void dislikeVideo(final String uniqueId, final WebCallListener target) {
		final IjoomerWebService iw = new IjoomerWebService();
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, DISLIKE);
				JSONObject taskData = new JSONObject();

				try {
					taskData.put(UNIQUEID, uniqueId);
					taskData.put(TYPE, VIDEOS);
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
	 * This method used to upload photo.
	 * 
	 * @param filePath
	 *            represented photo file path
	 * @param albumId
	 *            represented album id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void uploadPhoto(final String caption, final String voicePath, final String filePath, final String albumId, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, UPLOADPHOTO);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(ALBUMID, albumId);
					taskData.put(ISDEFAULT, "0");
					if (caption != null) {
						taskData.put(CAPTION, caption);
					}

				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);

				if (voicePath != null) {
					String[] files = new String[] { voicePath, filePath };
					iw.WSCall(files, new ProgressListener() {

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
				}

				if (validateResponse(iw.getJsonObject())) {
				}

				return null;
			}

			protected void onPostExecute(Void result) {
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			};
		}.execute();

	}
	
	public void uploadPhoto(final String caption, final String voicePath, final String[] filePath, final String albumId, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, UPLOADPHOTO);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(IMAGECOUNT, filePath.length);
					taskData.put(ALBUMID, albumId);
					taskData.put(ISDEFAULT, "0");
					if (caption != null) {
						taskData.put(CAPTION, caption);
					}

				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);

				if (voicePath != null) {
					String[] files = new String[filePath.length+1];
					files[0] = voicePath;
					for(int i=1;i<=filePath.length;i++){
						files[i] = filePath[i-1];
					}
					iw.WSCall(files, new ProgressListener() {

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
				}

				if (validateResponse(iw.getJsonObject())) {
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
	 * This method used to upload photo on default album.
	 * 
	 * @param filePath
	 *            represented photo file path
	 * @param photoCaption
	 *            represented photo caption (optional)
	 * @param voiceFilePath
	 *            represented photo voice caption (optional)
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void uploadPhotoDefaultAlbum(final String filePath, final String photoCaption, final String voiceFilePath, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, UPLOADPHOTO);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(ISDEFAULT, "0");
					taskData.put(PROFILE, "true");
					taskData.put(CAPTION, photoCaption);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);

				if (voiceFilePath != null) {
					iw.WSCall(new String[] { filePath, voiceFilePath }, new ProgressListener() {

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
				}

				if (validateResponse(iw.getJsonObject())) {
				}

				return null;
			}

			protected void onPostExecute(Void result) {
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();

	}

	/**
	 * This method used to get video category list
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getVideoCategoryList(final WebCallListener target) {

		isCalling = true;
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, VIDEOCATEGORIES);

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
					try {
						return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
					} catch (Throwable e) {
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				isCalling = false;
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();

	}

	/**
	 * This method used to get all video list.
	 * 
	 * @param categoryID
	 *            represented category id (optional - set 0 if not required)
	 * @param groupID
	 *            represented groupID(0- for video,groupID- for group video)
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getAllVideo(final String categoryID, final String groupID, final WebCallListener target) {

		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();
					iw.addWSParam(EXTNAME, JOMSOCIAL);
					iw.addWSParam(EXTVIEW, MEDIA);
					iw.addWSParam(EXTTASK, ALLVIDEOS);

					JSONObject taskData = new JSONObject();

					try {
						if (!categoryID.equals("0")) {
							taskData.put(CATEGORYID, categoryID);
						}
						if (!groupID.equals("0")) {
							taskData.put(GROUPID, groupID);
						}
						taskData.put(PRIVACY, "0");
						taskData.put(SORT, "latest");
						taskData.put(PAGENO, "" + getPageNo());
						taskData.put(WITHLIMIT, "TRUE");
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
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(PAGELIMIT);
							iw.getJsonObject().remove(TOTAL);
							return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
						} catch (Throwable e) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					isCalling = false;
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				}
			}.execute();
		} else {
			target.onProgressUpdate(100);
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
		}

	}

	/**
	 * This method used to get my video list.
	 * 
	 * @param userID
	 *            represented user id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getMyVideo(final String userID, final WebCallListener target) {

		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();
					iw.addWSParam(EXTNAME, JOMSOCIAL);
					iw.addWSParam(EXTVIEW, MEDIA);
					iw.addWSParam(EXTTASK, MYVIDEOS);

					JSONObject taskData = new JSONObject();

					try {
						if (!userID.equals("0")) {
							taskData.put(USERID, userID);
						}
						taskData.put(SORT, "latest");
						taskData.put(PAGENO, getPageNo());
						taskData.put(WITHLIMIT, "TRUE");
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
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(PAGELIMIT);
							iw.getJsonObject().remove(TOTAL);
							return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
						} catch (Throwable e) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					isCalling = false;
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				}
			}.execute();
		} else {
			target.onProgressUpdate(100);
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
		}

	}

	/**
	 * This method used to search video list.
	 * 
	 * @param query
	 *            represented search query
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getSearchVideo(final String query, final WebCallListener target) {

		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();
					iw.addWSParam(EXTNAME, JOMSOCIAL);
					iw.addWSParam(EXTVIEW, MEDIA);
					iw.addWSParam(EXTTASK, SEARCHVIDEO);

					JSONObject taskData = new JSONObject();

					try {

						taskData.put(PAGENO, getPageNo());
						taskData.put(QUERY, query);
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
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(PAGELIMIT);
							iw.getJsonObject().remove(TOTAL);
							return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
						} catch (Throwable e) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					isCalling = false;
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				}
			}.execute();
		} else {
			target.onProgressUpdate(100);
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
		}

	}

	/**
	 * This method used to get photo tag list.
	 * 
	 * @param photoID
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getPhotoTages(final String photoID, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, TAGS);

				JSONObject taskData = new JSONObject();

				try {

					taskData.put(UNIQUEID, photoID);
					taskData.put(TYPE, PHOTOS);
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
					try {
						return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
					} catch (Throwable e) {
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
	 * This method used to get video tag list.
	 * 
	 * @param videoID
	 *            represented video id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getVideosTages(final String videoID, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, TAGS);

				JSONObject taskData = new JSONObject();

				try {

					taskData.put(UNIQUEID, videoID);
					taskData.put(TYPE, VIDEOS);
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
					try {
						return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
					} catch (Throwable e) {
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
	 * This method used to report photo.
	 * 
	 * @param userID
	 *            represented user id
	 * @param albumId
	 *            represented album id
	 * @param photoID
	 *            represented photo id
	 * @param message
	 *            represented report message
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void reportPhoto(final String userID, final String albumId, final String photoID, final String message, final WebCallListener target) {
		final IjoomerWebService iw = new IjoomerWebService();
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, REPORT);
				JSONObject taskData = new JSONObject();

				try {
					taskData.put(UNIQUEID, photoID);
					taskData.put(ALBUMID, albumId);
					try {
						if (!userID.equals("0")) {
							taskData.put(USERID, userID);
						}
					} catch (Throwable e) {
					}
					taskData.put(MESSAGE, message);
					taskData.put(TYPE, PHOTOS);
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
	 * This method used to report video.
	 * 
	 * @param userID
	 *            represented user id
	 * @param videoID
	 *            represented video id
	 * @param message
	 *            represented report message
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void reportVideo(final String userID, final String videoID, final String message, final WebCallListener target) {
		final IjoomerWebService iw = new IjoomerWebService();
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, REPORT);
				JSONObject taskData = new JSONObject();

				try {
					taskData.put(UNIQUEID, videoID);
					try {
						if (!userID.equals("0")) {
							taskData.put(USERID, userID);
						}
					} catch (Throwable e) {
					}
					taskData.put(MESSAGE, message);
					taskData.put(TYPE, VIDEOS);
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
	 * This method used to add photo tag.
	 * 
	 * @param photoID
	 *            represented photo id
	 * @param userId
	 *            represented user id
	 * @param position
	 *            represented tag position
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void addPhotoTag(final String photoID, final String userId, final String position, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, ADDTAG);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, photoID);
					taskData.put(POSITION, position);
					taskData.put(TYPE, PHOTOS);
					if (userId != null && !userId.equals("0")) {
						taskData.put(USERID, userId);
					}
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
	 * This method used to remove photo tag.
	 * 
	 * @param tagID
	 *            represented tag id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void removePhotoTag(final String tagID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, REMOVETAG);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, tagID);
					taskData.put(TYPE, PHOTOS);
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
	 * This method used to add video tag.
	 * 
	 * @param videoID
	 *            represented video id
	 * @param userId
	 *            represented user id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void addVideoTag(final String videoID, final String userId, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, ADDTAG);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, videoID);
					taskData.put(POSITION, "0.0,0.0,0.0,0.0");
					taskData.put(TYPE, VIDEOS);
					if (userId != null && !userId.equals("0")) {
						taskData.put(USERID, userId);
					}
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
	 * This method used to remove video tag.
	 * 
	 * @param tagID
	 *            represented tag id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void removeVideoTag(final String tagID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, REMOVETAG);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, tagID);
					taskData.put(TYPE, VIDEOS);
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
	 * This method used to set photo as cover page.
	 * 
	 * @param albumID
	 *            represented album id
	 * @param photoID
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void setAsCoverPage(final String albumID, final String photoID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, SETCOVER);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, photoID);
					taskData.put(ALBUMID, albumID);
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
	 * This method used to set Profile Cover Photo
	 * 
	 * @param photoID
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void setProfileCover(final String photoID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, SETUSERCOVER);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, photoID);
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
	 * This method used to set Profile Cover Photo
	 * 
	 * @param photoID
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void setProfileCoverV30(final String photoID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, SETPHOTOCOVER);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(TYPE, PROFILE);
					taskData.put(PHOTOID, photoID);
					taskData.put(UNIQUEID, "0");
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
	 * This method used to set Group Cover Photo
	 * 
	 * @param photoID
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void setGroupCoverV30(final String groupID, final String photoID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, SETPHOTOCOVER);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(TYPE, GROUP);
					taskData.put(PHOTOID, photoID);
					taskData.put(UNIQUEID, groupID);
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
	 * This method used to set Group Cover Photo
	 * 
	 * @param photoID
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void setEventCoverV30(final String eventID, final String photoID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, SETPHOTOCOVER);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(TYPE, EVENT);
					taskData.put(PHOTOID, photoID);
					taskData.put(UNIQUEID, eventID);
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
	 * This method used to set photo as profile picture.
	 * 
	 * @param photoID
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void setAsProfileAvatar(final String photoID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, SETAVATAR);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, photoID);
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
	 * This method used to set video as profile video.
	 * 
	 * @param videoID
	 *            represented video id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void setAsProfileVideo(final String videoID, final WebCallListener target) {
		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			IjoomerWebService iw = new IjoomerWebService();

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, MEDIA);
				iw.addWSParam(EXTTASK, SETPROFILEVIDEO);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(UNIQUEID, videoID);
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
	 * This method id used for getting FriendList to tag video
	 * 
	 * @param videoID
	 *            represented video id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getFriendsForTagVideo(final String videoID, final String searchKeyword, final WebCallListener target) {

		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();
					iw.addWSParam(EXTNAME, JOMSOCIAL);
					iw.addWSParam(EXTVIEW, MEDIA);
					iw.addWSParam(EXTTASK, TAGFRIENDS);

					JSONObject taskData = new JSONObject();
					try {
						taskData.put(PAGENO, "" + getPageNo());
						taskData.put(UNIQUEID, videoID);
						taskData.put(TYPE, VIDEOS);
						if (searchKeyword != null) {
							taskData.put(KEYWORD, searchKeyword);
						}

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
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(PAGELIMIT);
							iw.getJsonObject().remove(TOTAL);
							return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
						} catch (Throwable e) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					isCalling = false;
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				}
			}.execute();
		} else {
			target.onProgressUpdate(100);
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
		}

	}

	/**
	 * This method is used for getting FriendList to tag photo
	 * 
	 * @param photoId
	 *            represented photo id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getFriendsForTagPhoto(final String photoId, final String searchKeyword, final WebCallListener target) {

		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();
					iw.addWSParam(EXTNAME, JOMSOCIAL);
					iw.addWSParam(EXTVIEW, MEDIA);
					iw.addWSParam(EXTTASK, TAGFRIENDS);

					JSONObject taskData = new JSONObject();
					try {
						taskData.put(PAGENO, "" + getPageNo());
						taskData.put(UNIQUEID, photoId);
						taskData.put(TYPE, PHOTOS);
						if (searchKeyword != null) {
							taskData.put(KEYWORD, searchKeyword);
						}
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
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(PAGELIMIT);
							iw.getJsonObject().remove(TOTAL);
							return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
						} catch (Throwable e) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					isCalling = false;
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				}
			}.execute();
		} else {
			target.onProgressUpdate(100);
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
		}

	}
}
