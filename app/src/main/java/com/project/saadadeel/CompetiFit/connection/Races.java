package com.project.saadadeel.CompetiFit.connection;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saadadeel on 22/02/2016.
 */
public class Races implements Parcelable{
    public String status;
    public String result;
    public String id;
    public String competitorUsername;

    public int challengedMiles;
    public int challengedTime;
    public Boolean isChallengeMet;
    public int speedChallengeCompleted;

    public Races(){}

    public Races(String cUN){
        this.status = "pending";
        this.competitorUsername = cUN;
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
        challengedTime = in.readInt();
        speedChallengeCompleted = in.readInt();
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

    public void challengeAccepted(int miles, int time){
        this.status = "active";
        this.challengedMiles = miles;
        this.challengedTime = time;
    }

    public String getCUsername(){
        return this.competitorUsername;
    }
    public String getStatus(){return this.status;}

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
        dest.writeInt(challengedMiles);
        dest.writeInt(challengedTime);
        dest.writeInt(speedChallengeCompleted);
    }
//
//        public void challengeCompleted(int completedMiles, int completedSpeed){
//            this.status = "complete";
//            if(completedMiles>=this.challengedMiles){
//                this.isChallengeMet = true;
//            }else{
//                this.isChallengeMet = false;
//            }
//        }
}
