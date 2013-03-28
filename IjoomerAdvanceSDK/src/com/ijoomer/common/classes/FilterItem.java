package com.ijoomer.common.classes;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

public class FilterItem {
	private String itemCaption;
	private Drawable itemIconNormal;
	private Drawable itemIconActive;
	private ArrayList<String> itemData;
	private String defaultItem;
	private ArrayList<String> selectedItems;
	private boolean allowMultipleSelection;
	private boolean hasChange;
	private int type = -1;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Drawable getItemIconNormal() {
		return itemIconNormal;
	}

	public void setItemIconNormal(Drawable itemIconNormal) {
		this.itemIconNormal = itemIconNormal;
	}

	public Drawable getItemIconActive() {
		return itemIconActive;
	}

	public void setItemIconActive(Drawable itemIconActive) {
		this.itemIconActive = itemIconActive;
	}

	public boolean isHasChange() {
		return hasChange;
	}

	public void setHasChange(boolean hasChange) {
		this.hasChange = hasChange;
	}

	public ArrayList<String> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(ArrayList<String> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public boolean isAllowMultipleSelection() {
		return allowMultipleSelection;
	}

	public void setAllowMultipleSelection(boolean allowMultipleSelection) {
		this.allowMultipleSelection = allowMultipleSelection;
	}

	public String getItemCaption() {
		return itemCaption;
	}

	public void setItemCaption(String itemCaption) {
		this.itemCaption = itemCaption;
	}

	public ArrayList<String> getItemData() {
		return itemData;
	}

	public void setItemData(ArrayList<String> itemData) {
		this.itemData = itemData;
	}

	public String getDefaultItem() {
		return defaultItem;
	}

	public void setDefaultItem(String defaultItem) {
		this.defaultItem = defaultItem;
	}

}