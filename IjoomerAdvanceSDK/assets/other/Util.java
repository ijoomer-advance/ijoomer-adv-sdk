import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.IjoomerTextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.IjoomerTextView.BufferType;

import com.application.configuration.ViewHolder;
import com.ijoomer.common.configuration.MyProgressBar;
import com.ijoomer.src.R;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;


public class Util {

	/** Called when the activity is first created. */
	private ListView lstTest;
	private SmartListAdapterWithHolder mAdapterWithHolder;
	Timer t;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ArrayList<SmartListItem> data = new ArrayList<SmartListItem>();

		for (int i = 0; i < 10; i++) {
			SmartListItem item = new SmartListItem();
			item.setItemLayout(R.layout.icms_list_item);
			data.add(item);
		}
		lstTest = (ListView) findViewById(R.id.lstTest);
		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup) inflater.inflate(R.layout.list_footer, lstTest, false);
		MyProgressBar progress = (MyProgressBar) header.findViewById(R.id.progress);
		progress.setVisibility(View.VISIBLE);
		progress.startAnimation();
		lstTest.addFooterView(header, null, false);
		lstTest.setFastScrollEnabled(true);
		mAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.icms_list_item, data, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});

		lstTest.setAdapter(mAdapterWithHolder);

		lstTest.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if ((firstVisibleItem + visibleItemCount) >= totalItemCount) {

					if (t == null) {
						t = new Timer();
						t.schedule(new TimerTask() {

							@Override
							public void run() {
								for (int i = 0; i < 10; i++) {
									final SmartListItem item = new SmartListItem();
									item.setItemLayout(R.layout.icms_list_item);
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											mAdapterWithHolder.add(item);
										}
									});
								}
								t.cancel();
								t = null;
							}
						}, 3000);
					}
				}
				// if (firstVisibleItem >= (totalItemCount / 2)) {
				// SmartListItem item = new SmartListItem();
				// item.setItemLayout(R.layout.icms_list_item);
				// mAdapterWithHolder.add(item);
				// }
			}
		});
	}

	private void doEllipsize(final IjoomerTextView tv, final int line) {
		ViewTreeObserver vto = tv.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {

				ViewTreeObserver obs = tv.getViewTreeObserver();
				obs.removeGlobalOnLayoutListener(this);
				if (line <= 0) {
					int lineEndIndex = tv.getLayout().getLineEnd(0);
					String text = tv.getText().subSequence(0, lineEndIndex - 3) + "...";
					tv.setText(text);
				} else if (tv.getLineCount() >= line) {
					int lineEndIndex = tv.getLayout().getLineEnd(line - 1);
					String text = tv.getText().subSequence(0, lineEndIndex - 3) + "...";
					tv.setText(text);
				}
			}
		});
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void showArticleDetail(String article, IjoomerTextView snippet) {
		String str = "Joomla! is a free open source framework and content publishing system designed for quickly creating highly interactive multi-language Web sites, online communities, media portals, blogs and eCommerce applications. {ijoomer_image0}Joomla! provides an easy-to-use graphical user interface that simplifies the management and publishing of large volumes of content including HTML, documents, and rich media.  Joomla! is used by organisations of all sizes for intranets and extranets and is supported by a community of tens of thousands of users. \n\n With a fully documented library of developer resources, Joomla! allows the customisation of every aspect of a Web site including presentation, layout, administration, and the rapid integration with third-party applications.Joomla! now provides more developer power while making the user experience all the more friendly. For those who always wanted increased extensibility, Joomla! 1.5 can make this happen.{ijoomer_image1} A new framework, ground-up refactoring, and a highly-active development team brings the excitement of 'the next generation CMS' to your fingertips.  Whether you are a systems architect or a complete 'noob' Joomla! can take you to the next level of content delivery. 'More than a CMS' is something we have been playing with as a catchcry because the new Joomla! API has such incredible power and flexibility, you are free to take whatever direction your creative mind takes you and Joomla! can help you get there so much more easily than ever before. Thinking Web publishing? Think Joomla!";
		StringBuilder sb = new StringBuilder(str);
		SpannableString spanString = new SpannableString(sb);

		int index = 0;
		int start = 0;
		do {
			Bitmap b = getBitmapFromURL("http://192.168.5.157/development/ijoomerwa_dev/images/stories/powered_by.png");
			ImageSpan span = new ImageSpan(b);

			start = str.indexOf("{", index + 1);
			index = str.indexOf("}", index + 1);
			if (index <= 0)
				break;

			spanString.setSpan(span, start, index + 1, Spannable.SPAN_INTERMEDIATE);

		} while (index >= 0);

		snippet.setText(spanString, BufferType.SPANNABLE);
		snippet.setMovementMethod(new ScrollingMovementMethod());
	}
	
	//youtube intent
	try {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://"
				+ new JSONObject(row.get("content_data")).getString("video_path").split("=")[1]));
		startActivity(intent);
	} catch (Throwable e) {
		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(new JSONObject(row.get("content_data")).getString("video_path"))));
		} catch (Throwable ee) {
			ee.printStackTrace();
		}
		e.printStackTrace();
	}
	
	// Filtering
	
	// FilterVIew filterVIew = new
	// FilterVIew(IjoomerLoginActivity.this);
	// filterVIew.setOnFilterListener(new FilterListener() {
	//
	// @Override
	// public void onFilterApply(ArrayList<FilterItem>
	// filteredItems) {
	//
	// }
	// });
	// ArrayList<String> dataTOfilter = new ArrayList<String>();
	// dataTOfilter.add("Birthday");
	// dataTOfilter.add("Merriege");
	// dataTOfilter.add("Religion");
	// dataTOfilter.add("General");
	//
	// filterVIew.addFilterItem("Catagories",
	// getResources().getDrawable(R.drawable.filter_category_icon_normal),
	// getResources().getDrawable(R.drawable.filter_category_icon_selected),
	// "General", dataTOfilter, true);
	// dataTOfilter = new ArrayList<String>();
	// dataTOfilter.add("Latest");
	// dataTOfilter.add("Most Viewed");
	// dataTOfilter.add("Title");
	// dataTOfilter.add("Popular");
	//
	// filterVIew.addFilterItem("Sort By",
	// getResources().getDrawable(R.drawable.filter_sorting_icon_normal),
	// getResources().getDrawable(R.drawable.filter_sorting_icon_selected),
	// "Latest", dataTOfilter, false);
	//
	// filterVIew.addFilterItem("Date",
	// getResources().getDrawable(R.drawable.filter_date_icon_normal),
	// getResources().getDrawable(R.drawable.filter_date_icon_selected),
	// FilterVIew.DATE);
	//
	// filterVIew.addFilterItem("Time",
	// getResources().getDrawable(R.drawable.filter_time_icon_normal),
	// getResources().getDrawable(R.drawable.filter_time_icon_selected),
	// FilterVIew.TIME);
	//
	// filterVIew.addFilterItem("Location",
	// getResources().getDrawable(R.drawable.filter_location_icon_normal),
	// getResources().getDrawable(R.drawable.filter_location_icon_selected),
	// FilterVIew.LOCATION);
	//
	// filterVIew.showFilter();

}
