package ipvc.estg.commov.sportfinder.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ipvc.estg.commov.sportfinder.R;

public class cursorAdapterDesportos extends CursorAdapter {
    private Context mContext;
    private Cursor mCursor;
    private List<String> listDesportosEscolhidos;

    public cursorAdapterDesportos(Context context, Cursor c, List<String> teste) {
        super(context, c,0);
        this.mContext = context;
        this.mCursor = c;
        this.listDesportosEscolhidos=teste;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.linha_desporto,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

            TextView txtNomeDesporto= (TextView)view.findViewById(R.id.tv_Desporto);
            txtNomeDesporto.setText(mCursor.getString(cursor.getColumnIndex("nome")));
       /* for(int i=0;i<listDesportosEscolhidos.size();i++){
            if(mCursor.getString(cursor.getColumnIndexOrThrow("nome")).equals(listDesportosEscolhidos.get(i))){

                txtNomeDesporto.setBackgroundColor(Color.RED);
            }else {
                txtNomeDesporto.setText(mCursor.getString(cursor.getColumnIndexOrThrow("nome")));
            }
        }*/

    }

}
