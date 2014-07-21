package com.ijoomer.customviews;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * This Class Contains All Method Related To IjoomerDataPickerView.
 * 
 * @author tasol
 * 
 */
public class IjoomerDataPickerView extends DatePickerDialog{

	private int year;
	private int month;
	private int day;
	boolean isBithDate;
	private final String format = "EEE, MMM dd, yyyy";

	public IjoomerDataPickerView(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth, boolean isBithDate) {
		super(context, theme, callBack, year, monthOfYear, dayOfMonth);
		this.year = year;
		this.month = monthOfYear;
		this.day = dayOfMonth;
		this.isBithDate = isBithDate;
	}

	public IjoomerDataPickerView(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth, boolean isBithDate) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		this.year = year;
		this.month = monthOfYear;
		this.day = dayOfMonth;
		this.isBithDate = isBithDate;
	}

	@Override
	public void onDateChanged(final DatePicker view, final int year, final int month, final int day) {

		if (this.isBithDate) {
			Calendar c = Calendar.getInstance();
			c.set(year, month, day);
			c.add(Calendar.YEAR, 18);

			if (c.get(Calendar.YEAR) > (Calendar.getInstance().get(Calendar.YEAR))) {
				view.init(IjoomerDataPickerView.this.year, IjoomerDataPickerView.this.month, IjoomerDataPickerView.this.day, IjoomerDataPickerView.this);
				Calendar calendar = Calendar.getInstance();
				calendar.set(IjoomerDataPickerView.this.year, IjoomerDataPickerView.this.month, IjoomerDataPickerView.this.day);
				setTitle(DateFormat.format(format, calendar));
			} else {
				this.year = year;
				this.month = month;
				this.day = day;
				view.init(year, month, day, IjoomerDataPickerView.this);
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, month, day);
				setTitle(DateFormat.format(format, calendar));
			}
		}
	}

}
