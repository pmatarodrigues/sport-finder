package ipvc.estg.commov.sportfinder.Classes;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mIntance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public MySingleton(Context context) {
        mCtx=context;
        mRequestQueue= getRequestQueue();
    }

    public static synchronized MySingleton getIntance(Context context){
        if(mIntance==null){
            mIntance=new MySingleton(context);
        }
        return mIntance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue==null){
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return  mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }


}
