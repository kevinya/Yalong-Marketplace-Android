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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yalong.ActivityWithMenu;
import com.yalong.R;
import com.yalong.UserDatabase;
import com.yalong.entity.User;

public class UserViewProfile extends ActivityWithMenu implements OnClickListener {

	private String userToken;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UserDatabase entry = new UserDatabase(this);
		entry.open();
		userToken = entry.getUserdataValue(1);
		entry.close();

	    setContentView(R.layout.user_profile);
	    
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup parent = (ViewGroup)findViewById(R.id.frame);
		inflater.inflate(R.layout.matching_detail_requests, parent);
		Button button = (Button) findViewById(R.id.button);
		button.setText("Modifier mon profil");
		button.setOnClickListener(this);
	    
		user = getUser();

		TextView tvName = (TextView) findViewById(R.id.tvName);
		tvName.setText(user.getName());
		TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
		tvEmail.setText(user.getEmail());
		TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
		tvAddress.setText(user.getAddress());
		TextView tvPhone = (TextView) findViewById(R.id.tvPhone);
		tvPhone.setText(user.getPhone());
		TextView tvMobile = (TextView) findViewById(R.id.tvMobile);
		tvMobile.setText(user.getMobile());

	}

	public User getUser() {
		Resources res = getResources();
		System.out.println(res.getString(R.string.app_url) + "users/view");
		HttpPost httppost = new HttpPost(res.getString(R.string.app_url) + "users/view");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", userToken));
		
		User user = null;
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
			JSONObject jsonuser = new JSONObject(jsondata.getString("User"));
			user = new User(jsonuser);

			Toast.makeText(this, jsonobject.get("responseMessage").toString(), 50).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, UserEditProfil.class);
		intent.putExtra("user", user);
		startActivity(intent);
	}
	
}
