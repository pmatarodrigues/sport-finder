package ipvc.estg.commov.sportfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ipvc.estg.commov.sportfinder.Classes.Desporto;
import ipvc.estg.commov.sportfinder.Classes.MySingleton;

public class ActivityGraphs extends AppCompatActivity {

    HashMap<Integer, Integer> dadosGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        getDadosGraphs();

    }

    private void getDadosGraphs(){
        String url="http://sportfinderapi.000webhostapp.com/slim/api/getPontoSemana/1";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Desporto desporto;
                            JSONArray arr = response.getJSONArray("DATA");
                            for(int i=0;i<arr.length();i++){
                                JSONObject obj=arr.optJSONObject(i);

                                obj.getString("sum(pontos)");

                            };

                            GraphView graph = (GraphView) findViewById(R.id.graph);
                            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                                    new DataPoint(0, 1),
                                    new DataPoint(1, 5),
                                    new DataPoint(2, 3),
                                    new DataPoint(3, 2),
                                    new DataPoint(4, 6)
                            });
                            graph.addSeries(series);

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

}
