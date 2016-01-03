package com.example.flickr.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Khắc Vỹ on 12/30/2015.
 */
public abstract class MvpViewHolder<P extends BasePresenter> extends RecyclerView.ViewHolder{
    protected P presenter;

    public MvpViewHolder(View itemView) {
        super(itemView);
    }

    public void bindPresenter(P presenter){
        this.presenter = presenter;
        this.presenter.bindView(this);
    }

    public void unbindPresenter(){
        this.presenter = null;
    }
}
