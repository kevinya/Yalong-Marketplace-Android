package com.yalong.serviceprovider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.yalong.DetailRequest;
import com.yalong.R;

public class MatchingDetailRequests extends DetailRequest implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup parent = (ViewGroup)findViewById(R.id.frame);
		inflater.inflate(R.layout.matching_detail_requests, parent);
		
		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		bundle.putParcelable("request", request);

		Intent intent = new Intent(this, OfferForm.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
}
