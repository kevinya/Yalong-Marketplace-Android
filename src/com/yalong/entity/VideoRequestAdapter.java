package com.yalong.entity;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.yalong.R;

public class VideoRequestAdapter extends ArrayAdapter<String> {

	public VideoRequestAdapter(Context context, int textViewResourceId, List<String> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {View returnedView = convertView;
		if (returnedView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			returnedView = layoutInflater.inflate(R.layout.video_item, null);
		}
		return returnedView;
	}

	
}
