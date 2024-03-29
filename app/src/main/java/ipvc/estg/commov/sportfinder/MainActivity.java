package ipvc.estg.commov.sportfinder;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;

    // NEEDED TO CHECK FOR NETWORK
    private BroadcastReceiver mNetworkReceiver;
    ClassNoInternet classNoInternet;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private TextView tv_warningLogin;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        // NEEDED TO CHECK FOR NETWORK
        mNetworkReceiver = new NetworkChangeReceiver();
        classNoInternet = new ClassNoInternet(mNetworkReceiver);
        registerNetworkBroadcastForNougat();

        editTextEmail = (EditText) findViewById(R.id.login_email);
        editTextPassword = (EditText) findViewById(R.id.login_password);
        tv_warningLogin=(TextView)findViewById(R.id.txtLoginWarning);

        buttonLogin = (Button)findViewById(R.id.button_login);

        final LinearLayout emailLayout = (LinearLayout) findViewById(R.id.emailLinearLayout);
        final LinearLayout passwordLayout = (LinearLayout) findViewById(R.id.passwordLinearLayout);
        emailLayout.setBackgroundColor(Color.TRANSPARENT);
        passwordLayout.setBackgroundColor(Color.TRANSPARENT);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextEmail.getText().toString().trim().isEmpty()){
                    tv_warningLogin.setText(R.string.warningEmailEmpty);
                    emailLayout.setBackgroundColor(Color.parseColor("#ff0000"));
                    if(editTextEmail.requestFocus()){
                        showKeyboard(editTextEmail);
                    }
                } else if(editTextPassword.getText().toString().trim().isEmpty()){
                    tv_warningLogin.setText(R.string.warningPasswordEmpty);
                    passwordLayout.setBackgroundColor(Color.parseColor("#ff0000"));
                    if(editTextPassword.requestFocus()){
                        showKeyboard(editTextPassword);
                    }
                } else{
                    authMailPass(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim());
                }
            }
        });


        setupListeners();

        progressDialog= new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            finish();
            Intent intent = new Intent(MainActivity.this, ActivityMainMenu.class);
            MainActivity.this.startActivity(intent);
        }
        //executeBackground();
    }

    private void setupListeners() {

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

    //
    private void showKeyboard(View view){
        InputMethodManager imm = (InputMethodManager)
                getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
    //TextViews brains
    private void txtViewCriarContaClicked(View v){
        //What happens when you click Criar Conta??
        //Define here what happens
        Toast.makeText(MainActivity.this, getResources().getString(R.string.criar_conta) +" Clicked", Toast.LENGTH_SHORT).show();//Comment this line for the end product
        Intent intent = new Intent(MainActivity.this, ActivityCreateAccount.class);
        MainActivity.this.startActivity(intent);

    }
    private void txtViewEsquecimeClicked(View v){
        //What happens when you click Esqueci-me??
        //Define here what happens
        Toast.makeText(MainActivity.this, getResources().getString(R.string.esqueci_a_passe)+" Clicked", Toast.LENGTH_SHORT).show();//Comment this line for the end product
    }

    private void authMailPass(String email,String pass){
        //mudar para string
        progressDialog.setMessage("A processar...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            Intent intent = new Intent(MainActivity.this, ActivityMainMenu.class);
                            MainActivity.this.startActivity(intent);
                        }else{
                            tv_warningLogin.setText(R.string.warningcredentialsInvalid);
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void executeBackground(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                teste();
                handler.postDelayed(this, 1000);
            }
        };

        //Start
        handler.postDelayed(runnable, 1000);
    }

    private void teste(){
        Log.d("TAG","pedro123 estou a correr em backgroung");
        //LocationManager manager= new LocationManager();
    }

    // NEEDED TO CHECK FOR NETWORK
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
    }

}
