package com.yalong.user;

import java.io.BufferedReader;
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

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yalong.ActivityWithMenu;
import com.yalong.R;
import com.yalong.UserDatabase;
import com.yalong.entity.Offer;
import com.yalong.entity.PhotoAdapter;
import com.yalong.entity.ServiceProvider;
import com.yalong.entity.VideoAdapter;

public class OfferChoiceActivity extends ActivityWithMenu implements OnClickListener {
	private Offer offer;
	private ServiceProvider serviceProvider;
	private String userToken;
	private PhotoAdapter photoAdapter;
	private VideoAdapter videoAdapter;
	private Gallery galleryPhoto;
	private Gallery galleryVideo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UserDatabase entry = new UserDatabase(this);
		entry.open();
		userToken = entry.getUserdataValue(1);
		entry.close();

	    setContentView(R.layout.service_provider_profile);
	    	    
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup parent = (ViewGroup)findViewById(R.id.frame);
		inflater.inflate(R.layout.offer_choice, parent);
		
		Bundle bundle = getIntent().getExtras();
		offer = bundle.getParcelable("offer");
		serviceProvider = getServiceProvider(offer.getServiceProviderId());

		TextView tvServiceProviderName = (TextView) findViewById(R.id.tvServiceProviderName);
		TextView tvService = (TextView) findViewById(R.id.tvService);
		TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
		TextView tvUrl = (TextView) findViewById(R.id.tvUrl);
		TextView tvPhone = (TextView) findViewById(R.id.tvPhone);
		TextView tvMobile = (TextView) findViewById(R.id.tvMobile);
		RatingBar tvNotation = (RatingBar) findViewById(R.id.notation);
		galleryPhoto = (Gallery) findViewById(R.id.galleryPhoto);
		galleryVideo = (Gallery) findViewById(R.id.galleryVideo);
		if (!serviceProvider.equals(null)) {
			tvServiceProviderName.setText(serviceProvider.getCompanyName());
			tvService.setText(serviceProvider.getService());
			tvAddress.setText(serviceProvider.getAddress());
			tvUrl.setText(serviceProvider.getUrl());
			tvPhone.setText(serviceProvider.getPhone());
			tvMobile.setText(serviceProvider.getMobile());
			tvNotation.setRating(serviceProvider.getNotation());


			photoAdapter = new PhotoAdapter(this.getBaseContext(), serviceProvider.getPhotos(), "serviceProviderImages");
			galleryPhoto.setAdapter(photoAdapter);


			videoAdapter = new VideoAdapter(this.getBaseContext(), serviceProvider.getVideos(), "serviceProviderVideos");
			galleryVideo.setAdapter(videoAdapter);
			galleryVideo.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					String url = (String) galleryVideo.getItemAtPosition(position);
					i.setDataAndType(Uri.parse(url), "video/*");
					startActivity(i);
				}
			});

		}

		TextView tvPrice = (TextView) findViewById(R.id.tvPrice);
		TextView tvDate = (TextView) findViewById(R.id.tvDateOffer);
		TextView tvTime = (TextView) findViewById(R.id.tvTime);
		tvPrice.setText(offer.getPrice());
		tvDate.setText(offer.getDate());
		tvTime.setText(offer.getTime());

		Button buttonAccept = (Button) findViewById(R.id.buttonAccept);
		buttonAccept.setOnClickListener(this);
		Button buttonDecline = (Button) findViewById(R.id.buttonDecline);
		buttonDecline.setOnClickListener(this);
		Button buttonNote = (Button) findViewById(R.id.buttonNote);
		buttonNote.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.buttonAccept) {
			answerOffer("accept");
		} else {
			answerOffer("decline");
		}
	}
	
	public ServiceProvider getServiceProvider(int id) {		
		Resources res = getResources();
		System.out.println(res.getString(R.string.app_url) + "serviceProviders/view/" + id);
		HttpPost httppost = new HttpPost(res.getString(R.string.app_url) + "serviceProviders/view/" + id);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		ServiceProvider serviceProvider = null;
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
			JSONObject jsondata = new JSONObject(jsonobject.getString("data"));
			JSONObject jsonserviceprovider = new JSONObject(jsondata.getString("ServiceProvider"));
			serviceProvider = new ServiceProvider(jsonserviceprovider);

			Toast.makeText(this, jsonobject.get("responseMessage").toString(), 50).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceProvider;
	}
	
	private void answerOffer(String uri) {
		Resources res = getResources();
		System.out.println(res.getString(R.string.app_url) + "offers/" + uri);
		HttpPost httppost = new HttpPost(res.getString(R.string.app_url) + "offers/" + uri);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", userToken));
		nameValuePairs.add(new BasicNameValuePair("offer_id", String.valueOf(offer.getId())));
		
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
				Intent intent = new Intent(this, UserRequestTabWidget.class);
				startActivity(intent);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
