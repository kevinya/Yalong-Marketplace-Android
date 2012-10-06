package com.yalong.entity;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.yalong.R;

public class PhotoRequestAdapter extends ArrayAdapter<String> {

	private List<String> uris;

	public PhotoRequestAdapter(Context context, int textViewResourceId, List<String> objects) {
		super(context, textViewResourceId, objects);
		uris = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View returnedView = convertView;
		if (returnedView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			returnedView = layoutInflater.inflate(R.layout.photo_item, null);
		}
		
		String uri = uris.get(position);
		ImageView imageView = (ImageView) returnedView.findViewById(R.id.imageView1);
		Bitmap bm = BitmapFactory.decodeFile(uri);
		imageView.setImageBitmap(bm);
		imageView.setAdjustViewBounds(true);
		
		return returnedView;
	}

	
}
