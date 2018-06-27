package com.app.legend.time.utils;

import android.util.Log;
import android.widget.EditText;

import com.app.legend.time.adapter.EditAdapter;
import com.app.legend.time.bean.AddItemInfo;
import com.app.legend.time.bean.DiaryInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 日记内容管理类
 */
public class DiaryItemManager {

    private static volatile DiaryItemManager manager;
    private DiaryInfo currentEditItem;
    private EditAdapter adapter;
    private EditText currentEditText;

    private List<DiaryInfo> diaryInfoList;

    private DiaryItemManager() {

    }



    public static DiaryItemManager getDefault() {
        if (manager == null) {

            synchronized (DiaryItemManager.class) {

                manager = new DiaryItemManager();

            }

        }

        return manager;

    }

    public void setCurrentEditItem(DiaryInfo currentEditItem) {
        this.currentEditItem = currentEditItem;
    }

    public void setAdapter(EditAdapter adapter) {

        if (this.adapter == null) {

            this.adapter = adapter;

            this.adapter.setOnEditTextFocusChangeListener((view, hasFocus, diaryInfo) -> {

                if (hasFocus) {

                    currentEditItem = diaryInfo;

                    currentEditText = view;

                }

            });


            //删除item
            this.adapter.setOnDeleteItemListener(position -> {

                DiaryInfo info = diaryInfoList.get(position);

                info.setImg_url("");

                this.adapter.notifyItemChanged(position);//先刷新


                if (position == 0) {//第一个排除，避免文字被删
                    return;
                }

                DiaryInfo pre_info = diaryInfoList.get(--position);//获取前一个item

                if (pre_info == null) {
                    return;
                }

                String pre_content = pre_info.getContent();


                String cur_content = info.getContent();

                if (cur_content == null || cur_content.isEmpty()) {//内容为null或没有文字，直接删除item

                    diaryInfoList.remove(info);

                    if (info.equals(currentEditItem)) {//如果移除的item就是当前item，那么将当前item设为null

                        currentEditItem = null;
                        currentEditText = null;

                    }

                    this.adapter.notifyDataSetChanged();
                    return;
                }

                if (pre_content == null || pre_content.isEmpty()) {//上一个content为null或者没有文字
                    pre_content = cur_content;
                } else {
                    pre_content = pre_content + "\n" + cur_content;//添加内容
                }

                pre_info.setContent(pre_content);//将内容添加到前一个
                diaryInfoList.remove(info);//移除当前item

                if (info.equals(currentEditItem)) {

                    currentEditItem = null;
                    currentEditText = null;

                }

                this.adapter.notifyDataSetChanged();


            });

        }

    }

    public void setDiaryInfoList(List<DiaryInfo> diaryInfoList) {
        this.diaryInfoList = diaryInfoList;
    }

    /**
     *  接收一个AdditemInfo对象，内部封装开始的item，光标的位置以及添加的队列
      * @param addItemInfo
     */
    public void addItem(AddItemInfo addItemInfo) {


        convert(addItemInfo);


    }

    /**
     * 转换
     *
     */
    private void convert(AddItemInfo addItemInfo){

        int itemPosition=addItemInfo.getItemIndex();
        int indexPosition=addItemInfo.getIndex();

        List<DiaryInfo> diaryInfos=addItemInfo.getDiaryInfoList();

        if (diaryInfos==null){

            return;
        }

        if (itemPosition<0){

            itemPosition=diaryInfos.size()-1;

        }

        String content=addItemInfo.getContent();

        String preContent="";

        String nexContent="";

        if (indexPosition>=0&&!content.isEmpty()){

            preContent=content.substring(0,indexPosition);

            nexContent=content.substring(indexPosition,content.length());

            currentEditItem.setContent(preContent);


        }

        for (int i=0;i<diaryInfos.size();i++){

            DiaryInfo diaryInfo=diaryInfos.get(i);

            if (i==diaryInfos.size()-1){//最后一个 添加光标后面的内容
                diaryInfo.setContent(nexContent);

            }

            addItemAndRefresh(diaryInfo,itemPosition+i+1);

        }

        this.adapter.notifyDataSetChanged();//刷新

    }


    private void addItemAndRefresh(DiaryInfo diaryInfo, int position) {

        Log.d("position---->>>",position+"");

        if (position<0||position>=this.diaryInfoList.size()){
            this.diaryInfoList.add(diaryInfo);
        }
//
//        else if (position==0){//第一位
//
//
//        }

        else {

            this.diaryInfoList.add(position, diaryInfo);

        }






    }

    /**
     * 在点击添加图片的时候，将这一瞬的结果封装传递出去，避免结果在之后遭到改变
     * @return
     */
    public AddItemInfo getAddItemInfo(){

        AddItemInfo addItemInfo=new AddItemInfo();

        String content="";

        if (currentEditItem==null){

            addItemInfo.setItemIndex(-1);
        }else {

            int itemIndex=this.diaryInfoList.indexOf(currentEditItem);

            addItemInfo.setItemIndex(itemIndex);
        }


        if (currentEditText==null){

            addItemInfo.setIndex(-1);
        }else {

            int index=currentEditText.getSelectionStart();

            addItemInfo.setIndex(index);

            content=currentEditText.getText().toString();

            addItemInfo.setContent(content);
        }


        return addItemInfo;







    }

    private void test(){




    }


}
