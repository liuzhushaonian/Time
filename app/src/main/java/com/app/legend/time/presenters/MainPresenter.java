package com.app.legend.time.presenters;

import com.app.legend.time.interfaces.IMainActivity;

public class MainPresenter extends BasePresenter<IMainActivity> {

    private IMainActivity activity;

    public MainPresenter(IMainActivity activity) {
        attachView(activity);

        this.activity=getView();
    }


}
