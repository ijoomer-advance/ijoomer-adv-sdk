package com.smart.framework;

import java.util.ArrayList;

public class SmartExpandableListItem {

	private int childItemLayout;
	private int[] childItemViews;
	private ArrayList<Object> childValues = new ArrayList<Object>();
	@SuppressWarnings("rawtypes")
	private ArrayList<Class> childClazz = new ArrayList<Class>();

	private int groupItemLayout;
	private int[] groupItemViews;
	private ArrayList<Object> groupValues = new ArrayList<Object>();
	@SuppressWarnings("rawtypes")
	private ArrayList<Class> groupClazz = new ArrayList<Class>();

	public SmartExpandableListItem() {
	}

	public SmartExpandableListItem(int childItemLayout, int[] childItemViews, ArrayList<Object> childValues, @SuppressWarnings("rawtypes") ArrayList<Class> childClazz, int groupItemLayout,
			int[] groupItemViews, ArrayList<Object> groupValues, @SuppressWarnings("rawtypes") ArrayList<Class> groupClazz) {
		this.childItemLayout = childItemLayout;
		this.childItemViews = childItemViews;
		this.childValues = childValues;
		this.childClazz = childClazz;

		this.groupItemLayout = groupItemLayout;
		this.groupItemViews = groupItemViews;
		this.groupValues = groupValues;
		this.groupClazz = groupClazz;
	}

	public void setchildItemLayout(int itemLayout) {
		this.childItemLayout = itemLayout;
	}

	public void setgroupItemLayout(int itemLayout) {
		this.groupItemLayout = itemLayout;
	}

	public int getchildItemLayout() {
		return this.childItemLayout;
	}

	public int getgroupItemLayout() {
		return this.groupItemLayout;
	}

	public void setchildItemViews(int[] itemViews) {
		this.childItemViews = itemViews;
	}

	public void setgroupItemViews(int[] itemViews) {
		this.groupItemViews = itemViews;
	}

	public int[] getchildItemViews() {
		return this.childItemViews;
	}

	public int[] getgroupItemViews() {
		return this.groupItemViews;
	}

	public void setchildValues(ArrayList<Object> values) {
		this.childValues = values;
	}

	public void setgroupValues(ArrayList<Object> values) {
		this.groupValues = values;
	}

	public ArrayList<Object> getchildValues() {
		return this.childValues;
	}

	public ArrayList<Object> getgroupValues() {
		return this.groupValues;
	}

	@SuppressWarnings("rawtypes")
	public void setchildClasses(ArrayList<Class> clazz) {
		this.childClazz = clazz;
	}

	@SuppressWarnings("rawtypes")
	public void setgroupClasses(ArrayList<Class> clazz) {
		this.groupClazz = clazz;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<Class> getchildClasses() {
		return this.childClazz;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<Class> getgroupClass() {
		return this.groupClazz;
	}

	public int getTotalChildViews() {
		return childItemViews.length;
	}

	public int getTotalGroupViews() {
		return groupItemViews.length;
	}
}
