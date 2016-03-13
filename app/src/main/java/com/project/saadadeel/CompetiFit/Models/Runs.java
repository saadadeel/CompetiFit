package com.project.saadadeel.CompetiFit.Models;

        import android.os.Parcel;
        import android.os.Parcelable;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;

/**
 * Created by saadadeel on 22/02/2016.
 */
public class Runs implements Parcelable{
    public String date;
    public int distance;
    public int time;
    public int speed;
    public int score = 3;
    public String username;

    public Runs(){}

    public Runs(int d, int t, String u){
        this.distance = d;
        this.time = t;
        this.speed = d/t;
        this.date = getDate();
        this.username = u;
    }

    protected Runs(Parcel in) {
        date = in.readString();
        distance = in.readInt();
        time = in.readInt();
        speed = in.readInt();
        score = in.readInt();
    }

    public static final Creator<Runs> CREATOR = new Creator<Runs>() {
        @Override
        public Runs createFromParcel(Parcel in) {
            return new Runs(in);
        }

        @Override
        public Runs[] newArray(int size) {
            return new Runs[size];
        }
    };

    public int getDistance(){
        return distance;
    }

    public int getSpeed() {
        return speed;
    }

    public int getTime() {
        return time;
    }

    public int getScore() {
        return score;
    }

    public String getUsername(){return this.username;}

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void setScore(User user) {
        int counter = 0;
        int temp = 0;
        int numOfRuns = user.getRuns().size();
        if (numOfRuns < 5) {
            counter = numOfRuns;
        } else {
            counter = 5;
        }

        ArrayList<Runs> userRuns = user.getRuns();
        ArrayList<Double> dist = new ArrayList<Double>();
        ArrayList<Double> speed = new ArrayList<Double>();

        if (userRuns != null && userRuns.size() > 2) {
            for (int i = 0; i < counter; i++) {
                if (userRuns.get(i).getDistance() > userRuns.get(i + 1).getDistance() && userRuns.get(i).getTime() < userRuns.get(i + 1).getTime()) {
                    System.out.println("/////// here");
                    System.out.println(this.score);
                    this.score += 4;
                    if (i == 4) {
                        this.score += 10;
                    }
                } else {
                    System.out.println("******* yessss what");
                    break;
                }
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeInt(distance);
        dest.writeInt(time);
        dest.writeInt(speed);
        dest.writeInt(score);
    }
}

