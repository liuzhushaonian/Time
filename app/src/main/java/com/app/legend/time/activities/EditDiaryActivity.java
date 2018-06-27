package com.app.legend.time.activities;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LongDef;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.legend.time.R;
import com.app.legend.time.adapter.EditAdapter;
import com.app.legend.time.bean.AddItemInfo;
import com.app.legend.time.bean.DiaryInfo;
import com.app.legend.time.bean.ImageInfo;
import com.app.legend.time.interfaces.IEditDiaryActivity;
import com.app.legend.time.presenters.EditActivityPresenter;
import com.app.legend.time.utils.DiaryEditText;
import com.app.legend.time.utils.DiaryItemManager;

import java.util.List;


public class EditDiaryActivity extends BaseActivity<IEditDiaryActivity,EditActivityPresenter> implements IEditDiaryActivity{



    private Button button;
    private static final String[] permissionStrings =
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private EditAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);
        getComponent();
        click();

        initDiary();
        slideHelper.setSlideActivity(EditDiaryActivity.this);//滑动返回
    }

    @Override
    protected EditActivityPresenter createPresenter() {
        return new EditActivityPresenter(this);
    }

    private void getComponent(){

        button=findViewById(R.id.insert_image);
        recyclerView=findViewById(R.id.diary_layout);


    }

    private void initDiary(){

        linearLayoutManager=new LinearLayoutManager(this);
        adapter=new EditAdapter(EditAdapter.EDIT);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 500://接收返回结果

                if(resultCode==201){


                    if (data==null){

                        return;
                    }

                    AddItemInfo addItemInfo=data.getParcelableExtra("result");

                    if (addItemInfo==null){

                        Log.d("add----->>","it is null!!!!!!");

                        return;
                    }

                    Log.d("add------->>>",addItemInfo.getDiaryInfoList().toString());

                    DiaryItemManager.getDefault().addItem(addItemInfo);


                }

                break;


            default:
                super.onActivityResult(requestCode, resultCode, data);

                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    openAlbums();

                } else {
                    Toast.makeText(this, "无法获取权限，请赋予相关权限", Toast.LENGTH_SHORT).show();
                }


                break;
        }

    }

    /**
     * 点击事件
     */
    private void click(){

        button.setOnClickListener(v -> {
            checkPermission();
        });
    }


    /**
     * 检查权限
     */
    private void checkPermission(){

        if (ContextCompat.checkSelfPermission(this, permissionStrings[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permissionStrings[0]}, 1000);

        }else {

            openAlbums();
        }

    }

    /**
     * 进入图片选择
     */
    private void openAlbums(){

        presenter.getEditInfo(this,this.linearLayoutManager);

    }


    @Override
    public void setDiaryData(List<DiaryInfo> diaryInfoList) {
        adapter.setDiaryInfoList(diaryInfoList);
    }
}
