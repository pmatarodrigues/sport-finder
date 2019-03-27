package ipvc.estg.commov.sportfinder;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
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

import ipvc.estg.commov.sportfinder.Classes.MySingleton;
import ipvc.estg.commov.sportfinder.adapter.cursorAdapterDesportos;


public class ActivitySportSearch extends AppCompatActivity {
    private EditText et_pesquisarDespostos;
    private Button btnContinuar;
    private String[] Desportos = {"Futebol", "Tenis", "Golf", "Andebol"};
    private Cursor cursorDesportos;
    private MatrixCursor matrixCursor;
    private cursorAdapterDesportos cursorAdapterDesportos;
    private ListView lv_listaDesporto;

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

        lv_listaDesporto.setClickable(true);
        lv_listaDesporto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lv_listaDesporto.getChildAt(i).setBackgroundColor(Color.RED);
                TextView auxView = (TextView) view.findViewById(R.id.tv_Desporto);
                String aux = auxView.getText().toString();

                Log.d("TAG","mata123478 "+ aux);
            }
        });

    }

    private void getListaDesportos(View v){
        String url="http://sportfinderapi.000webhostapp.com/slim/api/getDesportos";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       try {
                           JSONArray arr = response.getJSONArray("DATA");
                       } catch (JSONException e) {
                           e.printStackTrace();
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
        Log.d("TAG","pedro123456"+Desportos.length+Desportos[0]);
        for(int i=0;i<Desportos.length;i++){
           matrixCursor.newRow()
                   .add("_ID",i)
                   .add("nome",Desportos[i]);
        }
    }

    private void preencherListaDesporto(){
        criarCursor();
        cursorAdapterDesportos= new cursorAdapterDesportos( ActivitySportSearch.this,matrixCursor);
        lv_listaDesporto.setAdapter(cursorAdapterDesportos);

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
        lv_listaDesporto.setAdapter(cursorAdapterDesportos);
    }
}
