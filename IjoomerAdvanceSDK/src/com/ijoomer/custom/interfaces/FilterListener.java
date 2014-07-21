package com.ijoomer.custom.interfaces;

import com.ijoomer.common.classes.FilterItem;

import java.util.ArrayList;

/**
 * This Interface Contains All Method Related To FilterListener.
 * 
 * @author tasol
 * 
 */
public interface FilterListener {

	public void onFilterApply(ArrayList<FilterItem> filteredItems);

}
