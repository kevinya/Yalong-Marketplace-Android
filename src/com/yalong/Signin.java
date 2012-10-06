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
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signin extends Activity implements OnClickListener{

	Button home;
	JSONObject JSONSignin;
	String email, password, passwordConfirmation;
	EditText emailET, passwordET, passwordConfirmationET;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		initialize();

		home.setOnClickListener(this);
	}
	
	public void writeJSON() {
		email = emailET.getText().toString();
		password = passwordET.getText().toString();
		passwordConfirmation = passwordConfirmationET.getText().toString();
		
		if (!password.equals(passwordConfirmation)) {
			Toast.makeText(this, "Mot de passe différents", 50).show();
			return;
		}
		
		Resources res = getResources();
		HttpPost httppost = new HttpPost(res.getString(R.string.app_url) + "users/create");
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); //On cree la liste qui contiendra tous nos parametres
		 
		//Et on y rajoute nos parametres
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", password));
	
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
				Intent iLogin = new Intent("com.yalong.LOGIN");
				startActivity(iLogin);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void initialize() {
		home = (Button) findViewById(R.id.buttonSigninGoHome);
		
		emailET = (EditText) findViewById(R.id.etEmailSignin);
		passwordET = (EditText) findViewById(R.id.etPasswordSignin);
		passwordConfirmationET = (EditText) findViewById(R.id.etPasswordConfirmation);
	}
	
	@Override
	public void onClick(View v) {
		writeJSON();
	}

}
