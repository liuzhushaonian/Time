package com.app.legend.time.presenters;

import android.content.Context;

import com.app.legend.time.bean.ImageInfo;
import com.app.legend.time.interfaces.IAlbumActivity;
import com.app.legend.time.utils.ImageUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AlbumActivityPresenter {

    private IAlbumActivity activity;

    public AlbumActivityPresenter(IAlbumActivity activity) {
        this.activity = activity;
    }

    public void getData(Context context){

       Observable
                .create((ObservableOnSubscribe<List<ImageInfo>>) e -> {

                    List<ImageInfo> imageInfos= ImageUtils.getList(context);

                    e.onNext(imageInfos);

                    e.onComplete();

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ImageInfo>>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable=d;
                    }

                    @Override
                    public void onNext(List<ImageInfo> imageInfos) {
                        activity.setData(imageInfos);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });

    }
}
