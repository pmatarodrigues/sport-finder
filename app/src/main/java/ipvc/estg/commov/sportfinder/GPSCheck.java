package ipvc.estg.commov.sportfinder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

public class GPSCheck extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

        }
        else{
            Toast.makeText(context, "Please switch on the GPS", Toast.LENGTH_LONG).show();
        }
    }
}