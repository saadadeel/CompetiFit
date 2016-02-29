package com.project.saadadeel.CompetiFit.connection;

import android.os.Parcel;
import android.os.Parcelable;

import com.project.saadadeel.CompetiFit.Race;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by saadadeel on 01/02/2016.
 */
public class User implements Parcelable{

    private String username;
    private String firstName;
    private String lastName;
    private String userEmail;
    private String password;

    private int userAge;
    private String userGender;
    private String userLocation;

    private int userScore;
    private int userLevel;

    public ArrayList<Races> races = new ArrayList<Races>();
    public ArrayList<Runs> runs = new ArrayList<Runs>();

    public User (String uname, String fName, String lName, int age, String gender, String score){

    }

    public User (String uname, String password, String fName, String lName){
        this.username = uname;
        this.firstName = fName;
        this.lastName = lName;
        this.password = password;
    }

    public User(){}

    //Getters

    protected User(Parcel in) {
        username = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        userEmail = in.readString();
        userAge = in.readInt();
        userGender = in.readString();
        userLocation = in.readString();
        userScore = in.readInt();
//        races = in.readArrayList(null);
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
       return this.firstName;
    }
    public String getUserLastName(){
        return this.lastName;
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
    public String getUserPassword(){
        return this.password;
    }

    //Setters

    public void setUserScore(int addScore){
        this.userScore += addScore;
    }

    public void setUsername(String un){
        this.username = un;
    }

    public void setPassword(String pw){
        this.password = pw;
    }

    public ArrayList<Races> getRaces(){
        return this.races;
    }

    public Races getSingleRace(){
        ArrayList<Races> sample = this.getRaces();
        return sample.get(0);
    }

    public void addRun(Runs run){
        this.runs.add(0,run);
    }

    public ArrayList<Runs> getRuns(){return this.runs;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(userEmail);
        dest.writeInt(userAge);
        dest.writeString(userGender);
        dest.writeString(userLocation);
        dest.writeInt(userScore);
        dest.writeList(races);
    }

    public void setUserLevel(int miles, int time){
    }

    public void setUserLeague(){
    }

    public String[] getUserLeague(){
        return null;
    }
}
