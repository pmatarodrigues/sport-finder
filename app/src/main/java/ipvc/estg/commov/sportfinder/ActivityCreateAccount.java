package ipvc.estg.commov.sportfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityCreateAccount extends AppCompatActivity{
    EditText et_email,et_password, et_confirmarPassword;
    Button btn_criarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //incialização de variaveis
        //EditText
        et_email=(EditText)findViewById(R.id.et_email);
        et_password=(EditText)findViewById(R.id.et_password);
        et_confirmarPassword=(EditText)findViewById(R.id.et_confirmarPassword);

        //Botões
        btn_criarConta=(Button)findViewById(R.id.btn_criarConta);

        btn_criarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ActivityCreateAccount.this, "Criar Conta", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
