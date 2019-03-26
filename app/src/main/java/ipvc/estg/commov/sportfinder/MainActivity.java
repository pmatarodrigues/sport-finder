package ipvc.estg.commov.sportfinder;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        setupListeners();
        this.editTextEmail = (EditText) findViewById(R.id.login_email);
        this.editTextPassword = (EditText) findViewById(R.id.login_password);
    }

    private void setupListeners() {
        //Button login listener
        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainMenu();
            }
        });
        //Set clickListener for the textView criar_conta
        TextView txtViewCriarConta = (TextView) findViewById(R.id.criar_conta);
        txtViewCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtViewCriarContaClicked(v);
            }
        });
        //Set clickListener for the textView criar_conta
        TextView txtViewEsquecime = (TextView) findViewById(R.id.esqueci_me_);
        txtViewEsquecime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtViewEsquecimeClicked(v);
            }
        });
    }
    protected void openMainMenu(){
        //Run login checkups
        if(this.editTextEmail.getText().toString().trim().isEmpty()){
            //this.editTextEmail.get().setColorFilter(Color.parseColor("#ff0000"), PorterDuff.Mode.SRC_IN);
        }
        Intent intent = new Intent(MainActivity.this, ActivityMainMenu.class);
        MainActivity.this.startActivity(intent);
    }

    //TextViews brains
    private void txtViewCriarContaClicked(View v){
        //What happens when you click Criar Conta??
        //Define here what happens
        Toast.makeText(MainActivity.this, getResources().getString(R.string.criar_conta) +" Clicked", Toast.LENGTH_SHORT).show();

    }
    private void txtViewEsquecimeClicked(View v){
        //What happens when you click Esqueci-me??
        //Define here what happens
        Toast.makeText(MainActivity.this, getResources().getString(R.string.esqueci_a_passe)+" Clicked", Toast.LENGTH_SHORT).show();
    }
}
