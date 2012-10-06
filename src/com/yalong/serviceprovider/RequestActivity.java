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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.yalong.Dashboard;
import com.yalong.DetailRequest;
import com.yalong.R;
import com.yalong.UserDatabase;
import com.yalong.entity.Request;
import com.yalong.entity.RequestAdapter;

public abstract class RequestActivity extends ListActivity {
	private String userToken;
	private List<Request> requestList;
	protected abstract String getUri();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestList = new ArrayList<Request>();

		UserDatabase entry = new UserDatabase(RequestActivity.this);
		entry.open();
		userToken = entry.getUserdataValue(1);
		entry.close();
		
		getRequestList();
		setListAdapter(new RequestAdapter(this, android.R.layout.simple_list_item_1, requestList));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Request request = (Request) getListAdapter().getItem(position);
		Bundle bundle = new Bundle();
		bundle.putParcelable("request", request);

		Intent intent = new Intent(this, DetailRequest.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	private void getRequestList() {
		Resources res = getResources();
		HttpPost httppost = new HttpPost(res.getString(R.string.app_url) + "requests/" + getUri());
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userToken", userToken));

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
			JSONArray jsonarray = new JSONArray(jsonobject.getString("data"));
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonObjectElement = jsonarray.getJSONObject(i);
				JSONObject request = new JSONObject(jsonObjectElement.getString("Request"));
				requestList.add(new Request(request));
			}

			Toast.makeText(this, jsonobject.get("responseMessage").toString(), 50).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.menu_yalong, menu);		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		
		case R.id.menuUserMode:
			Intent i1 = new Intent(this, Dashboard.class);
			startActivity(i1);
			break;

		case R.id.menuServiceProviderMode:
			Intent i2 = new Intent(this, ServiceProviderHome.class);
			startActivity(i2);
			break;	
			
		}
		return false;
	}	

}

