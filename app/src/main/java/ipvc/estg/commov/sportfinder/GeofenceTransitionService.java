package ipvc.estg.commov.sportfinder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

public class GeofenceTransitionService extends IntentService {

    private static final String TAG = GeofenceTransitionService.class.getSimpleName();
    public static final int GEOFENCE_NOTIFICATION_ID = 0;

    private Date TIME_ENTERED;
    private Date TIME_LEFT;
    private Context rContext;

    public GeofenceTransitionService() {
        super(TAG);
    }

    public GeofenceTransitionService(Context rContext) {
        super(TAG);
        this.rContext = rContext;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve the Geofencing intent
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        // Handling errors
        if ( geofencingEvent.hasError() ) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode() );
            Log.e( TAG, errorMsg );
            return;
        }

        // Retrieve GeofenceTrasition
        int geoFenceTransition = geofencingEvent.getGeofenceTransition();

        // Check if the transition type
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ) {
            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            // Create a detail message with Geofences received
            String geofenceTransitionDetails = getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences );
            // Send notification details as a String
            sendNotification( geofenceTransitionDetails );
        }
    }

    // Create a detail message with Geofences received
    public String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {

        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for ( Geofence geofence : triggeringGeofences ) {
            triggeringGeofencesList.add( geofence.getRequestId() );
        }

        String status = null;
        if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ) {
            //Log.d(TAG, "calcTimeInsideGeofence: " + TIME_ENTERED);
            Toast.makeText(this, "Mostreiantes", Toast.LENGTH_SHORT).show();
            TIME_ENTERED = new Date(System.currentTimeMillis());
            Toast.makeText(this, "Mostrei"+TIME_ENTERED, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "calcTimeInsideGeofence: " + TIME_ENTERED);
            status = "A entrar em ";
        }
        else if ( geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ) {
            //Log.d(TAG, "calcTimeInsideGeofence: " + TIME_LEFT);
            TIME_LEFT = new Date(System.currentTimeMillis());
           // TIME_ENTERED =  new Date(System.currentTimeMillis());
            Log.d(TAG, "calcTimeInsideGeofence: " + TIME_LEFT);
            try {
                calcTimeInsideGeofence(TIME_ENTERED, TIME_LEFT);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            status = "A sair de ";
        }
        return status + TextUtils.join( ", ", triggeringGeofencesList);
    }


    public void calcTimeInsideGeofence(Date timeEntered, Date timeLeft) throws ParseException {
        Log.d("TAG","calcTimeENTREI");
        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        //timeEntered = //df.parse("2011-02-08 10:00:00 +0300");
        //timeLeft = df.parse("2011-02-08 08:00:00 +0100");
        long timeDiff = Math.abs(timeEntered.getTime() - timeLeft.getTime());
        Log.d(TAG, "calcTimeInsideGeofence: " + timeDiff);
        Toast.makeText(rContext, "difference: " + timeDiff, Toast.LENGTH_SHORT).show();
    }


    // Send a notification
    private void sendNotification( String msg ) {
        // Intent to start the main Activity
        Intent notificationIntent = ActivityParqueDetails.makeNotificationIntent(getApplicationContext(), msg);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Creating and sending Notification
        NotificationManager notificatioMng = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificatioMng.notify(GEOFENCE_NOTIFICATION_ID, createNotification(msg, notificationPendingIntent));
    }

    // Create a notification
    private Notification createNotification(String msg, PendingIntent notificationPendingIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_icon_search)
                .setColor(Color.RED)
                .setContentTitle(msg)
                .setContentText("Mapa com Geofence!")
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        return notificationBuilder.build();
    }

    // Handle errors
    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }
}