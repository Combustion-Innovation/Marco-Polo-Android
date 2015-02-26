package com.ci.contactmanager;

/**
 * Created by Alex on 1/7/15.
 */
public interface CICallback<A, B, C> {
    public void onStart(A a);
    public void onProgress(B b);
    public void onEnd(C c);
}
