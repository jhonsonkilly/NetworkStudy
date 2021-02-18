package com.xhb.component.volleyplus;

/**
 * Created by wei on 2021/2/8 4:42 PM
 */
public class Response<T> {

    public interface ErrorListener {
        void onErrorResponse(PlusVolleyError plusVolleyError);
    }

    public interface Listener<T> {
        void onResponse(T response);
    }


    public T mResult;

    public Cache.Entry mEntry;

    public PlusVolleyError mPlusVolleyError;


    public Response(T result, Cache.Entry entry) {
        mResult = result;
        mEntry = entry;
    }

    public Response(PlusVolleyError plusVolleyError) {
        mPlusVolleyError = plusVolleyError;
    }

    public boolean isSuccess() {
        return mResult == null;
    }

    public static <T> Response<T> success(T result, Cache.Entry entry) {
        return new Response<>(result, entry);
    }

    public static <T> Response<T> error(PlusVolleyError plusVolleyError) {
        return new Response<>(plusVolleyError);
    }


}
