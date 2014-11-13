package com.ijoomer.plugins;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.plugins.PluginsWeatherDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;

@SuppressLint("SimpleDateFormat")
public class PluginsWeatherFragment extends SmartFragment implements PluginsTagHolder {

	PluginsWeatherDataProvider dataProvider;
	ImageView imgWeatherIcon;
	ArrayList<HashMap<String, String>> weatherInfo;
	ArrayList<HashMap<String, String>> dailyWeatherInfo;
	AQuery aQuery;
	String locationID;
	ArrayList<HashMap<String, String>> locationData;
	IjoomerTextView txtLocationName, txtTemp, txtHumidity, txtVisibility, txtCloudCover, txtPressure, txtSpeed, txtDirection;
	LinearLayout lnrWeatherForcast;
	LinearLayout lnrWeatherBg;
	ProgressBar pbr;
	int MORNING_START_TIME_IN_HOUR = 5;
	int AFTERNOON_START_TIME_IN_HOUR = 11;
	int EVENING_START_TIME_IN_HOUR = 17;

	public PluginsWeatherFragment(String locationID) {
		this.locationID = locationID;
	}

	@Override
	public int setLayoutId() {
		return R.layout.plugins_weather_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		pbr = (ProgressBar) currentView.findViewById(R.id.weatherPbr);
		imgWeatherIcon = (ImageView) currentView.findViewById(R.id.imgWeatherIcon);
		txtCloudCover = (IjoomerTextView) currentView.findViewById(R.id.txtCloudCover);
		txtLocationName = (IjoomerTextView) currentView.findViewById(R.id.txtLocationName);
		txtTemp = (IjoomerTextView) currentView.findViewById(R.id.txtTemp);
		txtHumidity = (IjoomerTextView) currentView.findViewById(R.id.txtHumidity);
		txtVisibility = (IjoomerTextView) currentView.findViewById(R.id.txtVisibility);
		txtSpeed = (IjoomerTextView) currentView.findViewById(R.id.txtSpeed);
		txtPressure = (IjoomerTextView) currentView.findViewById(R.id.txtPressure);
		txtDirection = (IjoomerTextView) currentView.findViewById(R.id.txtDirection);
		lnrWeatherForcast = (LinearLayout) currentView.findViewById(R.id.lnrWeatherForcast);
		lnrWeatherBg = (LinearLayout) currentView.findViewById(R.id.lnrWeatherBg);
		aQuery = new AQuery(getActivity());
		dataProvider = new PluginsWeatherDataProvider(getActivity());
	}

	@Override
	public void prepareViews(View currentView) {

		getWeatherData();
	}

	@Override
	public void setActionListeners(View currentView) {
	}

	private void getWeatherData() {
		locationData = dataProvider.getLocation(locationID);
		if (locationData != null) {

			try {
				pbr.setVisibility(View.VISIBLE);
				dataProvider.getWeatherInfo(locationData.get(0).get(LOCATION), new WebCallListener() {
					@Override
					public void onProgressUpdate(int progressCount) {
						if (progressCount == 100)
							pbr.setVisibility(View.GONE);
					}

					@SuppressWarnings("unchecked")
					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						try {
							weatherInfo = data1;
							dailyWeatherInfo = (ArrayList<HashMap<String, String>>) data2;

							if (responseCode == 200) {
								prepareWeatherView();

							} else {
								IjoomerUtilities.getCustomOkDialog(((IjoomerSuperMaster) getActivity()).getScreenCaption(),
										getString(getResources().getIdentifier("code" + 204, "string", getActivity().getPackageName())), getString(R.string.ok),
										R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {
											@Override
											public void NeutralMethod() {
											}
										});
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void prepareWeatherView() {
		try {
			txtCloudCover.setText(weatherInfo.get(0).get(CLOUDCOVER) + "%");
			txtHumidity.setText(weatherInfo.get(0).get(HUMIDITY) + "%");
			txtVisibility.setText(weatherInfo.get(0).get(VISIBILITY) + getResources().getString(R.string.km));
			txtDirection.setText(weatherInfo.get(0).get(WINDDIR16POINT));
			txtSpeed.setText(weatherInfo.get(0).get(WINDSPEEDMILES) + getResources().getString(R.string.mph));
			txtPressure.setText(millibarToInches((weatherInfo.get(0).get(PRESSURE))));
			txtTemp.setText(weatherInfo.get(0).get(TEMP_F));
			txtLocationName.setText(locationData.get(0).get(NAME));
			JSONObject weatherIconUrl = new JSONArray(weatherInfo.get(0).get(WEATHERICONURL)).getJSONObject(0);
			aQuery.id(imgWeatherIcon).image(weatherIconUrl.getString(VALUE), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.ic_launcher);
			if (dailyWeatherInfo != null && dailyWeatherInfo.size() > 0) {
				lnrWeatherForcast.removeAllViews();
				for (int i = 1; i < dailyWeatherInfo.size(); i++) {
					LinearLayout lnr = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.plugins_weather_forcast_item, null);
					HashMap<String, String> weatherHash = (HashMap<String, String>) dailyWeatherInfo.get(i);
					JSONObject weahterIcon = new JSONArray(weatherHash.get(WEATHERICONURL)).getJSONObject(0);
					ImageView imageView = (ImageView) lnr.findViewById(R.id.imgWeatherIcon);
					aQuery.id(imageView).image(weahterIcon.getString("value"), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.ic_launcher);
					IjoomerTextView txtDate = (IjoomerTextView) lnr.findViewById(R.id.txtDate);
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date date = (Date) formatter.parse(weatherHash.get(DATE));
					SimpleDateFormat format = new SimpleDateFormat("MMM dd");
					txtDate.setText(format.format(date));
					IjoomerTextView txtTemp = (IjoomerTextView) lnr.findViewById(R.id.txtTemp);
					txtTemp.setText(weatherHash.get(TEMPMINF) + "/" + weatherHash.get(TEMPMAXF));
					lnrWeatherForcast.addView(lnr);
				}
			}
			String time = weatherInfo.get(0).get(OBSERVATION_TIME);
			SimpleDateFormat input = new SimpleDateFormat("hh:mm a");
			Date dt = input.parse(time);
			SimpleDateFormat output = new SimpleDateFormat("HH");
			int observationTime = Integer.parseInt(output.format(dt).toString());

			if (observationTime <= MORNING_START_TIME_IN_HOUR) {
				lnrWeatherBg.setBackgroundResource(R.drawable.plugins_weather_night_img);
			} else if (observationTime <= AFTERNOON_START_TIME_IN_HOUR) {
				lnrWeatherBg.setBackgroundResource(R.drawable.plugins_weather_morning_img);
			} else if (observationTime <= EVENING_START_TIME_IN_HOUR) {
				lnrWeatherBg.setBackgroundResource(R.drawable.plugins_weather_afternoon_img);
			} else {
				lnrWeatherBg.setBackgroundResource(R.drawable.plugins_weather_evening_img);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String millibarToInches(String pressureStr) {
		Double pressure = Double.parseDouble(pressureStr);
		try {
			pressure = pressure / 33.8637526;
			return Math.floor(pressure * 100) / 100 + "in";
		} catch (Exception e) {
		}
		return "0 in";
	}
}
