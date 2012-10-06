package com.yalong.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yalong.DetailRequest;
import com.yalong.R;
import com.yalong.entity.OfferAdapter;

public class UserWaitingDetailRequests extends DetailRequest implements OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup parent = (ViewGroup) findViewById(R.id.frame);
		inflater.inflate(R.layout.request_list, parent);
		
		ListView lv = (ListView) findViewById(R.id.listView1);
		OfferAdapter adapter = new OfferAdapter(this, android.R.layout.simple_list_item_1, request.getOffers());
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		 Intent intent = new Intent(this, OfferChoiceActivity.class);
		 intent.putExtra("offer", request.getOffers().get(arg2));
		 startActivity(intent);
	}
	
}
