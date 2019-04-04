package ipvc.estg.commov.sportfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ipvc.estg.commov.sportfinder.Classes.Desporto;
import ipvc.estg.commov.sportfinder.Classes.Localidade;
import ipvc.estg.commov.sportfinder.Classes.MySingleton;

public class ActivityShowSpotDetailed extends AppCompatActivity implements OnMapReadyCallback {
    //
    Localidade localidade;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_spot_detailed);
        this.localidade = new Localidade();
        Intent intent = getIntent();
        int id = (Integer) intent.getSerializableExtra("_ID");
        Toast.makeText(this, "_ID: "+id, Toast.LENGTH_SHORT).show();
        localidade.set_ID(id);
        //
        //getMoreInformation(id);
        objetoExemplo();
        //
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void objetoExemplo() {
        localidade.setNome("Largo dos Patos");
        localidade.setDescricao("Excelente local para correr e passear");
        localidade.setLat("41.692395");
        localidade.setLng("-8.835195");
        localidade.addUrl("praiaNorte1.png");
        localidade.addUrl("praiaNorte2.png");
    }

    private void getMoreInformation(int id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //callWebService(id);
    }

    private void callWebService(int id) {
        String url="http://ec2-18-223-143-185.us-east-2.compute.amazonaws.com/slim/index.php/api/getSpotDetailed/" + this.localidade.get_ID();
        final Localidade localidade = new Localidade();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{

                    JSONArray jsonArray = response.getJSONArray("DATA");
                    //((TextView) findViewById(R.id.lblResult)).setText(response.getString("descr"));
                    localidade.setNome(response.getString("nome"));
                    localidade.setDescricao(response.getString("descricao"));
                    localidade.setLat(response.getString("lat"));
                    localidade.setLng(response.getString("lng"));
                    localidade.addUrl("praiaNorte1.png");
                    localidade.addUrl("praiaNorte2.png");
                }catch (JSONException ex){}
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                //((TextView) findViewById(R.id.lblResult)).setText(error.getMessage());
            }
        });
        MySingleton.getIntance(this).addToRequestQueue(jsonObjectRequest);

        /*try {
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

        }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("TAG","pedro1234"+ error.getMessage());
        }
        });*/

    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        LatLng statue = new LatLng(Double.parseDouble(localidade.getLat()), Double.parseDouble(localidade.getLng()));
        map.addMarker(new MarkerOptions().position(statue).title(this.localidade.getNome()));
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        //Zoom com vista personalizada incluindo vista em diagonal ao objeto
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(statue)
                .zoom(17)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
