package com.xhb.component.volleyplus;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wei on 2021/2/8 4:21 PM
 */
public class PlusQueue {

    private final PriorityBlockingQueue<PlusRequest<?>> mNetworkQueue = new PriorityBlockingQueue<>();

    private final PriorityBlockingQueue<PlusRequest<?>> mCacheQueue = new PriorityBlockingQueue<>();


    private AtomicInteger mSequenceGenerator = new AtomicInteger();

    private final Set<PlusRequest<?>> mCurrentRequests = new HashSet<>();

    private final List<requestEventListener> requestEventListeners = new ArrayList<>();


    private static final int DEFAULT_THREADS = 4;

    private NetworkDispatcher[] mNetworkDispatchers;
    private CacheDispatcher mCacheDispatcher;

    private final Cache mCache;

    private final Network mNetwork;

    private final Delivery mDelivery;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            RequestEvent.REQUEST_START,
            RequestEvent.REQUEST_FINISH

    })
    public @interface RequestEvent {

        public static int REQUEST_START = 0;

        public static int REQUEST_FINISH = 1;
    }

    public interface requestEventListener {
        void onRequestEvent(PlusRequest<?> request, @RequestEvent int event);
    }


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

    public void add(PlusRequest<?> request) {
        synchronized (mCurrentRequests) {
            mCurrentRequests.add(request);
        }
        request.setSequence(getSequenceNumber());
        setEvent(request, RequestEvent.REQUEST_START);
        mNetworkQueue.add(request);
    }

    public void setEvent(PlusRequest<?> request, @RequestEvent int requestStart) {
        synchronized (requestEventListeners) {
            for (requestEventListener requestEventListener : requestEventListeners) {
                requestEventListener.onRequestEvent(request, requestStart);
            }
        }
    }


    int getSequenceNumber() {
        return mSequenceGenerator.incrementAndGet();
    }

    interface FilterSupplier {
        boolean apply(PlusRequest<?> plusRequest);
    }

    public void cancel(final Object tag) {
        cancelAll(
                new FilterSupplier() {
                    @Override
                    public boolean apply(PlusRequest<?> plusRequest) {
                        return tag == plusRequest.toString();
                    }
                });
    }

    void cancelAll(FilterSupplier filterSupplier) {
        synchronized (mCurrentRequests) {
            for (PlusRequest<?> plusRequest : mCurrentRequests) {
                if (filterSupplier.apply(plusRequest)) {
                    plusRequest.isCanceled();
                }
            }
        }
    }


}
