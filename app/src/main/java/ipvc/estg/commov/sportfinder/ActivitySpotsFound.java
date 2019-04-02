package ipvc.estg.commov.sportfinder;

import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ipvc.estg.commov.sportfinder.Classes.Desporto;
import ipvc.estg.commov.sportfinder.Classes.Localidade;
import ipvc.estg.commov.sportfinder.adapter.cursorAdapterSpotsFound;

public class ActivitySpotsFound extends AppCompatActivity {
    //
    ArrayList<Localidade> locais;
    //
    ArrayList<String> listIdEscolhidos;//lista que contem os ids dos desportos escolhidos
    //
    private EditText et_pesquisarCidades;
    private ListView lv_SpotsFound;
    private MatrixCursor matrixCursor;
    private cursorAdapterSpotsFound cursorAdapterLocaisEncontrados;
    private String[] Cidades = {"Porto", "Braga", "Viana do Castelo", "Lisboa", "Guimarães", "Famalicão"};
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spots_found);
        //
        lv_SpotsFound=(ListView)findViewById(R.id.spot_found_listView);
        et_pesquisarCidades = (EditText) findViewById(R.id.pesquisarCidade_spots_founded);
        //
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> tempList = (ArrayList<String>) bundle.getStringArrayList("selectedSports");
        listIdEscolhidos = tempList;

        this.locais = new ArrayList<>();
        //TODO
        adicionarObjetosLocalidade();

        //callWebServices();
        preencherListaLocais();
        setupListener();
    }

    private void adicionarObjetosLocalidade() {
        //
        Localidade tmp = new Localidade();
        tmp.set_ID(1);
        tmp.setNome("Largo dos Patos");
        tmp.setDescricao("Excelente local para a prática de basquetebol e estar com a familia");
        tmp.setRaio("100");
        tmp.setLat("41.692395");
        tmp.setLng("-8.835195");
        tmp.addDesporto(new Desporto("2", "Futebol"));
        tmp.addDesporto(new Desporto("5", "Basquetebol"));
        tmp.addUrl("largodospatos1.png");
        tmp.addUrl("largodospatos2.png");
        tmp.setAvaliacao("3,9");
        this.locais.add(tmp);
        //
        tmp = null;
        tmp = new Localidade();
        tmp.set_ID(2);
        tmp.setNome("Praia Norte");
        tmp.setDescricao("Excelente local para correr e passear com a familia. Local para crianças brincarem bastante seguro");
        tmp.setRaio("1000");
        tmp.setLat("41.693818");
        tmp.setLng("-8.848807");
        tmp.addDesporto(new Desporto("2", "Futebol"));
        tmp.addDesporto(new Desporto("3", "Futebol de Praia"));
        tmp.addUrl("praiaNorte1.png");
        tmp.addUrl("praiaNorte2.png");
        tmp.setAvaliacao("4");
        this.locais.add(tmp);
        //

    }

    private void callWebServices() {
        String json = new Gson().toJson(listIdEscolhidos);
        JSONArray idDesporto = new JSONArray();
        for (String temp: listIdEscolhidos) {
            idDesporto.put(temp);
        }
        JSONObject idDesportos = new JSONObject();
        try {
            idDesportos.put("ids", idDesporto);
            idDesportos.put("lat", "41.70482865291267");
            idDesportos.put("lon", "-8.843413777649403");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TAG","costa: "+ idDesportos);
        Toast.makeText(ActivitySpotsFound.this, idDesportos.toString(), Toast.LENGTH_LONG).show();
        //

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
                TextView auxView = (TextView) view.findViewById(R.id.txt_spot_name);
                Log.d("COSTAA", auxView.getText().toString());
                for (Localidade tmp: locais) {
                    if(tmp.getNome().equals(auxView.getText().toString().trim())){
                        mostrarLocalDetalhe(tmp);
                        return;
                    }
                }
                lv_SpotsFound.getChildAt(i).setBackgroundColor(Color.RED);
                //TextView auxView = (TextView) view.findViewById(R.id.tv_Desporto);
                //String aux = auxView.getText().toString();

                //Log.d("TAG","mata123478 "+ aux);
            }
        });

    }

    private void mostrarLocalDetalhe(Localidade tmp) {
        Intent intent = new Intent(ActivitySpotsFound.this, ActivityShowSpotDetailed.class);
        intent.putExtra("_ID", tmp.get_ID());
        startActivity(intent);
    }

    //
    private void criarCursor(){
        if(this.matrixCursor != null)
            this.matrixCursor.close();
        this.matrixCursor = null;
        matrixCursor= new MatrixCursor(new String[] {"_ID","nome_local", "avalicao","distancia","url_imagem"});
        for (Localidade tmp: this.locais) {
            matrixCursor.newRow()
                    .add("_ID",tmp.get_ID())
                    .add("nome_local",tmp.getNome())
                    .add("avalicao", tmp.getAvaliacao())
                    .add("distancia", "100m")
                    .add("url_imagem", tmp.getUrlFotos().get(0));
        }

        for(int i=100;i<110;i++){
            matrixCursor.newRow()
                    .add("_ID",i)
                    .add("nome_local","Local " + i)
                    .add("avalicao", "5")
                    .add("distancia", (i*50)+"")
                    .add("url_imagem", "empty.png");
        }

    }

    private void preencherListaLocais() {
        this.criarCursor();
        this.cursorAdapterLocaisEncontrados = new cursorAdapterSpotsFound( ActivitySpotsFound.this, matrixCursor);
        this.lv_SpotsFound.setAdapter(cursorAdapterLocaisEncontrados);
    }

    private void filtrarCidades(String filtro){
        return;
        //TODO ->  Change if statement for the spots arrayList
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
