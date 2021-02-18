package com.xhb.component.volleyplus;

import android.os.Handler;

import java.util.concurrent.Executor;


/**
 * Created by wei on 2021/2/8 4:48 PM
 */

public class PlusDelivery implements Delivery {

    Executor mExecutorPost;

    public PlusDelivery(final Handler handler) {
        mExecutorPost =
                new Executor() {
                    @Override
                    public void execute(Runnable command) {
                        handler.post(command);
                    }
                };
    }

    @Override
    public void postDelivery(PlusRequest<?> plusRequest, Response<?> response) {
        mExecutorPost.execute(new InnerRunnable(plusRequest, response));
    }

    @SuppressWarnings("rawtypes")
    private static class InnerRunnable implements Runnable {


        private PlusRequest mPlusRequest;
        private Response mResponse;

        public InnerRunnable(PlusRequest plusRequest, Response response) {
            mPlusRequest = plusRequest;
            mResponse = response;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            if (mPlusRequest.isCanceled()) {
                return;
            }
            if (mResponse.isSuccess()) {
                mPlusRequest.deliverResponse(mResponse.mResult);
            } else {
                mPlusRequest.deliverError(mResponse.mPlusVolleyError);
            }
        }
    }


}
