package com.yalong.entity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yalong.R;
import com.yalong.service.ImageDownloader;

public class PhotoAdapter extends BaseAdapter {
	private Context context;
	private List<Photo> photos;
	private String photoType;
	private final ImageDownloader imageDownloader = new ImageDownloader();
	
	public PhotoAdapter(Context ctx, String type) {
		context = ctx;
		photos = new ArrayList<Photo>();
		photoType = type;
	}
	
	public PhotoAdapter(Context ctx, List<Photo> photoList, String type) {
		context = ctx;
		photos = photoList;
		photoType = type;
	}
	
	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public String getItem(int position) {
		Resources res = context.getResources();
		return res.getString(R.string.app_url) + photoType + "/view/" + photos.get(position).getId();
	}

	@Override
	public long getItemId(int position) {
		return photos.get(position).getId();
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		View returnedView = view;
		if (returnedView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			returnedView = layoutInflater.inflate(R.layout.photo_item, null);
		}
		ImageView imageView = (ImageView) returnedView.findViewById(R.id.imageView1);
		imageView.setAdjustViewBounds(true);
		imageDownloader.download(getItem(position), (ImageView) imageView);
		return returnedView;
	}

	public ImageDownloader getImageDownloader() {
		return imageDownloader;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	public String getPhotoType() {
		return photoType;
	}

	public void setPhotoType(String photoType) {
		this.photoType = photoType;
	}

}
