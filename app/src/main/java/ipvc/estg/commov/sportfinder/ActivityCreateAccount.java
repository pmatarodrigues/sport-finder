package ipvc.estg.commov.sportfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityCreateAccount extends AppCompatActivity{
    private EditText et_username,et_email,et_password, et_confirmarPassword;
    private Button btn_criarConta;
    private TextView txt_Warning;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //incialização de variaveis
        //EditText
        et_email=(EditText)findViewById(R.id.et_email);
        et_password=(EditText)findViewById(R.id.et_password);
        et_confirmarPassword=(EditText)findViewById(R.id.et_confirmarPassword);
        et_username=(EditText) findViewById(R.id.et_username);
        //Button
        btn_criarConta=(Button)findViewById(R.id.btn_criarConta);
        //TextView
        txt_Warning=(TextView)findViewById(R.id.txtWarnings);
        //Firebase
        mAuth=FirebaseAuth.getInstance();
        //processdialog
        progressDialog = new ProgressDialog(this);


        btn_criarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(et_username.getText().toString().isEmpty()){
                txt_Warning.setText(R.string.warningUsernameEmpty);
                //falta verificar se já existe ou nao
                et_password.setText("");
                et_confirmarPassword.setText("");
            }else if(et_email.getText().toString().isEmpty()){
                txt_Warning.setText(R.string.warningEmailEmpty);
                //falta verificar se o email já existe
                et_password.setText("");
                et_confirmarPassword.setText("");
            }else if(!isValidEmailAddress(et_email.getText().toString().trim())){
                txt_Warning.setText(R.string.warningEmailInvalid);
                et_password.setText("");
                et_confirmarPassword.setText("");
            }else if(et_password.getText().toString().isEmpty()){
                txt_Warning.setText(R.string.warningPasswordEmpty);
            }else if (et_confirmarPassword.getText().toString().isEmpty()){
                txt_Warning.setText(R.string.warningConfirmPasswordEmpty);
            }else if(verificarPassword(et_password.getText().toString())==false){
                txt_Warning.setText(R.string.wartningPasswordRestriction);
                et_password.setText("");
                et_confirmarPassword.setText("");
            }else if(!et_password.getText().toString().equals(et_confirmarPassword.getText().toString())){
                txt_Warning.setText(R.string.wartningPasswordNotMatch);
                et_password.setText("");
                et_confirmarPassword.setText("");
            }else{
                registarUser(et_email.getText().toString().trim(),et_password.getText().toString());
                //Toast.makeText(ActivityCreateAccount.this, "Correu tudo bem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean verificarPassword(String password){
        boolean maiuscula=false, minuscula=false, numero=false,size=false;
        char ch;
        if(password.length()>=8){
            size=true;
        }

        for(int i=0;i<password.length();i++){
            ch=password.charAt(i);

            if(Character.isDigit(ch)){
                numero=true;
            }else if(Character.isUpperCase(ch)){
                maiuscula=true;
            }else if(Character.isLowerCase(ch)){
                minuscula=true;
            }
        }

        if(maiuscula&&minuscula&&numero&&size){
            return true;
        }else{
            return false;
        }
    }

    private boolean isValidEmailAddress(String email) {
        boolean verifyEmail=false;
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            verifyEmail=true;
        }
        return  verifyEmail;
    }

    private void registarUser(String email, String password){

        progressDialog.setMessage("A registar o utilizador....");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(ActivityCreateAccount.this, "Registado Com Sucesso", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            Log.d("TAG","pedro123"+task.getException());
                            Toast.makeText(ActivityCreateAccount.this, "Erro ao Registar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
