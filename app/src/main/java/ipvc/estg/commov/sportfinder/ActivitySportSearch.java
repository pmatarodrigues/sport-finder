package ipvc.estg.commov.sportfinder;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import ipvc.estg.commov.sportfinder.adapter.cursorAdapterDesportos;


public class ActivitySportSearch extends AppCompatActivity {
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
        setContentView(R.layout.activity_sport_search);


        btnContinuar = (Button)findViewById(R.id.button_continuar);
        et_pesquisarDespostos=(EditText)findViewById(R.id.et_pesquisarDesportos);

        lt_listaDesporto=(ListView)findViewById(R.id.lv_listaDesporto);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySportSearch.this, ActivityAddPlaceMap.class);
                ActivitySportSearch.this.startActivity(intent);
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

        lt_listaDesporto.setClickable(true);
        lt_listaDesporto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lt_listaDesporto.getChildAt(i).setBackgroundColor(Color.RED);
                String aux=lt_listaDesporto.getChildAt(i).findViewById(R.id.tv_Desporto).toString();
                Log.d("TAG","mata123478"+ aux);

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
        cursorAdapterDesportos= new cursorAdapterDesportos(ActivitySportSearch.this,matrixCursor);
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
        cursorAdapterDesportos= new cursorAdapterDesportos(ActivitySportSearch.this,matrixCursor);
        lt_listaDesporto.setAdapter(cursorAdapterDesportos);
    }
}
