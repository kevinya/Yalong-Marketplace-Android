package com.yalong.serviceprovider;

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

import com.yalong.ActivityWithMenu;
import com.yalong.R;
import com.yalong.ServiceProviderSignin;
import com.yalong.UserDatabase;

public class ServiceProviderHome extends ActivityWithMenu implements OnClickListener {
	private Button buttonUserHome, buttonServiceProviderSignin, buttonServiceProviderRequests;
	private Button buttonServiceProviderProfile;
	private String userToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UserDatabase entry = new UserDatabase(this);
		entry.open();
		userToken = entry.getUserdataValue(1);
		entry.close();
		
		routeIfNotRegistered();
		
		setContentView(R.layout.dashboard_layout_sp);
		initialize();
	}

	private void initialize() {
		
		buttonUserHome = (Button) findViewById(R.id.dashboardSPUser);
		buttonUserHome.setOnClickListener(this);

		buttonServiceProviderSignin = (Button) findViewById(R.id.dashboardSPCreate);
		buttonServiceProviderSignin.setOnClickListener(this);
		
		buttonServiceProviderRequests = (Button) findViewById(R.id.dashboardSPMyRequests);
		buttonServiceProviderRequests.setOnClickListener(this);
		
		buttonServiceProviderProfile = (Button) findViewById(R.id.dashboardSPProfile);
		buttonServiceProviderProfile.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch(v.getId()){
		case R.id.dashboardSPUser:
			intent = new Intent("com.yalong.DASHBOARD");
			break;
		case R.id.dashboardSPCreate:
			intent = new Intent("com.yalong.SERVICEPROVIDERSIGNIN");
			break;
		case R.id.dashboardSPMyRequests:
			intent = new Intent("com.yalong.serviceprovider.REQUESTTABWIDGET");
			break;
		case R.id.dashboardSPProfile:
			intent = new Intent(this, ServiceProviderViewProfil.class);
			break;
		}
		startActivity(intent);
	}

	protected void routeIfNotRegistered() {
		
		Resources res = getResources();
		HttpPost httppost = new HttpPost(res.getString(R.string.app_url) + "serviceProviders/isServiceProvider");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", userToken));
		
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response=httpclient.execute(httppost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			System.out.println(builder.toString());
			JSONObject jsonobject = new JSONObject(builder.toString());
			if (jsonobject.isNull("data")) {
				Intent intent = new Intent(this, ServiceProviderSignin.class);
				startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
