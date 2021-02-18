package com.xhb.component.volleyplus;

import android.content.Context;

import java.io.File;

/**
 * Created by wei on 2021/2/8 4:22 PM
 * <p>
 * Volley.newRequestQueue(ctx.getApplicationContext());
 */
public class PlusVolley {


    public static PlusQueue newRequestQueue(Context context) {
        DiskCache.FileSupplier fileSupplier = new DiskCache.FileSupplier() {
            @Override
            public File get() {
                return null;
            }
        };
        PlusQueue plusQueue = new PlusQueue(new DiskCache(fileSupplier), new NetworkImpl());
        plusQueue.start();
        return plusQueue;
    }


}
