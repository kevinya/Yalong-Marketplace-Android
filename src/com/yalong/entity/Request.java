package com.yalong.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Request implements Parcelable {
	private int id;
	private String service;
	private String status;
	private String address;
	private String date;
	private String time;
	private String description;
	private List<Offer> offers;
	private List<Photo> photos;
	private List<Video> videos;

	public static final Parcelable.Creator<Request> CREATOR
            = new Parcelable.Creator<Request>() {
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        public Request[] newArray(int size) {
            return new Request[size];
        }
    };
    
	@Override
	public int describeContents() {
		return 10;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(this.id);
		arg0.writeString(this.service);
		arg0.writeString(this.status);
		arg0.writeString(this.address);
		arg0.writeString(this.date);
		arg0.writeString(this.time);
		arg0.writeString(this.description);
		arg0.writeTypedList(this.offers);
		arg0.writeTypedList(this.photos);
		arg0.writeTypedList(this.videos);
	}
	
	public Request() {
		this.offers = new ArrayList<Offer>();
		this.photos = new ArrayList<Photo>();
		this.videos = new ArrayList<Video>();
	}

	public Request(Parcel in) {
		this();
		this.id = in.readInt();
		this.service = in.readString();
		this.status = in.readString();
		this.address = in.readString();
		this.date = in.readString();
		this.time = in.readString();
		this.description = in.readString();
		in.readTypedList(offers, Offer.CREATOR);
		in.readTypedList(photos, Photo.CREATOR);
		in.readTypedList(videos, Video.CREATOR);
	}

	public Request(JSONObject object) {
		this();
		try {
			this.id = object.getInt("id");
			this.service = object.getString("service");
			this.status = object.getString("status");
			this.address = object.getString("address");
			this.date = object.getString("date");
			this.time = object.getString("time");
			this.description = object.getString("description");

			if (!object.isNull("Offer")) {
				JSONArray offersList = new JSONArray(object.getString("Offer"));
				for (int i = 0; i < offersList.length(); i ++) {
					this.offers.add(new Offer(offersList.getJSONObject(i)));
				}
			}
			if (!object.isNull("RequestImage")) {
				JSONArray photoList = new JSONArray(object.getString("RequestImage"));
				for (int i = 0; i < photoList.length(); i ++) {
					this.photos.add(new Photo(photoList.getJSONObject(i)));
				}
			}
			if (!object.isNull("RequestVideo")) {
				JSONArray videoList = new JSONArray(object.getString("RequestVideo"));
				for (int i = 0; i < videoList.length(); i ++) {
					this.videos.add(new Video(videoList.getJSONObject(i)));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public List<Offer> getOffers() {
		return offers;
	}

	public void setOffers(List<Offer> offers) {
		this.offers = offers;
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
