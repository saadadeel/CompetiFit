package com.project.saadadeel.CompetiFit.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.project.saadadeel.CompetiFit.UserMain;

import java.util.List;

/**
 * Created by saadadeel on 07/04/2016.
 */
public class ActivityRecognitionService extends IntentService {

    public ActivityRecognitionService() {
        super("ActivityRecognizedService");
        System.out.print("YOOOOOOO");
    }

    public ActivityRecognitionService(String name) {
        super(name);
        System.out.print("YO");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleResult( result.getMostProbableActivity() );
        }
    }

    private void handleResult(DetectedActivity activity) {
        Intent intent=new Intent();
        intent.setAction("action");
        if(activity.getType() == DetectedActivity.STILL){
            System.out.print("Still "  + activity.getConfidence());
            intent.putExtra("motion", "STILL");
        }else{
            System.out.print("Still "  + activity.getConfidence());
            intent.putExtra("motion", "other");
        }
        sendBroadcast(intent);
    }
}
