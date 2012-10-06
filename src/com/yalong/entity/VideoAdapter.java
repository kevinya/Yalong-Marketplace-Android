package com.yalong.entity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yalong.R;

public class VideoAdapter extends BaseAdapter {
	private Context context;
	private List<Video> videos;
	private String videoType;
	
	public VideoAdapter(Context ctx, String type) {
		context = ctx;
		videos = new ArrayList<Video>();
		videoType = type;
	}
	
	public VideoAdapter(Context ctx, List<Video> videoList, String type) {
		context = ctx;
		videos = videoList;
		videoType = type;
	}

	@Override
	public int getCount() {
		return videos.size();
	}

	@Override
	public String getItem(int position) {
		Resources res = context.getResources();
		return res.getString(R.string.app_url) + videoType + "/view/" + videos.get(position).getId();
	}

	@Override
	public long getItemId(int position) {
		return videos.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.video_item, null);
		}
		return convertView;
	}

}
