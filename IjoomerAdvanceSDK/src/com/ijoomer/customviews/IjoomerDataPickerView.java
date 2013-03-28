package com.ijoomer.customviews;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

public class IjoomerDataPickerView extends DatePickerDialog {

	int year;
	int month;
	int day;
	Context mContext;

	boolean isBithDate;

	public IjoomerDataPickerView(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth, boolean isBithDate) {
		super(context, theme, callBack, year, monthOfYear, dayOfMonth);
		this.year = year;
		this.month = monthOfYear;
		this.day = dayOfMonth;
		this.isBithDate = isBithDate;
		mContext = context;
	}

	public IjoomerDataPickerView(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth, boolean isBithDate) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		this.year = year;
		this.month = monthOfYear;
		this.day = dayOfMonth;
		this.isBithDate = isBithDate;
		mContext = context;
	}

	@Override
	public void onDateChanged(final DatePicker view, final int year, final int month, final int day) {

		if (this.isBithDate) {
			Calendar c = GregorianCalendar.getInstance();
			c.set(year, month, day);
			c.add(Calendar.YEAR, 18);

			if (c.get(Calendar.YEAR) > (new Date().getYear() + 1900)) {
				super.onDateChanged(view, this.year, this.month, this.day);

				((Activity) mContext).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						IjoomerDataPickerView.this.updateDate(IjoomerDataPickerView.this.year, IjoomerDataPickerView.this.month, IjoomerDataPickerView.this.day);

					}
				});
			} else {
				this.year = year;
				this.month = month;
				this.day = day;
				super.onDateChanged(view, year, month, day);
			}
		}
	}
}
