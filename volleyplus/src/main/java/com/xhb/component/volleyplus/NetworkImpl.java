package com.xhb.component.volleyplus;

/**
 * Created by wei on 2021/2/8 4:36 PM
 */
public class NetworkImpl implements Network {


    @Override
    public NetworkResponse parseNetworkResponse(PlusRequest<?> plusRequest) {
        return new NetworkResponse();
    }
}
