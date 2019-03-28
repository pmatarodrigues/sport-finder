package ipvc.estg.commov.sportfinder;

import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Color;

import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ipvc.estg.commov.sportfinder.Classes.Desporto;

import ipvc.estg.commov.sportfinder.Classes.MySingleton;
import ipvc.estg.commov.sportfinder.adapter.cursorAdapterDesportos;

public class ActivitySportSearch extends AppCompatActivity{
    private EditText et_pesquisarDespostos;
    private Button btnContinuar;
   // private String[] Desportos = {"Futebol", "Tenis", "Golf", "Andebol"};
   // private Cursor cursorDesportos;
    private MatrixCursor matrixCursor;
    private cursorAdapterDesportos cursorAdapterDesportos;
    private ListView lv_listaDesporto;
    private List<Desporto> listDesportos;
    private List<String> listDesportosEscolhidos;

    //
    private String whereToGo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_search);

        //
        whereToGo = getIntent().getStringExtra("GoTo");
        btnContinuar = (Button)findViewById(R.id.button_continuar);
        et_pesquisarDespostos=(EditText)findViewById(R.id.et_pesquisarDesportos);

        lv_listaDesporto=(ListView)findViewById(R.id.lv_listaDesporto);

        listDesportos= new ArrayList<>();
        listDesportosEscolhidos= new ArrayList<>();

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whereToGo.equals("search")){
                    Intent intent = new Intent(ActivitySportSearch.this, ActivitySpotsFound.class);
                    ActivitySportSearch.this.startActivity(intent);
                }else if (whereToGo.equals("add")){
                    Intent intent = new Intent(ActivitySportSearch.this, ActivityAddPlaceMap.class);
                    ActivitySportSearch.this.startActivity(intent);
                }else{
                    Toast.makeText(ActivitySportSearch.this, "Go Back and Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ;
        getListaDesportos();

        et_pesquisarDespostos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //o adapter tem de ser neste metodo
                if(charSequence.length()>=3){
                    filtrarDesportos(charSequence.toString());
                }else{
                    preencherListaDesporto();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        lv_listaDesporto.setClickable(true);
        lv_listaDesporto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView auxView = (TextView) view.findViewById(R.id.tv_Desporto);

                String nomeDesportoSlect = auxView.getText().toString();

                Log.d("TAG","NOME DO DESPORTO: "+nomeDesportoSlect + "index: "+i);
                listDesportosEscolhidos.add(nomeDesportoSlect);
                //lv_listaDesporto.getChildAt(i).setBackgroundColor(Color.RED);
                view.setBackgroundColor(R.drawable.background_list_item_selected);
            }
        });

    }

    private void getListaDesportos(){
        String url="http://sportfinderapi.000webhostapp.com/slim/api/getDesportos";
        Desporto desporto= new Desporto();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       try {
                           Desporto desporto;
                           JSONArray arr = response.getJSONArray("DATA");
                           for(int i=0;i<arr.length();i++){
                               desporto= new Desporto();
                               JSONObject obj=arr.optJSONObject(i);
                               desporto.setId(obj.getString("id"));
                               desporto.setNome(obj.getString("nome"));
                               listDesportos.add(desporto);
                           };
                           preencherListaDesporto();
                       }catch (JSONException ex){

                       }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     Log.d("TAG","pedro1234"+ error.getMessage());
                    }
        });
        MySingleton.getIntance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void criarCursor(){
        matrixCursor= new MatrixCursor(new String[] {"_ID","nome"});
        for(int i=0;i<listDesportos.size();i++){
           matrixCursor.newRow()
                   .add("_ID",listDesportos.get(i).getId())
                   .add("nome",listDesportos.get(i).getNome());
        }
    }

    private void preencherListaDesporto(){
        criarCursor();
        cursorAdapterDesportos= new cursorAdapterDesportos( ActivitySportSearch.this,matrixCursor, listDesportosEscolhidos);

        lv_listaDesporto.setAdapter(cursorAdapterDesportos);

    }

    private void filtrarDesportos(String filtro){
        matrixCursor= new MatrixCursor(new String[] {"_ID","nome"});
        for(int i=0;i<listDesportos.size();i++){
            if(listDesportos.get(i).getNome().toLowerCase().contains(filtro.toLowerCase())) {
                matrixCursor.newRow()
                        .add("_ID", i)
                        .add("nome", listDesportos.get(i).getNome());
            }
        }
        cursorAdapterDesportos= new cursorAdapterDesportos(ActivitySportSearch.this,matrixCursor,listDesportosEscolhidos);
        lv_listaDesporto.setAdapter(cursorAdapterDesportos);
    }

}
