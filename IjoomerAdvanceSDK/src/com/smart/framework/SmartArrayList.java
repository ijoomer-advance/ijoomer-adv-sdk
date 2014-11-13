package com.smart.framework;

import java.util.ArrayList;

@SuppressWarnings({ "serial", "rawtypes" })
public class SmartArrayList extends ArrayList {

	@Override
	public SmartTable get(int index) {
		return (SmartTable) super.get(index);
	}

	public SmartTable get(String tblName) {
		for (int i = 0; i < size(); i++) {
			if (((SmartTable) super.get(i)).getTableName().equals(tblName))
				return ((SmartTable) super.get(i));
		}
		return null;
	}
}
