package com.ijoomer.src;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerScreenHolder;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerTextView;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

public class IjoomerHomeFragment extends Fragment {

	private GridView grdHome;

	private AQuery androidQuery;
	private ArrayList<SmartListItem> dataHomeMenu;
	private SmartListAdapterWithHolder gridAdapter;
	private JSONArray data;

	private int startCount;
	private int endCount;

	public IjoomerHomeFragment(int start, int end, JSONArray itemData) {
		startCount = start;
		endCount = end;
		data = itemData;
	}

	/**
	 * Overrides method
	 */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.ijoomer_home_grid, null);
		androidQuery = new AQuery(getActivity());
		dataHomeMenu = new ArrayList<SmartListItem>();
		grdHome = (GridView) v.findViewById(R.id.grdHome);
		prepareGrid();
		gridAdapter = getHomeMenuAdapter();
		grdHome.setAdapter(gridAdapter);
		grdHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					JSONObject object = (JSONObject) gridAdapter.getItem(arg2).getValues().get(0);
					((IjoomerSuperMaster)getActivity()).launchActivity(object);
				} catch (Exception e) {
				}
			}
		});
		return v;
	}

	/**
	 * Class method
	 */

	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getCustomOkDialog(getString(R.string.home), getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMathod() {
						if (responseCode == 599 && finishActivityOnConnectionProblem) {
							getActivity().finish();
						}
					}
				});

	}

	private void prepareGrid() {
		dataHomeMenu.clear();

		for (int i = startCount; i < endCount; i++) {
			try {
				JSONObject objItem = data.getJSONObject(i);
				ArrayList<HashMap<String, String>> iconData = IjoomerGlobalConfiguration.getSideMenuIcon(getActivity(), objItem.getString("itemview"));
				if (iconData != null && iconData.size() > 0) {
					objItem.put("icon", iconData.get(0).get("icon"));
				}
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.ijoomer_home_grid_menuitem);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(objItem);
				item.setValues(obj);
				dataHomeMenu.add(item);
			} catch (Exception e) {
			}
		}

	}

	/**
	 * List adapter
	 */
	private SmartListAdapterWithHolder getHomeMenuAdapter() {
		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.ijoomer_home_grid_menuitem, dataHomeMenu, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
				holder.imgMenuItemicon = (ImageView) v.findViewById(R.id.imgMenuItemicon);
				holder.txtMenuItemCaption = (IjoomerTextView) v.findViewById(R.id.txtMenuItemCaption);

				final JSONObject obj = (JSONObject) item.getValues().get(0);
				try {
					holder.txtMenuItemCaption.setText(obj.getString("itemcaption"));

					if (obj.has("icon")) {
						androidQuery.id(holder.imgMenuItemicon).image(obj.getString("icon"), true, true,((SmartActivity)getActivity()).getDeviceWidth(),0);
					}
				} catch (Exception e) {
				}

				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}

		});
		return listAdapterWithHolder;
	}

}
