package com.app.legend.time.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AddItemInfo implements Parcelable{

    private int itemIndex=-1;
    private int index=-1;
    private String content="";

    public AddItemInfo() {
    }



    private AddItemInfo(Parcel in) {
        itemIndex = in.readInt();
        index = in.readInt();
        content = in.readString();
        diaryInfoList = in.createTypedArrayList(DiaryInfo.CREATOR);
    }

    public static final Creator<AddItemInfo> CREATOR = new Creator<AddItemInfo>() {
        @Override
        public AddItemInfo createFromParcel(Parcel in) {
            return new AddItemInfo(in);
        }

        @Override
        public AddItemInfo[] newArray(int size) {
            return new AddItemInfo[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private List<DiaryInfo> diaryInfoList;

    public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<DiaryInfo> getDiaryInfoList() {
        return diaryInfoList;
    }

    public void setDiaryInfoList(List<DiaryInfo> diaryInfoList) {
        this.diaryInfoList = diaryInfoList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemIndex);
        dest.writeInt(index);
        dest.writeString(content);
        dest.writeTypedList(diaryInfoList);
    }
}
