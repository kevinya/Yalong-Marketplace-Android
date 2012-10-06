package com.yalong.serviceprovider;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.yalong.ActivityWithMenu;
import com.yalong.R;
import com.yalong.UserDatabase;
import com.yalong.entity.PhotoAdapter;
import com.yalong.entity.PhotoRequestAdapter;
import com.yalong.entity.ServiceProvider;
import com.yalong.entity.VideoAdapter;
import com.yalong.entity.VideoRequestAdapter;
import com.yalong.file.PhotoPicker;
import com.yalong.file.VideoPicker;
import com.yalong.service.ServiceProviderUploaderService;

public class ServiceProviderEditProfil extends ActivityWithMenu implements OnClickListener {
	private static final int PHOTO_PICKER = 0;
	private static final int VIDEO_PICKER = 1;
	private String userToken;
	private EditText companyNameEdit;
	private TextView serviceProvidedEdit;
	private EditText urlEdit;
	private EditText addressEdit;
	private EditText phoneEdit;
	private EditText mobileEdit;
	private Button inscriptionButton;
	private ServiceProvider serviceProvider;
	private Gallery galleryVideo;
	private Gallery galleryPhoto;
	private VideoRequestAdapter videoAdapter;
	private PhotoRequestAdapter photoAdapter;
	private Gallery galleryPhotoOnline;
	private Gallery galleryVideoOnline;
	private PhotoAdapter photoAdapterOnline;
	private VideoAdapter videoAdapterOnline;
	protected ArrayList<String> videoList;
	private Button buttonAddVideo;
	private View buttonAddPhoto;
	private ArrayList<String> photoList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_provider_edit_profile);
		initialize();
	}
	
	private void initialize() {
		UserDatabase entry = new UserDatabase(ServiceProviderEditProfil.this);
		entry.open();
		userToken = entry.getUserdataValue(1);
		entry.close();
		
		serviceProvider = getIntent().getParcelableExtra("serviceProvider");
		
		companyNameEdit = (EditText) findViewById(R.id.etCompanyName);
		companyNameEdit.setText(serviceProvider.getCompanyName());
		serviceProvidedEdit = (TextView) findViewById(R.id.etServiceProvided);
		serviceProvidedEdit.setText(serviceProvider.getService());
		urlEdit = (EditText) findViewById(R.id.etUrl);
		urlEdit.setText(serviceProvider.getUrl());
		addressEdit = (EditText) findViewById(R.id.etAddress);
		addressEdit.setText(serviceProvider.getAddress());
		phoneEdit = (EditText) findViewById(R.id.etPhone);
		phoneEdit.setText(serviceProvider.getPhone());
		mobileEdit = (EditText) findViewById(R.id.etMobile);
		mobileEdit.setText(serviceProvider.getMobile());
		inscriptionButton = (Button) findViewById(R.id.buttonServiceProviderSignin);
		inscriptionButton.setOnClickListener(this);
		

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
		

		
		// Current Gallery
		galleryPhotoOnline = (Gallery) findViewById(R.id.galleryPhotoOnline);
		galleryVideoOnline = (Gallery) findViewById(R.id.galleryVideoOnline);
		photoAdapterOnline = new PhotoAdapter(this.getBaseContext(), serviceProvider.getPhotos(), "serviceProviderImages");
		galleryPhotoOnline.setAdapter(photoAdapterOnline);
		galleryPhotoOnline.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				deletePhoto(serviceProvider.getPhotos().get(arg2).getId());
				serviceProvider.getPhotos().remove(arg2);
				photoAdapterOnline.notifyDataSetChanged();
				return false;
			}
		});

		videoAdapterOnline = new VideoAdapter(this.getBaseContext(), serviceProvider.getVideos(), "serviceProviderVideos");
		galleryVideoOnline.setAdapter(videoAdapterOnline);
		galleryVideoOnline.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				deleteVideo(serviceProvider.getVideos().get(arg2).getId());
				serviceProvider.getVideos().remove(arg2);
				videoAdapterOnline.notifyDataSetChanged();
				return false;
			}
		});
		galleryVideoOnline.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				String url = (String) galleryVideoOnline.getItemAtPosition(position);
				i.setDataAndType(Uri.parse(url), "video/*");
				startActivity(i);
			}
		});
	}

	protected void deleteVideo(int id) {
		Resources res = getResources();
		String url = res.getString(R.string.app_url) + "serviceProviderVideos/delete";
		HttpPost httppost = new HttpPost(url);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", userToken));
		nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));
		
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void deletePhoto(int id) {
		Resources res = getResources();
		String url = res.getString(R.string.app_url) + "serviceProviderImages/delete";
		HttpPost httppost = new HttpPost(url);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", userToken));
		nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));
		
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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void sendData() {
		Resources res = getResources();
		String url = res.getString(R.string.app_url) + "serviceProviders/create";
		
		ArrayList<String> keysList = new ArrayList<String>();
		ArrayList<String> dataList = new ArrayList<String>();
		keysList.add("userToken"); dataList.add(userToken);
		keysList.add("id"); dataList.add(String.valueOf(serviceProvider.getId()));
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
