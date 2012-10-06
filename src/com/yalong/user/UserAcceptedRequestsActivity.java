package com.yalong.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yalong.entity.Request;
import com.yalong.serviceprovider.RequestActivity;

public class UserAcceptedRequestsActivity extends RequestActivity {
	protected String getUri() {
		return "getAccepted";
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Request request = (Request) getListAdapter().getItem(position);
		
		Bundle bundle = new Bundle();
		bundle.putParcelable("request", request);

		Intent intent = new Intent(this, UserAcceptedDetailRequests.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
}
