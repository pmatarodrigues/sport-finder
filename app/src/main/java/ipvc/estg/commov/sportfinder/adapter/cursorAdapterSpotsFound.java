package ipvc.estg.commov.sportfinder.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ipvc.estg.commov.sportfinder.ActivitySpotsFound;
import ipvc.estg.commov.sportfinder.R;

public class cursorAdapterSpotsFound extends CursorAdapter {
    private Context mContext;
    private Cursor mCursor;
    //
    private String urlFotos = "http://ec2-18-223-143-185.us-east-2.compute.amazonaws.com/slim/img/";

    public cursorAdapterSpotsFound(Context context, Cursor c) {
        super(context, c,0);
        this.mContext = context;
        this.mCursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.linha_local_encontrado,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d("TAG","mata123");
        //TextView txtNomeDesporto= (TextView)view.findViewById(R.id.tv_Desporto);
        //txtNomeDesporto.setText(mCursor.getString(cursor.getColumnIndexOrThrow("nome")));
        TextView txtNomeLocal= (TextView)view.findViewById(R.id.txt_spot_name);
        TextView txtAvalicao= (TextView)view.findViewById(R.id.txtEvaluation);
        TextView txtDistancia= (TextView)view.findViewById(R.id.txtDistance);
        ImageView imgLocal = (ImageView)view.findViewById(R.id.img_spot_encontrado);
        Log.d("IMG_URL", this.urlFotos+(mCursor.getString(cursor.getColumnIndexOrThrow("url_imagem"))));
        txtNomeLocal.setText(mCursor.getString(cursor.getColumnIndexOrThrow("nome_local")));
        txtAvalicao.setText(mCursor.getString(cursor.getColumnIndexOrThrow("avalicao")));
        txtDistancia.setText(mCursor.getString(cursor.getColumnIndexOrThrow("distancia")));
        Picasso.get().load(this.urlFotos + mCursor.getString(cursor.getColumnIndexOrThrow("url_imagem"))).into(imgLocal);
    }

}
