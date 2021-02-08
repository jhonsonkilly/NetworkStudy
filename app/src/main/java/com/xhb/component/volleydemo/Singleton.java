package com.xhb.component.volleydemo;

import android.content.Context;
import android.media.MediaCodec;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by wei on 2021/2/7 5:57 PM
 */
public class Singleton {

    private static Singleton instance;

    private RequestQueue requestQueue;

    private Context ctx;

    private Singleton(Context context) {
        ctx = context;
        requestQueue = getQueue();
    }

    public synchronized static Singleton getInstance(Context context) {
        if (instance == null) {
            instance = new Singleton(context);
        }
        return instance;
    }

    public RequestQueue getQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addRequest(Request<T> request) {
        getQueue().add(request);
    }



}
