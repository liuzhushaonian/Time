package com.app.legend.time.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.app.legend.time.bean.ImageInfo;
import com.app.legend.time.fragments.ImageFragment;
import com.app.legend.time.interfaces.OnPagerSelectClickListener;

import java.util.ArrayList;
import java.util.List;

public class PreviewImageAdapter extends FragmentStatePagerAdapter implements OnPagerSelectClickListener{


    List<ImageInfo> imageInfoList;
    List<ImageFragment> fragmentList;


    public void setImageInfoList(List<ImageInfo> imageInfoList) {
        this.imageInfoList = imageInfoList;

        for (ImageInfo imageInfo:imageInfoList){

            ImageFragment fragment=new ImageFragment();
            Bundle bundle=new Bundle();
            bundle.putSerializable("image",imageInfo);
            bundle.putInt("order",this.imageInfoList.indexOf(imageInfo)+1);
            fragment.setArguments(bundle);

            fragment.setListener(this);

//            fragment.setOrder(this.imageInfoList.indexOf(imageInfo)+1);
            if (!this.fragmentList.contains(fragment)){
                this.fragmentList.add(fragment);
            }
        }


        notifyDataSetChanged();
    }

    public PreviewImageAdapter(FragmentManager fm) {
        super(fm);
        fragmentList=new ArrayList<>();

    }

    @Override
    public Fragment getItem(int position) {

        if (this.fragmentList==null){
            return null;
        }

//        ImageInfo imageInfo=imageInfoList.get(position);
////
////        ImageFragment fragment=new ImageFragment();
////        Bundle bundle=new Bundle();
////        bundle.putSerializable("image",imageInfo);
////        fragment.setArguments(bundle);
////        fragment.setOrder(this.imageInfoList.indexOf(imageInfo)+1);
////
////        if (!this.fragmentList.contains(fragment)){
////            this.fragmentList.add(fragment);
////        }


        return this.fragmentList.get(position);
    }

    @Override
    public int getCount() {

        if (this.fragmentList!=null){
            return this.fragmentList.size();
        }

        return 0;
    }


    /**
     * 点击选择事件
     * @param imageInfo
     */
    @Override
    public void itemClick(ImageInfo imageInfo){

        if (this.imageInfoList.contains(imageInfo)) {

            this.imageInfoList.remove(imageInfo);

        }else {

            this.imageInfoList.add(imageInfo);
        }

        notifyFragmentByPosition();//更新其他标签

    }

    /**
     * 重新设置fragment的顺序
     */
    private void notifyFragmentByPosition() {

        for (int i=0;i<fragmentList.size();i++){

            ImageFragment fragment=this.fragmentList.get(i);
            ImageInfo imageInfo=fragment.getImageInfo();

            int position=this.imageInfoList.indexOf(imageInfo);

            fragment.setOrder(position+1);

        }

        notifyDataSetChanged();
    }


//    @Override
//    public int getItemPosition(@NonNull Object object) {
//
//
//
//        return POSITION_NONE;
//    }
}
