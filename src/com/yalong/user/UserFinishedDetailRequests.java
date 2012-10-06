package com.yalong.user;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yalong.DetailRequest;
import com.yalong.R;
import com.yalong.entity.Offer;

public class UserFinishedDetailRequests extends DetailRequest {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup parent = (ViewGroup) findViewById(R.id.frame);
		inflater.inflate(R.layout.offer_choice, parent);
		
		Offer offer = request.getOffers().get(0);
		
		TextView tvPrice = (TextView) findViewById(R.id.tvPrice);
		TextView tvDate = (TextView) findViewById(R.id.tvDateOffer);
		TextView tvTime = (TextView) findViewById(R.id.tvTime);
		tvPrice.setText(offer.getPrice());
		tvDate.setText(offer.getDate());
		tvTime.setText(offer.getTime());

		Button buttonAccept = (Button) findViewById(R.id.buttonAccept);
		buttonAccept.setVisibility(View.GONE);
		Button buttonDecline = (Button) findViewById(R.id.buttonDecline);
		buttonDecline.setVisibility(View.GONE);
		Button buttonNote = (Button) findViewById(R.id.buttonNote);
		buttonNote.setVisibility(View.GONE);
		
	}

}
