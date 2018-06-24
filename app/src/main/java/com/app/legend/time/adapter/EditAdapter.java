package com.app.legend.time.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.legend.time.R;
import com.app.legend.time.bean.DiaryInfo;
import com.app.legend.time.utils.TimeApp;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class EditAdapter extends BaseAdapter<EditAdapter.ViewHolder> {


    private List<DiaryInfo> diaryInfoList;
    public static final int EDIT=0x00101;
    public static final int PREVIEW=0x00102;


    private int TYPE=EDIT;

    public EditAdapter(int type) {

        this.TYPE=type;

        if (TYPE==EDIT) {//如果是编辑模式，则初始化并默认添加
            this.diaryInfoList = new ArrayList<>();

            this.diaryInfoList.add(new DiaryInfo());
        }
    }

    public void setDiaryInfoList(List<DiaryInfo> diaryInfoList) {
        this.diaryInfoList = diaryInfoList;
        notifyDataSetChanged();
    }

    public void addDiary(DiaryInfo info) {

        this.diaryInfoList.add(info);

//        notifyDataSetChanged();
        notifyItemChanged(diaryInfoList.size()-1);
    }

    public void addDiaryByPosition(DiaryInfo info, int position) {

        if (position < 0 || position > this.diaryInfoList.size()) {
            return;
        }

        this.diaryInfoList.add(position, info);
//        notifyDataSetChanged();

        notifyItemChanged(position);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        if (diaryInfoList!=null){


            viewHolder.delete.setOnClickListener(v -> {
                int position=viewHolder.getAdapterPosition();
                convert(position);
            });
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (this.diaryInfoList == null) {
            return;
        }

        DiaryInfo diaryInfo = this.diaryInfoList.get(position);

        String image_url=diaryInfo.getImg_url();
        if (image_url==null||image_url.isEmpty()){

            holder.imageLayout.setVisibility(View.GONE);

        }else {//如果有图片，则进行图片加载

            holder.imageLayout.setVisibility(View.VISIBLE);

            Glide.with(TimeApp.getContext()).load(image_url).into(holder.imageView);

        }

        String content=diaryInfo.getContent();


        switch (TYPE){

            case EDIT:

                if (content!=null){
                    holder.editText.setText(content);
                }

                break;

            case PREVIEW://在预览时添加文字到textView上


                if (content!=null){
                    holder.textView.setText(content);
                }

                break;

        }


    }

    @Override
    public int getItemCount() {


        if (diaryInfoList==null) {
            return super.getItemCount();
        }

        return diaryInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView textView;
        ImageView imageView,delete;
        EditText editText, image_instructions;
        FrameLayout textLayout;
        LinearLayout imageLayout;

        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.textLayout = itemView.findViewById(R.id.single_diary_layout);
            this.imageLayout = itemView.findViewById(R.id.image_layout);
            this.textView = itemView.findViewById(R.id.diary_preview);
            this.imageView = itemView.findViewById(R.id.diary_image);
            this.editText = itemView.findViewById(R.id.diary_edit_text);
            this.image_instructions = itemView.findViewById(R.id.image_instructions);
            this.delete=itemView.findViewById(R.id.image_delete);
        }
    }

    /**
     * 对应item设置好文字
     * 删除图片后，如果该item带有text，则查找上一个item，如果上一个item存在，则将这个item的text添加到上一个item里，并删除本item
     * @param position 位置
     */
    private void convert(int position){

        DiaryInfo info=diaryInfoList.get(position);

        info.setImg_url("");

        notifyItemChanged(position);//先刷新


        if (position==0){//第一个排除，避免文字被删
            return;
        }

        DiaryInfo pre_info=diaryInfoList.get(--position);//获取前一个item

        if (pre_info==null){
            return;
        }

        String pre_content=pre_info.getContent();



        String cur_content=info.getContent();

        if (cur_content==null||cur_content.isEmpty()){//内容为null或没有文字，直接删除item

            diaryInfoList.remove(info);
//            notifyItemChanged(position);
            notifyDataSetChanged();
            return;
        }

        if (pre_content==null||pre_content.isEmpty()){//上一个content为null或者没有文字
            pre_content=cur_content;
        }else {
            pre_content= pre_content + "\n" +cur_content;//添加内容
        }

        pre_info.setContent(pre_content);//将内容添加到前一个
//        notifyItemChanged(--position);//刷新前一个

        diaryInfoList.remove(info);//移除当前item
//        notifyItemChanged(position);//刷新当前item

        notifyDataSetChanged();

    }
}
