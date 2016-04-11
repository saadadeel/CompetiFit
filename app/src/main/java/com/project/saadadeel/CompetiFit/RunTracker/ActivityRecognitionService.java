package com.project.saadadeel.CompetiFit.RunTracker;

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
            handleDetectedActivities( result.getMostProbableActivity() );
                // Get the update
//                ActivityRecognitionResult result =
//                        ActivityRecognitionResult.extractResult(intent);
//
//                DetectedActivity mostProbableActivity
//                        = result.getMostProbableActivity();
//
//                // Get the confidence % (probability)
//                int confidence = mostProbableActivity.getConfidence();
//
//                // Get the type
//                int activityType = mostProbableActivity.getType();
//           /* types:
//            * DetectedActivity.IN_VEHICLE
//            * DetectedActivity.ON_BICYCLE
//            * DetectedActivity.ON_FOOT
//            * DetectedActivity.STILL
//            * DetectedActivity.UNKNOWN
//            * DetectedActivity.TILTING
//            */
//                // process
//            }
        }
    }

    private void handleDetectedActivities(DetectedActivity activity) {
        Intent intent=new Intent();
        intent.setAction("action");
            switch( activity.getType() ) {
                case DetectedActivity.IN_VEHICLE: {
                    System.out.print("Vehicle "  + activity.getConfidence());
                    intent.putExtra("motion", "IN_VEHICLE");
//                    Log.e("ActivityRecogition", "In Vehicle: " + activity.getConfidence());
                    break;
                }
                case DetectedActivity.ON_BICYCLE: {
                    System.out.print("Bike " + activity.getConfidence());
                    intent.putExtra("motion", "ON_BICYCLE");
//                    Log.e( "ActivityRecogition", "On Bicycle: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.ON_FOOT: {
                    System.out.print("Foot " + activity.getConfidence());
                    intent.putExtra("motion", "ON_FOOT");
//                    Log.e( "ActivityRecogition", "On Foot: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.RUNNING: {
                    System.out.print("Running "  + activity.getConfidence());
                    intent.putExtra("motion", "RUNNING");
//                    Log.e( "ActivityRecogition", "Running: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.STILL: {
                    System.out.print("Still "  + activity.getConfidence());
                    intent.putExtra("motion", "STILL");
//                    Log.e( "ActivityRecogition", "Still: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.TILTING: {
                    System.out.print("Tilting "  + activity.getConfidence());
                    intent.putExtra("motion", "TILTING");
//                    Log.e( "ActivityRecogition", "Tilting: " + activity.getConfidence() );
                    break;
                }
                case DetectedActivity.WALKING: {
                    System.out.print("Walking "  + activity.getConfidence());
                    intent.putExtra("motion", "WALKING");
//                    Log.e( "ActivityRecogition", "Walking: " + activity.getConfidence() );
//                    if( activity.getConfidence() >= 75 ) {
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//                        builder.setContentText( "Are you walking?" );
//                        builder.setSmallIcon( R.mipmap.ic_launcher );
//                        builder.setContentTitle( getString( R.string.app_name ) );
//                        NotificationManagerCompat.from(this).notify(0, builder.build());
//                    }
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    System.out.print("Unknow"  + activity.getConfidence());
                    intent.putExtra("motion", "unknown");
//                    Log.e( "ActivityRecogition", "Unknown: " + activity.getConfidence() );
                    break;
                }
        }
        sendBroadcast(intent);
    }
}
