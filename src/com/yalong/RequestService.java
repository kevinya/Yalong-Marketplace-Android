package com.yalong;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.TimePicker;

import com.yalong.entity.PhotoRequestAdapter;
import com.yalong.entity.VideoRequestAdapter;
import com.yalong.file.PhotoPicker;
import com.yalong.file.VideoPicker;
import com.yalong.service.RequestUploaderService;

/**
 * Form to make a request
 *
 */
public class RequestService extends ActivityWithMenu implements OnClickListener, OnFocusChangeListener, OnItemClickListener{
	private int mHour;
	private int mMinute;


	Button registerRequest;
	AutoCompleteTextView pro;
	EditText address, date, time, description;
	String sPro, sAddress, sDate, sTime, sDescription, userToken;
	HttpPost httppost;
	//	DatePicker datePicker;
	private List<String> photoList;
	private Button buttonAddPhoto;
	private PhotoRequestAdapter photoAdapter;
	private Button buttonAddVideo;
	private ArrayList<String> videoList;
	private VideoRequestAdapter videoAdapter;
	private Gallery galleryPhoto;
	private Gallery galleryVideo;
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	static final int PHOTO_PICKER = 1;
	static final int VIDEO_PICKER = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.requestservice);
		initialize();

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
		registerRequest.setOnClickListener(this);
	}

	private String[] getServiceList() {
		String[] returnedString = new String[0];

		Resources res = getResources();
		HttpGet httpGet = new HttpGet(res.getString(R.string.app_url) + "services/getAll");
		HttpClient httpclient = new DefaultHttpClient();
		// Execute HTTP Get Request
		try {
			HttpResponse response = httpclient.execute(httpGet);

			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			JSONObject jsonResponse = new JSONObject(builder.toString());

			System.out.println(jsonResponse.toString());
			JSONArray jsonData = new JSONArray(jsonResponse.getString("data"));

			returnedString = new String[jsonData.length()];
			for (int i = 0; i < jsonData.length(); i++) {
				JSONObject jsonService = new JSONObject(jsonData.getJSONObject(i).toString());
				JSONObject jsonLabel = new JSONObject(jsonService.getJSONObject("Service").toString());
				returnedString[i] = jsonLabel.getString("label");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnedString;
	}

	private void initialize() {
		// TODO Auto-generated method stub

		pro = (AutoCompleteTextView) findViewById(R.id.etProRequest);
		String[] serviceList = getServiceList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.autocomplete_item, serviceList);
		pro.setAdapter(adapter);

		address = (EditText) findViewById(R.id.etAdressRequest);
		date = (EditText) findViewById(R.id.etDateRequest);
		time = (EditText) findViewById(R.id.etTimeRequest);
		description = (EditText) findViewById(R.id.etDescriptionRequest);
		registerRequest = (Button) findViewById(R.id.buttonRegisterRequest);

		buttonAddPhoto = (Button) findViewById(R.id.buttonAddPhoto);
		buttonAddPhoto.setOnClickListener(this);
		photoList = new ArrayList<String>();
		photoAdapter = new PhotoRequestAdapter(this, R.id.galleryPhoto, photoList);
		galleryPhoto = (Gallery) findViewById(R.id.galleryPhoto);
		galleryPhoto.setAdapter(photoAdapter);
		galleryPhoto.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				photoList.remove(arg2);
				photoAdapter.notifyDataSetChanged();
				return false;
			}
		});
		buttonAddVideo = (Button) findViewById(R.id.buttonAddVideo);
		buttonAddVideo.setOnClickListener(this);
		videoList = new ArrayList<String>();
		videoAdapter = new VideoRequestAdapter(this, R.id.galleryVideo, videoList);
		galleryVideo = (Gallery) findViewById(R.id.galleryVideo);
		galleryVideo.setAdapter(videoAdapter);
		galleryVideo.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				videoList.remove(arg2);
				videoAdapter.notifyDataSetChanged();
				return false;
			}
		});
		galleryVideo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				File file = new File(videoList.get(arg2));
				intent.setDataAndType(Uri.fromFile(file), "video/*");
				startActivity(intent);

			}
		});
		//		datePicker = (DatePicker) findViewById(R.id.datePicker);
	}

	public void WriteJSON(){

		sPro = pro.getText().toString();
		sAddress = address.getText().toString();
		sDate = date.getText().toString();
		sTime = time.getText().toString();
		sDescription = description.getText().toString();

		Resources res = getResources();
		String url = res.getString(R.string.app_url) + "requests/add";

		UserDatabase entry = new UserDatabase(RequestService.this);
		entry.open();
		userToken = entry.getUserdataValue(1);
		entry.close();

		ArrayList<String> keysList = new ArrayList<String>();
		ArrayList<String> dataList = new ArrayList<String>();
		keysList.add("userToken"); dataList.add(userToken);
		keysList.add("service_id"); dataList.add(sPro);
		keysList.add("address"); dataList.add(sAddress);
		keysList.add("date"); dataList.add(sDate);
		keysList.add("time"); dataList.add(sTime);
		keysList.add("description"); dataList.add(sDescription);

		Intent intent = new Intent(this, RequestUploaderService.class);
		intent.putExtra("url", url);
		intent.putStringArrayListExtra("keys", keysList);
		intent.putStringArrayListExtra("data", dataList);
		intent.putStringArrayListExtra("image", (ArrayList<String>) photoList);
		intent.putStringArrayListExtra("video", videoList);
		startService(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch(v.getId()){
		case R.id.etDateRequest:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.buttonAddPhoto:
			Intent intent = new Intent(this, PhotoPicker.class);
			startActivityForResult(intent, PHOTO_PICKER);
			break;
		case R.id.buttonAddVideo:
			Intent intentVideo = new Intent(this, VideoPicker.class);
			startActivityForResult(intentVideo, VIDEO_PICKER);
			break;
		case R.id.buttonRegisterRequest:
			System.out.println("***BUTTON REQUEST REGISTER PRESSED");
			WriteJSON();

			Intent iHome = new Intent("com.yalong.DASHBOARD");
			startActivity(iHome);

			break;
		}
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_PICKER && resultCode == RESULT_OK) {
			String uri = data.getStringExtra("path");
			photoList.add(uri);
			photoAdapter.notifyDataSetChanged();
		}
		if (requestCode == VIDEO_PICKER && resultCode == RESULT_OK) {
			String uri = data.getStringExtra("path");
			System.out.println(uri);
			videoList.add(uri);
			videoAdapter.notifyDataSetChanged();
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
		if (arg0.getId() == R.id.etDateRequest && arg1 == true) {
			showDialog(DATE_DIALOG_ID);
		}
		if (arg0.getId() == R.id.etTimeRequest && arg1 == true) {
			showDialog(TIME_DIALOG_ID);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg1.getId() == R.id.galleryPhoto) {
			photoList.remove(arg2);
			photoAdapter.notifyDataSetChanged();
		}

	}

}
