package com.yalong;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;

import com.yalong.entity.PhotoRequestAdapter;
import com.yalong.entity.VideoRequestAdapter;
import com.yalong.file.PhotoPicker;
import com.yalong.file.VideoPicker;
import com.yalong.service.ServiceProviderUploaderService;
import com.yalong.serviceprovider.ServiceProviderHome;

public class ServiceProviderSignin extends ActivityWithMenu implements OnClickListener {
	private static final int PHOTO_PICKER = 0;
	private static final int VIDEO_PICKER = 1;
	private String userToken;
	private EditText companyNameEdit;
	private AutoCompleteTextView serviceProvidedEdit;
	private EditText urlEdit;
	private EditText addressEdit;
	private EditText phoneEdit;
	private EditText mobileEdit;
	private Button inscriptionButton;
	protected List<String> videoList;
	private Gallery galleryVideo;
	protected ArrayAdapter<String> videoAdapter;
	private View buttonAddVideo;
	protected ArrayAdapter<String> photoAdapter;
	protected List<String> photoList;
	private Gallery galleryPhoto;
	private Button buttonAddPhoto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.serviceprovidersignin);
		initialize();
	}
	
	private void initialize() {
		UserDatabase entry = new UserDatabase(ServiceProviderSignin.this);
		entry.open();
		userToken = entry.getUserdataValue(1);
		entry.close();
		
		companyNameEdit = (EditText) findViewById(R.id.etCompanyName);
		serviceProvidedEdit = (AutoCompleteTextView) findViewById(R.id.etServiceProvided);
		String[] serviceList = getServiceList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.autocomplete_item, serviceList);
		serviceProvidedEdit.setAdapter(adapter);
		urlEdit = (EditText) findViewById(R.id.etUrl);
		addressEdit = (EditText) findViewById(R.id.etAddress);
		phoneEdit = (EditText) findViewById(R.id.etPhone);
		mobileEdit = (EditText) findViewById(R.id.etMobile);

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
		
		inscriptionButton = (Button) findViewById(R.id.buttonServiceProviderSignin);
		
		inscriptionButton.setOnClickListener(this);
	}
	

	protected void sendData() {
		Resources res = getResources();
		String url = res.getString(R.string.app_url) + "serviceProviders/create";
		
		ArrayList<String> keysList = new ArrayList<String>();
		ArrayList<String> dataList = new ArrayList<String>();
		keysList.add("userToken"); dataList.add(userToken);
		keysList.add("service_id"); dataList.add(serviceProvidedEdit.getText().toString());
		keysList.add("company_name"); dataList.add(companyNameEdit.getText().toString());
		keysList.add("url"); dataList.add(urlEdit.getText().toString());
		keysList.add("address"); dataList.add(addressEdit.getText().toString());
		keysList.add("phone"); dataList.add(phoneEdit.getText().toString());
		keysList.add("mobile"); dataList.add(mobileEdit.getText().toString());
		
		Intent intent = new Intent(this, ServiceProviderUploaderService.class);
		intent.putExtra("url", url);
		intent.putStringArrayListExtra("keys", keysList);
		intent.putStringArrayListExtra("data", dataList);
		intent.putStringArrayListExtra("image", (ArrayList<String>) photoList);
		intent.putStringArrayListExtra("video", (ArrayList<String>) videoList);
		startService(intent);
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

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.buttonAddPhoto:
				Intent intent = new Intent(this, PhotoPicker.class);
				startActivityForResult(intent, PHOTO_PICKER);
				break;
			case R.id.buttonAddVideo:
				Intent intentVideo = new Intent(this, VideoPicker.class);
				startActivityForResult(intentVideo, VIDEO_PICKER);
				break;
			case R.id.buttonServiceProviderSignin:
				sendData();
				Intent intentSignin = new Intent(this, ServiceProviderHome.class);
				startActivity(intentSignin);
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

}
