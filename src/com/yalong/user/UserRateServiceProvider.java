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

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.yalong.ActivityWithMenu;
import com.yalong.R;
import com.yalong.UserDatabase;
import com.yalong.entity.Offer;

public class UserRateServiceProvider extends ActivityWithMenu implements OnClickListener {
	private Offer offer;
	private String userToken;
	private RatingBar ratingBar;
	private EditText etCommentaire;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_rate_service_provider);
		
		UserDatabase entry = new UserDatabase(this);
		entry.open();
		userToken = entry.getUserdataValue(1);
		entry.close();
		offer = getIntent().getExtras().getParcelable("offer");
		
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		etCommentaire = (EditText) findViewById(R.id.etCommentaire);
		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		rateServiceProvider();
	}

	private void rateServiceProvider() {
		Resources res = getResources();
		System.out.println(res.getString(R.string.app_url) + "opinions/add");
		HttpPost httppost = new HttpPost(res.getString(R.string.app_url) + "opinions/add");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", userToken));
		nameValuePairs.add(new BasicNameValuePair("offer_id", String.valueOf(offer.getId())));
		nameValuePairs.add(new BasicNameValuePair("service_provider_id", String.valueOf(offer.getServiceProviderId())));
		nameValuePairs.add(new BasicNameValuePair("notation", String.valueOf(ratingBar.getRating())));
		nameValuePairs.add(new BasicNameValuePair("comment", etCommentaire.getText().toString()));
		
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
