package com.yalong.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yalong.entity.Request;
import com.yalong.serviceprovider.RequestActivity;

public class UserWaitingRequestsActivity extends RequestActivity {
	protected String getUri() {
		return "getWaiting";
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Request request = (Request) getListAdapter().getItem(position);
		
		Bundle bundle = new Bundle();
		bundle.putParcelable("request", request);

		Intent intent = new Intent(this, UserWaitingDetailRequests.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
}
