package com.yalong.serviceprovider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yalong.ActivityWithMenu;
import com.yalong.R;
import com.yalong.UserDatabase;
import com.yalong.entity.Request;

public class OfferForm extends ActivityWithMenu implements OnClickListener, OnFocusChangeListener {
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	private Request request;
	private TextView price;
	private TextView date;
	private TextView time;
	private Button button;
	private String userToken;
	private int mHour;
	private int mMinute;
	private int mYear;
	private int mMonth;
	private int mDay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.offer_form);

		UserDatabase entry = new UserDatabase(this);
		entry.open();
		userToken = entry.getUserdataValue(1);
		entry.close();
			
		Bundle bundle = getIntent().getExtras();
		request = bundle.getParcelable("request");
		
		price = (TextView) findViewById(R.id.etPrice);
		date = (TextView) findViewById(R.id.etDate);
		date.setText(request.getDate());
		date.setOnFocusChangeListener(this);
		time = (TextView) findViewById(R.id.etTime);
		time.setText(request.getTime());
		time.setOnFocusChangeListener(this);
		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(this);
		

		// get the current time
		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		// display the current date
		updateDisplay();

		date.setOnFocusChangeListener(this);
		time.setOnFocusChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		sendOffer();
	}

	private void sendOffer() {
		Resources res = getResources();
		HttpPost httppost = new HttpPost(res.getString(R.string.app_url) + "offers/add");
			
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", userToken));
		nameValuePairs.add(new BasicNameValuePair("request_id", String.valueOf(request.getId())));
		nameValuePairs.add(new BasicNameValuePair("price", price.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("date", date.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("time", time.getText().toString()));
		
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response=httpclient.execute(httppost);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			System.out.println(builder.toString());

			JSONObject jsonobject = new JSONObject(builder.toString());
			Toast.makeText(this, jsonobject.get("responseMessage").toString(), 50).show();
			
			if (jsonobject.get("responseNumber").toString().equals("200")) {
				Intent intent = new Intent(this, RequestTabWidget.class);
				startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);

		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}

	private void updateDisplay() {
		time.setText(
				new StringBuilder()
				.append(pad(mHour)).append(":")
				.append(pad(mMinute)));
		date.setText(
				new StringBuilder()
				// Month is 0 based so add 1
				.append(pad(mDay)).append("/")
				.append(pad(mMonth + 1)).append("/")
				.append(mYear));
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
		}
	};

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		if (arg0.getId() == R.id.etDate && arg1 == true) {
			showDialog(DATE_DIALOG_ID);
		}
		if (arg0.getId() == R.id.etTime && arg1 == true) {
			showDialog(TIME_DIALOG_ID);
		}
	}

}