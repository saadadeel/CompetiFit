package com.project.saadadeel.CompetiFit.Models;

        import android.os.Parcel;
        import android.os.Parcelable;

        import java.text.DateFormat;
        import java.text.DecimalFormat;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;

/**
 * Created by saadadeel on 22/02/2016.
 */
public class Runs implements Parcelable{
    private String date;
    public double distance;
    public int time;
    public double speed;
    public int score = 3;
    public String username;
    private int isSynced;

    public Runs(){}

    public Runs(Double d, Double speed, String u){
        this.distance = d;
        this.speed = speed;
        this.time = (int)(d/speed);
        this.username = u;
    }

    protected Runs(Parcel in) {
        date = in.readString();
        distance = in.readDouble();
        time = in.readInt();
        speed = in.readDouble();
        score = in.readInt();
        isSynced = in.readInt();
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

    public double getDistance(){
        return distance;
    }

    public double getSpeed() {
        return speed;
    }

    public int getTime() {
        return time;
    }

    public int getScore() {
        return this.score;
    }

    public String getUsername(){return this.username;}

    public String getDate(){
        return this.date;
    }

    public double getKMDist(){
        double kmDist = this.getDistance()/1000;
        return RoundTo2Decimals(kmDist);
    }

    public double getKMperHrSpeed(){
        double kmHRSpeed = this.getSpeed() * 3.6;
        return RoundTo2Decimals(kmHRSpeed);
    }

    double RoundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("#.00");
        System.out.println("here is the new format" + df2.format(val));

        return Double.valueOf(df2.format(val));
    }

    public int getIsSynced(){
        return this.isSynced;
    }

    public void setIsSynced(int a){
        this.isSynced = a;
    }

    public void setScore(User user) {
        int temp = 0;

        ArrayList<Runs> userRuns = user.getRuns();
        ArrayList<Double> dist = new ArrayList<Double>();
        ArrayList<Double> speed = new ArrayList<Double>();

        setBasicPoints(user);
        setBonusPoints(userRuns);
    }

    private void setBasicPoints(User user){
        if(user.getAverageDist()< this.getDistance() && user.getAverageSpeed()<this.getSpeed()){
            System.out.println("/////// here in basic");
            this.score += 3;
        }
    }

    private void setBonusPoints(ArrayList<Runs> userRuns){
        int counter = 0;
        int numOfRuns = userRuns.size();
        if (numOfRuns < 5) {
            counter = numOfRuns;
        } else {
            counter = 5;
        }
        if (userRuns != null && userRuns.size() > 2) {
            for (int i = 0; i < counter; i++) {
                if (userRuns.get(i).getDistance() > userRuns.get(i + 1).getDistance() && userRuns.get(i).getSpeed() > userRuns.get(i + 1).getTime()) {
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


    public void setDate(String date){
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeDouble(distance);
        dest.writeInt(time);
        dest.writeDouble(speed);
        dest.writeInt(score);
        dest.writeInt(isSynced);
    }
}

