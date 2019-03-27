package ipvc.estg.commov.sportfinder;

import android.database.MatrixCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import ipvc.estg.commov.sportfinder.adapter.cursorAdapterDesportos;
import ipvc.estg.commov.sportfinder.adapter.cursorAdapterSpotsFound;

public class ActivitySpotsFound extends AppCompatActivity {
    private ListView lv_SpotsFound;
    private MatrixCursor matrixCursor;
    private cursorAdapterSpotsFound cursorAdapterLocaisEncontrados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spots_found);
        //
        lv_SpotsFound=(ListView)findViewById(R.id.spot_found_listView);

        //
        setupListener();
        preencherListaLocais();
    }

    private void setupListener() {

    }

    //
    private void criarCursor(){
        matrixCursor= new MatrixCursor(new String[] {"_ID","nome_local", "avalicao","distancia"});
        for(int i=0;i<10;i++){
            matrixCursor.newRow()
                    .add("_ID",i)
                    .add("nome_local","Local " + i)
                    .add("avalicao", "5")
                    .add("distancia", (i*50)+"");
        }
    }

    private void preencherListaLocais() {
        this.criarCursor();
        this.cursorAdapterLocaisEncontrados = new cursorAdapterSpotsFound( ActivitySpotsFound.this,matrixCursor);
        this.lv_SpotsFound.setAdapter(cursorAdapterLocaisEncontrados);
    }

}
