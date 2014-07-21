package com.ijoomer.weservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.RFC2109Spec;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.media.MediaPlayer;
import android.text.format.DateFormat;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.MimeTypeMap;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;

/**
 * A Class Used For Generalize Call Of Web Service.
 * 
 * @author tasol
 * 
 */
public class IjoomerWebService  {

	public static List<Cookie> cookies;

	public JSONObject WSParameter;

	public JSONObject getWSParameter() {
		return WSParameter;
	}

	public JSONObject validatior;

	public InputStream responseInputStream;

	public String testUrl;
	public String response;
	public String domainTail = "index.php?option=com_ijoomeradv";
	private String charset;
	private String contentType = "";
	public String XML = "xml";
	public String JSON = "json";
	private String reqObject;

	private long totalLength = 0;
	private long startTime;
	private long endTime;
	public int timeOut = 600000;
	private boolean wasSessionExpired;

	public HttpPost httppost = new HttpPost(IjoomerApplicationConfiguration.getDomainName() + domainTail);
	public DefaultHttpClient httpclient;

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	/**
	 * A method used for testing purpose
	 * 
	 * @return
	 */
	public String getTestUrl() {
		return testUrl;
	}

	/**
	 * A method used to set test url
	 * 
	 * @param testUrl
	 */
	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

	/**
	 * A method used to get response content type(xml or json).
	 * 
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * A method used to set response content type(xml or json).
	 * 
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * A method used to add web service call parameter
	 * 
	 * @param parameterName
	 *            represent key as string
	 * @param parameterValue
	 *            represent value as string
	 */
	public void addWSParam(String parameterName, String parameterValue) {
		try {
			WSParameter.put(parameterName, parameterValue);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * A method used to add web service call parameter
	 * 
	 * @param parameterName
	 *            represent key as string
	 * @param parameterValue
	 *            represent value as Object
	 */
	public void addWSParam(String parameterName, Object parameterValue) {
		try {
			WSParameter.put(parameterName, parameterValue);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * A method used to add web service call parameter
	 * 
	 * @param parameterValue
	 *            represent value as Object
	 */
	public void addWSParam(Object parameterValue) {
		try {
			WSParameter = (JSONObject) parameterValue;
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private boolean isHTTPSEnabled() {

		String domain = getTestUrl() != null ? getTestUrl() : IjoomerApplicationConfiguration.getDomainName();

		if (domain.startsWith("https")) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isValidResponse(HttpResponse wsResponse) {

		if (wsResponse.getStatusLine().getStatusCode() == 302 || wsResponse.getStatusLine().getStatusCode() == 301) {
			String redirectURL = wsResponse.getFirstHeader("Location").getValue();
			redirectURL = redirectURL.replace(domainTail, "");
			IjoomerApplicationConfiguration.setDomainName(redirectURL);
			httppost = new HttpPost(IjoomerApplicationConfiguration.getDomainName() + domainTail);
			cookies = null;
			return false;
		} else {
			return true;
		}
	}

	/**
	 * A method used to call web service
	 * 
	 * @param target
	 *            represent {@link ProgressListener}
	 */
	public void WSCall(final ProgressListener target) {

		if (IjoomerUtilities.isNetwokReachable()) {

			reqObject = WSParameter.toString();
			startTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("WSRequest= " + IjoomerApplicationConfiguration.getDomainName() + domainTail + "\n" + "WSParam = "
					+ WSParameter.toString());

			try {

				StringBody data = new StringBody(WSParameter.toString(), Charset.forName(HTTP.UTF_8));
				CustomMultiPartEntity entity = new CustomMultiPartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, new ProgressListener() {

					@Override
					public void transferred(long num) {
						System.out.println("Tranfered : " + num);
						long progressCount = (long) ((num * 100) / totalLength);
						if (wasSessionExpired && progressCount <= 99) {
							target.transferred(99);
						} else {
							wasSessionExpired = false;
							target.transferred(progressCount);
						}
					}
				});
				entity.addPart("reqObject", data);
				httppost.setEntity(entity);
				totalLength = entity.getContentLength();
				System.out.println("TotalLength : " + totalLength);
				httpclient = getHttpclient();
				HttpResponse WSresponse = httpclient.execute(httppost);
				if (!isValidResponse(WSresponse)) {
					httpclient.getConnectionManager().closeExpiredConnections();
					WSCall(target);

				} else {
					this.response = getResponseBody(WSresponse.getEntity());

					System.out.println("WSResponse : " + this.response);
					try {
						cookies = httpclient.getCookieStore().getCookies();
						sync();
					} catch (Exception e) {
					}
					httpclient.getConnectionManager().closeExpiredConnections();

					if (getContentType() == JSON) {
						try {
							validatior = new JSONObject(this.response);
							if (validatior.getString("code").equals("704")) {
								login();
								WSCall(target);
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}

			} catch (Throwable e) {

				try {
					validatior = new JSONObject();
					validatior.put("code", "599");
				} catch (JSONException ee) {
					e.printStackTrace();
				}

				this.response = new String("false");
				System.out.println("WSResponse= " + this.response);
				StringWriter stackTrace = new StringWriter();
				e.printStackTrace(new PrintWriter(stackTrace));
				e.printStackTrace();
				return;
			}

		} else {
			this.response = new String("false");
			try {
				validatior = new JSONObject();
				validatior.put("code", "599");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		endTime = Calendar.getInstance().getTimeInMillis();
		appendLog("");
	}

	/**
	 * A method used to call web service
	 * 
	 * @param target
	 *            represent {@link ProgressListener}
	 */
	public void WSCallGetIS(final ProgressListener target) {
		if (IjoomerUtilities.isNetwokReachable()) {
			reqObject = WSParameter.toString();
			startTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("WSRequest= " + IjoomerApplicationConfiguration.getDomainName() + domainTail + "\n" + "WSParam = "
					+ WSParameter.toString());
			try {
				StringBody data = new StringBody(WSParameter.toString(), Charset.forName(HTTP.UTF_8));
				CustomMultiPartEntity entity = new CustomMultiPartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, new ProgressListener() {

					@Override
					public void transferred(long num) {
						System.out.println("Tranfered : " + num);
						long progressCount = (long) ((num * 100) / totalLength);
						if (wasSessionExpired && progressCount <= 99) {
							target.transferred(99);
						} else {
							wasSessionExpired = false;
							target.transferred(progressCount);
						}
					}
				});
				entity.addPart("reqObject", data);
				httppost.setEntity(entity);
				totalLength = entity.getContentLength();
				System.out.println("TotalLength : " + totalLength);
				httpclient = getHttpclient();
				HttpResponse WSresponse = httpclient.execute(httppost);
				if (!isValidResponse(WSresponse)) {
					httpclient.getConnectionManager().closeExpiredConnections();
					WSCall(target);
				} else {
					this.responseInputStream = WSresponse.getEntity().getContent();

					System.out.println("WSResponse : " + this.response);
					try {
						cookies = httpclient.getCookieStore().getCookies();
						sync();
					} catch (Exception e) {
					}
					httpclient.getConnectionManager().closeExpiredConnections();
				}
			} catch (Throwable e) {
				try {
					validatior = new JSONObject();
					validatior.put("code", "599");
				} catch (JSONException ee) {
					e.printStackTrace();
				}

				this.response = new String("false");
				System.out.println("WSResponse= " + this.response);
				StringWriter stackTrace = new StringWriter();
				e.printStackTrace(new PrintWriter(stackTrace));
				e.printStackTrace();
				return;
			}
		} else {
			this.response = new String("false");
			try {
				validatior = new JSONObject();
				validatior.put("code", "599");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		endTime = Calendar.getInstance().getTimeInMillis();
		appendLog("");
	}

	/**
	 * A method used to call web service.
	 * 
	 * @param filePath
	 *            represent file path for image or video
	 * @param target
	 *            represent {@link ProgressListener}
	 */
	public void WSCall(final String filePath, final ProgressListener target) {

		if (IjoomerUtilities.isNetwokReachable()) {

			reqObject = WSParameter.toString();
			startTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("WSRequest= " + IjoomerApplicationConfiguration.getDomainName() + domainTail + "\n" + "WSParam = "
					+ WSParameter.toString());
			try {

				StringBody data = new StringBody(WSParameter.toString(), Charset.forName(HTTP.UTF_8));
				CustomMultiPartEntity entity = new CustomMultiPartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, new ProgressListener() {

					@Override
					public void transferred(long num) {
						System.out.println("Tranfered : " + num);
						long progressCount = (long) ((num * 100) / totalLength);
						if (wasSessionExpired && progressCount <= 99) {
							target.transferred(99);
						} else {
							wasSessionExpired = false;
							target.transferred(progressCount);
						}
					}
				});

				entity.addPart("reqObject", data);
				if (getMimeType(filePath) != null && getMimeType(filePath).contains("image")) {
					entity.addPart("image", new FileBody(new File(filePath), getMimeType(filePath)));
				} else if (!is3gpFileVideo(new File(filePath))) {
					entity.addPart("voice", new FileBody(new File(filePath), "audio/3gp"));
				} else {
					entity.addPart("video", new FileBody(new File(filePath), getMimeType(filePath)));
				}
				httppost.setEntity(entity);
				totalLength = entity.getContentLength();
				System.out.println("TotalLength : " + totalLength);
				httpclient = getHttpclient();
				HttpResponse WSresponse = httpclient.execute(httppost);

				if (!isValidResponse(WSresponse)) {
					httpclient.getConnectionManager().closeExpiredConnections();
					WSCall(filePath, target);

				} else {
					this.response = getResponseBody(WSresponse.getEntity());

					System.out.println("WSResponse : " + this.response);
					try {
						cookies = httpclient.getCookieStore().getCookies();
						sync();
					} catch (Exception e) {
					}
					httpclient.getConnectionManager().closeExpiredConnections();

					if (getContentType() == JSON) {
						try {
							validatior = new JSONObject(this.response);
							if (validatior.getString("code").equals("704")) {
								login();
								WSCall(filePath, target);
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Throwable e) {

				try {
					validatior = new JSONObject();
					validatior.put("code", "599");
				} catch (JSONException ee) {
					e.printStackTrace();
				}
				this.response = new String("false");
				System.out.println("WSResponse= " + this.response);
				StringWriter stackTrace = new StringWriter();
				e.printStackTrace(new PrintWriter(stackTrace));
				e.printStackTrace();
				return;
			}

		} else {
			this.response = new String("false");
			try {
				validatior = new JSONObject();
				validatior.put("code", "599");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		endTime = Calendar.getInstance().getTimeInMillis();
		appendLog("");
	}

	/**
	 * A method used to call web service.
	 * 
	 * @param filePath
	 *            represent file path for image or video
	 * @param target
	 *            represent {@link ProgressListener}
	 */
	public void WSCallMedia(final String filePath, final ProgressListener target) {

		if (IjoomerUtilities.isNetwokReachable()) {

			reqObject = WSParameter.toString();
			startTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("WSRequest= " + IjoomerApplicationConfiguration.getDomainName() + domainTail + "\n" + "WSParam = "
					+ WSParameter.toString());
			try {

				StringBody data = new StringBody(WSParameter.toString(), Charset.forName(HTTP.UTF_8));
				CustomMultiPartEntity entity = new CustomMultiPartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, new ProgressListener() {

					@Override
					public void transferred(long num) {
						System.out.println("Tranfered : " + num);
						long progressCount = (long) ((num * 100) / totalLength);
						if (wasSessionExpired && progressCount <= 99) {
							target.transferred(99);
						} else {
							wasSessionExpired = false;
							target.transferred(progressCount);
						}
					}
				});

				entity.addPart("reqObject", data);
				entity.addPart("image", new FileBody(new File(filePath), getMimeType(filePath)));
				httppost.setEntity(entity);
				totalLength = entity.getContentLength();
				System.out.println("TotalLength : " + totalLength);
				httpclient = getHttpclient();
				HttpResponse WSresponse = httpclient.execute(httppost);

				if (!isValidResponse(WSresponse)) {
					httpclient.getConnectionManager().closeExpiredConnections();
					WSCall(filePath, target);

				} else {
					this.response = getResponseBody(WSresponse.getEntity());

					System.out.println("WSResponse : " + this.response);
					try {
						cookies = httpclient.getCookieStore().getCookies();
						sync();
					} catch (Exception e) {
					}
					httpclient.getConnectionManager().closeExpiredConnections();

					if (getContentType() == JSON) {
						try {
							validatior = new JSONObject(this.response);
							if (validatior.getString("code").equals("704")) {
								login();
								WSCall(filePath, target);
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Throwable e) {

				try {
					validatior = new JSONObject();
					validatior.put("code", "599");
				} catch (JSONException ee) {
					e.printStackTrace();
				}
				this.response = new String("false");
				System.out.println("WSResponse= " + this.response);
				StringWriter stackTrace = new StringWriter();
				e.printStackTrace(new PrintWriter(stackTrace));
				e.printStackTrace();
				return;
			}

		} else {
			this.response = new String("false");
			try {
				validatior = new JSONObject();
				validatior.put("code", "599");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		endTime = Calendar.getInstance().getTimeInMillis();
		appendLog("");
	}

	public void WSCall(final String filePaths[], final ProgressListener target) {

		if (IjoomerUtilities.isNetwokReachable()) {

			reqObject = WSParameter.toString();
			startTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("WSRequest= " + IjoomerApplicationConfiguration.getDomainName() + domainTail + "\n" + "WSParam = "
					+ WSParameter.toString());
			try {

				StringBody data = new StringBody(WSParameter.toString(), Charset.forName(HTTP.UTF_8));
				CustomMultiPartEntity entity = new CustomMultiPartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, new ProgressListener() {

					@Override
					public void transferred(long num) {
						System.out.println("Tranfered : " + num);
						long progressCount = (long) ((num * 100) / totalLength);
						if (wasSessionExpired && progressCount <= 99) {
							target.transferred(99);
						} else {
							wasSessionExpired = false;
							target.transferred(progressCount);
						}
					}
				});

				entity.addPart("reqObject", data);
				for (int i = 0; i < filePaths.length; i++) {
					if (getMimeType(filePaths[i]) != null && getMimeType(filePaths[i]).contains("image")) {
						entity.addPart("image"+(i+1), new FileBody(new File(filePaths[i]), getMimeType(filePaths[i])));
					} else if (!is3gpFileVideo(new File(filePaths[i]))) {
						entity.addPart("voice", new FileBody(new File(filePaths[i]), "audio/3gp"));
					} else {
						entity.addPart("video", new FileBody(new File(filePaths[i]), getMimeType(filePaths[i])));
					}
				}
				httppost.setEntity(entity);
				totalLength = entity.getContentLength();
				System.out.println("TotalLength : " + totalLength);
				httpclient = getHttpclient();
				HttpResponse WSresponse = httpclient.execute(httppost);

				if (!isValidResponse(WSresponse)) {
					httpclient.getConnectionManager().closeExpiredConnections();
					WSCall(filePaths, target);

				} else {
					this.response = getResponseBody(WSresponse.getEntity());

					System.out.println("WSResponse : " + this.response);
					try {
						cookies = httpclient.getCookieStore().getCookies();
						sync();
					} catch (Exception e) {
					}
					httpclient.getConnectionManager().closeExpiredConnections();

					if (getContentType() == JSON) {
						try {
							validatior = new JSONObject(this.response);
							if (validatior.getString("code").equals("704")) {
								login();
								WSCall(filePaths, target);
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}

			} catch (Throwable e) {

				try {
					validatior = new JSONObject();
					validatior.put("code", "599");
				} catch (JSONException ee) {
					e.printStackTrace();
				}
				this.response = new String("false");
				System.out.println("WSResponse= " + this.response);
				StringWriter stackTrace = new StringWriter();
				e.printStackTrace(new PrintWriter(stackTrace));
				e.printStackTrace();
				return;
			}

		} else {
			this.response = new String("false");
			try {
				validatior = new JSONObject();
				validatior.put("code", "599");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		endTime = Calendar.getInstance().getTimeInMillis();
		appendLog("");
	}

	public void WSCall(final ArrayList<HashMap<String, String>> filePath, final ProgressListener target) {

		if (IjoomerUtilities.isNetwokReachable()) {

			reqObject = WSParameter.toString();
			startTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("WSRequest= " + IjoomerApplicationConfiguration.getDomainName() + domainTail + "\n" + "WSParam = "
					+ WSParameter.toString());
			try {

				StringBody data = new StringBody(WSParameter.toString(), Charset.forName(HTTP.UTF_8));
				CustomMultiPartEntity entity = new CustomMultiPartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, new ProgressListener() {

					@Override
					public void transferred(long num) {
						System.out.println("Tranfered : " + num);
						long progressCount = (long) ((num * 100) / totalLength);
						if (wasSessionExpired && progressCount <= 99) {
							target.transferred(99);
						} else {
							wasSessionExpired = false;
							target.transferred(progressCount);
						}
					}
				});

				entity.addPart("reqObject", data);
				for (int i = 0; i < filePath.size(); i++) {
					Iterator<String> itr = filePath.get(i).keySet().iterator();
					while (itr.hasNext()) {
						String rowKey = itr.next();
						entity.addPart(rowKey,
								new FileBody(new File(filePath.get(i).get(rowKey)), getMimeType(filePath.get(i).get(rowKey))));
					}
				}
				httppost.setEntity(entity);
				totalLength = entity.getContentLength();
				System.out.println("TotalLength : " + totalLength);
				httpclient = getHttpclient();
				HttpResponse WSresponse = httpclient.execute(httppost);
				if (!isValidResponse(WSresponse)) {
					httpclient.getConnectionManager().closeExpiredConnections();
					WSCall(filePath, target);

				} else {
					this.response = getResponseBody(WSresponse.getEntity());

					System.out.println("WSResponse : " + this.response);
					try {
						cookies = httpclient.getCookieStore().getCookies();
						sync();
					} catch (Exception e) {
					}
					httpclient.getConnectionManager().closeExpiredConnections();

					if (getContentType() == JSON) {
						try {
							validatior = new JSONObject(this.response);
							if (validatior.getString("code").equals("704")) {
								login();
								WSCall(filePath, target);
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}

			} catch (Throwable e) {

				try {
					validatior = new JSONObject();
					validatior.put("code", "599");
				} catch (JSONException ee) {
					e.printStackTrace();
				}
				this.response = new String("false");
				System.out.println("WSResponse= " + this.response);
				StringWriter stackTrace = new StringWriter();
				e.printStackTrace(new PrintWriter(stackTrace));
				e.printStackTrace();
				return;
			}

		} else {
			this.response = new String("false");
			try {
				validatior = new JSONObject();
				validatior.put("code", "599");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		endTime = Calendar.getInstance().getTimeInMillis();
		appendLog("");
	}

	/**
	 * A method used to call web service.
	 * 
	 * @param keyName
	 *            represent key
	 * @param filePath
	 *            represent file path for image or video
	 * @param target
	 *            represent {@link ProgressListener}
	 */
	public void WSCall(final String keyName, final String filePath, final ProgressListener target) {

		if (IjoomerUtilities.isNetwokReachable()) {

			reqObject = WSParameter.toString();
			startTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("WSRequest= " + IjoomerApplicationConfiguration.getDomainName() + domainTail + "\n" + "WSParam = "
					+ WSParameter.toString());
			try {

				StringBody data = new StringBody(WSParameter.toString(), Charset.forName(HTTP.UTF_8));
				CustomMultiPartEntity entity = new CustomMultiPartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, new ProgressListener() {

					@Override
					public void transferred(long num) {
						System.out.println("Tranfered : " + num);
						long progressCount = (long) ((num * 100) / totalLength);
						if (wasSessionExpired && progressCount <= 99) {
							target.transferred(99);
						} else {
							wasSessionExpired = false;
							target.transferred(progressCount);
						}
					}
				});

				entity.addPart("reqObject", data);
				entity.addPart(keyName, new FileBody(new File(filePath)));
				httppost.setEntity(entity);
				totalLength = entity.getContentLength();
				System.out.println("TotalLength : " + totalLength);
				httpclient = getHttpclient();
				HttpResponse WSresponse = httpclient.execute(httppost);
				if (!isValidResponse(WSresponse)) {
					httpclient.getConnectionManager().closeExpiredConnections();
					WSCall(keyName, filePath, target);

				} else {
					this.response = getResponseBody(WSresponse.getEntity());

					System.out.println("WSResponse : " + this.response);
					try {
						cookies = httpclient.getCookieStore().getCookies();
						sync();
					} catch (Exception e) {
					}
					httpclient.getConnectionManager().closeExpiredConnections();

					if (getContentType() == JSON) {
						try {
							validatior = new JSONObject(this.response);
							if (validatior.getString("code").equals("704")) {
								login();
								WSCall(keyName, filePath, target);
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}

			} catch (Throwable e) {

				try {
					validatior = new JSONObject();
					validatior.put("code", "599");
				} catch (JSONException ee) {
					e.printStackTrace();
				}
				this.response = new String("false");
				System.out.println("WSResponse= " + this.response);
				StringWriter stackTrace = new StringWriter();
				e.printStackTrace(new PrintWriter(stackTrace));
				e.printStackTrace();
				return;
			}

		} else {
			this.response = new String("false");
			try {
				validatior = new JSONObject();
				validatior.put("code", "599");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		endTime = Calendar.getInstance().getTimeInMillis();
		appendLog("");
	}

	/**
	 * A method used to reset request / response object.
	 */
	public void reset() {

		WSParameter = new JSONObject();
		response = new String();
		validatior = new JSONObject();

		if (getTestUrl() != null) {
			httppost = new HttpPost(getTestUrl() + domainTail);
		}
	}

	/**
	 * A method used to get response from web service call.
	 * 
	 * @return
	 */
	public String getResponse() {
		return this.response;
	}

	/**
	 * A method used to get inputstream from web service call.
	 * 
	 * @return
	 */
	public InputStream getResponseInputStream() {
		return this.responseInputStream;
	}

	/**
	 * A method used to get json object from web service call.
	 * 
	 * @return
	 */
	public JSONObject getJsonObject() {
		return this.validatior;
	}

	/**
	 * A method used to get response body from web service call.
	 * 
	 * @param entity
	 * @return {@link String}
	 * @throws IOException
	 * @throws ParseException
	 */
	public String getResponseBody(final HttpEntity entity) throws IOException, ParseException {

		System.out.println("GEN START : " + Calendar.getInstance().getTimeInMillis());
		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}

		InputStream instream = entity.getContent();

		if (instream == null) {
			return "";
		}

		if (entity.getContentLength() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(

					"HTTP entity too large to be buffered in memory");
		}

		charset = getContentCharSet(entity);

		if (charset == null) {

			charset = HTTP.UTF_8;

		}
		StringBuilder buffer = new StringBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(instream, charset));

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

		} finally {
			instream.close();
			reader.close();
		}
		System.out.println("GEN END : " + Calendar.getInstance().getTimeInMillis());
		return buffer.toString();

	}

	/**
	 * A method used to get response content character set.
	 * 
	 * @param entity
	 * @return {@link String}
	 * @throws ParseException
	 */
	public String getContentCharSet(final HttpEntity entity) throws ParseException {

		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}

		String charset = null;

		if (entity.getContentType() != null) {

			HeaderElement values[] = entity.getContentType().getElements();

			if (values[0].getName() != null && values[0].getName().contains("json")) {
				setContentType("json");
			} else if (values[0].getName() != null && values[0].getName().contains("xml")) {
				setContentType("xml");
			} else {
				setContentType("json");
			}
			if (values.length > 0) {

				NameValuePair param = values[0].getParameterByName("charset");

				if (param != null) {

					charset = param.getValue();

				}

			}

		}

		return charset;

	}

	/**
	 * A method used to write log into file Ijoomer2.File on sdcard.
	 * 
	 * @param text
	 */
	public void appendLog(String text) {
		if (IjoomerApplicationConfiguration.isDebugOn()) {
			File logFile = new File("sdcard/" + "Ijoomer2.File");
			if (!logFile.exists()) {
				try {
					logFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
				buf.append("\n");
				buf.append(DateFormat.format(IjoomerApplicationConfiguration.dateTimeFormat, Calendar.getInstance()) + ";" + reqObject
						+ ";" + startTime + ";" + endTime);
				buf.newLine();
				buf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * A method used to get http client object.
	 * 
	 * @return {@link DefaultHttpClient}
	 */
	public DefaultHttpClient getHttpclient() {
		if (!isHTTPSEnabled()) {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeOut);
			HttpConnectionParams.setSoTimeout(httpParameters, timeOut);
			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpClientParams.setRedirecting(httpclient.getParams(), false);

			if (cookies != null) {
				int size = cookies.size();
				for (int i = 0; i < size; i++) {
					httpclient.getCookieStore().addCookie(cookies.get(i));
				}
			}
			httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android");
			return httpclient;
		} else {
			try {
				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);

				SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, timeOut);
				HttpConnectionParams.setSoTimeout(httpParameters, timeOut);
				HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);

				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
				registry.register(new Scheme("https", sf, 443));

				ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParameters, registry);
				DefaultHttpClient httpclient = new DefaultHttpClient(ccm, httpParameters);
				HttpClientParams.setRedirecting(httpclient.getParams(), false);

				if (cookies != null) {
					int size = cookies.size();
					for (int i = 0; i < size; i++) {
						httpclient.getCookieStore().addCookie(cookies.get(i));
					}
				}
				httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "android");
				return httpclient;

			} catch (Exception e) {
			}
		}
		return null;

	}

	/**
	 * A method used to get mime type of given file path of image or video.
	 * 
	 * @param filePath
	 * @return {@link String}
	 */
	public String getMimeType(String filePath) {
		String type = null;
		String extension = getFileExtensionFromUrl(filePath);
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}

	/**
	 * A method used to get extension type of given file path of image or video.
	 * 
	 * @param url
	 * @return {@link String}
	 */
	public String getFileExtensionFromUrl(String url) {
		int dotPos = url.lastIndexOf('.');
		if (0 <= dotPos) {
			return (url.substring(dotPos + 1)).toLowerCase();
		}

		return "";
	}


	/**
	 * A method used for auto login
	 */
	private void login() {
		wasSessionExpired = true;
		try {
			cookies = null;
			HttpPost httppost = new HttpPost(IjoomerApplicationConfiguration.getDomainName() + domainTail);
			StringBody data = new StringBody(IjoomerUtilities.getLoginParams().toString(), Charset.forName(HTTP.UTF_8));
			CustomMultiPartEntity entity = new CustomMultiPartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, new ProgressListener() {

				@Override
				public void transferred(long num) {
					System.out.println("Tranfered : " + num);
				}
			});

			entity.addPart("reqObject", data);
			httppost.setEntity(entity);
			totalLength = entity.getContentLength();
			System.out.println("TotalLength : " + totalLength);
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpResponse WSresponse = httpclient.execute(httppost);
			String response = getResponseBody(WSresponse.getEntity());
			System.out.println("WSResponse : " + response);
			try {
				cookies = httpclient.getCookieStore().getCookies();
			} catch (Exception e) {
			}
			httpclient.getConnectionManager().closeExpiredConnections();

		} catch (Throwable e) {

		}
	}

	@SuppressWarnings("resource")
	public boolean is3gpFileVideo(File mediaFile) {
		int height = 0;
		try {
			MediaPlayer mp = new MediaPlayer();
			FileInputStream fs;
			FileDescriptor fd;
			fs = new FileInputStream(mediaFile);
			fd = fs.getFD();
			mp.setDataSource(fd);
			mp.prepare();
			height = mp.getVideoHeight();
			mp.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return height > 0;
	}

	/**
	 * sync all the cookies of webview with the httpclient by generating cookie
	 * string.
	 */
	public void sync(String url) {

		CookieManager cookieManager = CookieManager.getInstance();
		if (cookieManager == null)
			return;

		RFC2109Spec cookieSpec = new RFC2109Spec();
		String rawCookieHeader = null;
		try {
			URL parsedURL = new URL(url);
			rawCookieHeader = cookieManager.getCookie(parsedURL.getHost());
			if (rawCookieHeader == null)
				return;
			int port = parsedURL.getPort() == -1 ? parsedURL.getDefaultPort() : parsedURL.getPort();

			CookieOrigin cookieOrigin = new CookieOrigin(parsedURL.getHost(), port, "/", false);
			cookies = cookieSpec.parse(new BasicHeader("set-cookie", rawCookieHeader), cookieOrigin);

		} catch (Exception e) {

		}
	}

	/**
	 * sync all the cookies of httpclient with the webview by generating cookie
	 * string.
	 */
	public void sync() {
		if (cookies != null) {

			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);
			for (Cookie cookie : cookies) {

				Cookie sessionInfo = cookie;
				String cookieString = sessionInfo.getName() + "=" + sessionInfo.getValue() + "; domain=" + sessionInfo.getDomain();
				cookieManager.setCookie(IjoomerApplicationConfiguration.getDomainName() + domainTail, cookieString);
				CookieSyncManager.getInstance().sync();
			}
		}
	}
}
