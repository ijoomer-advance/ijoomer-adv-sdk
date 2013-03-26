package com.smart.framework;

import android.view.View;

import com.ijoomer.common.classes.ViewHolder;

public interface ExpandableItemView {

	public abstract View setItemView(int position, View v, SmartExpandableListItem item);
	public abstract View setItemView(int position, View v, SmartExpandableListItem item, ViewHolder holder);
}
