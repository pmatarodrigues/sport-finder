package ipvc.estg.commov.sportfinder;

import android.Manifest;
import android.Manifest.permission;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ipvc.estg.commov.sportfinder.Classes.MySingleton;


public class ActivityAddPlaceMap extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = ActivityAddPlaceMap.class.getSimpleName();
    private GoogleMap mMap;
    private int STORAGE_PERMISSION_CODE = 23;

    Spinner sp_Raio;
    Button btnConfirmAddPlace;

    EditText edtxt_descricaolocal;
    EditText edtxt_nomedoparque;

    LocationManager locationManager;
    LocationListener locationListener;

    LatLng clickedLocation;
    LocationManager mLocationManager;

    String selectedRaioString;
    String selectedRaio;
    LatLng selectedLocation;
    String selectedPlaceDescription;
    String selectedNomeDoParque;

    ArrayList<String> listIdEscolhidos;

    // NEEDED TO CHECK FOR NETWORK
    private BroadcastReceiver mNetworkReceiver;
    ClassNoInternet classNoInternet;


    private String PROVIDER = LocationManager.GPS_PROVIDER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place_map);

        // NEEDED TO CHECK FOR NETWORK
        mNetworkReceiver = new NetworkChangeReceiver();
        classNoInternet = new ClassNoInternet(mNetworkReceiver);
        registerNetworkBroadcastForNougat();

        Bundle bundle = getIntent().getExtras();
        //ArrayList<String> tempList = bundle.getStringArrayList("selectedSports");
        ArrayList<String> tempList = (ArrayList<String>) bundle.getStringArrayList("selectedSports");
        listIdEscolhidos = tempList;
        //Toast.makeText(ActivityAddPlaceMap.this, "SEC " + String.valueOf(tempList.size()), Toast.LENGTH_SHORT).show();

        // clear clicked location
        clickedLocation = null;

        btnConfirmAddPlace = (Button)findViewById(R.id.btn_addplace);
        sp_Raio = (Spinner)findViewById(R.id.sp_raio);
        edtxt_descricaolocal = (EditText)findViewById(R.id.edtxt_descricaolocal);
        edtxt_nomedoparque = (EditText)findViewById(R.id.edtxt_nomedoparque);

        btnConfirmAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtView = (TextView)sp_Raio.getSelectedView();
                selectedRaioString = txtView.getText().toString();
                //selectedRaioString = sp_Raio.getSelectedItem().toString();
                selectedLocation = clickedLocation;
                selectedPlaceDescription = edtxt_descricaolocal.getText().toString();
                selectedNomeDoParque = edtxt_nomedoparque.getText().toString();

               clickPost();
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    // POST DATA TO SERVER
    public void clickPost(){
        String url = "http://ec2-18-223-143-185.us-east-2.compute.amazonaws.com/slim/index.php/api/adicionarlocal";

        switch (selectedRaioString.toString()){
            case "100m":
                selectedRaio = "100";
                break;
            case "200m":
                selectedRaio = "200";
                break;
            case "500m":
                selectedRaio = "500";
                break;
            case "1km":
                selectedRaio = "1000";
                break;
            case "5km":
                selectedRaio = "5000";
                break;
        }


        final Map<String, String> newPlacelocalidade = new HashMap<String, String>();
        newPlacelocalidade.put("coordenadas", selectedLocation.latitude + "," + selectedLocation.longitude);
        newPlacelocalidade.put("descricao", selectedPlaceDescription);
        newPlacelocalidade.put("nome", selectedNomeDoParque);
        newPlacelocalidade.put("raio", selectedRaio);
        newPlacelocalidade.put("utilizador_id", "1");

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(newPlacelocalidade),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("status")) {
                                String idResponse = response.getString("id");
                                String url = "http://ec2-18-223-143-185.us-east-2.compute.amazonaws.com/slim/index.php/api/adicionarlocaldesportos";

                                // ADICIONAR DESPORTOS RECEBIDOS DA ATIVIDADE ANTERIOR
                                // PERCORRE DESPORTOS TODOS
                                // ADICIONA A JSON ARRAY
                                JSONArray arrayDesportosNewPlace = new JSONArray();
                                for(int i= 0 ; i<listIdEscolhidos.size() ; i++){
                                    JSONObject desportoAdicionado = new JSONObject();
                                    desportoAdicionado.put("desporto_id", String.valueOf(listIdEscolhidos.get(i)));
                                    desportoAdicionado.put("localidade_id", idResponse);
                                    arrayDesportosNewPlace.put(desportoAdicionado);
                                }

                                // FAZ JSON ARRAY REQUEST PARA ENVIAR DADOS PARA O WEB SERVICE
                                JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.POST, url,
                                        arrayDesportosNewPlace,
                                        new Response.Listener<JSONArray>() {
                                            @Override
                                            public void onResponse(JSONArray response) {
                                                try {
                                                    if (response.getJSONObject(0).getBoolean("status")) {
                                                        Toast.makeText(ActivityAddPlaceMap.this, "Sucesso", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        //Toast.makeText(ActivityAddPlaceMap.this, response.getJSONObject(0).getString("MSG"), Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException ex) {}
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                //Toast.makeText(ActivityAddPlaceMap.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                                MySingleton.getIntance(ActivityAddPlaceMap.this).addToRequestQueue(postRequest);

                                Toast.makeText(ActivityAddPlaceMap.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ActivityAddPlaceMap.this, response.getString("MSG"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ex) {}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityAddPlaceMap.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        MySingleton.getIntance(ActivityAddPlaceMap.this).addToRequestQueue(postRequest);

        Intent intent = new Intent(ActivityAddPlaceMap.this, ActivityMainMenu.class);
        ActivityAddPlaceMap.this.startActivity(intent);

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position(latLng);

                markerOptions.title("Novo Parque");

                mMap.clear();

                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                mMap.addMarker(markerOptions);

                clickedLocation = latLng;
            }
        });

        // Zoom into users location
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location location = getLastKnownLocation();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            if (location == null) {

                locationManager.requestLocationUpdates(PROVIDER, 1000, 0, new android.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        centerMapOnLocation(location, "YOur Location");
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
                centerMapOnLocation(location, "Your Location");

            } else {
                centerMapOnLocation(location, "Your Location");
            }

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION}, 1);
        }

    }


    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)ActivityAddPlaceMap.this.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION}, 1);

        }
        return bestLocation;
    }


    public void centerMapOnLocation(Location location, String title) {

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12));

    }


    protected Boolean checkIfLocationIsEnabled(){
        LocationManager lm = (LocationManager)ActivityAddPlaceMap.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex){

        }

        if(!gpsEnabled){
            new AlertDialog.Builder(ActivityAddPlaceMap.this)
                    .setMessage("GPS not enabled")
                    .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            ActivityAddPlaceMap.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show();
            return false;
        }

        return true;
    }

    // NEEDED TO CHECK FOR NETWORK
    public void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    public void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //classNoInternet.unregisterNetworkChanges();
    }

}
