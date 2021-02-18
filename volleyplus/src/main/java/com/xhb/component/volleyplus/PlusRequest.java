package com.xhb.component.volleyplus;

/**
 * Created by wei on 2021/2/8 4:21 PM
 */
public abstract class PlusRequest<T> {

    private int mSequence;

    public boolean isCanceled() {
        return true;
    }

    public abstract void deliverResponse(T result);


    public abstract void deliverError(PlusVolleyError plusVolleyError);


    public abstract Response<T> parseResponse(NetworkResponse response);

    public void setSequence(int sequence) {
        this.mSequence = sequence;
    }

    ;
}
