package com.app.legend.time.bean;

import java.io.Serializable;

public class ImageInfo implements Serializable{

    private String path;
    private String folder;
    private int isSelect=-1;

    public ImageInfo() {
    }

    public ImageInfo(String path, String folder) {
        this.path = path;
        this.folder = folder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }
}
