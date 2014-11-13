package com.ijoomer.src;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * This Fragment Contains All Method Related To IjoomerHomeFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class IjoomerMenuGridFragment extends SmartFragment implements IjoomerSharedPreferences {

	private GridView grdMenu;

	private AQuery androidQuery;
	private ArrayList<SmartListItem> dataMenu;
	private SmartListAdapterWithHolder gridAdapter;
	private JSONArray data;

	private final String ICON = "icon";
	private final String ITEMVIEW = "itemview";
	private final String ITEMCAPTION = "itemcaption";

	/**
	 * Constructor
	 * 
	 * @param itemData
	 *            json array data
	 */
	public IjoomerMenuGridFragment(JSONArray itemData) {
		data = itemData;
	}

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_menu_grid;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		androidQuery = new AQuery(getActivity());
		dataMenu = new ArrayList<SmartListItem>();
		grdMenu = (GridView) currentView.findViewById(R.id.grdMenu);
	}

	@Override
	public void prepareViews(View currentView) {
		((TextView) ((SmartActivity) getActivity()).getHeaderView().findViewById(R.id.txtHeader)).setText(((IjoomerSuperMaster) getActivity()).getScreenCaption());
		prepareGrid();
		gridAdapter = getMenuAdapter();
		grdMenu.setAdapter(gridAdapter);
	}

	@Override
	public void setActionListeners(View currentView) {
		grdMenu.setOnItemClickListener(new OnItemClickListener() {

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

	private void prepareGrid() {
		dataMenu.clear();

		for (int i = 0; i < data.length(); i++) {
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
				item.setItemLayout(R.layout.ijoomer_menu_grid_menuitem);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(objItem);
				item.setValues(obj);
				dataMenu.add(item);
			} catch (Exception e) {
			}
		}

	}

	private SmartListAdapterWithHolder getMenuAdapter() {
		SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.ijoomer_menu_grid_menuitem, dataMenu, new ItemView() {

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
