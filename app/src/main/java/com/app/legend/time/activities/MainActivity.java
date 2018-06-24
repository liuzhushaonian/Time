package com.app.legend.time.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.app.legend.time.R;

public class MainActivity extends BaseActivity {

    private Button add_diary,see_diary;

    private static final String[] permissionStrings =
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getComponent();

        click();
    }

    /**
     * 获取组件
     */
    private void getComponent(){

        add_diary=findViewById(R.id.add_diary);
        see_diary=findViewById(R.id.see_diary);

    }

    private void click(){

        add_diary.setOnClickListener(v -> {
            getPermission();

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    openEdit();

                } else {
                    Toast.makeText(this, "无法获取权限，请赋予相关权限", Toast.LENGTH_SHORT).show();
                }


                break;
        }

    }

    private void getPermission() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, permissionStrings[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permissionStrings[0]}, 1000);

        }else {

            openEdit();
        }

    }

    private void openEdit(){
        Intent intent=new Intent(MainActivity.this,EditDiaryActivity.class);
        startActivity(intent);

    }


}
