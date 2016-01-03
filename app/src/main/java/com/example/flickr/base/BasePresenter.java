package com.example.flickr.base;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Created by Khắc Vỹ on 12/30/2015.
 */
public abstract class BasePresenter<M, V> {
    protected M model;
    private WeakReference<V> view;

    public void setModel(M model){
        resetState();
        this.model = model;
        if(setUpDone()){
            updateView();
        }
    }

    protected void resetState(){
    }

    public void bindView(@NonNull V view){
        this.view = new WeakReference<V>(view);
        if(setUpDone()){
            updateView();
        }
    }

    public void unbindView(){
        this.view = null;
    }

    protected V view(){
        if(view == null){
            return null;
        }else{
            return view.get();
        }
    }

    protected abstract void updateView();

    protected boolean setUpDone(){
        return (view != null) && (model != null);
    }
}
