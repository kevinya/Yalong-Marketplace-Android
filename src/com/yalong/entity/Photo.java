package com.yalong.entity;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
	private int id;
	private String name;

    public static final Parcelable.Creator<Photo> CREATOR
            = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
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
	
	public Photo(JSONObject object) {
		try {
			this.id = object.getInt("id");
			this.name = object.getString("name");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Photo(Parcel in) {
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
