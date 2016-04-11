package com.project.saadadeel.CompetiFit.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

/**
 * Created by saadadeel on 07/03/2016.
 */
public class minimalUser implements Parcelable {
    public String username;
    public double averageDistance = 0.0;
    public double averageSpeed = 0.0;
    public int userScore = 0;
    public int userLevel;

    public minimalUser(){}

    protected minimalUser(Parcel in) {
        username = in.readString();
        userScore = in.readInt();
        userLevel = in.readInt();
        averageDistance = in.readDouble();
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

    public double getAverageDist(){
        Double aDist = this.averageDistance/1000;
        return RoundTo2Decimals(aDist);
    }
    public double getAverageSpeed(){
        Double sDist = this.averageSpeed*3.6;
        return RoundTo2Decimals(sDist);
    }

    public double RoundTo2Decimals(double val) {
        if (val < 1000) {
            DecimalFormat df2 = new DecimalFormat("#.00");
            return Double.valueOf(df2.format(val));
        }
        return 0.0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeInt(userScore);
        dest.writeInt(userLevel);
        dest.writeDouble(averageDistance);
        dest.writeDouble(averageSpeed);
    }
}
