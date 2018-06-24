package com.app.legend.time.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.app.legend.time.R;
import com.app.legend.time.activities.AlbumActivity;
import com.app.legend.time.adapter.PreviewImageAdapter;
import com.app.legend.time.bean.ImageInfo;

import java.util.List;

/**
 * 展示预览
 * A simple {@link Fragment} subclass.
 */
public class PreviewImageFragment extends BaseFragment {

    private ViewPager viewPager;
    private Button button;
    private List<ImageInfo> imageInfoList;
    private PreviewImageAdapter adapter;
    private ImageView back;


    private void getList(){

        Bundle bundle=getArguments();
        if (bundle==null){
            Log.d("size---->>>","nulll");
            return;
        }

        this.imageInfoList= (List<ImageInfo>) bundle.getSerializable("list");




    }

    public PreviewImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_preview_image, container, false);

        getComponent(view);

        getList();

        click();

        initPager();

        initBack();

        return view;
    }

    private void getComponent(View view){

        viewPager=view.findViewById(R.id.preview_pager);

        button=view.findViewById(R.id.pager_determine);
        back=view.findViewById(R.id.back_to_album);

    }

    private void click(){

        button.setOnClickListener(v -> {



        });

        back.setOnClickListener(v -> {

            AlbumActivity activity= (AlbumActivity) getActivity();

            activity.removeFragment(this);

        });

    }

    private void initBack(){

        back.setPadding(0,getStatusBarHeight(),0,0);
    }


    private void initPager(){

        adapter=new PreviewImageAdapter(getChildFragmentManager());
        adapter.setImageInfoList(this.imageInfoList);
        viewPager.setAdapter(adapter);


    }

}
