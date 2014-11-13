package com.ijoomer.customviews;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

import com.ijoomer.media.player.IjoomerMediaPlayer;

/**
 * This Class Contains All Method Related To IjoomerButton.
 * 
 * @author tasol
 * 
 */
public class IjoomerWebView extends WebView {

	private OnMimeResourceClickListener onMimeResourceClickListener;

	public OnMimeResourceClickListener getOnMimeResourceClickListener() {
		return onMimeResourceClickListener;
	}

	public void setOnMimeResourceClickListener(OnMimeResourceClickListener onMimeResourceClickListener) {
		this.onMimeResourceClickListener = onMimeResourceClickListener;
	}

	public IjoomerWebView(Context context) {

		super(context);
		init();
	}
	public IjoomerWebView(Context context,AttributeSet attr) {

		super(context,attr);
		init();
	}
	public IjoomerWebView(Context context,AttributeSet attr,int style) {

		super(context,attr,style);
		init();
	}
	public IjoomerWebView(Context context,AttributeSet attr,int style,boolean privateBrowsing) {
		super(context,attr,style,privateBrowsing);
		init();
	}


	
	private void init() {
		try {
			getSettings().setJavaScriptEnabled(true);
			getSettings().setPluginState(PluginState.ON);
			JavaScriptInterface jsInterface = new JavaScriptInterface(getContext());
			addJavascriptInterface(jsInterface, "JSInterface");
		} catch (Throwable e) {
		}
	}

	@Override
	public void loadData(String data, String mimeType, String encoding) {
		super.loadData(arrangeContent(data), mimeType, encoding);
	}

	@Override
	public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
		super.loadDataWithBaseURL("file:///android_asset/css/", arrangeContent(data), "text/html", "utf-8", null);
	}

	private String arrangeContent(String data) {
		StringBuilder sb = new StringBuilder(); // StringBuilder();
		sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"weblayout.css\" /></HEAD><body>");
		String str = data.trim();
		str = str.replaceAll("<iframe width=\"[0-9]*", "<iframe width=\"100\\%\" onload=\"window.JSInterface.onMimeResourceClickd(src);\"");
		str = str.replaceAll("<video width=\"[0-9]*", "<video width=\"100\\%\" onclick=\"window.JSInterface.onMimeResourceClickd(src);\"");
		str = str.replaceAll("<img[\\w]*", "<img height=\"auto\" style=\"max-width:100\\%\"; onclick=\"window.JSInterface.onMimeResourceClickd(src);\"");
		sb.append(str);
		sb.append("</body></HTML>");
		return sb.toString();
	}

	public class JavaScriptInterface {
		private Context context;

		public JavaScriptInterface(Context activiy) {
			this.context = activiy;
		}

		public void onMimeResourceClickd(String sourceAddress) {

			int type = 0;

			if (sourceAddress.toLowerCase().contains("youtube") || sourceAddress.toLowerCase().contains("youtu.be")) {
				type = 1;
			} else if (sourceAddress.toLowerCase().contains("mp4") || sourceAddress.toLowerCase().contains("3gp") || sourceAddress.contains("3gpp")) {
				type = 2;
			} else if (sourceAddress.toLowerCase().contains("png") || sourceAddress.toLowerCase().contains("jpeg") || sourceAddress.toLowerCase().toLowerCase().contains("jpg")) {
				type = 3;
			}

			if (getOnMimeResourceClickListener() != null) {
				if (type == 1) {
					getOnMimeResourceClickListener().onYoutubeVideoClick(sourceAddress);
				} else if (type == 2) {
					getOnMimeResourceClickListener().onVideoClick(sourceAddress);
				} else if (type == 3) {
					getOnMimeResourceClickListener().onImageClick(sourceAddress);
				}
			} else {
				if (type != 3) {
					Intent lVideoIntent = new Intent(null, getVideoPlayURI(sourceAddress), context, IjoomerMediaPlayer.class);
					context.startActivity(lVideoIntent);
				}

				// if (type == 1) {
				// // call youtube player
				// } else if (type == 2) {
				// // call default player
				//
				// // Intent intent = new Intent(Intent.ACTION_VIEW);
				// // intent.setDataAndType(Uri.parse(videoAddress),
				// // "video/3gpp");
				// // context.startActivity(intent);
				// }
			}

		}
	}

	public Uri getVideoPlayURI(String videoUrl) {

		String video_id = "";
		if (videoUrl != null && videoUrl.trim().length() > 0) {

			String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
			CharSequence input = videoUrl;
			Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(input);
			if (matcher.matches()) {
				String groupIndex1 = matcher.group(7);
				if (groupIndex1 != null && groupIndex1.length() == 11)
					video_id = groupIndex1;
			}
		}

		if (video_id.trim().length() > 0) {
			return Uri.parse("ytv://" + video_id);
		} else {
			return Uri.parse("mp4://" + videoUrl);
		}
	}

	public interface OnMimeResourceClickListener {

		void onImageClick(String imageSrc);

		void onVideoClick(String videoSrc);

		void onYoutubeVideoClick(String youTubeVideoSrc);

	}

}
