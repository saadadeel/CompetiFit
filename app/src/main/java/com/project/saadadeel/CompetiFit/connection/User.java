package com.project.saadadeel.CompetiFit.connection;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saadadeel on 01/02/2016.
 */
public class User implements Parcelable{

    private String username;
    private String userFirstName;
    private String userLastName;
    private String userEmail;

    private int userAge;
    private String userGender;
    private String userLocation;

    private int userScore;

    public User (String uname, String fName, String lName, String email, int age, String gender, String score){

    }

    public User(){}

    //Getters

    protected User(Parcel in) {
        username = in.readString();
        userFirstName = in.readString();
        userLastName = in.readString();
        userEmail = in.readString();
        userAge = in.readInt();
        userGender = in.readString();
        userLocation = in.readString();
        userScore = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUsername(){
      return this.username;
    }
    public String getUserFirstName(){
       return this.userFirstName;
    }
    public String getUserLastName(){
        return this.userLastName;
    }
    public String getUserEmail(){
        return this.userEmail;
    }
    public int getUserAge(){
        return this.userAge;
    }
    public String getUserGender(){
        return this.userGender;
    }
    public String getUserLocation(){
        return this.userLocation;
    }
    public int getUserScore(){
        return this.userScore;
    }

    //Setters

    public void setUserScore(int addScore){
        this.userScore += addScore;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(userFirstName);
        dest.writeString(userLastName);
        dest.writeString(userEmail);
        dest.writeInt(userAge);
        dest.writeString(userGender);
        dest.writeString(userLocation);
        dest.writeInt(userScore);
    }
}
