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
import android.widget.TextView;
import android.widget.Toast;

import com.yalong.ActivityWithMenu;
import com.yalong.Dashboard;
import com.yalong.R;
import com.yalong.entity.User;

public class UserEditProfil extends ActivityWithMenu implements OnClickListener {

	private User user;
	private EditText etPassword;
	private EditText etName;
	private EditText etAddress;
	private EditText etPhone;
	private EditText etMobile;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_edit_profile);
		initialize();
	}

	private void initialize() {
		user = getIntent().getParcelableExtra("user");
		
		TextView etEmailSignin = (TextView) findViewById(R.id.etEmailSignin);
		etEmailSignin.setText(user.getEmail());
		etPassword = (EditText) findViewById(R.id.etPassword);
		etName = (EditText) findViewById(R.id.etName);
		etName.setText(user.getName());
		etAddress = (EditText) findViewById(R.id.etAddress);
		etAddress.setText(user.getAddress());
		etPhone = (EditText) findViewById(R.id.etPhone);
		etPhone.setText(user.getPhone());
		etMobile = (EditText) findViewById(R.id.etMobile);
		etMobile.setText(user.getMobile());
		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		editProfile();
	}

	private void editProfile() {
		Resources res = getResources();
		HttpPost httppost = new HttpPost(res.getString(R.string.app_url) + "users/create");
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); //On cree la liste qui contiendra tous nos parametres
		 
		//Et on y rajoute nos parametres
		nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(user.getId())));
		if (!etPassword.getText().toString().equals("")) {
			nameValuePairs.add(new BasicNameValuePair("password", etPassword.getText().toString()));
		}
		nameValuePairs.add(new BasicNameValuePair("name", etName.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("address", etAddress.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("phone", etPhone.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("mobile", etMobile.getText().toString()));
	
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
			
			JSONObject jsonobject = new JSONObject(builder.toString());

			Toast.makeText(this, jsonobject.get("responseMessage").toString(), 50).show();
			
			if (jsonobject.get("responseNumber").toString().equals("200")) {
				Intent iLogin = new Intent(this, Dashboard.class);
				startActivity(iLogin);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
