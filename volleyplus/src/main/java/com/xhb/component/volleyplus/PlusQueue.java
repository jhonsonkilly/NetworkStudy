package com.xhb.component.volleyplus;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by wei on 2021/2/8 4:21 PM
 */
public class PlusQueue {

    private final PriorityBlockingQueue<PlusRequest> mNetworkQueue = new PriorityBlockingQueue<>();

    private final PriorityBlockingQueue<PlusRequest> mCacheQueue = new PriorityBlockingQueue<>();


    private static final int DEFAULT_THREADS = 4;

    private NetworkDispatcher[] mNetworkDispatchers;
    private CacheDispatcher mCacheDispatcher;

    private final Cache mCache;

    private final Network mNetwork;

    private final Delivery mDelivery;

    public PlusQueue(
            Cache cache, Network network, Delivery delivery) {
        mCache = cache;
        mNetwork = network;
        mNetworkDispatchers = new NetworkDispatcher[DEFAULT_THREADS];
        mDelivery = delivery;
    }

    public PlusQueue(Cache cache, Network network) {
        this(
                cache,
                network,
                new PlusDelivery(new Handler(Looper.getMainLooper())));
    }

    private void stop() {
        for (NetworkDispatcher networkDispatcher : mNetworkDispatchers) {
            if (networkDispatcher != null) {
                networkDispatcher.quit();
            }
        }
        if (mCacheDispatcher != null) {
            mCacheDispatcher.quit();
        }
    }


    public void start() {
        stop();
        mCacheDispatcher = new CacheDispatcher();
        mCacheDispatcher.start();
        for (int i = 0; i < DEFAULT_THREADS; i++) {
            NetworkDispatcher mNetworkDispatcher = new NetworkDispatcher(mNetwork, mDelivery, mNetworkQueue);
            mNetworkDispatchers[i] = mNetworkDispatcher;
            mNetworkDispatcher.start();
        }
    }
}
