package ipvc.estg.commov.sportfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityMainMenu extends AppCompatActivity {

    Button buttonAdd;
    Button buttonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        buttonAdd = (Button) findViewById(R.id.button_add_main_menu);
        buttonProfile = (Button)findViewById(R.id.button_profile_main_menu);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMainMenu.this, ActivitySportSearch.class);
                ActivityMainMenu.this.startActivity(intent);
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMainMenu.this, ActivityUserAccount.class);
                ActivityMainMenu.this.startActivity(intent);
            }
        });
    }
}
