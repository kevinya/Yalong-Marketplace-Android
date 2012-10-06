package com.yalong.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.yalong.entity.Request;
import com.yalong.serviceprovider.RequestActivity;

public class UserFinishedRequestsActivity extends RequestActivity {
	protected String getUri() {
		return "getFinished";
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Request request = (Request) getListAdapter().getItem(position);
		
		Bundle bundle = new Bundle();
		bundle.putParcelable("request", request);

		Intent intent = new Intent(this, UserFinishedDetailRequests.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
}
