package com.ijoomer.custom.interfaces;

import java.util.ArrayList;

import com.ijoomer.common.classes.FilterItem;

public interface FilterListener {

	public void onFilterApply(ArrayList<FilterItem> filteredItems);

}
