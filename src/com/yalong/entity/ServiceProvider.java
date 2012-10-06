package com.yalong.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceProvider implements Parcelable {
	private int id;
	private String service;
	private String companyName;
	private String url;
	private String address;
	private String phone;
	private String mobile;
	private int notation;
	private List<Photo> photos;
	private List<Video> videos;

    public static final Parcelable.Creator<ServiceProvider> CREATOR
            = new Parcelable.Creator<ServiceProvider>() {
        public ServiceProvider createFromParcel(Parcel in) {
            return new ServiceProvider(in);
        }

        public ServiceProvider[] newArray(int size) {
            return new ServiceProvider[size];
        }
    };
    
	public ServiceProvider() {
		this.photos = new ArrayList<Photo>();
		this.videos = new ArrayList<Video>();
	}

	public ServiceProvider(JSONObject object) {
		this();
		try {
			this.id = object.getInt("id");
			this.service = object.getString("service");
			this.companyName = object.getString("company_name");
			this.url = object.getString("url");
			this.address = object.getString("address");
			this.phone = object.getString("phone");
			this.mobile = object.getString("mobile");
			this.notation = object.getInt("notation");
			
			if (!object.isNull("ServiceProviderImage")) {
				JSONArray photoList = new JSONArray(object.getString("ServiceProviderImage"));
				for (int i = 0; i < photoList.length(); i ++) {
					this.photos.add(new Photo(photoList.getJSONObject(i)));
				}
			}
			if (!object.isNull("ServiceProviderVideo")) {
				JSONArray videoList = new JSONArray(object.getString("ServiceProviderVideo"));
				for (int i = 0; i < videoList.length(); i ++) {
					this.videos.add(new Video(videoList.getJSONObject(i)));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public ServiceProvider(Parcel in) {
		this();
		id = in.readInt();
		service = in.readString();
		companyName = in.readString();
		url = in.readString();
		address = in.readString();
		phone = in.readString();
		mobile = in.readString();
		in.readTypedList(photos, Photo.CREATOR);
		in.readTypedList(videos, Video.CREATOR);
	}

	@Override
	public int describeContents() {
		return 9;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(service);
		dest.writeString(companyName);
		dest.writeString(url);
		dest.writeString(address);
		dest.writeString(phone);
		dest.writeString(mobile);
		dest.writeTypedList(photos);
		dest.writeTypedList(videos);
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getService() {
		return service;
	}


	public void setService(String service) {
		this.service = service;
	}


	public String getCompanyName() {
		return companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public int getNotation() {
		return notation;
	}


	public void setNotation(int notation) {
		this.notation = notation;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

}
