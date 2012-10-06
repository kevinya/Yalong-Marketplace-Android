package com.yalong.entity;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	private int id;
	private String email;
	private String name;
	private String address;
	private String phone;
	private String mobile;

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    
	public User(JSONObject object) {
		try {
			id = object.getInt("id");
			email = object.getString("email");
			name = object.getString("name");
			address = object.getString("address");
			phone = object.getString("phone");
			mobile = object.getString("mobile");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public User(Parcel in) {
		id = in.readInt();
		email = in.readString();
		name = in.readString();
		address = in.readString();
		phone = in.readString();
		mobile = in.readString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public int describeContents() {
		return 6;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(id);
		arg0.writeString(email);
		arg0.writeString(name);
		arg0.writeString(address);
		arg0.writeString(phone);
		arg0.writeString(mobile);
	}
	
}
