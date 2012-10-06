package com.yalong.entity;

import java.util.List;

import com.yalong.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RequestAdapter extends ArrayAdapter<Request> {
	List<Request> list;

	public RequestAdapter(Context context, int textViewResourceId, List<Request> objects) {
		super(context, textViewResourceId, objects);
		list = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View returnedView = convertView;
		if (returnedView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			returnedView = layoutInflater.inflate(R.layout.request_item, null);
		}
		Request request = getItem(position);
		TextView tvService = (TextView) returnedView.findViewById(R.id.textView1);
		tvService.setText(request.getService());
		TextView tvDescription = (TextView) returnedView.findViewById(R.id.textView2);
		String description = (request.getDescription().length() > 60) ? request.getDescription().substring(0, 60) : request.getDescription();
		tvDescription.setText(description);
		TextView tvDate = (TextView) returnedView.findViewById(R.id.date);
		tvDate.setText(request.getDate());
		TextView tvTime = (TextView) returnedView.findViewById(R.id.time);
		tvTime.setText(request.getTime());
		return returnedView;
	}
	
}
