package ipvc.estg.commov.sportfinder;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ipvc.estg.commov.sportfinder.Classes.Local;
import ipvc.estg.commov.sportfinder.Classes.MySingleton;

public class ActivityParqueDetails extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        ResultCallback<Status> {

    private static final String TAG = ActivityParqueDetails.class.getSimpleName();

    LatLng latLng;

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;

    private MapFragment mapFragment;
    private Button btnDirecoes;

    ArrayList<LatLng> locs= new ArrayList<LatLng>();
    private List<Local> listaLocais;

    int idParque;
    String nomeParque;
    String descricaoParque;
    LatLng latLngParque;
    String raioParque;

    public Date horasEntrada;
    public Date horasSaida;

    private static final long GEO_DURATION = 60 * 60 * 1000;
    private String GEOFENCE_REQ_ID = null;
    private float GEOFENCE_RADIUS = 0.0f;

    public Long tempoDentroGeofence;

    public Date getHorasEntrada() {
        return horasEntrada;
    }
    public void setHorasEntrada(Date horasEntrada) {
        this.horasEntrada = horasEntrada;
    }
    public Date getHorasSaida() {
        return horasSaida;
    }
    public void setHorasSaida(Date horasSaida) {
        this.horasSaida = horasSaida;
    }

    // NEEDED TO CHECK FOR NETWORK
    private BroadcastReceiver mNetworkReceiver;
    ClassNoInternet classNoInternet;

    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";

    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
        Log.i(TAG, "------------> notification makeNotificationIntent" );

        Intent intent = new Intent( context, ActivityParqueDetails.class );
        intent.putExtra( NOTIFICATION_MSG, msg );
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parque_details);

        Bundle extras = getIntent().getExtras();

        idParque = extras.getInt("_ID");
        nomeParque = extras.getString("nome");
        GEOFENCE_REQ_ID = nomeParque;
        raioParque = extras.getString("raio");
        GEOFENCE_RADIUS = Float.parseFloat(raioParque);
        descricaoParque = extras.getString("descricao");


        //MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        latLng = new LatLng(41.6920494, -8.8346252);
        btnDirecoes=(Button) findViewById(R.id.btnDirection);
        getListaLocais();

        // NEEDED TO CHECK FOR NETWORK
        mNetworkReceiver = new NetworkChangeReceiver();
        classNoInternet = new ClassNoInternet(mNetworkReceiver);
        registerNetworkBroadcastForNougat();

        //GEOFENCE_REQ_ID = "NOME DO PARQUE";
        //GEOFENCE_RADIUS = 1000;

        LocalBroadcastManager lbc = LocalBroadcastManager.getInstance(this);
        GoogleReceiver receiver = new GoogleReceiver(this);
        lbc.registerReceiver(receiver, new IntentFilter("googlegeofence"));
        //Anything with this intent will be sent to this receiver

        // initialize GoogleMaps
        initGMaps();

        // create GoogleApiClient
        createGoogleApi();

    }

    static class GoogleReceiver extends BroadcastReceiver {

        ActivityParqueDetails activityParqueDetails;
        public GoogleReceiver(Activity activity){
            activityParqueDetails = (ActivityParqueDetails) activity;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            //Handle the intent here
            Bundle extras = intent.getExtras();
            Date horasEntradaAUX = (Date) extras.get("horasEntrada");
            Date horasSaidaAUX = (Date) extras.get("horasSaida");

            if(horasEntradaAUX != null) {
                activityParqueDetails.setHorasEntrada(horasEntradaAUX);
            }
            if(horasSaidaAUX != null){
                activityParqueDetails.setHorasSaida(horasSaidaAUX);
            }

            if(activityParqueDetails.getHorasEntrada() != null && activityParqueDetails.getHorasSaida() != null){
                Long diff = activityParqueDetails.getHorasSaida().getTime() - activityParqueDetails.getHorasEntrada().getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                activityParqueDetails.tempoDentroGeofence = minutes;
                Log.i("TAG", "HORAS DIFF " + diff + ":" + seconds + ":" + minutes);
                Toast.makeText(context, "Ganhou " + seconds + " pts", Toast.LENGTH_LONG).show();
                activityParqueDetails.setHorasEntrada(null);
                activityParqueDetails.setHorasSaida(null);

            }
        }
    }

    // Create GoogleApiClient instance
    private void createGoogleApi() {
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder( this )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( this )
                    .addApi( LocationServices.API )
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }


    private final int REQ_PERMISSION = 999;

    // Check for permission to access Location
    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION
        );
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION: {
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    getLastKnownLocation();
                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }

    // Initialize GoogleMaps
    private void initGMaps(){
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Callback called when Map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Nome do Parque");

        markerForGeofence(latLng);
        markerLocation(latLng);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        markerForGeofence(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL =  1000;
    private final int FASTEST_INTERVAL = 900;

    // Start location Updates
    private void startLocationUpdates(){
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if ( checkPermission() ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
    }

    // GoogleApiClient.ConnectionCallbacks connected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startGeofence();
        getLastKnownLocation();
        recoverGeofenceMarker();
    }

    // GoogleApiClient.ConnectionCallbacks suspended
    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }

    // GoogleApiClient.OnConnectionFailedListener fail
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
    }

    // Get last known location
    private void getLastKnownLocation() {
        if ( checkPermission() ) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if ( lastLocation != null ) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        }else{
            askPermission();
        }
    }

    private void writeLastLocation() {
    }

    private Marker locationMarker;
    private void markerLocation(LatLng latLng) {
        String title = latLng.latitude + ", " + latLng.longitude;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        if ( mMap!=null ) {
            if ( locationMarker != null )
                locationMarker.remove();
            locationMarker = mMap.addMarker(markerOptions);
            float zoom = 14f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            mMap.animateCamera(cameraUpdate);
        }
    }


    private Marker geoFenceMarker;
    private ArrayList<Marker> gfmarkr = new ArrayList<Marker>();

    private void markerForGeofence(LatLng latLng) {
        Log.i(TAG, "markerForGeofence("+latLng+")");
        String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
        if ( mMap!=null ) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = mMap.addMarker(markerOptions);
            gfmarkr.add(geoFenceMarker);

        }
    }

    // Start Geofence creation process
    private void startGeofence() {
        Log.i(TAG, "startGeofence()este");
        if( geoFenceMarker != null ) {
            for(int i=0;i<gfmarkr.size();i++) {
                Geofence geofence = createGeofence(gfmarkr.get(i).getPosition(), GEOFENCE_RADIUS);
                GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
                addGeofence(geofenceRequest);
            }
        } else {
            Log.e(TAG, "Geofence marker is null");
        }
    }

    // Create a Geofence
    private Geofence createGeofence( LatLng latLng, float radius ) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion( latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration( GEO_DURATION )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest( Geofence geofence ) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence( geofence )
                .build();
    }

    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;
        Log.d(TAG,"chegou aqui123");
        Intent intent = new Intent( this, GeofenceTrasitionService.class);

        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if ( status.isSuccess() ) {
            saveGeofence();
            drawGeofence();
        } else {
            // inform about fail
        }
    }

    // Draw Geofence circle on GoogleMap
    private Circle geoFenceLimits;
    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");

        if ( geoFenceLimits != null )
            geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center( geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50, 200,70,70))
                .fillColor( Color.argb(100, 150,0,150) )
                .radius( GEOFENCE_RADIUS );
        geoFenceLimits = mMap.addCircle( circleOptions );
    }

    private final String KEY_GEOFENCE_LAT = "GEOFENCE LATITUDE";
    private final String KEY_GEOFENCE_LON = "GEOFENCE LONGITUDE";

    // Saving GeoFence marker with prefs mng
    private void saveGeofence() {
        Log.d(TAG, "saveGeofence()");
        SharedPreferences sharedPref = getPreferences( Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong( KEY_GEOFENCE_LAT, Double.doubleToRawLongBits( geoFenceMarker.getPosition().latitude ));
        editor.putLong( KEY_GEOFENCE_LON, Double.doubleToRawLongBits( geoFenceMarker.getPosition().longitude ));
        editor.apply();
    }

    // Recovering last Geofence marker
    private void recoverGeofenceMarker() {
        Log.d(TAG, "recoverGeofenceMarker");
        SharedPreferences sharedPref = getPreferences( Context.MODE_PRIVATE );

        if ( sharedPref.contains( KEY_GEOFENCE_LAT ) && sharedPref.contains( KEY_GEOFENCE_LON )) {
            double lat = Double.longBitsToDouble( sharedPref.getLong( KEY_GEOFENCE_LAT, -1 ));
            double lon = Double.longBitsToDouble( sharedPref.getLong( KEY_GEOFENCE_LON, -1 ));
            LatLng latLng = new LatLng( lat, lon );
            markerForGeofence(latLng);
            drawGeofence();
        }
    }

    // Clear Geofence
    private void clearGeofence() {
        Log.d(TAG, "clearGeofence()");
        LocationServices.GeofencingApi.removeGeofences(
                googleApiClient,
                createGeofencePendingIntent()
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if ( status.isSuccess() ) {
                    // remove drawing
                    removeGeofenceDraw();
                }
            }
        });
    }

    private void removeGeofenceDraw() {
        Log.d(TAG, "removeGeofenceDraw()");
        if ( geoFenceMarker != null)
            geoFenceMarker.remove();
        if ( geoFenceLimits != null )
            geoFenceLimits.remove();
    }
    private void getListaLocais(){
        String url="http://ec2-18-223-143-185.us-east-2.compute.amazonaws.com/slim/index.php/api/getLocaisSmall";
        //Local local= new Local();
        listaLocais= new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Local local;
                            JSONArray arr = response.getJSONArray("DATA");
                            Log.d(TAG,"Locais123arr"+listaLocais.size());
                            for(int i=0;i<arr.length();i++){
                                local= new Local();
                                JSONObject obj=arr.optJSONObject(i);
                                local.setId(obj.getString("id"));
                                local.setNome(obj.getString("nome"));

                                String latLngAux=obj.getString("coordenadas");
                                String aux[]= latLngAux.split(",");

                                String latitude = aux[0];
                                String longitude = aux[1];
                                Log.d(TAG,"Locais123lat: "+latitude);
                                local.setLatLng(new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude)));

                                local.setRaio(Integer.valueOf(obj.getString("raio")));
                                listaLocais.add(local);
                            };
                            preencherListaLocais();
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

    private void preencherListaLocais(){
        for(int i=0;i<listaLocais.size();i++){
            Log.d(TAG,"Locais123id: "+listaLocais.get(i).getId());
        }

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