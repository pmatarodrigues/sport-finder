package ipvc.estg.commov.sportfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        setupListeners();
        this.editTextEmail = (EditText) findViewById(R.id.login_email);
        this.editTextPassword = (EditText) findViewById(R.id.login_password);

        progressDialog= new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
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
        LinearLayout emailLayout = (LinearLayout) findViewById(R.id.emailLinearLayout);
        LinearLayout passwordLayout = (LinearLayout) findViewById(R.id.passwordLinearLayout);
        emailLayout.setBackgroundColor(Color.TRANSPARENT);
        passwordLayout.setBackgroundColor(Color.TRANSPARENT);
        if(this.editTextEmail.getText().toString().trim().isEmpty()){
            emailLayout.setBackgroundColor(Color.parseColor("#ff0000"));
            if(this.editTextEmail.requestFocus()){
                this.showKeyboard(this.editTextEmail);
            }
        }else if (this.editTextPassword.getText().toString().trim().isEmpty()){
            passwordLayout.setBackgroundColor(Color.parseColor("#ff0000"));
            if(this.editTextPassword.requestFocus()){
                this.showKeyboard(this.editTextPassword);
            }
        }else{
            authMailPass(this.editTextEmail.getText().toString().trim(),this.editTextPassword.getText().toString().trim());
        }
        //Remover linhas seguintes para produto final
        //TODO

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
                            Log.d("TAG","pedro123"+task.getException());
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}
