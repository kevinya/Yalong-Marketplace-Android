package com.yalong.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Offer implements Parcelable {
	private int id;
	private int serviceProviderId;
	private String serviceProviderName;
	private int serviceProviderNotation;
	private String date;
	private String time;
	private String price;
	private int accepted;
	private String reason;

    public static final Parcelable.Creator<Offer> CREATOR
            = new Parcelable.Creator<Offer>() {
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };
    
	@Override
	public int describeContents() {
		return 5;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeInt(this.serviceProviderId);
		dest.writeString(this.serviceProviderName);
		dest.writeInt(this.serviceProviderNotation);
		dest.writeString(this.date);
		dest.writeString(this.time);
		dest.writeString(this.price);
		dest.writeInt(this.accepted);
		dest.writeString(this.reason);
	}

	public Offer(Parcel in) {
		this.id = in.readInt();
		this.serviceProviderId = in.readInt();
		this.serviceProviderName = in.readString();
		this.serviceProviderNotation = in.readInt();
		this.date = in.readString();
		this.time = in.readString();
		this.price = in.readString();
		this.accepted = in.readInt();
		this.reason = in.readString();
	}
	
	public Offer(JSONObject object) {
		try {
			this.id = object.getInt("id");
			this.serviceProviderId = object.getInt("service_provider_id");
			this.serviceProviderName = object.getString("service_provider_name");
			this.serviceProviderNotation = object.getInt("service_provider_notation");
			this.date = object.getString("date");
			this.time = object.getString("time");
			this.price = object.getString("price");
			this.accepted = object.getInt("accepted");
			this.reason = object.getString("reason");
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

	public int getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(int serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	public String getServiceProviderName() {
		return serviceProviderName;
	}

	public void setServiceProviderName(String serviceProviderName) {
		this.serviceProviderName = serviceProviderName;
	}

	public int getServiceProviderNotation() {
		return serviceProviderNotation;
	}

	public void setServiceProviderNotation(int serviceProviderNotation) {
		this.serviceProviderNotation = serviceProviderNotation;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
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

	public int getAccepted() {
		return accepted;
	}

	public void setAccepted(int accepted) {
		this.accepted = accepted;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
