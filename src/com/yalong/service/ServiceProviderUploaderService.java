package com.yalong.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.webkit.MimeTypeMap;

public class ServiceProviderUploaderService extends IntentService {

	public ServiceProviderUploaderService() {
		super("ServiceProviderUploaderService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		System.out.println("serviceCalled");
		String url = intent.getStringExtra("url");
		ArrayList<String> keysList = intent.getStringArrayListExtra("keys");
		ArrayList<String> dataList = intent.getStringArrayListExtra("data");
		ArrayList<String> imageList = intent.getStringArrayListExtra("image");
		ArrayList<String> videoList = intent.getStringArrayListExtra("video");
		
		List<PostParameter> params = new ArrayList<PostParameter>();
		for(int i = 0; i < dataList.size(); i++) {
			params.add(new PostParameter<String>("data[ServiceProvider]["+keysList.get(i)+"]", dataList.get(i)));
		}
		for(int i = 0; i < imageList.size(); i++) {
			params.add(new PostParameter<File>("data[ServiceProviderImage]["+i+"][file]", new File(imageList.get(i)), getMimeTypeFromUrl(imageList.get(i))));
		}
		for(int i = 0; i < videoList.size(); i++) {
			params.add(new PostParameter<File>("data[ServiceProviderVideo]["+i+"][file]", new File(videoList.get(i)), getMimeTypeFromUrl(videoList.get(i))));
		}
		
		MultipartPost post = new MultipartPost(params);
		try {
			String response = post.send(url);
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getMimeTypeFromUrl(String url) {
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		return mime.getMimeTypeFromExtension(extension);
	}
}
