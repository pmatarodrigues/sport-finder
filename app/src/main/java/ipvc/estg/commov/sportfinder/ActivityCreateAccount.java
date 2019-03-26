package ipvc.estg.commov.sportfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityCreateAccount extends AppCompatActivity{
    EditText et_username,et_email,et_password, et_confirmarPassword;
    Button btn_criarConta;
    TextView txt_Warning;

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
        //btn_criarConta=(Button)findViewById(R.id.btn_criarConta);
        //TextView
        txt_Warning=(TextView)findViewById(R.id.txtWarnings);

        /*btn_criarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(et_username.getText().toString().isEmpty()){
                txt_Warning.setText(R.string.warningUsernameEmpty);
                //falta verificar se já existe ou nao
                return;
            }else if(et_email.getText().toString().isEmpty()){
                txt_Warning.setText(R.string.warningEmailEmpty);
                //falta verificar se o email já existe
                return;
            }else if(et_password.getText().toString().isEmpty()){
                txt_Warning.setText(R.string.warningPasswordEmpty);
                //falta verificar se o email já existe
                return;
            }
            }
        });*/
    }
}
