package com.yalong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.yalong.user.UserViewProfile;

/**
 * Dashboard of the user
 *
 */
public class Dashboard extends ActivityWithMenu implements OnClickListener {
	 
	Button serviceProvider, profile, doRequest, myRequests;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
		initialize();
        
    }

	private void initialize() {
		// TODO Auto-generated method stub
		serviceProvider = (Button) findViewById(R.id.dashboardServiceProvider);
		profile = (Button) findViewById(R.id.dashboardProfile);
		doRequest = (Button) findViewById(R.id.dashboardDoRequest);
		myRequests = (Button) findViewById(R.id.dashboardMyRequests);
		serviceProvider.setOnClickListener(this);
		profile.setOnClickListener(this);
		doRequest.setOnClickListener(this);
		myRequests.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch(v.getId()){
		
		case R.id.dashboardServiceProvider:
			intent = new Intent("com.yalong.serviceprovider.SERVICEPROVIDERHOME");
			break;
			
		case R.id.dashboardDoRequest:
			intent = new Intent("com.yalong.REQUESTSERVICE");
			break;

		case R.id.dashboardMyRequests:
			intent = new Intent("com.yalong.USERREQUESTTABWIDGET");
			break;

		case R.id.dashboardProfile:
			intent = new Intent(this, UserViewProfile.class);
			break;
			
		}
		startActivity(intent);
	}
}
