package com.yalong;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.TextView;

import com.yalong.entity.PhotoAdapter;
import com.yalong.entity.Request;
import com.yalong.entity.VideoAdapter;

public class DetailRequest extends ActivityWithMenu {

	TextView job, address, date, time, description, ratingText;
	String sJob, sAddress, sDate, sDuration, sDescription;
	protected Request request;
	private Gallery galleryPhoto;
	private Gallery galleryVideo;
	private PhotoAdapter photoAdapter;
	private VideoAdapter videoAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request_detail);
		initialize();

		Bundle bundle = getIntent().getExtras();
		request = bundle.getParcelable("request");
		job.setText(request.getService());
		address.setText(request.getAddress());
		date.setText(request.getDate());
		time.setText(request.getTime());
		description.setText(request.getDescription());

		photoAdapter = new PhotoAdapter(this.getBaseContext(), request.getPhotos(), "requestImages");
		galleryPhoto.setAdapter(photoAdapter);

		videoAdapter = new VideoAdapter(this.getBaseContext(), request.getVideos(), "requestVideos");
		galleryVideo.setAdapter(videoAdapter);
		galleryVideo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				String url = (String) galleryVideo.getItemAtPosition(position);
				i.setDataAndType(Uri.parse(url), "video/*");
				startActivity(i);
			}
		});

	}

	private void initialize() {
		// TODO Auto-generated method stub
		job = (TextView) findViewById(R.id.tvProAsked);
		address = (TextView) findViewById(R.id.tvAddress);
		date = (TextView) findViewById(R.id.tvDate);
		time = (TextView) findViewById(R.id.tvDuration);
		description = (TextView) findViewById(R.id.tvDescriptionService);
		galleryPhoto = (Gallery) findViewById(R.id.galleryPhoto);
		galleryVideo = (Gallery) findViewById(R.id.galleryVideo);
	}
	
}
