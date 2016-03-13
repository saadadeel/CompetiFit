package com.project.saadadeel.CompetiFit.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saadadeel on 07/03/2016.
 */
public class minimalUser implements Parcelable {
    public String username;
    public int averageDistance = 0;
    public Double averageSpeed = 0.0;
    public int userScore = 0;
    public int userLevel;

    public minimalUser(){}

    protected minimalUser(Parcel in) {
        username = in.readString();
        userScore = in.readInt();
        userLevel = in.readInt();
        averageDistance = in.readInt();
        averageSpeed = in.readDouble();
    }

    public static final Creator<minimalUser> CREATOR = new Creator<minimalUser>() {
        @Override
        public minimalUser createFromParcel(Parcel in) {
            return new minimalUser(in);
        }

        @Override
        public minimalUser[] newArray(int size) {
            return new minimalUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeInt(userScore);
        dest.writeInt(userLevel);
        dest.writeInt(averageDistance);
        dest.writeDouble(averageSpeed);
    }
}
