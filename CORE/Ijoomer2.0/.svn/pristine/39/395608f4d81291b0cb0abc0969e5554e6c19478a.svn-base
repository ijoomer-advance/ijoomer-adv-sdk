package com.smart.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;

public class SmartExpandableListAdapter extends SimpleExpandableListAdapter {

	private Context context;
	@SuppressWarnings("unused")
	private List<? extends Map<String, ?>> groupData;
	@SuppressWarnings("unused")
	private int groupLayout;
	@SuppressWarnings("unused")
	private String[] groupFrom;
	@SuppressWarnings("unused")
	private int[] groupTo;
	@SuppressWarnings("unused")
	private List<? extends List<? extends Map<String, ?>>> childData;
	@SuppressWarnings("unused")
	private int childLayout;
	@SuppressWarnings("unused")
	private String[] childFrom;
	@SuppressWarnings("unused")
	private int[] childTo;
	
	private ArrayList<SmartExpandableListItem> items;
	
	private ExpandableItemView target;
	
	private ArrayList<View> childViewSet;
	private ArrayList<View> groupViewSet;
	
	public SmartExpandableListAdapter(Context context, List<? extends Map<String, ?>> groupData, int groupLayout,
			String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout,
			String[] childFrom, int[] childTo, ArrayList<SmartExpandableListItem> items, ExpandableItemView target) {
		super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);

		this.context = context;
		this.groupData = groupData;
		this.groupLayout = groupLayout;
		this.groupFrom = groupFrom;
		this.groupTo = groupTo;
		this.childData = childData;
		this.childLayout = childLayout;
		this.childFrom = childFrom;
		this.childTo = childTo;
		this.items = items;
		this.target = target;
		
		prepareViewSets();
		
	}
	
	private void prepareViewSets() {
		prepareGroupViewSet();
		prepareChildViewSet();
	}
	
	private void prepareChildViewSet() {
		childViewSet.clear();
		
		LayoutInflater vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		int i = 0;
		for(SmartExpandableListItem item : items)
		{
			View v = vi.inflate(item.getchildItemLayout(), null);
			target.setItemView(i, v, item);
			childViewSet.add(v);
			i++;
		}
		
	}
	
	private void prepareGroupViewSet() {
		groupViewSet.clear();
		
		LayoutInflater vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		int i = 0;
		for (SmartExpandableListItem item : items) {
			View v = vi.inflate(item.getgroupItemLayout(), null);
			target.setItemView(i, v, item);
			groupViewSet.add(v);
			i++;
		}
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		return groupViewSet.get(groupPosition);
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		return childViewSet.get(childPosition);
	}
	
	
}