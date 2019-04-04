package ipvc.estg.commov.sportfinder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ipvc.estg.commov.sportfinder.Classes.Desporto;
import ipvc.estg.commov.sportfinder.Classes.Local;
import ipvc.estg.commov.sportfinder.Classes.Localidade;
import ipvc.estg.commov.sportfinder.Classes.MySingleton;
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
    //
    private String PROVIDER = LocationManager.GPS_PROVIDER;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    LatLng clickedLocation;
    LocationManager mLocationManager;
    Location location;

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
        //
        getcurrentlocation();
        //TODO
        //adicionarObjetosLocalidade();

        callWebServices();
        //preencherListaLocais();
        setupListener();
    }

    private void callWebServices() {
        String url="http://ec2-18-223-143-185.us-east-2.compute.amazonaws.com/slim/index.php/api/getlocais2" ;

        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();
        for (String id:this.listIdEscolhidos) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put("id", id);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("lat", this.location.getLatitude());
            jsonObject1.put("lng", this.location.getLongitude());
            jsonObject1.put("sports", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(1 == 1){
            Toast.makeText(this, "##: "+jsonObject1.toString(),Toast.LENGTH_LONG).show();
        }
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonObject1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("STATUS")) {
                                JSONArray data = response.getJSONArray("DATA");
                                for (int i=0; i < data.length();i++){
                                    JSONObject aux = data.getJSONObject(i);
                                    Localidade aux2 = new Localidade();
                                    if(1==1){
                                        Toast.makeText(ActivitySpotsFound.this, "ID::"+aux.getString("id"),Toast.LENGTH_LONG).show();
                                        //return;
                                    }
                                    aux2.set_ID(Integer.parseInt(aux.getString("id")));
                                    aux2.setNome(aux.getString("nome"));
                                    Double d = Double.parseDouble(aux.getString("distancia"));
                                    aux2.setDistanciaAtual(d.intValue());
                                    aux2.setAvaliacao(aux.getString("avalicao"));
                                    aux2.addUrl("empty.png");
                                    locais.add(aux2);
                                }
                                preencherListaLocais();
                            } else {
                                Toast.makeText(ActivitySpotsFound.this,  getResources().getString(R.string.spotsFound_noSpot), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivitySpotsFound.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };
        MySingleton.getIntance(this).addToRequestQueue(postRequest);
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
        Intent intent = new Intent(ActivitySpotsFound.this, ActivityParqueDetails.class);
        //TODO -> Colocar id que foi clicado
        intent.putExtra("_ID", 27);
        intent.putExtra("nome", "Largo dos Patos");
        intent.putExtra("raio", "500");
        intent.putExtra("descricao", "Excelente local para a prática de basquetebol!");
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
                    .add("distancia", tmp.getDistanciaAtual())
                    .add("url_imagem", tmp.getUrlFotos().get(0));
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

    //TODO -> Get current location
    private void getcurrentlocation() {
        // Zoom into users location
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        this.location = getLastKnownLocation();
        //Toast.makeText(ActivitySpotsFound.this, "# Localização atual: "+location.getLatitude()+" # Longitude: "+location.getLongitude(),Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (location == null) {
                locationManager.requestLocationUpdates(PROVIDER, 1000, 0, new android.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        //centerMapOnLocation(location, "YOur Location");
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
                location = locationManager.getLastKnownLocation(PROVIDER);
                //centerMapOnLocation(location, "Your Location");

            } else {
                //centerMapOnLocation(location, "Your Location");
            }

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }
    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)ActivitySpotsFound.this.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            for (String provider : providers) {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l;
                }
            }
        } else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }
        return bestLocation;
    }

    //TODO -> Delete those methods
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

}
