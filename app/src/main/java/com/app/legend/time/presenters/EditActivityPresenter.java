package com.app.legend.time.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.EditText;

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

public class EditActivityPresenter {

    private IEditDiaryActivity activity;
    private int screenWidth;
    private int screenHeight;

    public EditActivityPresenter(IEditDiaryActivity activity) {
        this.activity = activity;

        screenWidth= (TimeApp.getContext().getResources().getDisplayMetrics().widthPixels/3);
        screenHeight= (TimeApp.getContext().getResources().getDisplayMetrics().heightPixels/3);
    }

    public void insertImage(Uri uri, DiaryEditText editText, Context context){

        int start=editText.getSelectionStart();

        Editable editable=editText.getEditableText();

        Log.d("string--->>",editable.toString());

        Observable
                .create((ObservableOnSubscribe<SpannableString>) e -> {

                    Bitmap bitmap=getSizeBitmap(uri);
                    String editString=editText.getEditableText().toString();

                    if (editString.isEmpty()){
                        editString=" ";
                    }
                    SpannableString spannableString=new SpannableString(editString);

                    spannableString.setSpan(new ImageSpan(context,bitmap),0,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    e.onNext(spannableString);
                    e.onComplete();

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SpannableString>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable=d;
                    }

                    @Override
                    public void onNext(SpannableString spannableString) {
                        if (start<=0||start>=spannableString.length()){
                            editable.append(spannableString);
                        }else {
                            editable.insert(start,spannableString);
                        }

                        editable.insert(start+spannableString.length(),"\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }


    private Bitmap getSizeBitmap(Uri uri){

        Bitmap bitmap=null;

        try {
            InputStream inputStream=TimeApp.getContext().getContentResolver().openInputStream(uri);
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeStream(inputStream,null,options);

            if (inputStream != null) {
                inputStream.close();
            }

            inputStream=TimeApp.getContext().getContentResolver().openInputStream(uri);

            options.inSampleSize= reSize(options,screenWidth,screenHeight);
            options.inJustDecodeBounds=false;
            options.inPreferredConfig= Bitmap.Config.RGB_565;
            bitmap=BitmapFactory.decodeStream(inputStream,null,options);

            Log.d("bitmap--->>>",bitmap.getByteCount()/1024/1024+"M");

            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;

    }

    private int reSize(BitmapFactory.Options options,int rw,int rh){

        int w=options.outWidth;
        int h=options.outHeight;
        int inSampleSize=1;

        if (h>rh||w>rw){
            int halfH=h/2;
            int halfW=w/2;


            while ((halfH/inSampleSize)>=rh&&(halfW/inSampleSize)>=rw){

                inSampleSize*=2;
            }
        }


        Log.d("ins---->>",inSampleSize+"");

        return inSampleSize;

    }

    public void handlerList(List<ImageInfo> imageInfos){

        Observable
                .create((ObservableOnSubscribe<List<DiaryInfo>>) e -> {

                    List<DiaryInfo> diaryInfos=new ArrayList<>();

                    for (int i=0;i<imageInfos.size();i++){

                        DiaryInfo diaryInfo=new DiaryInfo();

                        ImageInfo info=imageInfos.get(i);
                        diaryInfo.setImg_url(info.getPath());
                        diaryInfos.add(diaryInfo);
                    }

                    e.onNext(diaryInfos);
                    e.onComplete();

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DiaryInfo>>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable=d;
                    }

                    @Override
                    public void onNext(List<DiaryInfo> diaryInfos) {
                        activity.setDiaryData(diaryInfos);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (!disposable.isDisposed()){

                            disposable.dispose();
                        }
                    }
                });

    }



}
