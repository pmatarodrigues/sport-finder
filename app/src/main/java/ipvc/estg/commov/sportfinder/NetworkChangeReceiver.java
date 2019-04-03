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
import android.widget.Toast;

import static ipvc.estg.commov.sportfinder.ClassNoInternet.dialog;


public class NetworkChangeReceiver extends BroadcastReceiver {

    ViewDialog alert;


    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            if (isOnline(context)) {
                Log.i("TAG", "ENTROU ONLINE");
                Toast.makeText(context, "Ligado à internet!", Toast.LENGTH_LONG).show();
                //dialog(true);
            } else {
                Log.i("TAG", "ENTROU OFF");

                Activity activity = (Activity) context;
                //MainActivity activity = new MainActivity();

                alert = new ViewDialog(activity);
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