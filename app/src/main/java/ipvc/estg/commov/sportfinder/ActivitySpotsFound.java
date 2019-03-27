package ipvc.estg.commov.sportfinder;

import android.database.MatrixCursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import ipvc.estg.commov.sportfinder.adapter.cursorAdapterSpotsFound;

public class ActivitySpotsFound extends AppCompatActivity {
    private EditText et_pesquisarCidades;
    private ListView lv_SpotsFound;
    private MatrixCursor matrixCursor;
    private cursorAdapterSpotsFound cursorAdapterLocaisEncontrados;
    private String[] Cidades = {"Porto", "Braga", "Viana do Castelo", "Lisboa", "Guimarães", "Famalicão"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spots_found);
        //
        lv_SpotsFound=(ListView)findViewById(R.id.spot_found_listView);
        et_pesquisarCidades = (EditText) findViewById(R.id.pesquisarCidade_spots_founded);
        //
        preencherListaLocais();
        setupListener();
    }

    private void setupListener() {
        //Listener for the search bar
        et_pesquisarCidades.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TAG","pedro1234"+charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //o adapter tem de ser neste metodo
                if(charSequence.length()>=3){
                    filtrarCidades(charSequence.toString());
                }else{
                    preencherListaLocais();
                }
                Log.d("TAG", "pedro1235"+charSequence+" "+charSequence.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("TAG","pedro1236");
            }
        });

        lv_SpotsFound.setClickable(true);
        lv_SpotsFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lv_SpotsFound.getChildAt(i).setBackgroundColor(Color.RED);
                TextView auxView = (TextView) view.findViewById(R.id.tv_Desporto);
                String aux = auxView.getText().toString();

                Log.d("TAG","mata123478 "+ aux);
            }
        });

    }

    //
    private void criarCursor(){
        matrixCursor= new MatrixCursor(new String[] {"_ID","nome_local", "avalicao","distancia"});
        for(int i=0;i<10;i++){
            matrixCursor.newRow()
                    .add("_ID",i)
                    .add("nome_local","Local " + i)
                    .add("avalicao", "5")
                    .add("distancia", (i*50)+"");
        }
    }

    private void preencherListaLocais() {
        this.criarCursor();
        this.cursorAdapterLocaisEncontrados = new cursorAdapterSpotsFound( ActivitySpotsFound.this,matrixCursor);
        this.lv_SpotsFound.setAdapter(cursorAdapterLocaisEncontrados);
    }

    private void filtrarCidades(String filtro){
        return;
        //TODO -> Change if statement for the spots arrayList
        /*
        matrixCursor= new MatrixCursor(new String[] {"_ID","nome"});
        for(int i=0;i<10;i++){
            //Change if statement for the spots arrayList
            if(Cidades[i].toLowerCase().contains(filtro.toLowerCase())) {
                matrixCursor.newRow()
                        .add("_ID",i)
                        .add("nome_local","Local " + i)
                        .add("avalicao", "5")
                        .add("distancia", (i*50)+"");
            }
        }
        this.cursorAdapterLocaisEncontrados= new cursorAdapterSpotsFound(ActivitySpotsFound.this,matrixCursor);
        lv_SpotsFound.setAdapter(this.cursorAdapterLocaisEncontrados);
        */
    }

}
