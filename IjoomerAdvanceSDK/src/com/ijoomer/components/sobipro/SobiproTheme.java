package com.ijoomer.components.sobipro;

public class SobiproTheme {

	int bgColor;
	int bgLightColor;
	int selectorBgDrawable;
	int favouriteBtnDrawable;
	int mapBtnDrawable;
	int ascendingDrawable;
	int descendingDrawable;
	int prosDrawable;
	int consDrawable;
	int mapInfoWindowDrawable;
	int mapMarkerDrawable;

	int textColor;
	int gridBorderColor;
	int imgCall;
	int imgEmail;

	public SobiproTheme(int bgColor, int bgLightColor, int selectorBgDrawable, int favoriteBtnDrawable, int mapBtnDrawable, int ascendingDrawable, int descendingDrawable,
			int prosDrawable, int consDrawable, int mapInfoWindowDrawable, int mapMarkerDrawable) {
		this.bgColor = bgColor;
		this.bgLightColor = bgLightColor;
		this.selectorBgDrawable = selectorBgDrawable;
		this.favouriteBtnDrawable = favoriteBtnDrawable;
		this.mapBtnDrawable = mapBtnDrawable;
		this.ascendingDrawable = ascendingDrawable;
		this.descendingDrawable = descendingDrawable;
		this.prosDrawable = prosDrawable;
		this.consDrawable = consDrawable;
		this.mapInfoWindowDrawable = mapInfoWindowDrawable;
		this.mapMarkerDrawable = mapMarkerDrawable;
	}

	public SobiproTheme() {
	}

	public void setCarTheme(int bgColor, int bgLightColor, int textColor, int gridBorderColor, int imgCall, int imgEmail, int ascendingDrawable, int descendingDrawable) {
		this.bgColor = bgColor;
		this.bgLightColor = bgLightColor;
		this.textColor = textColor;
		this.gridBorderColor = gridBorderColor;
		this.imgCall = imgCall;
		this.imgEmail = imgEmail;
		this.ascendingDrawable = ascendingDrawable;
		this.descendingDrawable = descendingDrawable;

	}

	public int getImgCall() {
		return imgCall;
	}

	public void setImgCall(int imgCall) {
		this.imgCall = imgCall;
	}

	public int getImgEmail() {
		return imgEmail;
	}

	public void setImgEmail(int imgEmail) {
		this.imgEmail = imgEmail;
	}

	public int getGridBorderColor() {
		return gridBorderColor;
	}

	public void setGridBorderColor(int gridBorderColor) {
		this.gridBorderColor = gridBorderColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getMapMarkerDrawable() {
		return mapMarkerDrawable;
	}

	public void setMapMarkerDrawable(int mapMarkerDrawable) {
		this.mapMarkerDrawable = mapMarkerDrawable;
	}

	public int getMapInfoWindowDrawable() {
		return mapInfoWindowDrawable;
	}

	public void setMapInfoWindowDrawable(int mapInfoWindowDrawable) {
		this.mapInfoWindowDrawable = mapInfoWindowDrawable;
	}

	public int getProsDrawable() {
		return prosDrawable;
	}

	public void setProsDrawable(int prosDrawable) {
		this.prosDrawable = prosDrawable;
	}

	public int getConsDrawable() {
		return consDrawable;
	}

	public void setConsDrawable(int consDrawable) {
		this.consDrawable = consDrawable;
	}

	public int getAscendingDrawable() {
		return ascendingDrawable;
	}

	public void setAscendingDrawable(int ascendingDrawable) {
		this.ascendingDrawable = ascendingDrawable;
	}

	public int getDescendingDrawable() {
		return descendingDrawable;
	}

	public void setDescendingDrawable(int descendingDrawable) {
		this.descendingDrawable = descendingDrawable;
	}

	public int getFavouriteBtnDrawable() {
		return favouriteBtnDrawable;
	}

	public void setFavouriteBtnDrawable(int favouriteBtnDrawable) {
		this.favouriteBtnDrawable = favouriteBtnDrawable;
	}

	public int getMapBtnDrawable() {
		return mapBtnDrawable;
	}

	public void setMapBtnDrawable(int mapBtnDrawable) {
		this.mapBtnDrawable = mapBtnDrawable;
	}

	public int getSelectorBgDrawable() {
		return selectorBgDrawable;
	}

	public void setSelectorBgDrawable(int selectorBgDrawable) {
		this.selectorBgDrawable = selectorBgDrawable;
	}

	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	public int getBgLightColor() {
		return bgLightColor;
	}

	public void setBgLightColor(int bgLightColor) {
		this.bgLightColor = bgLightColor;
	}

}
