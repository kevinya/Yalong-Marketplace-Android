package com.yalong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
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

/**
 * Application login
 *
 */
public class Login extends Activity implements OnClickListener{

	Button signin, login;
	JSONObject object;
	EditText emailET, passwordET;
	String email, password, responseNumber;
	String userToken="defaultToken";
	HttpPost httppost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initialize();
		signin.setOnClickListener(this);
		login.setOnClickListener(this);

	}

	public void writeJSON() {

		email = emailET.getText().toString();
		password = passwordET.getText().toString();
		
		Resources res = getResources();
		httppost = new HttpPost(res.getString(R.string.app_url) + "users/login");

		//On cree la liste qui contiendra tous nos parametres
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 

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
			
			responseNumber = jsonobject.get("responseNumber").toString();
			
			if(responseNumber.contentEquals("200")){
			userToken = jsonobject.get("userToken").toString();
			}
			System.out.println("***USERTOKEN : " + userToken);
			

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("EMAIL : " + email);
		
		// Ecrit le userToken en base de donnees locale :
		
		UserDatabase entry = new UserDatabase(Login.this);
		entry.open();
		entry.deleteEntry(1);
		entry.createEntryWithID(1, "userToken", userToken);

		entry.close();

		if(responseNumber.contains("200")){
		Intent iHome = new Intent("com.yalong.DASHBOARD");
		startActivity(iHome);
		}
	}

	private void initialize() {
		// TODO Auto-generated method stub
		signin = (Button) findViewById(R.id.buttonSignin);
		login = (Button) findViewById(R.id.buttonLogin);
		emailET = (EditText) findViewById(R.id.etEmail);
		passwordET = (EditText) findViewById(R.id.etPassword);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.buttonSignin:
			Intent iSignin = new Intent("com.yalong.SIGNIN");
			startActivity(iSignin);
			break;

		case R.id.buttonLogin:
			writeJSON();
			
			break;
		}
	}
	
	

}
