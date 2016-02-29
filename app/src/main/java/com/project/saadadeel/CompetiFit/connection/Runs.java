package com.project.saadadeel.CompetiFit.connection;

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

    public Runs(){}

    public Runs(int d, int t){
        this.distance = d;
        this.time = t;
        this.speed = d/t;
        this.date = getDate();
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

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
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

