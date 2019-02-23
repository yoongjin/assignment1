package com.example.jeanniesecure;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/* Vconnection is required by Volley to initiate the connection to our server
 * This is referenced from https://www.youtube.com/watch?v=EwJMWVAkKno (Volley) */
public class Vconnection {
    private static Vconnection nInstance;
    private RequestQueue RQ;
    private Context CTX;

    private Vconnection(Context context){
        CTX=context;
        RQ=getRequestQue();
    }

    public RequestQueue getRequestQue(){
        if(RQ == null){
            RQ = Volley.newRequestQueue(CTX.getApplicationContext());
        }
        return RQ;
    }

    public static synchronized Vconnection getnInstance(Context context){
        if(nInstance == null){
            nInstance = new Vconnection(context);
        }
        return nInstance;
    }

    public <T> void addRequestQue (Request<T> request) { RQ.add(request); }
}
