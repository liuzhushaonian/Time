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
import com.app.legend.time.interfaces.OnDeleteItemListener;
import com.app.legend.time.interfaces.OnEditTextFocusChangeListener;
import com.app.legend.time.utils.DiaryItemManager;
import com.app.legend.time.utils.TimeApp;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class EditAdapter extends BaseAdapter<EditAdapter.ViewHolder> {


    private List<DiaryInfo> diaryInfoList;
    public static final int EDIT=0x00101;
    public static final int PREVIEW=0x00102;
    private OnEditTextFocusChangeListener onEditTextFocusChangeListener;
    private OnDeleteItemListener onDeleteItemListener;

    public void setOnDeleteItemListener(OnDeleteItemListener onDeleteItemListener) {
        this.onDeleteItemListener = onDeleteItemListener;
    }

    public void setOnEditTextFocusChangeListener(OnEditTextFocusChangeListener onEditTextFocusChangeListener) {
        this.onEditTextFocusChangeListener = onEditTextFocusChangeListener;
    }

    private int TYPE=EDIT;

    public EditAdapter(int type) {

        this.TYPE=type;

        if (TYPE==EDIT) {//如果是编辑模式，则初始化并默认添加
            this.diaryInfoList = new ArrayList<>();

            this.diaryInfoList.add(new DiaryInfo());
        }

        DiaryItemManager.getDefault().setAdapter(this);

        DiaryItemManager.getDefault().setDiaryInfoList(this.diaryInfoList);

    }

    public void setDiaryInfoList(List<DiaryInfo> diaryInfoList) {
        this.diaryInfoList = diaryInfoList;
        notifyDataSetChanged();
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        if (diaryInfoList!=null){


            viewHolder.delete.setOnClickListener(v -> {
                int position=viewHolder.getAdapterPosition();
//                convert(position);

                if (this.onDeleteItemListener!=null){
                    this.onDeleteItemListener.deleteItem(position);
                }
            });
        }

        viewHolder.editText.setOnFocusChangeListener((v, hasFocus) -> {

            int position=viewHolder.getAdapterPosition();

            if (position<0||position>=diaryInfoList.size()){
                return;
            }

            DiaryInfo diaryInfo=this.diaryInfoList.get(position);

            if (this.onEditTextFocusChangeListener!=null){

                this.onEditTextFocusChangeListener.changeFocus(viewHolder.editText,hasFocus,diaryInfo);
            }

        });


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

    public static class ViewHolder extends RecyclerView.ViewHolder {

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

    /**
     * 添加item
     * 要先判断当前光标在哪个item上，然后根据光标对文字进行分割
     * 1、如果光标所在的edittext没有文字，则默认添加到该item的后面
     * 2、如果光标在文件中间，则将这段文字一分为二，光标前端文字留在原item，后端文字放入到新item里
     * 3、如果没有光标，则默认放在最后
     * 然而以上方法根本做不到……
     *
     */
    private void addItem(){




    }



}
