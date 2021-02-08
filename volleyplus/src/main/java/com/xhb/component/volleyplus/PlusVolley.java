package com.xhb.component.volleyplus;

import android.content.Context;

/**
 * Created by wei on 2021/2/8 4:22 PM
 * <p>
 * Volley.newRequestQueue(ctx.getApplicationContext());
 */
public class PlusVolley {


    public static PlusQueue newRequestQueue(Context context) {
        return new PlusQueue(new NetworkImpl());
    }


}
