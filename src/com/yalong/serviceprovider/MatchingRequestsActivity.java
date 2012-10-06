package com.yalong.serviceprovider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yalong.entity.Request;

public class MatchingRequestsActivity extends RequestActivity {

	@Override
	protected String getUri() {
		return "getServiceProviderMatching";
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Request request = (Request) getListAdapter().getItem(position);
		
		Bundle bundle = new Bundle();
		bundle.putParcelable("request", request);

		Intent intent = new Intent(this, MatchingDetailRequests.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
}
