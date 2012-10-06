package com.yalong;

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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yalong.entity.Request;
import com.yalong.entity.RequestAdapter;

public class MyRequests extends ListActivity {

	TextView myRequest;
	LinearLayout linearLayout;
	int numberOfRequests;
	HttpPost httppost;
	String userToken="defaultToken";
	private List<Request> requestList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initialize();
		getJSON();
		doListView();

	}

	private void doListView() {
		// TODO Auto-generated method stub
		
		RequestAdapter adapter = new RequestAdapter(this, android.R.layout.simple_list_item_1, requestList);
		setListAdapter(adapter);
		
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
	
	private void initialize() {
		requestList = new ArrayList<Request>();

		// TODO Auto-generated method stub
//		myRequest = (TextView) findViewById(R.id.tvMyRequest);
//		linearLayout = (LinearLayout) findViewById(R.id.linearLayoutMyRequests);
	}

	private void getJSON() {
		// TODO Auto-generated method stub
		Resources res = getResources();
		httppost = new HttpPost(res.getString(R.string.app_url) + "requests/getAll");
		
		UserDatabase entry = new UserDatabase(MyRequests.this);
		entry.open();
		userToken = entry.getUserdataValue(1);
		entry.close();
		
		//On cree la liste qui contiendra tous nos parametres
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 

		//Et on y rajoute nos parametres
		nameValuePairs.add(new BasicNameValuePair("userToken", userToken));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

}
