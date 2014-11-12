package com.qubaopen.activity;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;

import com.qubaopen.R;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

public class MoodHistoryActivity extends Activity {
	  private CalendarPickerView calendar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mood_history);
		 final Calendar nextYear = Calendar.getInstance();
		    nextYear.add(Calendar.YEAR, 1);

		    final Calendar lastYear = Calendar.getInstance();
		    lastYear.add(Calendar.YEAR, -1);

		    calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
		    calendar.init(lastYear.getTime(), nextYear.getTime()) //
		        .inMode(SelectionMode.SINGLE) //
		        .withSelectedDate(new Date());
	}
}
