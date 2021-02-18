package com.xhb.component.volleyplus;

import android.os.Process;
import android.os.SystemClock;

import java.util.concurrent.PriorityBlockingQueue;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * Created by wei on 2021/2/8 4:21 PM
 */
public class NetworkDispatcher extends Thread {

    private Network mNetwork;

    private Delivery mDelivery;

    private boolean mQuit;

    private PriorityBlockingQueue<PlusRequest<?>> mPriorityQueue;


    public NetworkDispatcher(Network network,
                             Delivery delivery,
                             PriorityBlockingQueue<PlusRequest<?>> priorityQueue) {
        this.mNetwork = network;
        this.mDelivery = delivery;
        this.mPriorityQueue = priorityQueue;
    }

    @Override
    public void run() {
        super.run();
        Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
        while (true) {
            try {
                PlusRequest<?> request = mPriorityQueue.take();
                processRequest(request);
            } catch (InterruptedException e) {
                if (mQuit) {
                    Thread.currentThread().interrupt();
                }
                e.printStackTrace();
            }
        }
    }


    private void processRequest(PlusRequest<?> request) {
        long startTime = SystemClock.elapsedRealtime();
        NetworkResponse networkResponse = mNetwork.parseNetworkResponse(request);
        Response<?> response = request.parseResponse(networkResponse);
        mDelivery.postDelivery(request, response);
    }

    public void quit() {
        mQuit = true;
        interrupt();
    }
}
