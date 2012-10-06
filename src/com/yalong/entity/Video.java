package com.yalong.entity;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {
	private int id;
	private String name;

    public static final Parcelable.Creator<Video> CREATOR
            = new Parcelable.Creator<Video>() {
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
    
	@Override
	public int describeContents() {
		return 2;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.name);
	}
	
	public Video(JSONObject object) {
		try {
			this.id = object.getInt("id");
			this.name = object.getString("name");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Video(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
