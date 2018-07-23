package com.bitmap.database.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/10.
 */

public class VideoListData {

    private String backdata;
    private List<VideoListData> list;

    public String getBackdata() {
        return backdata;
    }

    public void setBackdata(String backdata) {
        this.backdata = backdata;
    }

    public List<VideoListData> getList() {
        return list;
    }

    public void setList(List<VideoListData> list) {
        this.list = list;
    }
}
