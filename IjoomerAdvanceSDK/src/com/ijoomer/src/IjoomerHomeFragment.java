package com.ijoomer.src;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.customviews.IjoomerTextView;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartApplication;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Fragment Contains All Method Related To IjoomerHomeFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class IjoomerHomeFragment extends SmartFragment implements IjoomerSharedPreferences {

	private GridView grdHome;

	private AQuery androidQuery;
	private ArrayList<SmartListItem> dataHomeMenu;
	private SmartListAdapterWithHolder gridAdapter;
	private JSONArray data;

	private final String ICON = "icon";
	private final String ITEMVIEW = "itemview";
	private final String ITEMCAPTION = "itemcaption";
	private int startCount;
	private int endCount;

	/**
	 * Constructor
	 * 
	 * @param start
	 *            start index
	 * @param end
	 *            end index
	 * @param itemData
	 *            json array data
	 */
	public IjoomerHomeFragment(int start, int end, JSONArray itemData) {
		startCount = start;
		endCount = end;
		data = itemData;
	}

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_home_grid;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		androidQuery = new AQuery(getActivity());
		dataHomeMenu = new ArrayList<SmartListItem>();
		grdHome = (GridView) currentView.findViewById(R.id.grdHome);
	}

	@Override
	public void prepareViews(View currentView) {
		prepareGrid();
		gridAdapter = getHomeMenuAdapter();
		grdHome.setAdapter(gridAdapter);
	}

	@Override
	public void setActionListeners(View currentView) {
		grdHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					JSONObject object = (JSONObject) gridAdapter.getItem(arg2).getValues().get(0);
					((IjoomerSuperMaster) getActivity()).launchActivity(object);
				} catch (Exception e) {
				}
			}
		});
	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to prepare list home icon grid.
	 */
	private void prepareGrid() {
		dataHomeMenu.clear();

		for (int i = startCount; i < endCount; i++) {
			try {
				JSONObject objItem = data.getJSONObject(i);
				if (objItem.getString(ITEMVIEW).equals("Login") && (SmartApplication.REF_SMART_APPLICATION.readSharedPreferences().getString(SP_LOGIN_REQ_OBJECT, null)) != null) {
					objItem.put("logout", "logout");
				}
				if (!objItem.has(ICON)) {
					ArrayList<HashMap<String, String>> iconData = IjoomerGlobalConfiguration.getSideMenuIcon(getActivity(), objItem.getString(ITEMVIEW));
					if (iconData != null && iconData.size() > 0) {
						objItem.put(ICON, iconData.get(0).get(ICON));
					}
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
	 * List adapter for home icon grid.
	 */
	private SmartListAdapterWithHolder getHomeMenuAdapter() {
		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.ijoomer_home_grid_menuitem, dataHomeMenu, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
				holder.imgMenuItemicon = (ImageView) v.findViewById(R.id.imgMenuItemicon);
				holder.txtMenuItemCaption = (IjoomerTextView) v.findViewById(R.id.txtMenuItemCaption);

				final JSONObject obj = (JSONObject) item.getValues().get(0);
				if (obj.has("logout")) {
					holder.txtMenuItemCaption.setText(getString(R.string.logout));
					holder.imgMenuItemicon.setImageResource(R.drawable.logout);
				} else {
					try {
						holder.txtMenuItemCaption.setText(obj.getString(ITEMCAPTION));

						if (obj.has(ICON)) {
							androidQuery.id(holder.imgMenuItemicon).image(obj.getString(ICON), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0);
						}
					} catch (Exception e) {
					}
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
