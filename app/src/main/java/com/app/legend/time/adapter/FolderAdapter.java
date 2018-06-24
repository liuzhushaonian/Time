package com.app.legend.time.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.legend.time.R;
import com.app.legend.time.interfaces.OnFolderSelectListener;

import java.util.List;

public class FolderAdapter extends BaseAdapter<FolderAdapter.ViewHolder> {

    private List<String> folderList;
    private OnFolderSelectListener listener;

    public void setListener(OnFolderSelectListener listener) {
        this.listener = listener;
    }

    public void setFolderList(List<String> folderList) {
        this.folderList = folderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());

        View view=inflater.inflate(R.layout.folder_item,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);


        viewHolder.view.setOnClickListener(v -> {

            int position=viewHolder.getAdapterPosition();

            String name=this.folderList.get(position);

            if (this.listener!=null){

                listener.onFolderSelected(name);
            }


        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (this.folderList==null){
            return;
        }

        String name=this.folderList.get(position);

        name=name.substring(name.lastIndexOf("/")+1,name.length());

        holder.textView.setText(name);

    }

    @Override
    public int getItemCount() {
        if (this.folderList!=null){
            return folderList.size();
        }
        return super.getItemCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        View view;
//        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            this.view=itemView;
//            this.imageView=itemView.findViewById(R.id.preview_image);
            this.textView=itemView.findViewById(R.id.folder_name);
        }
    }
}
