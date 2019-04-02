package ipvc.estg.commov.sportfinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ipvc.estg.commov.sportfinder.NetworkChangeReceiver;

public class ActivityMainMenu extends AppCompatActivity {

    Button buttonAdd;
    Button buttonProfile;
    Button buttonSearch;

    private BroadcastReceiver mNetworkReceiver;

    ClassNoInternet classNoInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mNetworkReceiver = new NetworkChangeReceiver();

        classNoInternet = new ClassNoInternet(mNetworkReceiver);
        registerNetworkBroadcastForNougat();

        buttonAdd = (Button) findViewById(R.id.button_add_main_menu);
        buttonProfile = (Button) findViewById(R.id.button_profile_main_menu);
        buttonSearch = (Button) findViewById(R.id.button_search_main_menu);

        //button_search_main_menu
        setupListeners();
    }

    public void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    public void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        classNoInternet.unregisterNetworkChanges();
    }



    private void setupListeners() {
        this.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMainMenu.this, ActivitySportSearch.class);
                intent.putExtra("GoTo", "search");
                ActivityMainMenu.this.startActivity(intent);
            }
        });
        this.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMainMenu.this, ActivitySportSearch.class);
                intent.putExtra("GoTo", "add");
                ActivityMainMenu.this.startActivity(intent);
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMainMenu.this, ActivityParqueDetails.class);
                ActivityMainMenu.this.startActivity(intent);
            }
        });
    }
}
