package com.project.saadadeel.CompetiFit.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.util.UUID;

/**
 * Created by saadadeel on 22/02/2016.
 */
public class Races implements Parcelable{
    public String status;
    public String result;
    public String id;
    public String competitorUsername;

    public double challengedMiles;
    public double challengedSpeed;
    public Boolean isComplete;
    public double completedMiles;
    public double completedSpeed;
    public double speedChallengeCompleted;
    public int compUserLevel;
    public boolean challengeComplete;
    public int points;

    public Races(){}

    public Races(String cUN){
        this.status = "pending";
        this.competitorUsername = cUN;
        String uniqueID = UUID.randomUUID().toString();
        this.id = uniqueID;
    }

    public Races(String st, String cUN){
        this.status = "recieved";
        this.competitorUsername = cUN;
        this.id = st;
    }

    protected Races(Parcel in) {
        status = in.readString();
        result = in.readString();
        id = in.readString();
        competitorUsername = in.readString();
        challengedMiles = in.readInt();
        challengedSpeed = in.readDouble();
        speedChallengeCompleted = in.readInt();
        points = in.readInt();
    }

    public static final Creator<Races> CREATOR = new Creator<Races>() {
        @Override
        public Races createFromParcel(Parcel in) {
            return new Races(in);
        }

        @Override
        public Races[] newArray(int size) {
            return new Races[size];
        }
    };

    public void challengeAccepted(int miles, Double speed){
        this.status = "active";
        this.challengedMiles = miles;
        this.challengedSpeed = speed;
    }

    public String getCUsername(){
        return this.competitorUsername;
    }
    public String getStatus(){return this.status;}
    public String getId(){return this.id;}
    public int getCompLevel(){
        return compUserLevel;
    }
    public void setComplete(double dist, double speed){
        this.status = "complete";
        this.isComplete=true;
        this.completedMiles = dist;
        this.completedSpeed = speed;
        if(completedSpeed >= challengedSpeed && completedMiles >= challengedMiles){
            this.challengeComplete = true;
        }
        else{
            this.challengeComplete = false;
        }
    }
    public double getKMChallengedMiles(){
        return RoundTo2Decimals(this.challengedMiles/1000);
    }
    public double getKMChallengedSpeed(){
        return RoundTo2Decimals(this.challengedSpeed*3.6);
    }
    public double RoundTo2Decimals(double val) {
        if (val < 1000) {
            System.out.println(val);
            DecimalFormat df2 = new DecimalFormat("#.00");
            System.out.println("here is the new format" + df2.format(val));

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
        dest.writeString(status);
        dest.writeString(result);
        dest.writeString(id);
        dest.writeString(competitorUsername);
        dest.writeDouble(challengedMiles);
        dest.writeDouble(challengedSpeed);
        dest.writeDouble(speedChallengeCompleted);
        dest.writeInt(points);
    }
}
