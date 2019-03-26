package ipvc.estg.commov.sportfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityAddPlace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        Button btnContinuar = (Button)findViewById(R.id.button_continuar);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAddPlace.this, ActivityAddPlaceMap.class);
                ActivityAddPlace.this.startActivity(intent);
            }
        });

    }
}
