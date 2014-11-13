package com.smart.framework;


import android.view.View;

import com.ijoomer.common.classes.ViewHolder;

public interface ItemView {

	public abstract View setItemView(int position, View v, SmartListItem item);
	public abstract View setItemView(int position, View v, SmartListItem item, ViewHolder holder);
}
