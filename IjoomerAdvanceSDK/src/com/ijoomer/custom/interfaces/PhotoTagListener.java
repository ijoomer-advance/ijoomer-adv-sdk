package com.ijoomer.custom.interfaces;

/**
 * This Interface Contains All Method Related To PhotoTagListener.
 * 
 * @author tasol
 * 
 */
public interface PhotoTagListener {

	public void onTagedItemClicked(int position, Object data);

	public void onAddNewTag(String rectPosition);

	public void showTagOptions(boolean isTagCanceld);

	public void onTagAreaConflict();
	
	public void onCancel();

}
