package com.yalong.entity;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yalong.R;

public class OfferAdapter extends ArrayAdapter<Offer> {

	public OfferAdapter(Context context, int textViewResourceId, List<Offer> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View returnedView = convertView;
		if (returnedView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			returnedView = layoutInflater.inflate(R.layout.offer_item, null);
		}
		Offer offer = getItem(position);
		
		TextView price = (TextView) returnedView.findViewById(R.id.price);
		price.setText(String.valueOf(offer.getPrice()));

		RatingBar notation = (RatingBar) returnedView.findViewById(R.id.notation);
		notation.setRating(offer.getServiceProviderNotation());
		
		TextView name = (TextView) returnedView.findViewById(R.id.serviceProviderName);
		name.setText(offer.getServiceProviderName());

		TextView date = (TextView) returnedView.findViewById(R.id.date);
		date.setText(offer.getDate());
		
		TextView time = (TextView) returnedView.findViewById(R.id.time);
		time.setText(offer.getTime());
		
		return returnedView;
	}
	
}
