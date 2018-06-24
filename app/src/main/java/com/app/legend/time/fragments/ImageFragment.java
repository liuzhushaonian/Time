package com.app.legend.time.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.legend.time.R;
import com.app.legend.time.bean.ImageInfo;
import com.app.legend.time.interfaces.OnPagerSelectClickListener;
import com.app.legend.time.views.SelectView;
import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends BaseFragment {

    private ImageView imageView;
    private ImageInfo imageInfo;
    private SelectView selectView;
    private OnPagerSelectClickListener listener;
    private int order=-1;

    public void setListener(OnPagerSelectClickListener listener) {
        this.listener = listener;
    }

    public ImageInfo getImageInfo() {
        return imageInfo;
    }

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_image, container, false);

        getComponent(view);

        initImage();

        click();

        return view;
    }

    private void getComponent(View view){

        this.imageView=view.findViewById(R.id.pre_image);
        this.selectView=view.findViewById(R.id.select_pager_number);
    }


    private void initImage(){

        Bundle bundle=getArguments();

        if (bundle==null){
            return;
        }

        ImageInfo imageInfo= (ImageInfo) bundle.getSerializable("image");

        if (imageInfo==null){
            return;
        }

        Glide.with(this).load(imageInfo.getPath()).into(this.imageView);

        this.imageInfo=imageInfo;

        int order=bundle.getInt("order");

        if (this.order<0) {
            setOrder(order);
        }

        this.order=order;

    }

    public void setOrder(int order){

        if (this.selectView==null){

            return;
        }

        this.selectView.setOrder(order);

    }

    private void click(){

        selectView.setOnClickListener(v -> {

            if (this.listener!=null){

                listener.itemClick(this.imageInfo);

            }

        });

    }

}
