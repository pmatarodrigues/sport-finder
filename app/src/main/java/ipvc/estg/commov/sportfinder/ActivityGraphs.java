package ipvc.estg.commov.sportfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
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
        String url="http://ec2-18-223-143-185.us-east-2.compute.amazonaws.com/slim/index.php/api/getPontoSemana/1";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int pontos;
                            int semana;
                            Desporto desporto;
                            GraphView graph = (GraphView) findViewById(R.id.graph);
                            JSONArray arr = response.getJSONArray("DATA");

                            DataPoint[] dataPoints = new DataPoint[arr.length()];
                            for(int i=0;i<arr.length();i++){
                                JSONObject obj=arr.optJSONObject(i);

                                pontos = Integer.parseInt(obj.getString("pontos"));
                                semana = Integer.parseInt(obj.getString("semana_registo"));
                                dataPoints[i] = new DataPoint(semana, pontos); // segundo param possivel double
                            };

                            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints);

                            // enable scaling and scrolling
                            graph.getViewport().setScalable(true);
                            graph.getViewport().setScalableY(true);

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
