package com.app.legend.time.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.app.legend.time.R;
import com.app.legend.time.activities.AlbumActivity;
import com.app.legend.time.activities.EditDiaryActivity;
import com.app.legend.time.bean.AddItemInfo;
import com.app.legend.time.bean.DiaryInfo;
import com.app.legend.time.bean.ImageInfo;
import com.app.legend.time.interfaces.IEditDiaryActivity;
import com.app.legend.time.utils.DiaryEditText;
import com.app.legend.time.utils.TimeApp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditActivityPresenter extends BasePresenter<IEditDiaryActivity> {

    private IEditDiaryActivity activity;
    private int screenWidth;
    private int screenHeight;

    public EditActivityPresenter(IEditDiaryActivity activity) {
        this.activity = activity;

        screenWidth = (TimeApp.getContext().getResources().getDisplayMetrics().widthPixels / 3);
        screenHeight = (TimeApp.getContext().getResources().getDisplayMetrics().heightPixels / 3);
    }


    /**
     * 收集信息
     *
     * @param activity
     */
    public void getEditInfo(Activity activity, LinearLayoutManager linearLayoutManager) {

        Observable
                .create((ObservableOnSubscribe<AddItemInfo>) e -> {
                    AddItemInfo addItemInfo = new AddItemInfo();

                    for (int i = 0; i < linearLayoutManager.getItemCount(); i++) {

                        View holder = linearLayoutManager.findViewByPosition(i);

                        if (holder != null) {

                            EditText editText = holder.findViewById(R.id.diary_edit_text);

                            if (editText.hasFocus()) {

                                int index = editText.getSelectionStart();

                                addItemInfo.setIndex(index);
                                addItemInfo.setItemIndex(i);
                                addItemInfo.setContent(editText.getText().toString());
                                break;
                            }
                        }

                    }

                    e.onNext(addItemInfo);

                    e.onComplete();

                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddItemInfo>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(AddItemInfo addItemInfo) {

                        Intent intent = new Intent(activity, AlbumActivity.class);

                        intent.putExtra("info", addItemInfo);

                        activity.startActivityForResult(intent, 500);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (!disposable.isDisposed()) {
                            disposable.dispose();
                        }
                    }
                });

    }



}
