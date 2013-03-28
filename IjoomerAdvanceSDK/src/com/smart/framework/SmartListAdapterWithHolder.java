package com.smart.framework;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ijoomer.common.classes.ViewHolder;

public class SmartListAdapterWithHolder extends ArrayAdapter<SmartListItem>{

	private Context context;
	private ArrayList<SmartListItem> items;
	private ItemView target;
	public SmartListAdapterWithHolder(Context context, int resource, ArrayList<SmartListItem> items, ItemView target) {
		super(context, resource, items);
		this.items 	  = items;
		this.context  = context;
		this.target	  = target;
	}
	
	public ArrayList<SmartListItem> getSmartListItems() {
		return items;
	}
	
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		Log.v("SmartListAdapter", observer.toString());
		super.registerDataSetObserver(observer);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if ((convertView == null) || (convertView.getTag() == null)) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(items.get(position).getItemLayout(), null);
			holder = new ViewHolder();
		} else 
			holder = (ViewHolder) convertView.getTag();
		
			target.setItemView(position, convertView, items.get(position), holder);
		
			convertView.setTag(holder);
			
		return convertView;
	}
	
}