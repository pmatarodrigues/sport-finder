package ipvc.estg.commov.sportfinder;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import static ipvc.estg.commov.sportfinder.ClassNoInternet.dialog;


public class NetworkChangeReceiver extends BroadcastReceiver {

    ViewDialog alert;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            if (isOnline(context)) {
                Activity activity = (Activity) context;

                //ViewDialog alert = new ViewDialog(context);
                alert.dismiss();
                //dialog(true);
            } else {
                Activity activity = (Activity) context;

                alert = new ViewDialog(context);
                alert.showDialog(activity, "Erro de ligação à internet!");
                //dialog(false);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}