package com.app.legend.time.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.app.legend.time.R;
import com.app.legend.time.bean.DiaryInfo;
import com.app.legend.time.bean.ImageInfo;
import com.app.legend.time.interfaces.OnSelectListener;
import com.app.legend.time.utils.ImageLoader;
import com.app.legend.time.utils.TimeApp;
import com.app.legend.time.views.SelectView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示图片
 */
public class AlbumAdapter extends BaseAdapter<AlbumAdapter.ViewHolder> implements Filterable {


    private List<ImageInfo> imageInfoList;//原始数据

    private List<ImageInfo> showList;//展示数据

    private List<ImageInfo> diaryInfoList;//储存数据 沃日那么多数据累死我了
    private OnSelectListener listener;
    private String folder="all";

    public List<ImageInfo> getImageInfoList() {
        return imageInfoList;
    }

    public void setFolder(String folder) {
        if (folder.equals("全部"))
        {
            this.folder="all";
        }else {
            this.folder = folder;
        }

        getFilter().filter(this.folder);//过滤
    }

    public void setListener(OnSelectListener listener) {
        this.listener = listener;
    }

    public void setImageInfoList(List<ImageInfo> imageInfoList) {
        this.imageInfoList = imageInfoList;

        if (this.showList==null){
            this.showList=new ArrayList<>();
        }

        this.showList.addAll(imageInfoList);

        notifyDataSetChanged();
    }

    public AlbumAdapter() {
        diaryInfoList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.image_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.selectView.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();

            clickSelect(position);

        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (this.showList == null) {
            return;
        }


        ImageInfo imageInfo = showList.get(position);

        Glide.with(TimeApp.getContext()).load(imageInfo.getPath()).into(holder.imageView);



        if (diaryInfoList == null) {
            return;
        }

        if (diaryInfoList.contains(imageInfo)) {

            int order=diaryInfoList.indexOf(imageInfo);

            holder.selectView.setOrder(order+1);

        } else {

            holder.selectView.setOrder(-1);

        }

    }

    @Override
    public int getItemCount() {

        if (this.showList != null) {
            return showList.size();
        }

        return super.getItemCount();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results=new FilterResults();

                List<ImageInfo> imageList=new ArrayList<>();

                if (constraint!=null&&constraint.toString().trim().length()>0&&!constraint.equals("all")) {

                    for (ImageInfo imageInfo : imageInfoList) {


                        if (imageInfo.getFolder().equals(constraint)) {

                            imageList.add(imageInfo);
                        }

                    }
                }else {

                    imageList.clear();
                    imageList.addAll(imageInfoList);
                }

                results.count=imageList.size();
                results.values=imageList;


                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                showList= (List<ImageInfo>) results.values;

                if (results.count>0){

                    notifyDataSetChanged();
                }

            }
        };

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        View view;
        SelectView selectView;

        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.imageView = itemView.findViewById(R.id.image_show);
            this.selectView = itemView.findViewById(R.id.select_view);

            reDraw();
        }


        /**
         * 重新设置item的大小
         */
        private void reDraw() {

            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();

            int space=this.view.getResources().getDimensionPixelSize(R.dimen.album_item_space)*2;

            int w = (TimeApp.getContext().getResources().getDisplayMetrics().widthPixels-space) / 3;

            params.width = w;
            params.height = w;

            view.setLayoutParams(params);

        }
    }

    private void clickSelect(int position) {

        if (showList == null) {

            return;
        }

        if (position<0){

            return;
        }

        ImageInfo imageInfo = showList.get(position);

        if (diaryInfoList == null) {
            diaryInfoList = new ArrayList<>();
        }

        if (diaryInfoList.contains(imageInfo)) {
            diaryInfoList.remove(imageInfo);
        } else {


            if (diaryInfoList.size()<9) {
                diaryInfoList.add(imageInfo);

            }else {
                //到达9张后不再添加
                return;
            }
        }

        if (this.listener!=null){

            listener.selectImage(diaryInfoList.size());
        }

        notifyDataSetChanged();

    }

    public List<ImageInfo> getDiaryInfoList() {
        return diaryInfoList;
    }


}
