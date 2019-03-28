package ipvc.estg.commov.sportfinder;

import android.Manifest;
import android.Manifest.permission;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.Toast;

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

import java.lang.reflect.Array;
import java.util.List;


public class ActivityAddPlaceMap extends AppCompatActivity implements OnMapReadyCallback {

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


    private String PROVIDER = LocationManager.GPS_PROVIDER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place_map);

        // clear clicked location
        clickedLocation = null;

        btnConfirmAddPlace = (Button)findViewById(R.id.btn_addplace);
        sp_Raio = (Spinner)findViewById(R.id.sp_raio);
        edtxt_descricaolocal = (EditText)findViewById(R.id.edtxt_descricaolocal);
        edtxt_nomedoparque = (EditText)findViewById(R.id.edtxt_nomedoparque);

        btnConfirmAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedRaio = sp_Raio.getSelectedItem().toString();
                LatLng selectedLocation = clickedLocation;
                String selectedPlaceDescription = edtxt_descricaolocal.getText().toString();
                String selectedNomeDoParque = edtxt_nomedoparque.getText().toString();

                JSONObject newPlace = new JSONObject();
                try {
                    newPlace.put("newRaio", selectedRaio);
                    newPlace.put("newLocation", selectedLocation);
                    newPlace.put("newPlaceDescription", selectedPlaceDescription);
                    newPlace.put("newPlaceName", selectedNomeDoParque);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(newPlace);

                //TODO
                //ADICIONAR CONFIRMAÇÃO
                //LIGAÇÃO AO WEBSERVICE PARA ADICIONAR LOCAL COM ESTAS INFORMAÇÕES
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



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

}
