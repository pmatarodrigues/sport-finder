package ipvc.estg.commov.sportfinder;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import ipvc.estg.commov.sportfinder.adapter.cursorAdapterDesportos;


public class ActivityAddPlace extends AppCompatActivity {
    private EditText et_pesquisarDespostos;
    private Button btnContinuar;
    private String[] Desportos = {"Futebol", "Tenis", "Golf", "Andebol"};
    private Cursor cursorDesportos;
    private MatrixCursor matrixCursor;
    private cursorAdapterDesportos cursorAdapterDesportos;
    private ListView lt_listaDesporto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);


        btnContinuar = (Button)findViewById(R.id.button_continuar);
        et_pesquisarDespostos=(EditText)findViewById(R.id.et_pesquisarDesportos);

        lt_listaDesporto=(ListView)findViewById(R.id.lv_listaDesporto);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAddPlace.this, ActivityAddPlaceMap.class);
                ActivityAddPlace.this.startActivity(intent);
            }
        });
        preencherListaDesporto();
        et_pesquisarDespostos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TAG","pedro1234"+charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //o adapter tem de ser neste metodo
                if(charSequence.length()>=3){
                    filtrarDesportos(charSequence.toString());
                }else{
                    preencherListaDesporto();
                }
                Log.d("TAG", "pedro1235"+charSequence+" "+charSequence.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("TAG","pedro1236");
            }
        });
    }

    private void criarCursor(){
        matrixCursor= new MatrixCursor(new String[] {"_ID","nome"});
        Log.d("TAG","pedro123456"+Desportos.length+Desportos[0]);
        for(int i=0;i<Desportos.length;i++){
           matrixCursor.newRow()
                   .add("_ID",i)
                   .add("nome",Desportos[i]);
        }
    }

    private void preencherListaDesporto(){
        criarCursor();
        cursorAdapterDesportos= new cursorAdapterDesportos(ActivityAddPlace.this,matrixCursor);
        lt_listaDesporto.setAdapter(cursorAdapterDesportos);
    }

    private void filtrarDesportos(String filtro){
        matrixCursor= new MatrixCursor(new String[] {"_ID","nome"});
        Log.d("TAG","pedro123456"+Desportos.length+Desportos[0]);
        for(int i=0;i<Desportos.length;i++){
            if(Desportos[i].toLowerCase().contains(filtro.toLowerCase())) {
                matrixCursor.newRow()
                        .add("_ID", i)
                        .add("nome", Desportos[i]);
            }
        }
        cursorAdapterDesportos= new cursorAdapterDesportos(ActivityAddPlace.this,matrixCursor);
        lt_listaDesporto.setAdapter(cursorAdapterDesportos);
    }
}
