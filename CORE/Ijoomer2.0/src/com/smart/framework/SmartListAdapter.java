package com.smart.framework;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class SmartListAdapter extends ArrayAdapter<SmartListItem>{

	private Context context;
	private ArrayList<SmartListItem> items;
	private ItemView target;
	private ArrayList<View> viewSet = new ArrayList<View>();
	public SmartListAdapter(Context context, int resource, ArrayList<SmartListItem> items, ItemView target) {
		super(context, resource, items);
		this.items 	  = items;
		this.context  = context;
		this.target	  = target;
		prepareViewSet();
	}
	
	public void prepareViewSet()
	{
		viewSet.clear();
		LayoutInflater vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		int i = 0;
		for(SmartListItem item : items)
		{
			View v = vi.inflate(item.getItemLayout(), null);
			target.setItemView(i, v, item);
			viewSet.add(v);
			i++;
		}
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
	public void notifyDataSetChanged() {
		prepareViewSet();
		super.notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return viewSet.get(position);
	}
}