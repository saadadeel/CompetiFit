package com.project.saadadeel.CompetiFit.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.project.saadadeel.CompetiFit.Race;

import java.text.DecimalFormat;
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

    private int userScore = 0;
    private int userLevel;
    private double averageDistance;
    private double averageSpeed;

    public int offlineMode = 0;
    private int isSynced;

    public ArrayList<Races> races = new ArrayList<Races>();
    public ArrayList<Runs> runs = new ArrayList<Runs>();
    public ArrayList<minimalUser> league = new ArrayList<minimalUser>();

    public User (String uname, String fName, String lName, int age, String gender, String score){

    }

    public User (String uname, String password, String fName, String lName, int level){
        this.username = uname;
        this.firstName = fName;
        this.lastName = lName;
        this.password = password;
        this.userLevel = level;
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
        offlineMode = in.readByte();
        isSynced = in.readInt();
        averageDistance = in.readDouble();
        averageSpeed = in.readDouble();
        userLevel = in.readInt();
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
    public ArrayList<minimalUser> getleague(){return this.league;}
    public int getSynced(){return this.isSynced;}
    public int getIsOffline(){return this.offlineMode;}
    public int getUserLevel(){return this.userLevel;}
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
            System.out.println(val);
            DecimalFormat df2 = new DecimalFormat("#.00");
            System.out.println("here is the new format" + df2.format(val));

            return Double.valueOf(df2.format(val));
        }
        return 0.0;
    }



        //Setters
    public void setUserScore(int addScore){this.userScore += addScore;}
    public void setUsername(String un){this.username = un;}
    public void setPassword(String pw){
        this.password = pw;
    }
    public void setOfflineMode(int b){this.offlineMode = b;}
    public void setSynced(int a){this.isSynced = a;}

    public ArrayList<Races> getRaces(){
        return this.races;
    }
    public Races getSingleRace(){
        ArrayList<Races> sample = this.getRaces();
        return sample.get(0);
    }
    public void setRaces(ArrayList<Races> races){

    }
    public void addRace(Races races){
        this.races.add(0, races);
    }

    public void addRun(Runs run){
        this.runs.add(0, run);
        run.setScore(this);

        this.updateScore();
        setLevel();
        this.updateAverageDistandSpeed();
    }
    public ArrayList<Runs> getRuns(){return this.runs;}
    public void setRuns(ArrayList<Runs> r){this.runs=r;}

    public void updateScore(){
        int[] runScores;
        int totalScore = 0;
        if(this.getRuns()!=null){
            runScores = new int[this.getRuns().size()];
            for(int i = 0; i<this.getRuns().size(); i++){
                totalScore += this.getRuns().get(i).getScore();
            }
        }
        this.userScore = totalScore;
    }
    public void updateAverageDistandSpeed(){
        double totalDist = 0.0;
        double totalSpeed = 0.0;

        if(this.getRuns()!=null){
            for(int i = 0; i<this.getRuns().size(); i++){
                totalDist += this.getRuns().get(i).getDistance();
                totalSpeed += this.getRuns().get(i).getSpeed();
            }
        }
        this.averageDistance = totalDist/this.getRuns().size();
        this.averageSpeed = totalSpeed/this.getRuns().size();
    }
    public void updateRaces(Races race){
        for (Races r : this.races) {
            if (race.getId() == r.id) {
                this.races.set(this.races.indexOf(r), race);
            }
        }
    }

    public void setLevel(){
        double avgDist = this.getAverageDist();
        double avgSpeed = this.getAverageSpeed();

        if(this.runs.size()>=4) {
            this.userLevel = (int)((avgDist*avgSpeed)/10);
        }
    }

    private void checkAvgSpeedLevel(double speed){
        if(speed <= 8){

        }else if (speed<=10){

        }else if (speed<=12){

        }else if (speed<=14){

        }else if (speed<=16){

        }else if (speed<=18){

        }else if (speed<=20){

        }else if (speed<=22){

        }else if (speed<=24){

        }else if (speed<=26){

        }else if (speed<=28){

        }else if (speed<=30){

        }else if (speed<=31){

        }else if (speed<=32){

        }
    }

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
        dest.writeInt(offlineMode);
        dest.writeInt(isSynced);
        dest.writeDouble(averageDistance);
        dest.writeDouble(averageSpeed);
        dest.writeInt(userLevel);
    }

    public void setUserLeague(ArrayList<minimalUser> minimalUsers){
        this.league = minimalUsers;
    }

    public ArrayList<minimalUser> getUserLeague(){
        return this.league;
    }
}
