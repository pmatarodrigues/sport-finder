package ipvc.estg.commov.sportfinder;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ClassNoInternet extends AppCompatActivity{


    private static LinearLayout tv_check_connection;
    private static RelativeLayout rl_content;
    private BroadcastReceiver mNetworkReceiver;

    public ClassNoInternet(BroadcastReceiver mNetworkReceiver) {
        this.mNetworkReceiver = mNetworkReceiver;
    }

    public static void dialog(boolean value){

        if(value){
            tv_check_connection.setVisibility(View.GONE);
            rl_content.setVisibility(View.VISIBLE);
        }else {
            tv_check_connection.setVisibility(View.VISIBLE);
            rl_content.setVisibility(View.GONE);
        }
    }


    public void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }



}
