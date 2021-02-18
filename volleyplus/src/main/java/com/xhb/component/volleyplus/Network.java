package com.xhb.component.volleyplus;

/**
 * Created by wei on 2021/2/8 4:36 PM
 */
public interface Network {
    NetworkResponse parseNetworkResponse(PlusRequest<?> plusRequest);
}
