package com.app.legend.time.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class DiaryInfo implements Parcelable{
    private String content;
    private String img_url;
    private int id;
    private int type;
    public static final int TEXT=0x0010;
    public static final int IMAGE=0x0020;
    public static final int MUSIC=0x0030;

    public DiaryInfo() {
    }

    protected DiaryInfo(Parcel in) {
        content = in.readString();
        img_url = in.readString();
        id = in.readInt();
        type = in.readInt();
    }

    public static final Creator<DiaryInfo> CREATOR = new Creator<DiaryInfo>() {
        @Override
        public DiaryInfo createFromParcel(Parcel in) {
            return new DiaryInfo(in);
        }

        @Override
        public DiaryInfo[] newArray(int size) {
            return new DiaryInfo[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(img_url);
        dest.writeInt(id);
        dest.writeInt(type);
    }
}
